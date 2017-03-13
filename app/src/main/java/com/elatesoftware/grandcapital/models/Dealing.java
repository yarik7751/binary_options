package com.elatesoftware.grandcapital.models;


public class Dealing {

    public String active, amount;
    public int timeMin;
    public long createTime;

    public Dealing(String active, String amount, int timeMin, long createTime) {
        this.active = active;
        this.amount = amount;
        this.timeMin = timeMin;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "active: " + active + ", amount: " + amount + ", timeMin: " + timeMin + ", createTime: " + createTime;
    }
}
