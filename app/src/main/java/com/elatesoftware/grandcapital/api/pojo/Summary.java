package com.elatesoftware.grandcapital.api.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Summary {

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

    /**
     * @return
     * The marginFree
     */
    public Double getMarginFree() {
        return marginFree;
    }

    /**
     * @param marginFree
     * The margin_free
     */
    public void setMarginFree(Double marginFree) {
        this.marginFree = marginFree;
    }

    /**
     * @return
     * The credit
     */
    public Double getCredit() {
        return credit;
    }

    /**
     * @param credit
     * The credit
     */
    public void setCredit(Double credit) {
        this.credit = credit;
    }

    /**
     * @return
     * The equity
     */
    public Double getEquity() {
        return equity;
    }

    /**
     * @param equity
     * The equity
     */
    public void setEquity(Double equity) {
        this.equity = equity;
    }

    /**
     * @return
     * The closedProfit
     */
    public Double getClosedProfit() {
        return closedProfit;
    }

    /**
     * @param closedProfit
     * The closed_profit
     */
    public void setClosedProfit(Double closedProfit) {
        this.closedProfit = closedProfit;
    }

    /**
     * @return
     * The balance
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * @param balance
     * The balance
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * @return
     * The margin
     */
    public Double getMargin() {
        return margin;
    }

    /**
     * @param margin
     * The margin
     */
    public void setMargin(Double margin) {
        this.margin = margin;
    }

    /**
     * @return
     * The marginLevel
     */
    public Double getMarginLevel() {
        return marginLevel;
    }

    /**
     * @param marginLevel
     * The margin_level
     */
    public void setMarginLevel(Double marginLevel) {
        this.marginLevel = marginLevel;
    }

}
