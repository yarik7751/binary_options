package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by Дарья Высокович on 28.02.2017.
 */

public class SocketAnswer {

    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("bid")
    @Expose
    private Double bid;
    @SerializedName("ask")
    @Expose
    private Double ask;
    @SerializedName("digits")
    @Expose
    private Long digits;
    @SerializedName("count")
    @Expose
    private Long count;
    @SerializedName("point")
    @Expose
    private Double point;
    @SerializedName("high")
    @Expose
    private Double high;
    @SerializedName("low")
    @Expose
    private Double low;
    @SerializedName("time")
    @Expose
    private Long time;

    public static SocketAnswer answersInstance = null;
    public static SocketAnswer getInstance() {
        return answersInstance;
    }
    public static void setInstance(SocketAnswer answers) {
        answersInstance = answers;
    }
    public static void setInstance(String message) {
        answersInstance =  new Gson().fromJson(message, SocketAnswer.class);
    }
    public static SocketAnswer getSetInstance(String message) {
        answersInstance = new Gson().fromJson(message, SocketAnswer.class);
        return answersInstance;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public SocketAnswer withSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public SocketAnswer withBid(Double bid) {
        this.bid = bid;
        return this;
    }

    public Double getAsk() {
        return ask;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }

    public SocketAnswer withAsk(Double ask) {
        this.ask = ask;
        return this;
    }

    public Long getDigits() {
        return digits;
    }

    public void setDigits(Long digits) {
        this.digits = digits;
    }

    public SocketAnswer withDigits(Long digits) {
        this.digits = digits;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public SocketAnswer withCount(Long count) {
        this.count = count;
        return this;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }

    public SocketAnswer withPoint(Double point) {
        this.point = point;
        return this;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public SocketAnswer withHigh(Double high) {
        this.high = high;
        return this;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public SocketAnswer withLow(Double low) {
        this.low = low;
        return this;
    }

    public Long getTime() {
        return (time * 1000);
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public SocketAnswer withTime(Long time) {
        this.time = time;
        return this;
    }
}