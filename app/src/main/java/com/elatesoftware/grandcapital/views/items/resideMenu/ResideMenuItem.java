package com.elatesoftware.grandcapital.views.items.resideMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;

/**
 * Created by Дарья Высокович on 17.02.2017.
 */

public class ResideMenuItem extends ResideMenuBaseItem {

    protected TextView tv_title;

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
        inflater.inflate(R.layout.item_reside_menu, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }
    public void setTitle(int title){
        tv_title.setText(title);
    }
    public void setTitle(String title){
        tv_title.setText(title);
    }
}
