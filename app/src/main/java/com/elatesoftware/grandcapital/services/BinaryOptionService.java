package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Ярослав Левшунов on 24.02.2017.
 */

public class BinaryOptionService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_BINARY_OPTION= "com.elatesoftware.grandcapital.services.BinaryOptionService";
    private final static String NAME_STREAM = "GetBinatyOption";

    public BinaryOptionService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "BinatyOptionService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = GrandCapitalApi.getBinaryOption();
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_BINARY_OPTION);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }
}
