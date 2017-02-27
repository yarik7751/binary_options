package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Ярослав Левшунов on 23.02.2017.
 */

public class QuestionsService extends IntentService {

    public final static String RESPONSE = "response";
    public static final String ACTION_SERVICE_QUESTIONS= "com.elatesoftware.grandcapital.services.QuestionsService";
    private final static String NAME_STREAM = "GetAnswers";
    public final static String PAGE = "page";

    public QuestionsService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "QuestionsService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String response = GrandCapitalApi.getQuestions(intent.getIntExtra(PAGE, 1));
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_QUESTIONS);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }
}
