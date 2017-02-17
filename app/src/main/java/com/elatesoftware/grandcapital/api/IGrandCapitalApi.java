package com.elatesoftware.grandcapital.api;

import com.elatesoftware.grandcapital.api.pojo.AuthorizationAnswer;
import com.elatesoftware.grandcapital.api.pojo.Order;

import java.util.List;

import okhttp3.RequestBody;
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
}
