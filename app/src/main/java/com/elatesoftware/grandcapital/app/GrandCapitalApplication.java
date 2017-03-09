package com.elatesoftware.grandcapital.app;

import android.app.Application;
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

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        GrandCapitalApplication.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return GrandCapitalApplication.context;
    }
    private static void connectWebSocket() throws URISyntaxException {
        mClient = new WebSocketClient(new URI(GrandCapitalApi.SOCKET_URL), new Draft_17(), null, 30000){
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d(TAG_SOCKET, "Open Connect Socket");
                mClient.send(symbolCurrent);
                startTimer();
            }
            @Override
            public void onMessage(final String message) {
                Log.d(TAG_SOCKET, message);
                if (message == null || message.equals("success") || message.equals("answer") || message.equals("") || message.equals("true") || message.equals("false")) {
                    return;
                }
                answerCurrent = SocketAnswer.getSetInstance(message);
                if(!answerCurrent.getSymbol().equals(symbolCurrent)){
                    answerCurrent = null;
                }
            }
            @Override
            public void onClose(int code, String reason, boolean remote){
                Log.d(TAG_SOCKET, " Closed Connect in Socket  because - " + reason);
                stopTimer();
            }
            @Override
            public void onError(Exception ex){
                Log.d(TAG_SOCKET, "Error Connect in Socket - " + ex.toString());
                stopTimer();
                if(!symbolCurrent.equals("")){
                    openSocket(symbolCurrent);
                }
            }
        };
        WebSocketClient.WebSocketClientFactory factory = new DefaultSSLWebSocketClientFactory(GrandCapitalApplication.getSSLContext());
        mClient.setWebSocketFactory(factory);
        mClient.connect();
    }

    private static void sentAnswer(final SocketAnswer answer){
        if(answer != null && answer.getTime() != null && answer.getTime() != 0L /*&& TerminalFragment.getInstance().getActivity() != null*/){
            if(answerSave != null && answerSave.getTime() != null && answerSave.getTime() != 0L && ConventDate.equalsTimeSocket(answerSave.getTime(), answer.getTime())){
                answer.setTime(ConventDate.getTimePlusOneSecond(answer.getTime())/1000);
            }
            TerminalFragment.getInstance().getActivity().runOnUiThread(() -> {
                TerminalFragment.getInstance().addEntry(answer);
            });
            answerSave = answer;
        }
    }

    private static void startTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(answerCurrent != null){
                    sentAnswer(answerCurrent);
                }
            }
        }, 0, 1500);
    }
    private static void stopTimer(){
        answerCurrent = null;
        answerSave = null;
        symbolCurrent = "";
        timer.cancel();
    }
    public static void closeSocket(){
        if (mClient != null && mClient.getReadyState() == WebSocket.READYSTATE.OPEN){
            mClient.close();
            stopTimer();
        }
    }
    public static void openSocket(String symbol){
        if(!symbol.equals("")){
            symbolCurrent = symbol;
            if (mClient == null || mClient.getReadyState() != WebSocket.READYSTATE.OPEN){
                new SocketTask().execute();
            }else{
                mClient.close();
                while(mClient.getReadyState() != WebSocket.READYSTATE.OPEN){

                }
                new SocketTask().execute();
            }
        }
    }
    public static SSLContext getSSLContext() {
        return sc;
    }
    static class SocketTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params){
            try {
                connectWebSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
