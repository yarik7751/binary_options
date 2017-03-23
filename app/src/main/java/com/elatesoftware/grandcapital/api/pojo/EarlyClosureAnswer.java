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

    public static class Group {

        @SerializedName("is_demo")
        @Expose
        private Boolean isDemo;

        @SerializedName("options_style")
        @Expose
        private String optionsStyle;

        @SerializedName("is_options")
        @Expose
        private Boolean isOptions;

        @SerializedName("slug")
        @Expose
        private String slug;

        @SerializedName("display")
        @Expose
        private String display;

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public Boolean getDemo() {
            return isDemo;
        }

        public void setDemo(Boolean demo) {
            isDemo = demo;
        }

        public Boolean getOptions() {
            return isOptions;
        }

        public void setOptions(Boolean options) {
            isOptions = options;
        }

        public String getOptionsStyle() {
            return optionsStyle;
        }

        public void setOptionsStyle(String optionsStyle) {
            this.optionsStyle = optionsStyle;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }
    }
}
