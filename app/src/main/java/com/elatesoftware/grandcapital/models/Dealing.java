package com.elatesoftware.grandcapital.models;


public class Dealing {

    private String symbol;
    private int volume;
    private int expiration;
    private String cmd;

    public Dealing() {

    }

    public Dealing(String symbol, int volume, int expiration, String cmd) {
        this.symbol = symbol;
        this.volume = volume;
        this.expiration = expiration;
        this.cmd = cmd;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }
}
