package com.elatesoftware.grandcapital.api;

import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Order;
import com.elatesoftware.grandcapital.api.pojo.SummaryAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.utils.ConventToJson;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.util.List;

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

    public static Call<List<Order>> getOrders() {
        return getApiService().getOrders(User.getInstance().getLogin());
    }

    public static void getInfoUser() {
        Call<InfoAnswer> responseBodyCall = getApiService().getInfo(User.getInstance().getLogin());
        Response<InfoAnswer> response = null;
        try {
            response = responseBodyCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == 200) {
                InfoAnswer infoAnswer = response.body();
                User.getInstance().setUserName(infoAnswer.getName());
            }
        }
    }

    public static void getSummary() {
       Call<SummaryAnswer> responseCall = getApiService().getSummary(User.getInstance().getLogin());
       Response<SummaryAnswer> response = null;
        try {
            response = responseCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == 200) {
                SummaryAnswer answer = response.body();
                Double balance = answer.getBalance();
                User.getInstance().setBalance(balance);
            }
        }
    }
}
