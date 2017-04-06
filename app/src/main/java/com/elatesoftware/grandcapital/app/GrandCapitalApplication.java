package com.elatesoftware.grandcapital.app;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.socket.WebSocketHTTP3;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GrandCapitalApplication extends Application{

    public final static String TAG_SOCKET = "debug_for_socket";
    public static boolean isTypeOptionAmerican = false;
    private static Context context;

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
