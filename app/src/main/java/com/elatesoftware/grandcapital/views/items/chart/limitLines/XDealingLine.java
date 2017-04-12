package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Bitmap;
import android.util.Log;

import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class XDealingLine extends DealingLine {

    public static final String TAG = "XDealingLine_Logs";
    private Bitmap mBitmapLabelX = null;

    XDealingLine(float limit, String label, Bitmap bitmapX, Bitmap bitmapY, String timer, boolean isAmerican, boolean isActive) {
        super(limit, label, bitmapY, timer, isAmerican, isActive);
        mBitmapLabelX = bitmapX;
        if(isActive){
            super.enableDashedLine(0f, 0f, 0f);
        }else{
            super.enableDashedLine(10f, 10f, 0f);
        }
    }
     static void createXDealingLine(OrderAnswer order, double mCurrentValueY, boolean isAmerican){
        XDealingLine lineX = new XDealingLine(ConventDate.genericTimeForChart(
                ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000),
                new Gson().toJson(order), null, null, String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), isAmerican, false);
        updateColorDealingLine(lineX, order, mCurrentValueY);
        xAxis.addLimitLine(lineX);
    }

    public Bitmap getmBitmapLabelX() {
        return mBitmapLabelX;
    }

    public void setmBitmapLabelX(Bitmap mBitmapLabelX) {
        this.mBitmapLabelX = mBitmapLabelX;
    }
}
