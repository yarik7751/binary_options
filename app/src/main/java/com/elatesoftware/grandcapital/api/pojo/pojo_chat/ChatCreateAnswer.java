package com.elatesoftware.grandcapital.api.pojo.pojo_chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ярослав Левшунов on 28.02.2017.
 */

public class ChatCreateAnswer {

    @SerializedName("caseId")
    @Expose
    private String caseId;

    private static ChatCreateAnswer chatCreateAnswer = null;
    public static void setInstance(ChatCreateAnswer _chatCreateAnswer) {
        chatCreateAnswer = _chatCreateAnswer;
    }
    public static ChatCreateAnswer getInstance() {
        return chatCreateAnswer;
    }

    public String getCaseId() {
        return caseId;
    }

    @Override
    public String toString() {
        return "caseId: " + caseId;
    }
}
