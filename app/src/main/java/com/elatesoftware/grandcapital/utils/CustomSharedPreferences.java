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

     private static final String SHARED_PREFERENCES = "com.elatesoftware.grandcapital.user";
     private static final String SHARED_PREFERENCES_USER = "user";

    public static boolean isSaveUserInPreferences(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(SHARED_PREFERENCES_USER, null);
        if (json == null) {
            return false;
        } else {
            Gson gson = new Gson();
            User user = gson.fromJson(json, User.class);
            User.setInstance(user);
            return true;
        }
    }
    public static void updateInfoUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(User.getInstance());
        editor.putString(CustomSharedPreferences.SHARED_PREFERENCES_USER, json);
        editor.commit();
    }

    public static void deleteInfoUser(Context context){
        User.setInstance(null);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CustomSharedPreferences.SHARED_PREFERENCES_USER, null);
        editor.commit();
    }

    public static void saveUser(Context context, User currentUser) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentUser);
        editor.putString(CustomSharedPreferences.SHARED_PREFERENCES_USER, json);
        editor.commit();
        User.setInstance(currentUser);
    }
}
