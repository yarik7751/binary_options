package com.elatesoftware.grandcapital.views.items.ResideMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;

public class ResideMenuWithMarkItem extends ResideMenuItem {

    private int value = 0;

    public ResideMenuWithMarkItem(Context context) {
        super(context);
        addMark(context);
    }

    public ResideMenuWithMarkItem(Context context, String title) {
        super(context, title);
        addMark(context);
    }

    private void addMark(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.residemenu_item_with_mark, (LinearLayout) findViewById(R.id.menu_item_layout), true);
        checkValue();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        checkValue();
    }

    private void checkValue(){
        LinearLayout badgeLayout = (LinearLayout) findViewById(R.id.badge_layout);
        if (value > 0) {
            ((TextView) findViewById(R.id.menu_badge)).setText(Integer.toString(value));
            badgeLayout.setVisibility(VISIBLE);
        }
        if(value == 0){
            badgeLayout.setVisibility(INVISIBLE);
        }
    }
}
