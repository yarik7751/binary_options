package com.elatesoftware.grandcapital.utils;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Ярослав Левшунов on 13.04.2017.
 */

public class GoogleAnalyticsUtil {

    public final static String ANALYTICS_QUOTES_SCREEN = "QUOTES_SCREEN";
    public final static String ANALYTICS_BUTTON_FAVORITES = "BUTTON_FAVORITES";
    public final static String ANALYTICS_BUTTON_DELETE_FAVORITES = "BUTTON_DELETE_FAVORITES";

    public final static String ANALYTICS_SUPPORT_SCREEN = "SUPPORT_SCREEN";
    public final static String ANALYTICS_BUTTON_SEND_MESSAGE = "BUTTON_SEND_MESSAGE";

    public final static String TRACKING_ID = "UA-96895164-2";

    public final static String ANALYTICS_TERMINAL_SCREEN = "TERMINAL_SCREEN";
    public final static String ANALYTICS_BUTTON_CHANGE_ACTIVE = "BUTTON_CHANGE_ACTIVE";
    public final static String ANALYTICS_BUTTON_CHANGE_AMOUNT_INVESTMENTS = "BUTTON_CHANGE_AMOUNT_INVESTMENTS";
    public final static String ANALYTICS_BUTTON_CHANGE_TIME = "BUTTON_CHANGE_TIME";
    public final static String ANALYTICS_BUTTON_UP = "BUTTON_UP";
    public final static String ANALYTICS_BUTTON_DOWN = "BUTTON_DOWN";
    public final static String ANALYTICS_BUTTON_CLOSE_DEALINGS = "BUTTON_CLOSE_DEALINGS";
    public final static String ANALYTICS_BUTTON_WIDGET_SIGNALS = "BUTTON_WIDGET_SIGNALS";

    public final static String ANALYTICS_DEALINGS_SCREEN = "DEALINGS_SCREEN";
    public final static String ANALYTICS_TAB_OPEN_DEALINGS = "TAB_OPEN_DEALINGS";
    public final static String ANALYTICS_TAB_CLOSE_DEALINGS = "TAB_CLOSE_DEALINGS";

    public final static String ANALYTICS_IN_OUT_SCREEN = "IN_OUT_SCREEN";
    public final static String ANALYTICS_BUTTON_WITHDRAW = "BUTTON_WITHDRAW";
    public final static String ANALYTICS_BUTTON_DEPOSIT = "BUTTON_DEPOSIT";

    public final static String ANALYTICS_QUESTION_SCREEN = "QUESTION_SCREEN";
    public final static String ANALYTICS_LIST_QUESTION = "LIST_QUESTION";

    public final static String ANALYTICS_PROMOTIONS_SCREEN = "PROMOTIONS_SCREEN";
    public final static String ANALYTICS_LIST_PROMOTION = "LIST_PROMOTION";

    public final static String ANALYTICS_EVENT_EXIT = "EVENT_EXIT";

    public static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    public static void init(GoogleAnalytics analytics) {
        sAnalytics = analytics;
        sAnalytics.setLocalDispatchPeriod(1800);
        sTracker = sAnalytics.newTracker(TRACKING_ID);
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
