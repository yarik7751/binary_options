package com.elatesoftware.grandcapital.api.pojo.pojo_chat;


public class HistoryMessage {

    public boolean isTheir;
    public String text;
    public long time;

    public HistoryMessage(boolean isTheir, String text, long time) {
        this.isTheir = isTheir;
        this.text = text;
        this.time = time;
    }
}