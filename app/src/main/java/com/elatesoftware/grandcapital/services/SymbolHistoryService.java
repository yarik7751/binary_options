package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Дарья Высокович on 22.02.2017.
 */

public class SymbolHistoryService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_SYMBOL_HISTORY = "com.elatesoftware.grandcapital.services.SymbolHistoryService";
    private final static String NAME_STREAM = "SymbolHistory";
    public final static String SYMBOL = "Symbol";

    public SymbolHistoryService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "SymbolHistoryService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = GrandCapitalApi.getSymbolHistory(intent.getStringExtra(SYMBOL) + "_OP");
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_SYMBOL_HISTORY);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        responseIntent.putExtra(SYMBOL, intent.getStringExtra(SYMBOL));
        sendBroadcast(responseIntent);
    }
}
