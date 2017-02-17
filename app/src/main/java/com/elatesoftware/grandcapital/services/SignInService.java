package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Дарья Высокович on 16.02.2017.
 */

public class SignInService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_SIGN_IN= "com.elatesoftware.grandcapital.services.SignInService";
    private final static String NAME_STREAM = "SignIn";
    public final static String LOGIN = "login";
    public final static String PASSWORD = "password";

    public SignInService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "SignInService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response;
        response = GrandCapitalApi.authorizationRequest(intent.getStringExtra(LOGIN), intent.getStringExtra(PASSWORD));
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_SIGN_IN);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }
}
