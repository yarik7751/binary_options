package com.elatesoftware.grandcapital.utils;

import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Дарья Высокович on 17.02.2017.
 */

public class ConventToJson {

    public static String getJsonRequestSignIn(final String login, final String password){
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("login", login);
        jsonParams.put("password", password);
        return new Gson().toJson(jsonParams);
    }

    public static String getJsonRequestMake(final String cmd, final String volume, final String expiration, final String symbol){
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("cmd", cmd);
        jsonParams.put("volume", volume);
        jsonParams.put("expiration", expiration);
        jsonParams.put("symbol", symbol);
        return new Gson().toJson(jsonParams);
    }

}
