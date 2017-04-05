package com.elatesoftware.grandcapital.api.socket;

import android.os.AsyncTask;
import android.util.Log;

import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;

import org.java_websocket.WebSocket;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Дарья Высокович on 05.04.2017.
 */

public class WebSocketApi {

    private static SocketAnswer answerCurrent = null;
    private static SocketAnswer answerSave = null;
    private static Timer timer;
    private static String symbolCurrent = "";
    public static WebSocketClient mClient;
    private static SSLContext sc;
    static {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
            }
            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

            }
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[0];
            }
        }};

        KeyManager[] keyManager = null;
        try {
            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            KeyManagerFactory factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            factory.init(keyStore, null);
            keyManager = factory.getKeyManagers();
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(keyManager, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static WebSocketApi webSocketApi = null;
    public static WebSocketApi getInstance(){
        if(webSocketApi == null){
            webSocketApi = new WebSocketApi();
        }
        return webSocketApi;
    }
    private WebSocketApi(){
        timer = new Timer();
        startTimer();
    }

    public static void closeSocket(){
        SocketAnswer.clearListBackGround();
        answerCurrent = null;
        answerSave = null;
        symbolCurrent = "";
        if (mClient != null && (mClient.getReadyState() == WebSocket.READYSTATE.OPEN
                || mClient.getReadyState() == WebSocket.READYSTATE.CONNECTING
                || mClient.getReadyState() == WebSocket.READYSTATE.NOT_YET_CONNECTED)){
            mClient.close();
            while(mClient.getReadyState() == WebSocket.READYSTATE.OPEN || mClient.getReadyState() == WebSocket.READYSTATE.CONNECTING){

            }
        }
    }
    public static void closeAndOpenSocket(final String symbol){
        closeSocket();
        symbolCurrent = symbol.replace("_OP", "") + "_OP";
        if (mClient != null){
            while(mClient.getReadyState() == WebSocket.READYSTATE.OPEN || mClient.getReadyState() == WebSocket.READYSTATE.CONNECTING){

            }
        }
        if(!symbolCurrent.equals("")){
            new SocketTask().execute();
        }
    }

    private static void startTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(TerminalFragment.getInstance().getActivity() != null && TerminalFragment.getInstance() != null){
                    if(!symbolCurrent.equals("") && answerCurrent != null && symbolCurrent.equals(answerCurrent.getSymbol())){
                        if(answerCurrent != null && answerCurrent.getTime() != null && answerCurrent.getTime() != 0L) {
                            if (answerSave != null && answerSave.getTime() != null && answerSave.getTime() != 0L && ConventDate.equalsTimeSocket(answerSave.getTime(), answerCurrent.getTime())) {
                                answerCurrent.setTime(ConventDate.getTimePlusOneSecond(answerCurrent.getTime()) / 1000);
                            }
                            if (TerminalFragment.getInstance() != null) {
                                if(TerminalFragment.isAddInChart) {
                                    TerminalFragment.getInstance().getActivity().runOnUiThread(() -> {
                                        TerminalFragment.getInstance().addEntry(answerCurrent);
                                    });
                                } else {
                                    TerminalFragment.getInstance().getActivity().runOnUiThread(() -> {
                                        SocketAnswer.addItemListBackGround(answerCurrent);
                                    });
                                }
                            }
                        }
                        answerSave = answerCurrent;
                    }else{
                        TerminalFragment.getInstance().getActivity().runOnUiThread(() -> {
                            TerminalFragment.getInstance().redrawXYDealingLimitLines();
                        });
                    }
                }
            }
        }, 3000, 1500);
    }

    static class SocketTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params){
            if(mClient == null || mClient.getReadyState() != WebSocket.READYSTATE.OPEN){
                try {
                    mClient = new WebSocketClient(new URI(GrandCapitalApi.SOCKET_URL), new Draft_17(), null, 10000){
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Log.d(GrandCapitalApplication.TAG_SOCKET, "Open Connect Socket");
                            if(!symbolCurrent.equals("")){
                                mClient.send(symbolCurrent);
                                SocketAnswer.clearListBackGround();
                                Log.d(GrandCapitalApplication.TAG_SOCKET, "Open Connect Socket for symbol - " + symbolCurrent);
                            }
                        }
                        @Override
                        public void onMessage(final String message) {
                            Log.d(GrandCapitalApplication.TAG_SOCKET, message);
                            if (message == null || message.equals("success") || message.equals("answer") || message.isEmpty() || message.equals("true") || message.equals("false")) {
                                return;
                            }
                            answerCurrent = SocketAnswer.getSetInstance(message);
                            if(answerCurrent != null && !answerCurrent.getSymbol().equals(symbolCurrent)){
                                answerCurrent = null;
                            }
                        }
                        @Override
                        public void onClose(int code, String reason, boolean remote){
                            Log.d(GrandCapitalApplication.TAG_SOCKET, " Closed Connect in Socket  because - " + reason);
                            SocketAnswer.clearListBackGround();
                            if(!symbolCurrent.equals("")){
                                closeAndOpenSocket(symbolCurrent);
                            }
                        }
                        @Override
                        public void onError(Exception ex){
                            Log.d(GrandCapitalApplication.TAG_SOCKET, "Error Connect in Socket - " + ex.toString());
                            if(!symbolCurrent.equals("")){
                                closeAndOpenSocket(symbolCurrent);
                            }
                        }
                    };
                    WebSocketClient.WebSocketClientFactory factory = new DefaultSSLWebSocketClientFactory(getSSLContext());
                    mClient.setWebSocketFactory(factory);
                    mClient.connect();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
    public static SSLContext getSSLContext() {
        return sc;
    }
}
