package com.elatesoftware.grandcapital.app;

import android.app.Application;
import android.content.Context;

import com.elatesoftware.grandcapital.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class GrandCapitalApplication extends Application{

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

    public static Context getAppContext() {
        return GrandCapitalApplication.context;
    }
}
