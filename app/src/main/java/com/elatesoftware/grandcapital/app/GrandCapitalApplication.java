package com.elatesoftware.grandcapital.app;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
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

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GrandCapitalApplication extends Application{

    private static Context context;
    private static SSLContext sc;
    public static WebSocketClient mClient;
    public final static String TAG_SOCKET = "debug_for_socket";

    private static SocketAnswer answerCurrent = null;
    private static SocketAnswer answerSave = null;
    private static Timer timer;
    private static String symbolCurrent = "";

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        GrandCapitalApplication.context = getApplicationContext();
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

        timer = new Timer();
        startTimer();
    }

    public static void closeSocket(){
        TerminalFragment.listSocketPointsBackGround.clear();
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
                                        TerminalFragment.listSocketPointsBackGround.add(answerCurrent);
                                    });
                                }
                            }
                        }
                        answerSave = answerCurrent;
                    }
                    TerminalFragment.getInstance().getActivity().runOnUiThread(() -> {
                        TerminalFragment.getInstance().redrawXYDealingLimitLines();

                    });
                }
            }
        }, 3000, 1000);
    }

    static class SocketTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params){
            if(mClient == null || mClient.getReadyState() != WebSocket.READYSTATE.OPEN){
                try {
                    mClient = new WebSocketClient(new URI(GrandCapitalApi.SOCKET_URL), new Draft_17(), null, 10000){
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Log.d(TAG_SOCKET, "Open Connect Socket");
                            if(!symbolCurrent.equals("")){
                                mClient.send(symbolCurrent);
                                TerminalFragment.listSocketPointsBackGround.clear();
                                Log.d(TAG_SOCKET, "Open Connect Socket for symbol - " + symbolCurrent);
                            }
                        }
                        @Override
                        public void onMessage(final String message) {
                            Log.d(TAG_SOCKET, message);
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
                            Log.d(TAG_SOCKET, " Closed Connect in Socket  because - " + reason);

                            TerminalFragment.listSocketPointsBackGround.clear();
                            if(!symbolCurrent.equals("")){
                                closeAndOpenSocket(symbolCurrent);
                            }
                        }
                        @Override
                        public void onError(Exception ex){
                            Log.d(TAG_SOCKET, "Error Connect in Socket - " + ex.toString());
                            if(!symbolCurrent.equals("")){
                                closeAndOpenSocket(symbolCurrent);
                            }
                        }
                    };
                    WebSocketClient.WebSocketClientFactory factory = new DefaultSSLWebSocketClientFactory(GrandCapitalApplication.getSSLContext());
                    mClient.setWebSocketFactory(factory);
                    mClient.connect();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            System.gc();
        }
    }
    public static SSLContext getSSLContext() {
        return sc;
    }
    public static Context getAppContext() {
        return GrandCapitalApplication.context;
    }
}
