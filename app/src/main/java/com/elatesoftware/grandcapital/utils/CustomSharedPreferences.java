package com.elatesoftware.grandcapital.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.elatesoftware.grandcapital.models.User;
import com.google.gson.Gson;

/**
 * Created by Дарья Высокович on 16.02.2017.
 */

public class CustomSharedPreferences {

    public static final String SHARED_PREFERENCES_NAME = "com.elatesoftware.grandcapital.user";

    public static boolean isSaveInPreferences(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(SHARED_PREFERENCES_NAME, null);
        if (json == null) {
            return false;
        } else {
            Gson gson = new Gson();
            User user = gson.fromJson(json, User.class);
            User.setInstance(user);
            return true;
        }
    }

    public static void deleteInfoUser(Context context){
        User.setInstance(null);
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
    }

    public static void saveUser(Context context, User currentUser) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentUser);
        editor.putString(CustomSharedPreferences.SHARED_PREFERENCES_NAME, json);
        editor.commit();
        User.setInstance(currentUser);
    }
}
