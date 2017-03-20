package com.elatesoftware.grandcapital.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.models.Dealing;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.fragments.DealingFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CheckDealingService extends Service {

    public static final String TAG = "CheckDealingService_Logs";

    private static Timer timer;
    public static  int countCloseDealingLast;
    public static  List<OrderAnswer> listOpenDealingLast;
    public static final String ACTION_SERVICE_CHECK_DEALINGS = "com.elatesoftware.grandcapital.services.CheckDealingService";
    public static final String TICKET = "TICKET";
    public static final String INDEX = "INDEX";

    private final static int INTERVAL = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        listOpenDealingLast = new ArrayList<>();
        countCloseDealingLast = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(OrderAnswer.getInstance() != null){
                    List<OrderAnswer> listCurrentClose = OrderAnswer.filterOrders(OrderAnswer.getInstance(), DealingFragment.CLOSE_TAB_POSITION);
                    List<OrderAnswer> listCurrentOpen = OrderAnswer.filterOrders(OrderAnswer.getInstance(), DealingFragment.CLOSE_TAB_POSITION);
                    if(listCurrentClose != null && listCurrentClose.size() != 0) {
                        if(listOpenDealingLast != null && listOpenDealingLast.size() != 0){
                            if(listCurrentClose.size() != countCloseDealingLast){
                                for(OrderAnswer orderAnswerOpen : listOpenDealingLast){
                                    for(OrderAnswer orderAnswerClose : listCurrentClose){
                                        if((int)orderAnswerClose.getTicket() == orderAnswerOpen.getTicket()){
                                            Intent responseIntent = new Intent();
                                            responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                                            responseIntent.setAction(ACTION_SERVICE_CHECK_DEALINGS);
                                            responseIntent.putExtra(TICKET, orderAnswerClose.getTicket());
                                            responseIntent.putExtra(INDEX, listCurrentClose.indexOf(orderAnswerClose));
                                            sendBroadcast(responseIntent);
                                            CustomSharedPreferences.setAmtCloseDealings(getApplicationContext(), CustomSharedPreferences.getAmtCloseDealings(getApplicationContext()) + 1);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        listOpenDealingLast = listCurrentOpen;
                        countCloseDealingLast = listCurrentClose.size();
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
