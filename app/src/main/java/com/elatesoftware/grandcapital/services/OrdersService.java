package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Дарья Высокович on 22.02.2017.
 */

public class OrdersService extends IntentService {

    public final static String RESPONSE= "response";
    public static final String ACTION_SERVICE_ORDERS= "com.elatesoftware.grandcapital.services.OrdersService";
    private final static String NAME_STREAM = "Orders";

    public static final String FRAGMENT = "FRAGMENT";
    public static final String FRAGMENT_TERMINAL ="FRAGMENT_TERMINAL";

    public OrdersService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "OrdersService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = GrandCapitalApi.getOrders();
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_ORDERS + intent.getStringExtra(FRAGMENT));
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }
}
