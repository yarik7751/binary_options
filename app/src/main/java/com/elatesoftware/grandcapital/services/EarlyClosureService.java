package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Ярослав Левшунов on 23.03.2017.
 */

public class EarlyClosureService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_EARLY_CLOSURE= "com.elatesoftware.grandcapital.services.EarlyClosureService";
    private final static String NAME_STREAM = "EarlyClosureService";

    public EarlyClosureService() {
        super(NAME_STREAM);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = GrandCapitalApi.getEarlyClosureAnswer();
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_EARLY_CLOSURE);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }
}
