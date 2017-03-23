package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ярослав Левшунов on 23.03.2017.
 */

public class EarlyClosureAnswer {

    @SerializedName("instruments")
    @Expose
    private List<Instrument> instruments;

    @SerializedName("group")
    @Expose
    private Group group;

    @SerializedName("server_name")
    @Expose
    private String serverName;

    @SerializedName("default_symbol")
    @Expose
    private String defaultSymbol;

    private static EarlyClosureAnswer sEarlyClosureAnswer = null;
    public static void setInstance(EarlyClosureAnswer _earlyClosureAnswer) {
        sEarlyClosureAnswer = _earlyClosureAnswer;
    }
    public static EarlyClosureAnswer getInstance() {
        return sEarlyClosureAnswer;
    }

    public String getDefaultSymbol() {
        return defaultSymbol;
    }

    public void setDefaultSymbol(String defaultSymbol) {
        this.defaultSymbol = defaultSymbol;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
