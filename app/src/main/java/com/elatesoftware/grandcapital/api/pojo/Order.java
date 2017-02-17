package com.elatesoftware.grandcapital.api.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Order {

    @SerializedName("close_price")
    @Expose
    private Double closePrice;
    @SerializedName("close_time")
    @Expose
    private String closeTime;
    @SerializedName("cmd")
    @Expose
    private Integer cmd;
    @SerializedName("commission")
    @Expose
    private Double commission;
    @SerializedName("commission_agent")
    @Expose
    private Double commissionAgent;
    @SerializedName("expiration")
    @Expose
    private String expiration;
    @SerializedName("open_price")
    @Expose
    private Double openPrice;
    @SerializedName("open_time")
    @Expose
    private String openTime;
    @SerializedName("options_data")
    @Expose
    private OptionsData optionsData;
    @SerializedName("profit")
    @Expose
    private Double profit;
    @SerializedName("sl")
    @Expose
    private Double sl;
    @SerializedName("swaps")
    @Expose
    private Double swaps;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("taxes")
    @Expose
    private Double taxes;
    @SerializedName("ticket")
    @Expose
    private Integer ticket;
    @SerializedName("tp")
    @Expose
    private Double tp;
    @SerializedName("volume")
    @Expose
    private Integer volume;

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommissionAgent() {
        return commissionAgent;
    }

    public void setCommissionAgent(Double commissionAgent) {
        this.commissionAgent = commissionAgent;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public OptionsData getOptionsData() {
        return optionsData;
    }

    public void setOptionsData(OptionsData optionsData) {
        this.optionsData = optionsData;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getSl() {
        return sl;
    }

    public void setSl(Double sl) {
        this.sl = sl;
    }

    public Double getSwaps() {
        return swaps;
    }

    public void setSwaps(Double swaps) {
        this.swaps = swaps;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Double getTp() {
        return tp;
    }

    public void setTp(Double tp) {
        this.tp = tp;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

}