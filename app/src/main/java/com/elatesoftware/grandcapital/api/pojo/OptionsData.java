package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OptionsData {

    @SerializedName("early_commission")
    @Expose
    private Integer earlyCommission;

    @SerializedName("early_pay")
    @Expose
    private Integer earlyPay;

    @SerializedName("expiration_time")
    @Expose
    private String expirationTime;

    @SerializedName("loss_pay")
    @Expose
    private Integer lossPay;

    @SerializedName("nill_pay")
    @Expose
    private Integer nillPay;

    @SerializedName("option_type")
    @Expose
    private Integer optionType;

    @SerializedName("win_pay")
    @Expose
    private Integer winPay;

    public OptionsData() {
        this.earlyCommission = 0;
        this.earlyPay = 0;
        this.expirationTime = "";
        this.lossPay = 0;
        this.nillPay = 0;
        this.optionType = 0;
        this.winPay = 0;
    }

    public Integer getEarlyCommission() {
        return earlyCommission;
    }

    public void setEarlyCommission(Integer earlyCommission) {
        this.earlyCommission = earlyCommission;
    }

    public Integer getEarlyPay() {
        return earlyPay;
    }

    public void setEarlyPay(Integer earlyPay) {
        this.earlyPay = earlyPay;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getLossPay() {
        return lossPay;
    }

    public void setLossPay(Integer lossPay) {
        this.lossPay = lossPay;
    }

    public Integer getNillPay() {
        return nillPay;
    }

    public void setNillPay(Integer nillPay) {
        this.nillPay = nillPay;
    }

    public Integer getOptionType() {
        return optionType;
    }

    public void setOptionType(Integer optionType) {
        this.optionType = optionType;
    }

    public Integer getWinPay() {
        return winPay;
    }

    public void setWinPay(Integer winPay) {
        this.winPay = winPay;
    }

}