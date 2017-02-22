
package com.elatesoftware.grandcapital.api.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoAnswer {

    @SerializedName("group")
    @Expose
    private Group group;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("server_name")
    @Expose
    private String serverName;
    @SerializedName("cmd_types")
    @Expose
    private CmdTypes cmdTypes;
    @SerializedName("instruments")
    @Expose
    private List<Instrument> instruments = null;
    @SerializedName("currency")
    @Expose
    private Currency currency;
    @SerializedName("default_symbol")
    @Expose
    private String defaultSymbol;
    @SerializedName("leverage")
    @Expose
    private Integer leverage;

    private static InfoAnswer infoInstance = null;
    public static InfoAnswer getInstance() {
        return infoInstance;
    }
    public static void setInstance(InfoAnswer info) {
        infoInstance = info;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public InfoAnswer withGroup(Group group) {
        this.group = group;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InfoAnswer withName(String name) {
        this.name = name;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public InfoAnswer withServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public CmdTypes getCmdTypes() {
        return cmdTypes;
    }

    public void setCmdTypes(CmdTypes cmdTypes) {
        this.cmdTypes = cmdTypes;
    }

    public InfoAnswer withCmdTypes(CmdTypes cmdTypes) {
        this.cmdTypes = cmdTypes;
        return this;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public InfoAnswer withInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public InfoAnswer withCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public String getDefaultSymbol() {
        return defaultSymbol;
    }

    public void setDefaultSymbol(String defaultSymbol) {
        this.defaultSymbol = defaultSymbol;
    }

    public InfoAnswer withDefaultSymbol(String defaultSymbol) {
        this.defaultSymbol = defaultSymbol;
        return this;
    }

    public Integer getLeverage() {
        return leverage;
    }

    public void setLeverage(Integer leverage) {
        this.leverage = leverage;
    }

    public InfoAnswer withLeverage(Integer leverage) {
        this.leverage = leverage;
        return this;
    }
}
