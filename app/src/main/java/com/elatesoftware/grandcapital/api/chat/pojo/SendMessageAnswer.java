package com.elatesoftware.grandcapital.api.chat.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ярослав Левшунов on 01.03.2017.
 */

public class SendMessageAnswer {

    @SerializedName("caseId")
    @Expose
    private String caseId;

    @SerializedName("messageType")
    @Expose
    private String messageType;

    @SerializedName("messageBody")
    @Expose
    private String messageBody;

    private static SendMessageAnswer sendMessageAnswer = null;
    public static void setInstance(SendMessageAnswer _sendMessageAnswer) {
        sendMessageAnswer = _sendMessageAnswer;
    }
    public static SendMessageAnswer getInstance() {
        return sendMessageAnswer;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageBody() {
        return messageBody;
    }

    @Override
    public String toString() {
        return "messageBody: " + messageBody;
    }
}
