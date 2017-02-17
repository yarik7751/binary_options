package com.elatesoftware.grandcapital.views.items.ResideMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;

public class ResideMenuItem extends LinearLayout{

    /** menu item  title */
    protected TextView tv_title;
    protected int layout = R.layout.residemenu_item;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Context context, String title) {
        super(context);
        initViews(context);
        tv_title.setText(title);
    }

    private void initViews(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layout, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    /**
     * set the title with resource
     * ;
     * @param title
     */
    public void setTitle(int title){
        tv_title.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }
}
