package com.elatesoftware.grandcapital.api;

import android.util.Log;

import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.api.pojo.BinaryOptionAnswer;
import com.elatesoftware.grandcapital.api.pojo.EarlyClosureAnswer;
import com.elatesoftware.grandcapital.api.pojo.InOutAnswer;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.api.pojo.QuestionsAnswer;
import com.elatesoftware.grandcapital.api.pojo.SignalAnswer;
import com.elatesoftware.grandcapital.api.pojo.SummaryAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.ChatCreateAnswer;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.PollChatAnswer;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.SendMessageAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.utils.ConventToJson;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GrandCapitalApi {

    public static final String TAG = "GrandCapitalApi_Log";

    private static IGrandCapitalApi grandCapitalApiService = null;
    private static IGrandCapitalApi grandCapitalApiSimpleService = null;
    private static IGrandCapitalApi grandCapitalApiServiceChat = null;

    private static final String BASE_URL = "https://grandcapital.ru";
    public static final String SOCKET_URL = "wss://ws.grandcapital.net/";
    private static final String BASE_URL_CHAT = "https://www.snapengage.com";
    static final String API_KEY_CHART = "0a79a7fafe494bdca35793a2e68cd847";

    private final static int CODE_SUCCESS = 200;
    private final static int CODE_SUCCESS_DELETE_DEALING = 204;

    private static IGrandCapitalApi getApiService() {
        if (grandCapitalApiService == null) {
            CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(GrandCapitalApplication.getAppContext()));
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .addInterceptor(interceptor)
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
    private static IGrandCapitalApi getSimpleService() {
        if (grandCapitalApiService == null) {
            CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(GrandCapitalApplication.getAppContext()));
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            grandCapitalApiService = retrofit.create(IGrandCapitalApi.class);
        }
        return grandCapitalApiService;
    }
    private static IGrandCapitalApi getApiServiceChart() {
        if (grandCapitalApiServiceChat == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(5, TimeUnit.MINUTES);
            httpClient.writeTimeout(5, TimeUnit.MINUTES);
            httpClient.readTimeout(5, TimeUnit.MINUTES);

            httpClient.addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_CHAT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            grandCapitalApiServiceChat = retrofit.create(IGrandCapitalApi.class);
        }
        return grandCapitalApiServiceChat;
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
            if(response.code() == CODE_SUCCESS) {
                AuthorizationAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }
        return result;
     }
    public static String getOrders() {
        if(User.getInstance() != null && User.getInstance().getLogin() != null){
            Call<List<OrderAnswer>> call = getApiService().getOrders(User.getInstance().getLogin());
            Response<List<OrderAnswer>> response = null;
            String result = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response != null){
                if(response.code() == CODE_SUCCESS) {
                    List<OrderAnswer> list = response.body();
                    Collections.sort(list, (o1, o2) -> o2.getOpenTime().compareTo(o1.getOpenTime()));
                    OrderAnswer.setInstance(list);
                }
                result = String.valueOf(response.code());
            }
            return result;
        }else{
            return "";
        }
    }
    public static String getInfoUser() {
        String result = null;
        if(User.getInstance()!= null && User.getInstance().getLogin() != null && !User.getInstance().getLogin().isEmpty()){
            Call<InfoAnswer> call = getApiService().getInfo(User.getInstance().getLogin());
            Response<InfoAnswer> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response != null){
                if(response.code() == CODE_SUCCESS) {
                    InfoAnswer.setInstance(response.body());
                    User.getInstance().setUserName(InfoAnswer.getInstance().getName());
                    CustomSharedPreferences.updateInfoUser(GrandCapitalApplication.getAppContext());
                    if(InfoAnswer.getInstance() != null) {
                        if(InfoAnswer.getInstance().getServerName().contains("demo") && CustomSharedPreferences.getIntervalAdvertising(GrandCapitalApplication.getAppContext()) <= 0) {
                            CustomSharedPreferences.setIntervalAdvertising(GrandCapitalApplication.getAppContext(), 0);
                        } else if(!InfoAnswer.getInstance().getServerName().contains("demo")) {
                            CustomSharedPreferences.setIntervalAdvertising(GrandCapitalApplication.getAppContext(), -1);
                        }
                        Log.d(GrandCapitalApplication.TAG_SOCKET, "IntervalAdvertising: " + CustomSharedPreferences.getIntervalAdvertising(GrandCapitalApplication.getAppContext()));
                    }
                }
                result = String.valueOf(response.code());
            }
        }
        return result;
    }
    public static String getSummary() {
        String result = null;
        if(User.getInstance() != null && User.getInstance().getLogin() != null){
            Call<SummaryAnswer> call = getApiService().getSummary(User.getInstance().getLogin());
            Response<SummaryAnswer> response = null;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response != null){
                if(response.code() == CODE_SUCCESS) {
                    SummaryAnswer.setInstance(response.body());
                    Double balance = SummaryAnswer.getInstance().getBalance();
                    User.getInstance().setBalance(balance);
                }
                result = String.valueOf(response.code());
            }
        }
        return result;
    }

    public static String getSignals(){
        Call<List<SignalAnswer>> call = getSimpleService().getSignals();
        Response<List<SignalAnswer>> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == CODE_SUCCESS) {
               SignalAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }
        return result;
    }
    public static String getSymbolHistory(String symbol) {
        Response<List<SymbolHistoryAnswer>> response = null;
        String result = null;
        String toTime = ConventDate.getTimeStampCurrentDate();
        String fromTime = ConventDate.getTimeStampLastDate();
        Log.d(TAG, "toTime: " + toTime);
        Call<List<SymbolHistoryAnswer>> call = getApiService().getSymbolHistory(User.getInstance().getLogin(), fromTime, toTime, "1", symbol);
        try {
            response = call.execute();
         } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == CODE_SUCCESS) {
                List<SymbolHistoryAnswer> list = response.body();
                if(list == null || list.size() < 2){
                    SymbolHistoryAnswer.setInstance(null);
                }else{
                    long currentDate = 0L;
                    for (int i = list.size() - 1; i >= 0; i--){
                        if(!ConventDate.equalsTimeSymbols(currentDate, list.get(i).getTime())){
                            list.add(list.get(i));
                            currentDate = list.get(i).getTime();
                        }
                    }
                    SymbolHistoryAnswer.setInstance(list);
                }
            }
            result = String.valueOf(response.code());
        }
        return result;
    }
    public static String getMakeDealing(String cmd, String volume, String symbol, String expiration){
        Call<ResponseBody> call = getApiService().makeDealing(User.getInstance().getToken(),
                                                              User.getInstance().getLogin(),
                                                              cmd, volume, symbol, expiration);
        Response<ResponseBody> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null){
            if(response.code() == CODE_SUCCESS) {
                try {
                    result = response.body().string();
                    try {
                        JSONObject json = new JSONObject(result);
                        if(json.has("success")){
                            if(json.getBoolean("success")){
                               return "true";
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            result = String.valueOf(response.code());
        }
        return result;
    }
    public static String getQuestions(int page) {
        String language = Locale.getDefault().getLanguage();
        if(!ConventString.isOurLanguage(language)) {
            language = "en";
        }
        Call<QuestionsAnswer> call = getApiService().getQuestions(page, language);
        Response<QuestionsAnswer> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null) {
            if(response.code() == CODE_SUCCESS) {
                QuestionsAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }

        return result;
    }
    public static String getEarlyClosureAnswer() {
        Call<EarlyClosureAnswer> call = getApiService().getEarlyClosureAnswer();
        Response<EarlyClosureAnswer> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null) {
            if(response.code() == CODE_SUCCESS) {
                EarlyClosureAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }

        return result;
    }
    public static String getBinaryOption() {
        Call<BinaryOptionAnswer> call = getApiService().getBinaryOption();
        Response<BinaryOptionAnswer> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null) {
            if(response.code() == CODE_SUCCESS) {
                BinaryOptionAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }
        return result;
    }
    public static String getMoneyTransactions(String tran) {
        Call<ArrayList<InOutAnswer>> call = getApiService().getMoneyTransactions(User.getInstance().getLogin(), tran);
        Response<ArrayList<InOutAnswer>> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null) {
            if(response.code() == CODE_SUCCESS) {
                InOutAnswer.setInstance(response.body());
            }
            result = String.valueOf(response.code());
        }
        return result;
    }
    public static String getDeleteDealing(int ticket) {
        Call<ResponseBody> call = getApiService().deleteDealing(User.getInstance().getToken(), User.getInstance().getLogin(), ticket);
        Response<ResponseBody> response = null;
        String result = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null) {
            result = String.valueOf(response.code());
        }
        return result;
    }
    public static String createNewChat(String widgetId, String visitorMessage) {
        Call<ChatCreateAnswer> call = getApiServiceChart().createNewChat(widgetId, visitorMessage);
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
        Call<PollChatAnswer> call = getApiServiceChart().pollChat(caseId);
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
        Call<SendMessageAnswer> call = getApiServiceChart().sendMessageChat(caseId, messageType, messageBody);
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
