package com.elatesoftware.grandcapital.api.pojo;

/**
 * Created by Дарья Высокович on 10.03.2017.
 */

import android.view.View;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SignalAnswer {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("time")
    @Expose
    private Long time;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("instrument")
    @Expose
    private String instrument;

    private static List<SignalAnswer> signalInstance = null;
    public static List<SignalAnswer> getInstance() {
        return signalInstance;
    }
    public static void setInstance(List<SignalAnswer> signal) {
        signalInstance = signal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SignalAnswer withId(Long id) {
        this.id = id;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public SignalAnswer withTime(Long time) {
        this.time = time;
        return this;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public SignalAnswer withCost(String cost) {
        this.cost = cost;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public SignalAnswer withSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SignalAnswer withType(String type) {
        this.type = type;
        return this;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public SignalAnswer withInstrument(String instrument) {
        this.instrument = instrument;
        return this;
    }
    public static List<SignalAnswer> getSignalsActive(String symbol) {
        List<SignalAnswer> list = null;
        if (SignalAnswer.getInstance() != null) {
            list = new ArrayList<>();
            for (SignalAnswer answer : SignalAnswer.getInstance()) {
                if (answer.getInstrument().replace("/", "").equals(symbol)) {
                    list.add(answer);
                }
            }
        }
        return list;
    }
}