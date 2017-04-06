package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
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

    private SymbolHistoryAnswer(Double high, Double close, Double open, Double low, Long time) {
        this.high = high;
        this.close = close;
        this.open = open;
        this.low = low;
        this.time = time;
    }

    private static List<SymbolHistoryAnswer> symbolInstance = null;
    public static List<SymbolHistoryAnswer> getInstance() {
        sortList();
        return symbolInstance;
    }
    public static void setInstance(List<SymbolHistoryAnswer> symbolList) {
        symbolInstance = symbolList;
    }
    public static void addSocketAnswerInSymbol(final SocketAnswer item) {
        if (SymbolHistoryAnswer.getInstance() != null) {
            SymbolHistoryAnswer.getInstance().add(new SymbolHistoryAnswer(item.getHigh(), item.getBid(), item.getAsk(), item.getLow(), item.getTime()));
        }
    }
    private static void sortList(){
        if(symbolInstance != null){
            Collections.sort((List)symbolInstance, new Comparator<SymbolHistoryAnswer>() {
                @Override
                public int compare(SymbolHistoryAnswer o1, SymbolHistoryAnswer o2) {
                    return o1.getTime().compareTo(o2.getTime());
                }
            });
        }
    }

    public static void nullInstance() {
        if(symbolInstance != null){
            symbolInstance.clear();
        }
        symbolInstance = null;
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


