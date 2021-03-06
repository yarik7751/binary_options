package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Дарья Высокович on 16.02.2017.
 */

public class InfoUserService extends IntentService {

    public final static String RESPONSE_INFO = "response_info";
    public final static String RESPONSE_SUMMARY = "response_summary";
    public static final String ACTION_SERVICE_GET_INFO = "com.elatesoftware.grandcapital.services.InfoUserService";
    private final static String NAME_STREAM = "InfoUser";

    public InfoUserService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "InfoUserService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String responseInfo = GrandCapitalApi.getInfoUser();
        String responseSummary = GrandCapitalApi.getSummary();
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_GET_INFO);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE_INFO, responseInfo);
        responseIntent.putExtra(RESPONSE_SUMMARY, responseSummary);
        sendBroadcast(responseIntent);
    }
}
