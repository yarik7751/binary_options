
package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

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

    public Boolean getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(Boolean isDemo) {
        this.isDemo = isDemo;
    }

    public Group withIsDemo(Boolean isDemo) {
        this.isDemo = isDemo;
        return this;
    }

    public String getOptionsStyle() {
        return optionsStyle;
    }

    public void setOptionsStyle(String optionsStyle) {
        this.optionsStyle = optionsStyle;
    }

    public Group withOptionsStyle(String optionsStyle) {
        this.optionsStyle = optionsStyle;
        return this;
    }

    public Boolean getIsOptions() {
        return isOptions;
    }

    public void setIsOptions(Boolean isOptions) {
        this.isOptions = isOptions;
    }

    public Group withIsOptions(Boolean isOptions) {
        this.isOptions = isOptions;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Group withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Group withDisplay(String display) {
        this.display = display;
        return this;
    }

}
