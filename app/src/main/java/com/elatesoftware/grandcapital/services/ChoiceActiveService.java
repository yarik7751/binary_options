package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Дарья Высокович on 25.05.2017.
 */

public class ChoiceActiveService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_CHOICE_ACTIVE = "com.elatesoftware.grandcapital.services.ChoiceActiveService";
    private final static String NAME_STREAM = "ChoiceActiveService";
    public final static String SYMBOL = "Symbol";

    public ChoiceActiveService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "ChoiceActiveService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = intent.getStringExtra(SYMBOL);
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_CHOICE_ACTIVE);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }
}