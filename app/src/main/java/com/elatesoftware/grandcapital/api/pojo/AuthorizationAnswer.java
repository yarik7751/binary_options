package com.elatesoftware.grandcapital.api.pojo;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class AuthorizationAnswer {

    @SerializedName("server_name")
    @Expose
    private String serverName;

    @SerializedName("token")
    @Expose
    private String token;

    private static AuthorizationAnswer authInstance = null;
    public static AuthorizationAnswer getInstance() {
        return authInstance;
    }
    public static void setInstance(AuthorizationAnswer auth) {
        authInstance = auth;
    }
    /**
     * @return
     * The serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @param serverName
     * The server_name
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @return
     * The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     * The token
     */
    public void setToken(String token) {
        this.token = token;
    }
/*
    @Override
    public String toString() {
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("nameServer", getServerName());
        jsonParams.put("token", getToken());
        return new Gson().toJson(jsonParams);
    }

    public static AuthorizationAnswer getAuthorizationAnswer(String response){
        JSONObject obj = null;
        AuthorizationAnswer answer = new AuthorizationAnswer();
        if(response != null){
            try {
                obj = new JSONObject(response);
                answer.setServerName(obj.getString("nameServer"));
                answer.setToken(obj.getString("token"));
                return answer;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }*/
}