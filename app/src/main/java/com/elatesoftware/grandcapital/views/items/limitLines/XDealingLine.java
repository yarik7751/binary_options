package com.elatesoftware.grandcapital.views.items.limitLines;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.google.gson.Gson;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class XDealingLine extends DealingLine {

    public static final String TAG = "XDealingLine_Logs";
    private Bitmap bitmapXLabel;
    private Bitmap bitmapYLabel;
    private float floatOpenPrice;

    XDealingLine(float limit, String label,String timer, boolean isAmerican, boolean isActive) {
        super(limit, label, timer, isAmerican, isActive);
        if(isActive){
            super.enableDashedLine(0f, 0f, 0f);
        }else{
            super.enableDashedLine(10f, 10f, 0f);
        }
        floatOpenPrice = Float.valueOf(String.valueOf(new Gson().fromJson(label, OrderAnswer.class).getOpenPrice()));
    }
     static void createXDealingLine(OrderAnswer order, boolean isAmerican){
        XDealingLine lineX = new XDealingLine(ConventDate.genericTimeForChart(
                ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000),
                new Gson().toJson(order), String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), isAmerican, false);
        updateColorDealingLine(lineX, order);
        xAxis.addLimitLine(lineX);
    }

    public Bitmap getBitmapXLabel() {
        return bitmapXLabel;
    }

    public void setBitmapXLabel(Bitmap bitmapXLabel) {
        this.bitmapXLabel = bitmapXLabel;
    }

    public Bitmap getBitmapYLabel() {
        return bitmapYLabel;
    }

    public void setBitmapYLabel(Bitmap bitmapYLabel) {
        this.bitmapYLabel = bitmapYLabel;
    }

    public float getFloatOpenPrice() {
        return floatOpenPrice;
    }

    public void setFloatOpenPrice(float floatOpenPrice) {
        this.floatOpenPrice = floatOpenPrice;
    }
}
