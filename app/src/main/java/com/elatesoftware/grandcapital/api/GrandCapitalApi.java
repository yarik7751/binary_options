package com.elatesoftware.grandcapital.api;

import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.api.pojo.SummaryAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.SymbolHistoryService;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.ConventToJson;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
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
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
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
                AuthorizationAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }
        return result;
     }

    public static String getOrders() {
        Call<List<OrderAnswer>> call = getApiService().getOrders(User.getInstance().getLogin());
        Response<List<OrderAnswer>> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (final java.net.SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == 200) {
                List<OrderAnswer> list = response.body();
                Collections.sort(list, (o1, o2) -> o2.getOpenTime().compareTo(o1.getOpenTime()));
                OrderAnswer.setInstance(list);
            }
            result = String.valueOf(response.code());
        }
        return result;
    }

    public static String getInfoUser() {
        Call<InfoAnswer> call = getApiService().getInfo(User.getInstance().getLogin());
        Response<InfoAnswer> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (final java.net.SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == 200) {
                InfoAnswer.setInstance(response.body());
                User.getInstance().setUserName(InfoAnswer.getInstance().getName());
            }
            result = String.valueOf(response.code());
        }
        return result;
    }

    public static String getSummary() {
       Call<SummaryAnswer> call = getApiService().getSummary(User.getInstance().getLogin());
       Response<SummaryAnswer> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (final java.net.SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == 200) {
                SummaryAnswer.setInstance(response.body());
                Double balance = SummaryAnswer.getInstance().getBalance();
                User.getInstance().setBalance(balance);
            }
            result = String.valueOf(response.code());
        }
        return result;
    }

    public static String getSymbolHistory(String symbol) {
        Response<List<SymbolHistoryAnswer>> response = null;
        String result = null;
        //String toTime = ConventDate.getTimeStampLastDate();
        //String fromTime = ConventDate.getTimeStampCurrentDate();
        String toTime = "1487856360";
        String fromTime = "1487853000";
        symbol = (symbol + "_OP");
        Call<List<SymbolHistoryAnswer>> call = getApiService().getSymbolHistory(User.getInstance().getLogin(), fromTime, toTime, "1", symbol);
        try {
            response = call.execute();
         } catch (final java.net.SocketTimeoutException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == 200) {
                List<SymbolHistoryAnswer> list = response.body();
                SymbolHistoryAnswer.setInstance(list);
            }
            result = String.valueOf(response.code());
        }
        return result;
    }
}
