package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.utils.Const;

/**
 * Created by Дарья Высокович on 28.03.2017.
 */

public class DeleteDealingService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_DELETE_FEALING = "com.elatesoftware.grandcapital.services.DeleteDealingService";
    private final static String NAME_STREAM = "DeleteDealing";
    public final static String TICKET = "ticket";

    public DeleteDealingService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "DeleteDealingService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = GrandCapitalApi.getDeleteDealing(intent.getIntExtra(TICKET, 0));
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_DELETE_FEALING);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        responseIntent.putExtra(TICKET, intent.getIntExtra(TICKET, 0));
        sendBroadcast(responseIntent);
    }
}
