
package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Instrument {

    @SerializedName("win_full")
    @Expose
    private Integer winFull;
    @SerializedName("win_lt_5")
    @Expose
    private Integer winLt5;
    @SerializedName("bonus")
    @Expose
    private Double bonus;
    @SerializedName("win")
    @Expose
    private Integer win;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("early_commission")
    @Expose
    private Double earlyCommission;
    @SerializedName("start_period")
    @Expose
    private String startPeriod;
    @SerializedName("period")
    @Expose
    private Integer period;
    @SerializedName("high")
    @Expose
    private Double high;
    @SerializedName("ask")
    @Expose
    private Double ask;
    @SerializedName("win_early")
    @Expose
    private Integer winEarly;
    @SerializedName("min_time")
    @Expose
    private Integer minTime;
    @SerializedName("spread")
    @Expose
    private Integer spread;
    @SerializedName("digits")
    @Expose
    private Integer digits;
    @SerializedName("loss")
    @Expose
    private Integer loss;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("win_15")
    @Expose
    private Integer win15;
    @SerializedName("win_30")
    @Expose
    private Integer win30;
    @SerializedName("nill")
    @Expose
    private Integer nill;
    @SerializedName("min_pips")
    @Expose
    private Integer minPips;
    @SerializedName("bid")
    @Expose
    private Double bid;
    @SerializedName("price_scale")
    @Expose
    private Integer priceScale;
    @SerializedName("direction")
    @Expose
    private Boolean direction;
    @SerializedName("tradable")
    @Expose
    private Boolean tradable;
    @SerializedName("win_5")
    @Expose
    private Integer win5;
    @SerializedName("modify_time")
    @Expose
    private String modifyTime;
    @SerializedName("max_time")
    @Expose
    private Integer maxTime;
    @SerializedName("time")
    @Expose
    private Double time;
    @SerializedName("at_time")
    @Expose
    private String atTime;
    @SerializedName("low")
    @Expose
    private Double low;

    public Integer getWinFull() {
        return winFull;
    }

    public void setWinFull(Integer winFull) {
        this.winFull = winFull;
    }

    public Instrument withWinFull(Integer winFull) {
        this.winFull = winFull;
        return this;
    }

    public Integer getWinLt5() {
        return winLt5;
    }

    public void setWinLt5(Integer winLt5) {
        this.winLt5 = winLt5;
    }

    public Instrument withWinLt5(Integer winLt5) {
        this.winLt5 = winLt5;
        return this;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Instrument withBonus(Double bonus) {
        this.bonus = bonus;
        return this;
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public Instrument withWin(Integer win) {
        this.win = win;
        return this;
    }

    public String getSymbol() {
        return symbol.replace("_OP", "");
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Instrument withSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Double getEarlyCommission() {
        return earlyCommission;
    }

    public void setEarlyCommission(Double earlyCommission) {
        this.earlyCommission = earlyCommission;
    }

    public Instrument withEarlyCommission(Double earlyCommission) {
        this.earlyCommission = earlyCommission;
        return this;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public Instrument withStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
        return this;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Instrument withPeriod(Integer period) {
        this.period = period;
        return this;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Instrument withHigh(Double high) {
        this.high = high;
        return this;
    }

    public Double getAsk() {
        return ask;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }

    public Instrument withAsk(Double ask) {
        this.ask = ask;
        return this;
    }

    public Integer getWinEarly() {
        return winEarly;
    }

    public void setWinEarly(Integer winEarly) {
        this.winEarly = winEarly;
    }

    public Instrument withWinEarly(Integer winEarly) {
        this.winEarly = winEarly;
        return this;
    }

    public Integer getMinTime() {
        return minTime;
    }

    public void setMinTime(Integer minTime) {
        this.minTime = minTime;
    }

    public Instrument withMinTime(Integer minTime) {
        this.minTime = minTime;
        return this;
    }

    public Integer getSpread() {
        return spread;
    }

    public void setSpread(Integer spread) {
        this.spread = spread;
    }

    public Instrument withSpread(Integer spread) {
        this.spread = spread;
        return this;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public Instrument withDigits(Integer digits) {
        this.digits = digits;
        return this;
    }

    public Integer getLoss() {
        return loss;
    }

    public void setLoss(Integer loss) {
        this.loss = loss;
    }

    public Instrument withLoss(Integer loss) {
        this.loss = loss;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Instrument withGroup(String group) {
        this.group = group;
        return this;
    }

    public Integer getWin15() {
        return win15;
    }

    public void setWin15(Integer win15) {
        this.win15 = win15;
    }

    public Instrument withWin15(Integer win15) {
        this.win15 = win15;
        return this;
    }

    public Integer getWin30() {
        return win30;
    }

    public void setWin30(Integer win30) {
        this.win30 = win30;
    }

    public Instrument withWin30(Integer win30) {
        this.win30 = win30;
        return this;
    }

    public Integer getNill() {
        return nill;
    }

    public void setNill(Integer nill) {
        this.nill = nill;
    }

    public Instrument withNill(Integer nill) {
        this.nill = nill;
        return this;
    }

    public Integer getMinPips() {
        return minPips;
    }

    public void setMinPips(Integer minPips) {
        this.minPips = minPips;
    }

    public Instrument withMinPips(Integer minPips) {
        this.minPips = minPips;
        return this;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Instrument withBid(Double bid) {
        this.bid = bid;
        return this;
    }

    public Integer getPriceScale() {
        return priceScale;
    }

    public void setPriceScale(Integer priceScale) {
        this.priceScale = priceScale;
    }

    public Instrument withPriceScale(Integer priceScale) {
        this.priceScale = priceScale;
        return this;
    }

    public Boolean getDirection() {
        return direction;
    }

    public void setDirection(Boolean direction) {
        this.direction = direction;
    }

    public Instrument withDirection(Boolean direction) {
        this.direction = direction;
        return this;
    }

    public Boolean getTradable() {
        return tradable;
    }

    public void setTradable(Boolean tradable) {
        this.tradable = tradable;
    }

    public Instrument withTradable(Boolean tradable) {
        this.tradable = tradable;
        return this;
    }

    public Integer getWin5() {
        return win5;
    }

    public void setWin5(Integer win5) {
        this.win5 = win5;
    }

    public Instrument withWin5(Integer win5) {
        this.win5 = win5;
        return this;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Instrument withModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }

    public Instrument withMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Instrument withTime(Double time) {
        this.time = time;
        return this;
    }

    public String getAtTime() {
        return atTime;
    }

    public void setAtTime(String atTime) {
        this.atTime = atTime;
    }

    public Instrument withAtTime(String atTime) {
        this.atTime = atTime;
        return this;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Instrument withLow(Double low) {
        this.low = low;
        return this;
    }

}
