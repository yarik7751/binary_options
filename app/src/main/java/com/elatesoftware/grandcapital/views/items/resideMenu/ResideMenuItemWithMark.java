package com.elatesoftware.grandcapital.views.items.resideMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;

public class ResideMenuItemWithMark extends ResideMenuBaseItem {

    private int value = 0;
    private TextView tv_title;
    private TextView tv_value;

    public ResideMenuItemWithMark(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItemWithMark(Context context, String title) {
        super(context);
        initViews(context);
        tv_title.setText(title);
    }
    private void initViews(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_reside_menu_item_mark, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_value = (TextView) findViewById(R.id.menu_badge);
        checkValue();
    }
    public void setTitle(int title){
        tv_title.setText(title);
    }
    public void setTitle(String title){
        tv_title.setText(title);
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
        checkValue();
    }
    private void checkValue(){
        RelativeLayout badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        tv_value = (TextView) findViewById(R.id.menu_badge);
        if (value > 0) {
            tv_value.setText(String.valueOf(value));
            badgeLayout.setVisibility(VISIBLE);
        }else{
            badgeLayout.setVisibility(INVISIBLE);
        }
    }
}
