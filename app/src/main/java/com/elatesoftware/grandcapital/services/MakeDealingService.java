package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Darya on 10.03.2017.
 */

public class MakeDealingService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_MAKE_DEALING = "com.elatesoftware.grandcapital.services.MakeDealingService";
    private final static String NAME_STREAM = "MakeDealing";
    public final static String SYMBOL = "symbol";
    public final static String VOLUME = "volume";
    public final static String CMD = "cmd";
    public final static String EXPIRATION = "expiration";

    public MakeDealingService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "MakeDealingService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = GrandCapitalApi.getMakeDealing(intent.getStringExtra(CMD),
                                                         intent.getStringExtra(VOLUME),
                                                         intent.getStringExtra(SYMBOL) + "_OP",
                                                         intent.getStringExtra(EXPIRATION));
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_MAKE_DEALING);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        responseIntent.putExtra(CMD, intent.getStringExtra(CMD));
        responseIntent.putExtra(SYMBOL, intent.getStringExtra(SYMBOL));
        responseIntent.putExtra(VOLUME, intent.getStringExtra(VOLUME));
        responseIntent.putExtra(EXPIRATION, intent.getStringExtra(EXPIRATION));
        sendBroadcast(responseIntent);
    }
}
