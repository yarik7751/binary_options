package com.elatesoftware.grandcapital.api;

import com.elatesoftware.grandcapital.api.pojo.pojo_chat.ChatCreateAnswer;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.PollChatAnswer;
import com.elatesoftware.grandcapital.api.pojo.pojo_chat.SendMessageAnswer;
import com.elatesoftware.grandcapital.api.pojo.BinaryOptionAnswer;
import com.elatesoftware.grandcapital.api.pojo.EarlyClosureAnswer;
import com.elatesoftware.grandcapital.api.pojo.InOutAnswer;
import com.elatesoftware.grandcapital.api.pojo.QuestionsAnswer;
import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.api.pojo.SignalAnswer;
import com.elatesoftware.grandcapital.api.pojo.SummaryAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;

import java.util.ArrayList;
import java.util.List;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface IGrandCapitalApi {

    @POST("/my/webtrader/api/account/signin/")
    Call<AuthorizationAnswer> tryToAuthorize(@Body RequestBody params);

    @GET("/my/webtrader/api/account/{login}/trade/")
    Call<List<OrderAnswer>> getOrders(@Path("login") String login);

    @GET("/my/webtrader/api/account/{login}/info/")
    Call<InfoAnswer> getInfo(@Path("login") String login);

    @GET("/my/webtrader/api/account/{login}/summary/")
    Call<SummaryAnswer> getSummary(@Path("login") String login);

    @GET("/my/webtrader/api/account/{login}/symbol_history/")
    Call<List<SymbolHistoryAnswer>> getSymbolHistory(@Path("login") String login,
                                        @Query ("from") String from,
                                        @Query ("to") String to,
                                        @Query ("resolution") String resolution,
                                        @Query ("symbol") String symbol);

    @GET("/api/faq/questions?category=options")
    Call<QuestionsAnswer> getQuestions(@Query("page") int page);

    @GET("/api/bonus/bonus_description")
    Call<BinaryOptionAnswer> getBinaryOption();

    @GET("/my/webtrader/api/account/{login}/payments/{tran}/")
    Call<ArrayList<InOutAnswer>> getMoneyTransactions(@Path("login") String login, @Path("tran") String tran);

    @POST("/my/webtrader/api/account/{login}/trade/order/")
    @FormUrlEncoded
    Call<ResponseBody> makeDealing(@Header("X-Trader-Token") String token,
                                   @Path("login") String login,
                                   @Field("cmd") String cmd,
                                   @Field("volume") String volume,
                                   @Field("symbol") String symbol,
                                   @Field("expiration") String expiration);

    @GET("https://grandcapital.net/api/technical_summary/")
    Call<List<SignalAnswer>> getSignals();

    @GET("/my/webtrader/api/account/options/info/")
    Call<EarlyClosureAnswer> getEarlyClosureAnswer();

    @DELETE("/my/webtrader/api/account/{login}/trade/{ticket}/")
    Call<ResponseBody> deleteDealing(@Header("X-Trader-Token") String token,
                                     @Path("login") String login,
                                     @Path("ticket") Integer ticket);

    @FormUrlEncoded
    @Headers({
            "X-Api-Key: " + GrandCapitalApi.API_KEY_CHART
    })
    @POST("/api/v1/chat")
    Call<ChatCreateAnswer> createNewChat(
            @Field("widgetId") String widgetId,
            @Field("visitorMessage") String visitorMessage
    );

    @Headers({
            "X-Api-Key: " + GrandCapitalApi.API_KEY_CHART,
            "User-Agent: runscope/0.1"
    })
    @GET("/api/v1/chat/poll/{caseId}")
    Call<PollChatAnswer> pollChat(@Path("caseId") String caseId);

    @FormUrlEncoded
    @Headers({
            "X-Api-Key: " + GrandCapitalApi.API_KEY_CHART,
            "User-Agent: runscope/0.1"
    })
    @PUT("/api/v1/chat")
    Call<SendMessageAnswer> sendMessageChat(
            @Field("caseId") String caseId,
            @Field("messageType") Integer messageType,
            @Field("messageBody") String messageBody
    );
}
