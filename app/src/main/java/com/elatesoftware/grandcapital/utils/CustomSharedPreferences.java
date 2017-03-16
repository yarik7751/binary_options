package com.elatesoftware.grandcapital.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.elatesoftware.grandcapital.models.User;
import com.google.gson.Gson;

/**
 * Created by Дарья Высокович on 16.02.2017.
 */

public class CustomSharedPreferences {

     private static final String SHARED_PREFERENCES = "com.elatesoftware.grandcapital.user";
     private static final String SHARED_PREFERENCES_USER = "USER";
     private static final String SHARED_PREFERENCES_CHAT_HISTORY = "SHARED_PREFERENCES_CHAT_HISTORY";
     private static final String SHARED_PREFERENCES_SELECTED_QUOTES = "SHARED_PREFERENCES_SELECTED_QUOTES";
     private static final String SHARED_PREFERENCES_CLOSE_DEALINGS = "SHARED_PREFERENCES_CLOSE_DEALINGS";

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

    public static void saveChatHistory(Context context, String history) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SHARED_PREFERENCES_CHAT_HISTORY, history).commit();
    }

    public static String getChatHistory(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREFERENCES_CHAT_HISTORY, null);
    }

    public static void saveSelectedQuotes(Context context, String quotes) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SHARED_PREFERENCES_SELECTED_QUOTES, quotes).commit();
    }

    public static String getSelectedQuotes(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREFERENCES_SELECTED_QUOTES, "");
    }

    public static void setAmtCloseDealings(Context context, int amt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(SHARED_PREFERENCES_CLOSE_DEALINGS, amt).commit();
    }

    public static int getAmtCloseDealings(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SHARED_PREFERENCES_CLOSE_DEALINGS, 0);
    }
}
