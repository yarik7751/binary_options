package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Дарья Высокович on 09.06.2017.
 */

public class SummaryService extends IntentService {

    public final static String RESPONSE_SUMMARY = "response_summary";
    public static final String ACTION_SERVICE_SUMMARY = "com.elatesoftware.grandcapital.services.SummaryService";
    private final static String NAME_STREAM = "Summary";

    public SummaryService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "SummaryService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String responseSummary = GrandCapitalApi.getSummary();
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_SUMMARY);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE_SUMMARY, responseSummary);
        sendBroadcast(responseIntent);
    }
}
