package com.elatesoftware.grandcapital.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

/**
 * Created by Ярослав Левшунов on 28.02.2017.
 */

public class ChatService extends IntentService {

    public final static String RESPONSE = "response";
    public final static String ACTION = "ACTION";

    public final static String CREATE_CHAT = "CREATE_CHAT";
    public final static String WIDGET_ID = "CHART_WIDGET_ID";
    public final static String VISITOR_MESSAGE = "VISITOR_MESSAGE";

    public final static String POLL_CHAT = "POLL_CHAT";
    public final static String CASE_ID = "CASE_ID";

    public final static String SEND_MESSAGE_CHAT = "SEND_MESSAGE_CHAT";
    public final static String MESSAGE_TYPE = "MESSAGE_TYPE";
    public final static String MESSAGE_BODY = "MESSAGE_BODY";

    public static final String ACTION_SERVICE_CHAT= "com.elatesoftware.grandcapital.services.ChatService";
    private final static String NAME_STREAM = "ChatService";

    public ChatService() {
        super(NAME_STREAM);
        Log.d(NAME_STREAM, "ChatService start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(NAME_STREAM, NAME_STREAM + " onHandleIntent");
        String response = null;
        String action = intent.getStringExtra(ACTION);
        Log.d(NAME_STREAM, "action: " + action);
        if(action.equals(CREATE_CHAT)) {
            Log.d(NAME_STREAM, NAME_STREAM + " CREATE_CHAT");
            String widgetId = intent.getStringExtra(WIDGET_ID);
            String visitorMessage = intent.getStringExtra(VISITOR_MESSAGE);
            response = GrandCapitalApi.createNewChat(widgetId, visitorMessage);
        }
        if(action.equals(POLL_CHAT)) {
            Log.d(NAME_STREAM, NAME_STREAM + " POLL_CHAT");
            String caseId = intent.getStringExtra(CASE_ID);
            response = GrandCapitalApi.pollChat(caseId);
        }
        if(action.equals(SEND_MESSAGE_CHAT)) {
            Log.d(NAME_STREAM, NAME_STREAM + " SEND_MESSAGE_CHAT");
            String caseId = intent.getStringExtra(CASE_ID);
            Integer messageType = intent.getIntExtra(MESSAGE_TYPE, 1);
            String messageBody = intent.getStringExtra(MESSAGE_BODY);
            response = GrandCapitalApi.sendMessage(caseId, messageType, messageBody);
        }
        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_SERVICE_CHAT);
        responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
        responseIntent.putExtra(ACTION, action);
        responseIntent.putExtra(RESPONSE, response);
        sendBroadcast(responseIntent);
    }
}
