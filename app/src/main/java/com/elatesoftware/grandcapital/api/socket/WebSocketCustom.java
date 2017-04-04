package com.elatesoftware.grandcapital.api.socket;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * Created by Дарья Высокович on 30.03.2017.
 */

public final class WebSocketCustom extends okhttp3.WebSocketListener {

    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private static WebSocket mWebSocket = null;
    private static OkHttpClient client = new OkHttpClient();

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        //webSocket.send("Knock, knock!");
    }
    @Override
    public void onMessage(WebSocket webSocket, String text) {

    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    public static WebSocket getmWebSocket() {
        return mWebSocket;
    }

    public static void openSocket(){
        if(client != null){
            client = new OkHttpClient();
        }
        Request request = new Request.Builder().url(GrandCapitalApi.SOCKET_URL).build();
        mWebSocket = client.newWebSocket(request, new WebSocketCustom());
    }
    public static void closeSocket(){
        mWebSocket.close(NORMAL_CLOSURE_STATUS, null);
        if(client != null){
            client.dispatcher().executorService().shutdown();
        }
    }
}

