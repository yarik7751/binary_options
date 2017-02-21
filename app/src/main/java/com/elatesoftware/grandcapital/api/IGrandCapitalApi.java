package com.elatesoftware.grandcapital.api;

import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Order;
import com.elatesoftware.grandcapital.api.pojo.SummaryAnswer;

import java.util.List;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IGrandCapitalApi {

    @POST("/my/webtrader/api/account/signin/")
    Call<AuthorizationAnswer> tryToAuthorize(@Body RequestBody params);

    @GET("/my/webtrader/api/account/{login}/trade/")
    Call<List<Order>> getOrders(@Path("login") String login);

    @GET("/my/webtrader/api/account/{login}/info/")
    Call<InfoAnswer> getInfo(@Path("login") String login);

    @GET("/my/webtrader/api/account/{login}/summary/")
    Call<SummaryAnswer> getSummary(@Path("login") String login);

    @GET("/my/webtrader/api/account/{login}/symbol_history/")
    Call<ResponseBody> getSymbolHistory(@Path("login") String login);

}
