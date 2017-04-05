package com.elatesoftware.grandcapital.app;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.api.socket.WebSocketApi;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;
import com.github.mikephil.charting.data.Entry;

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
    public static WebSocketApi webSocketApi;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        GrandCapitalApplication.context = getApplicationContext();
        webSocketApi = WebSocketApi.getInstance();
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
            System.gc();
        }
    }
    public static Context getAppContext() {
        return GrandCapitalApplication.context;
    }
}
