package com.elatesoftware.grandcapital.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CheckDealingService extends Service {

    private final static int INTERVAL = 1000;

    public static final String TAG = "CheckDealingService_Logs";
    public static final String ACTION_SERVICE_CHECK_DEALINGS = "com.elatesoftware.grandcapital.services.CheckDealingService";
    public static final String RESPONSE = "RESPONSE";

    private static Timer timer;
    private static List<OrderAnswer> listOrderAnswer = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       /* timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(listOrderAnswer != null && listOrderAnswer.size() != 0){
                    for(OrderAnswer order: listOrderAnswer) {
                        if (ConventDate.equalsTimeDealing(order.getOptionsData().getExpirationTime())){
                            Intent responseIntent = new Intent();
                            responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                            responseIntent.setAction(ACTION_SERVICE_CHECK_DEALINGS);
                            responseIntent.putExtra(RESPONSE, new Gson().toJson(order));
                            sendBroadcast(responseIntent);
                            listOrderAnswer.remove(order);
                            break;
                        }
                    }
                }
            }
        }, 0, INTERVAL);*/
        return super.onStartCommand(intent, flags, startId);
    }

    public static List<OrderAnswer> getListOrderAnswer() {
        return listOrderAnswer;
    }
    public static void addOrderAnswerList(OrderAnswer order) {
        listOrderAnswer.add(order);
    }
    public static void setListOrderAnswer(List<OrderAnswer> listOrderAnswer) {
        CheckDealingService.listOrderAnswer = listOrderAnswer;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
