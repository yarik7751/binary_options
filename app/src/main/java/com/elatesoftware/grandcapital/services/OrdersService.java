package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.fragments.DealingFragment;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Дарья Высокович on 22.02.2017.
 */

public class OrdersService extends IntentService {

    public final static String RESPONSE= "response";
    public static final String ACTION_SERVICE_ORDERS= "com.elatesoftware.grandcapital.services.OrdersService";
    private final static String NAME_STREAM = "Orders";
    public final static String FUNCTION = "function";
    public final static String ORDER = "order";

    public final static int GET_ALL_ORDERS_TERMINAL = 0;
    public final static int GET_ALL_ORDERS_DEALING = 1;
    public final static int GET_TICKET_ORDER = 2;


    public OrdersService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "OrdersService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = GrandCapitalApi.getOrders();
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_ORDERS);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        switch(intent.getIntExtra(FUNCTION, 0)){
            case GET_ALL_ORDERS_TERMINAL:
                responseIntent.putExtra(RESPONSE, response);
                responseIntent.putExtra(FUNCTION, GET_ALL_ORDERS_TERMINAL);
                break;
            case GET_TICKET_ORDER:
                OrderAnswer orderAnswer = new Gson().fromJson(intent.getStringExtra(ORDER), OrderAnswer.class);
                if(OrderAnswer.getInstance() != null){
                    List<OrderAnswer> listOrders = OrderAnswer.filterOrdersCurrentActive(OrderAnswer.getInstance(), DealingFragment.OPEN_TAB_POSITION, orderAnswer.getSymbol());
                    if(listOrders != null && listOrders.size() != 0){
                        for(OrderAnswer order: listOrders){
                            if(ConventDate.equalsTimePoints(order.getOpenTime(), orderAnswer.getOpenTime()) &&
                                    ConventDate.equalsTimePoints(order.getOptionsData().getExpirationTime(), orderAnswer.getOptionsData().getExpirationTime())){
                                responseIntent.putExtra(RESPONSE, new Gson().toJson(order));
                                responseIntent.putExtra(FUNCTION, GET_TICKET_ORDER);
                                break;
                            }
                        }
                    }
                }
                break;
            case GET_ALL_ORDERS_DEALING:
                responseIntent.putExtra(RESPONSE, response);
                responseIntent.putExtra(FUNCTION, GET_ALL_ORDERS_DEALING);
                break;
            default:
                break;
        }
        sendBroadcast(responseIntent);
    }
}
