package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by user on 06.03.2017.
 */

public class InOutService extends IntentService {

    public final static String RESPONSE= "response";
    public final static String TRANSACTION= "TRANSACTION";
    public final static String DEPOSIT= "deposit";
    public final static String WITHDRAW= "withdraw";
    public static final String ACTION_SERVICE_IN_OUT= "com.elatesoftware.grandcapital.services.InOutService";
    private final static String NAME_STREAM = "InOutService";

    public InOutService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "InOutService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String transaction = intent.getStringExtra(TRANSACTION);
        String response = GrandCapitalApi.getMoneyTransactions(transaction);
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_IN_OUT);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(TRANSACTION, transaction);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }
}
