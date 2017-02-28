package com.elatesoftware.grandcapital.api;

import com.elatesoftware.grandcapital.api.pojo.BinaryOptionAnswer;
import com.elatesoftware.grandcapital.api.pojo.QuestionsAnswer;
import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.api.pojo.SummaryAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IGrandCapitalApi {

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
}
