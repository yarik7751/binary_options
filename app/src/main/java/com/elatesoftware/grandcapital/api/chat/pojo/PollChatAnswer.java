package com.elatesoftware.grandcapital.api.chat.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ярослав Левшунов on 01.03.2017.
 */

public class PollChatAnswer {

    @SerializedName("caseId")
    @Expose
    private String caseId;

    @SerializedName("agentTyping")
    @Expose
    private String agentTyping;

    @SerializedName("messageList")
    @Expose
    ArrayList<Message> messageList = new ArrayList<>();

    private static PollChatAnswer pollChatAnswer = null;
    public static void setInstance(PollChatAnswer _pollChatAnswer) {
        pollChatAnswer = _pollChatAnswer;
    }
    public static PollChatAnswer getInstance() {
        return pollChatAnswer;
    }

    public String getCaseId() {
        return caseId;
    }

    public String getAgentTyping() {
        return agentTyping;
    }

    public ArrayList<Message> getMessageList() {
        ArrayList<Message> ans = new ArrayList<>();
        for(Message msg : messageList) {
            if(msg.getType().equals("agent")) {
                ans.add(msg);
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "caseId: " + caseId + "\n" +
                "agentTyping: " + agentTyping + "\n" +
                "messageList: " + messageList + "\n";
    }

    public static class Message {

        @SerializedName("index")
        @Expose
        private Integer index;

        @SerializedName("type")
        @Expose
        private String type;

        @SerializedName("author")
        @Expose
        private String author;

        @SerializedName("text")
        @Expose
        private String text;

        public Integer getIndex() {
            return index;
        }

        public String getType() {
            return type;
        }

        public String getAuthor() {
            return author;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return "\nindex: " + index + "\n" +
                    "type: " + type + "\n" +
                    "author: " + author + "\n" +
                    "text: " + text + "\n";
        }
    }
}
