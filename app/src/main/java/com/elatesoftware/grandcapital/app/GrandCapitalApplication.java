package com.elatesoftware.grandcapital.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.java_websocket.WebSocket;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GrandCapitalApplication extends Application{

    private static Context context;

    private static SSLContext sc;
    public static WebSocketClient mClient;
    public final static String TAG_SOCKET = "socket";

    public static String symbol = "EURUSD_OP";
    private static long currentTime = 0L;

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

    public static void closeSocket(){
        if (mClient != null && mClient.getReadyState() == WebSocket.READYSTATE.OPEN){
            mClient.close();
        }
    }
    public static void openSocket(){
        if (mClient != null && mClient.getReadyState() == WebSocket.READYSTATE.OPEN){
            mClient.close();
        }
        if (mClient == null ||mClient.getReadyState() == WebSocket.READYSTATE.CLOSED ||
                mClient.getReadyState() == WebSocket.READYSTATE.NOT_YET_CONNECTED ||
                mClient.getReadyState() == WebSocket.READYSTATE.CLOSING){
            openSocketInActivity();
        }
    }

    public static SSLContext getSSLContext() {
        return sc;
    }

    public static Context getAppContext() {
        return GrandCapitalApplication.context;
    }
    private static void connectWebSocket(String symbol) throws URISyntaxException {
        mClient = new WebSocketClient(new URI(GrandCapitalApi.SOCKET_URL), new Draft_17(), null, 30000){
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d(TAG_SOCKET, "Open Connect Socket");
                mClient.send(symbol);

            }
            @Override
            public void onMessage(final String message) {
                Log.d(TAG_SOCKET, message);
                if (message == null || message.equals("success") || message.equals("answer") || message.equals("") || message.equals("true") || message.equals("false")) {
                    return;
                }
                SocketAnswer answer = SocketAnswer.getSetInstance(message);
                if (!ConventDate.equalsTimeSocket(currentTime, answer.getTime())) {
                   currentTime = answer.getTime();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TerminalFragment.getInstance().getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TerminalFragment.getInstance().updateChart(answer);
                                }
                            });
                        }
                    }).start();
                }
            }
            @Override
            public void onClose(int code, String reason, boolean remote){
                Log.d(TAG_SOCKET, " Closed Connect in Socket  because - " + reason);
            }
            @Override
            public void onError(Exception ex){
                if(ex instanceof ConnectException){
                    //runOnUiThread(() -> Toast.makeText(getApplicationContext(), getResources().getString(R.string.request_error_text), Toast.LENGTH_LONG).show());
                }
                Log.d(TAG_SOCKET, "Error Connect in Socket - " + ex.toString());
            }
        };
        WebSocketClient.WebSocketClientFactory factory = new DefaultSSLWebSocketClientFactory(GrandCapitalApplication.getSSLContext());
        mClient.setWebSocketFactory(factory);
        mClient.connect();
    }
    static class SocketTask extends AsyncTask<Void, Void, Void> {
        String symbol = "";

        public SocketTask(String symbol) {
            this.symbol = symbol;
        }

        @Override
        protected Void doInBackground(Void... params){
            try {
                connectWebSocket(symbol);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static void openSocketInActivity(){
        if (mClient == null || mClient.getReadyState() != WebSocket.READYSTATE.OPEN){
            new SocketTask(symbol).execute();
        }
    }
}
