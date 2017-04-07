package com.elatesoftware.grandcapital.api.socket;

/**
 * Created by Дарья Высокович on 06.04.2017.
 */

import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.WebSocket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocketListener;

public final class WebSocketHTTP3 extends WebSocketListener {

    private WebSocket webSocket = null;
    private Timer mTimer;

    private static String mSymbolCurrent = null;
    private String mMessageCurrent = null;

    private SocketAnswer answerCurrent = null;
    private SocketAnswer answerSave = null;

    public WebSocketHTTP3(String symbol){
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url(GrandCapitalApi.SOCKET_URL)
                .build();
        if(webSocket != null){
            closeSocket();
        }
        webSocket = client.newWebSocket(request, this);
        client.dispatcher().executorService().shutdown();
        mTimer = new Timer();
        mSymbolCurrent = symbol.replace("_OP", "") + "_OP";
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d(GrandCapitalApplication.TAG_SOCKET, "Open Connect Socket " + mSymbolCurrent);
        if(mSymbolCurrent != null && !mSymbolCurrent.isEmpty()){
            webSocket.send(mSymbolCurrent);
        }
        startTimer();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(GrandCapitalApplication.TAG_SOCKET, text);
        if (text == null ||  text.equals("true") || text.equals("false")) {
            return;
        }
        mMessageCurrent = text;
    }
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d(GrandCapitalApplication.TAG_SOCKET, "CloseSocket = " + reason);
    }
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        answerCurrent = null;
        answerSave = null;
        mSymbolCurrent = null;
    }
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d(GrandCapitalApplication.TAG_SOCKET, "FailSocket = " +  t.getMessage());
    }

    private void startTimer(){
        mTimer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                if(mSymbolCurrent != null &&  mMessageCurrent != null && TerminalFragment.getInstance().getActivity() != null && TerminalFragment.getInstance() != null){
                    answerCurrent = SocketAnswer.getSetInstance(mMessageCurrent);
                    if(answerCurrent != null && mSymbolCurrent.equals(answerCurrent.getSymbol())){
                        if(answerSave != null && mSymbolCurrent.equals(answerSave.getSymbol())){
                            if(answerCurrent.getTime() - answerSave.getTime() >= 0){
                                if(ConventDate.equalsTimeSocket(answerSave.getTime(), answerCurrent.getTime())){
                                    answerCurrent.setTime(ConventDate.getTimePlusOneSecond(answerCurrent.getTime()) / 1000);
                                }
                            }else{
                                return;
                            }
                        }
                        TerminalFragment.getInstance().getActivity().runOnUiThread(() -> {
                            TerminalFragment.getInstance().answerSocket(answerCurrent);
                        });
                        answerSave = answerCurrent;
                    }else{
                        answerSave = null;
                        answerCurrent = null;
                        mMessageCurrent = "";
                    }
                }
            }
        }, 2000, 1200);
    }

    public void closeSocket(){
        mTimer.purge();
        mTimer.cancel();
        webSocket.close(1000, "Goodbye, Socket!");
    }

    public static String getmSymbolCurrent() {
        if(mSymbolCurrent == null){
            return "";
        }else{
            return mSymbolCurrent.replace("_OP", "");
        }
    }
}