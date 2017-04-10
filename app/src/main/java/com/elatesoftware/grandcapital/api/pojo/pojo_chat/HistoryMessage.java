package com.elatesoftware.grandcapital.api.pojo.pojo_chat;


import com.orm.SugarRecord;

public class HistoryMessage extends SugarRecord {

    public boolean isTheir;
    public String text;
    public long time;

    public HistoryMessage() {
    }

    public HistoryMessage(boolean isTheir, String text, long time) {
        this.isTheir = isTheir;
        this.text = text;
        this.time = time;
    }

    @Override
    public String toString() {
        return "isTheir: " + isTheir + ", text: " + text + ", time: " + time;
    }
}
