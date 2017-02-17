package com.elatesoftware.grandcapital.services;

/**
 * Created by Дарья Высокович on 16.02.2017.
 */

public class SignOutService {
/*
    private final String SESSION_ID = "sessionId";
    public final static String RESPONSE = "response";

    public static final String ACTION_SERVICE_LOGOUT = "by.lwo.vysokovich_d.msgr.connect.services.requests_retrofit.LogoutService";
    private final static String NAME_STREAM = "LOGOUT";

    public LogoutService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "LogoutService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = "";
        try {
            response =  RetroFitGenerator.logout(intent.getStringExtra(SESSION_ID));
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | JSONException | IOException e) {
            e.printStackTrace();
        }
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_LOGOUT);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }*/
}
