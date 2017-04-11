package com.elatesoftware.grandcapital.app;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.socket.WebSocketHTTP3;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.orm.SugarApp;
import com.orm.SugarContext;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GrandCapitalApplication extends Application{

    public final static String TAG_SOCKET = "debug_for_socket";
    public static boolean isTypeOptionAmerican = false;
    private static Context context;

    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        GrandCapitalApplication.context = getApplicationContext();
        SugarContext.init(getApplicationContext());
        sAnalytics = GoogleAnalytics.getInstance(getApplicationContext());
        sAnalytics.setLocalDispatchPeriod(1800);
        sTracker = sAnalytics.newTracker("UA-96895164-2");
        sTracker.enableExceptionReporting(true);
        sTracker.enableAdvertisingIdCollection(true);
        sTracker.enableAutoActivityTracking(true);
    }

    public static GoogleAnalytics getAnalytics() {
        return sAnalytics;
    }

    public static Tracker getDefaultTracker() {
        return sTracker;
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
