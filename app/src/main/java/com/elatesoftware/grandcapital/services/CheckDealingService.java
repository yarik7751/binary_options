package com.elatesoftware.grandcapital.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.elatesoftware.grandcapital.models.Dealing;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CheckDealingService extends Service {

    public static final String TAG = "CheckDealingService_Logs";

    private static Timer timer;

    public static ArrayList<Dealing> dealings = new ArrayList<>();
    public static final String ACTION_SERVICE_CHECK_DEALINGS = "com.elatesoftware.grandcapital.services.CheckDealingService";
    public static final String ACTIVE = "ACTIVE";
    public static final String AMOUNT = "AMOUNT";
    public static final String CLOSE_DATE = "CLOSE_DATE";

    private final int INTERVAL = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer.schedule(new TimerTask() {
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                Log.d(TAG, "dealings.size(): " + dealings.size());
                for(int i = 0; i < dealings.size(); i++) {
                    long currTime = Long.valueOf(ConventDate.getTimeStampCurrentDate());
                    long difference = currTime - dealings.get(i).createTime;
                    Log.d(TAG, "dealings(" + i + "): " + dealings.get(i));
                    Log.d(TAG, "currTimeUnix: " + System.currentTimeMillis());
                    Log.d(TAG, "currTime    : " + currTime);
                    Log.d(TAG, "difference: " + difference);
                    if(difference >= dealings.get(i).timeMin) {
                        Log.d(TAG, "CLOSE dealings(" + i + "): " + dealings.get(i));
                        Intent responseIntent = new Intent();
                        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        responseIntent.setAction(ACTION_SERVICE_CHECK_DEALINGS);
                        responseIntent.putExtra(ACTIVE, dealings.get(i).active);
                        responseIntent.putExtra(AMOUNT, dealings.get(i).amount);
                        responseIntent.putExtra(CLOSE_DATE, currTime/* - 3600000*/);
                        //TerminalFragment.getInstance().addEntry();
                        sendBroadcast(responseIntent);
                        dealings.remove(i);
                        CustomSharedPreferences.setAmtCloseDealings(getApplicationContext(), CustomSharedPreferences.getAmtCloseDealings(getApplicationContext()) + 1);
                        break;
                    }
                }
            }
        }, 0, INTERVAL);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
