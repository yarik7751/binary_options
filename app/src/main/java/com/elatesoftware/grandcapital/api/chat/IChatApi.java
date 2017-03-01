package com.elatesoftware.grandcapital.api.chat;

import com.elatesoftware.grandcapital.api.chat.pojo.ChatCreateAnswer;
import com.elatesoftware.grandcapital.api.chat.pojo.PollChatAnswer;
import com.elatesoftware.grandcapital.api.chat.pojo.SendMessageAnswer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Ярослав Левшунов on 28.02.2017.
 */

public interface IChatApi {

    String API_KEY = "0a79a7fafe494bdca35793a2e68cd847";

    @FormUrlEncoded
    @Headers("X-Api-Key:" + API_KEY)
    @POST("/api/v1/chat")
    Call<ChatCreateAnswer> createNewChat(
            @Field("widgetId") String widgetId,
            @Field("visitorMessage") String visitorMessage
    );

    @Headers("X-Api-Key:" + API_KEY)
    @GET("/api/v1/chat/poll/{caseId}")
    Call<PollChatAnswer> pollChat(@Path("caseId") String caseId);

    @FormUrlEncoded
    @Headers("X-Api-Key:" + API_KEY)
    @PUT("/api/v1/chat")
    Call<SendMessageAnswer> sendMessageChat(
            @Field("caseId") String caseId,
            @Field("messageType") Integer messageType,
            @Field("messageBody") String messageBody
    );
}
