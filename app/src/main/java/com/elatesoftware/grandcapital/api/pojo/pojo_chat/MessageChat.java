package com.elatesoftware.grandcapital.api.pojo.pojo_chat;

import com.orm.SugarRecord;

public class MessageChat extends SugarRecord {

    private String text;
    private long time;
    private boolean isTheir, isTyping = false, isLoading = true;

    public MessageChat(String text, long time, boolean isTheir) {
        this.text = text;
        this.time = time;
        this.isTheir = isTheir;
        if(isTheir) {
            isLoading = false;
        }
    }

    public MessageChat() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTheir() {
        return isTheir;
    }

    public void setTheir(boolean their) {
        isTheir = their;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
