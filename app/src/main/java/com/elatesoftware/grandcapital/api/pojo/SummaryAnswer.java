
package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummaryAnswer {

    @SerializedName("margin_free")
    @Expose
    private Double marginFree;
    @SerializedName("credit")
    @Expose
    private Double credit;
    @SerializedName("equity")
    @Expose
    private Double equity;
    @SerializedName("closed_profit")
    @Expose
    private Double closedProfit;
    @SerializedName("balance")
    @Expose
    private Double balance;
    @SerializedName("margin")
    @Expose
    private Double margin;
    @SerializedName("margin_level")
    @Expose
    private Double marginLevel;

    private static SummaryAnswer summaryInstance = null;
    public static SummaryAnswer getInstance() {
        return summaryInstance;
    }
    public static void setInstance(SummaryAnswer summary) {
        summaryInstance = summary;
    }

    public Double getMarginFree() {
        return marginFree;
    }

    public void setMarginFree(Double marginFree) {
        this.marginFree = marginFree;
    }

    public SummaryAnswer withMarginFree(Double marginFree) {
        this.marginFree = marginFree;
        return this;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public SummaryAnswer withCredit(Double credit) {
        this.credit = credit;
        return this;
    }

    public Double getEquity() {
        return equity;
    }

    public void setEquity(Double equity) {
        this.equity = equity;
    }

    public SummaryAnswer withEquity(Double equity) {
        this.equity = equity;
        return this;
    }

    public Double getClosedProfit() {
        return closedProfit;
    }

    public void setClosedProfit(Double closedProfit) {
        this.closedProfit = closedProfit;
    }

    public SummaryAnswer withClosedProfit(Double closedProfit) {
        this.closedProfit = closedProfit;
        return this;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public SummaryAnswer withBalance(Double balance) {
        this.balance = balance;
        return this;
    }

    public Double getMargin() {
        return margin;
    }

    public void setMargin(Double margin) {
        this.margin = margin;
    }

    public SummaryAnswer withMargin(Double margin) {
        this.margin = margin;
        return this;
    }

    public Double getMarginLevel() {
        return marginLevel;
    }

    public void setMarginLevel(Double marginLevel) {
        this.marginLevel = marginLevel;
    }

    public SummaryAnswer withMarginLevel(Double marginLevel) {
        this.marginLevel = marginLevel;
        return this;
    }

}
