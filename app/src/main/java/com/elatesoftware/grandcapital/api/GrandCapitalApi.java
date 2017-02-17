package com.elatesoftware.grandcapital.api;

import android.support.v4.util.ArrayMap;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.api.pojo.Order;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.utils.ConventFromJson;
import com.elatesoftware.grandcapital.utils.ConventToJson;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GrandCapitalApi {

    private static final String BASE_URL = "https://grandcapital.ru";

    private static IGrandCapitalApi grandCapitalApiService = null;

    private static IGrandCapitalApi getApiService() {
        if (grandCapitalApiService == null) {
            CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(GrandCapitalApplication.getAppContext()));
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            grandCapitalApiService = retrofit.create(IGrandCapitalApi.class);
        }
        return grandCapitalApiService;
    }
/*
    public static Call<AuthorizationAnswer> tryAuthorize(String login, String password) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), ConventToJson.getJsonRequestSignIn(login, password));
        return getApiService().tryToAuthorize(body);
    }*/

    public static String authorizationRequest(String login, String password){
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), ConventToJson.getJsonRequestSignIn(login, password));
        Call<AuthorizationAnswer> call = getApiService().tryToAuthorize(body);
        Response<AuthorizationAnswer> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == 200) {
                AuthorizationAnswer authorizationAnswer = response.body();
                result = authorizationAnswer.toString();
            }else if(response.code() == 400){
                result = "400";
            }
        }
        return result;
     }


    public static Call<List<Order>> getOrders(String login) {
        return getApiService().getOrders(login);
    }
}
