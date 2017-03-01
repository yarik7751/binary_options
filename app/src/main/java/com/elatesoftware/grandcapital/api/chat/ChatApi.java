package com.elatesoftware.grandcapital.api.chat;

import com.elatesoftware.grandcapital.api.chat.pojo.ChatCreateAnswer;
import com.elatesoftware.grandcapital.api.chat.pojo.PollChatAnswer;
import com.elatesoftware.grandcapital.api.chat.pojo.SendMessageAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

/**
 * Created by Ярослав Левшунов on 28.02.2017.
 */

public class ChatApi {

    private static final String BASE_URL = "https://www.snapengage.com";
    private static IChatApi chatApiService = null;

    private static IChatApi getApiService() {
        if (chatApiService == null) {

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
            chatApiService = retrofit.create(IChatApi.class);
        }
        return chatApiService;
    }

    public static String createNewChat(String widgetId, String visitorMessage) {
        Call<ChatCreateAnswer> call = getApiService().createNewChat(widgetId, visitorMessage);
        Response<ChatCreateAnswer> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null) {
            if(response.code() == 200) {
                ChatCreateAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }

        return result;
    }

    public static String pollChat(String caseId) {
        Call<PollChatAnswer> call = getApiService().pollChat(caseId);
        Response<PollChatAnswer> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null) {
            if(response.code() == 200) {
                PollChatAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }

        return result;
    }

    public static String sendMessage(String caseId, Integer messageType, String messageBody) {
        Call<SendMessageAnswer> call = getApiService().sendMessageChat(caseId, messageType, messageBody);
        Response<SendMessageAnswer> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null) {
            if(response.code() == 200) {
                SendMessageAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }

        return result;
    }
}
