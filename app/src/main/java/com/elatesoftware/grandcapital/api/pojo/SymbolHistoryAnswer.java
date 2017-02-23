package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SymbolHistoryAnswer {

    @SerializedName("high")
    @Expose
    private Double high;
    @SerializedName("close")
    @Expose
    private Double close;
    @SerializedName("open")
    @Expose
    private Double open;
    @SerializedName("low")
    @Expose
    private Double low;
    @SerializedName("time")
    @Expose
    private Long time;

    private static List<SymbolHistoryAnswer> symbolInstance = null;
    public static List<SymbolHistoryAnswer> getInstance() {
        return symbolInstance;
    }
    public static void setInstance(List<SymbolHistoryAnswer> symbolList) {
        symbolInstance = symbolList;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public SymbolHistoryAnswer withHigh(Double high) {
        this.high = high;
        return this;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public SymbolHistoryAnswer withClose(Double close) {
        this.close = close;
        return this;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public SymbolHistoryAnswer withOpen(Double open) {
        this.open = open;
        return this;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public SymbolHistoryAnswer withLow(Double low) {
        this.low = low;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public SymbolHistoryAnswer withTime(Long time) {
        this.time = time;
        return this;
    }
}


