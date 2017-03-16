package com.elatesoftware.grandcapital.views.items.tooltabsview.adapter;

import android.content.Context;

public class ToolTabsViewAdapter {

    private Context context;
    private int[] icons;
    private String[] titles;

    public ToolTabsViewAdapter(Context context, int[] icons, String[] titles) {
        this.context = context;
        this.icons = icons;
        this.titles = titles;
    }

    public ToolTabsViewAdapter(Context context, int[] icons) {
        this.context = context;
        this.icons = icons;
        this.titles = null;
    }

    public int getItemsCount(){
        return Math.max(icons.length, titles == null ? 0 : titles.length);
    }

    public int[] getIcons() {
        return icons;
    }

    public String[] getTitles() {
        return titles;
    }
}
