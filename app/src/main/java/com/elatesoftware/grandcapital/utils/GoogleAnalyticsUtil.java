package com.elatesoftware.grandcapital.utils;

import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Ярослав Левшунов on 13.04.2017.
 */

public class GoogleAnalyticsUtil {

    public static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    public static void init(GoogleAnalytics analytics) {
        sAnalytics = analytics;
        sAnalytics.setLocalDispatchPeriod(1800);
        sTracker = sAnalytics.newTracker(Const.TRACKING_ID);
        sTracker.enableExceptionReporting(true);
        sTracker.enableAdvertisingIdCollection(true);
        sTracker.enableAutoActivityTracking(true);
    }

    public static void sendEvent(String category, String action, String label, Long value) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder();
        if(category != null) {
            builder.setCategory(category);
        }
        if(action != null) {
            builder.setAction(action);
        }
        if(label != null) {
            builder.setLabel(label);
        }
        if(value != null) {
            builder.setValue(value);
        }
        sTracker.send(builder.build());
    }
}
