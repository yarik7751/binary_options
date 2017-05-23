package com.elatesoftware.grandcapital.api.pojo.pojo_chat;

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
    private boolean agentTyping;

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

    public boolean isAgentTyping() {
        return agentTyping;
    }

    public void setAgentTyping(boolean agentTyping) {
        this.agentTyping = agentTyping;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public ArrayList<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<Message> messageList) {
        this.messageList = messageList;
    }

    public static PollChatAnswer getPollChatAnswer() {
        return pollChatAnswer;
    }

    public static void setPollChatAnswer(PollChatAnswer pollChatAnswer) {
        PollChatAnswer.pollChatAnswer = pollChatAnswer;
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
