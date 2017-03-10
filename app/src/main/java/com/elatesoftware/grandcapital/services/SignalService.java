package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Дарья Высокович on 10.03.2017.
 */

public class SignalService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_SIGNAL= "com.elatesoftware.grandcapital.services.SignalService";
    private final static String NAME_STREAM = "Signal";

    public SignalService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "SignalService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = GrandCapitalApi.getSignals();
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_SIGNAL);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }
}