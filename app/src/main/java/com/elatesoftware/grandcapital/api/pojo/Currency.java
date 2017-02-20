
package com.elatesoftware.grandcapital.api.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Currency {

    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("is_metal")
    @Expose
    private Boolean isMetal;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("name")
    @Expose
    private String name;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Currency withSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Boolean getIsMetal() {
        return isMetal;
    }

    public void setIsMetal(Boolean isMetal) {
        this.isMetal = isMetal;
    }

    public Currency withIsMetal(Boolean isMetal) {
        this.isMetal = isMetal;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Currency withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency withName(String name) {
        this.name = name;
        return this;
    }

}
