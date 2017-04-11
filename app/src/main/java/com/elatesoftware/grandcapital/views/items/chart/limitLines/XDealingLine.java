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

public class XDealingLine extends BaseLimitLine {

    public static final String TAG = "XDealingLine_Logs";

    private float mLimit = 0f;
    private String mLabel = "";
    private String mTimer = "";
    private Bitmap mBitmapLabelX = null;
    private Bitmap mBitmapLabelY = null;
    private boolean mIsAmerican = false;
    private boolean mIsActive = false;

    public XDealingLine(float limit, String label, Bitmap bitmapX, Bitmap bitmapY, String timer, boolean isAmerican, boolean isActive) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mBitmapLabelX = bitmapX;
        mBitmapLabelY = bitmapY;
        mTimer = timer;
        mIsAmerican = isAmerican;
        mIsActive = isActive;
        if(isActive){
            super.enableDashedLine(0f, 0f, 0f);
        }else{
            super.enableDashedLine(10f, 10f, 0f);
        }
    }
    public static void createXDealingLine(OrderAnswer order, double mCurrentValueY, boolean isAmerican){
        XDealingLine line = new XDealingLine(ConventDate.genericTimeForChart(
                ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000),
                new Gson().toJson(order), null, null, String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), isAmerican, false);
        XDealingLine.updateColorXLimitLine(line, order, mCurrentValueY);
        BaseLimitLine.addLineQueueDrawingChart(line);
    }
    public static void updateColorXLimitLine(XDealingLine line, OrderAnswer order, double mCurrentValueY){
        if(order.getCmd() == 0 && order.getOpenPrice() <= mCurrentValueY ||
                order.getCmd() == 1 && order.getOpenPrice() >= mCurrentValueY){
            line.setmBitmapLabelX(bitmapIconGreenXLabel);
            line.setLineColor(colorGreen);
            line.setmBitmapLabelY(bitmapIconGreenYLabel);
        }else{
            line.setmBitmapLabelX(bitmapIconRedXLabel);
            line.setLineColor(colorRed);
            line.setmBitmapLabelY(bitmapIconRedYLabel);
        }
        line.setmTimer(String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())));
        Log.d(TAG, "order.getOptionsData().getExpirationTime(): " + order.getOptionsData().getExpirationTime());
        Log.d(TAG, "ConventDate.getDifferenceDate: " + ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime()));
        Log.d(TAG, "timer: " + line.getmTimer());
        Log.d(TAG, "-------------------------------------------------------------------------------------------------------");
    }

    public float getmLimit() {
        return mLimit;
    }

    public void setmLimit(float mLimit) {
        this.mLimit = mLimit;
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public String getmTimer() {
        return mTimer;
    }

    public void setmTimer(String mTimer) {
        this.mTimer = mTimer;
    }

    public Bitmap getmBitmapLabelX() {
        return mBitmapLabelX;
    }

    public void setmBitmapLabelX(Bitmap mBitmapLabelX) {
        this.mBitmapLabelX = mBitmapLabelX;
    }

    public Bitmap getmBitmapLabelY() {
        return mBitmapLabelY;
    }

    public void setmBitmapLabelY(Bitmap mBitmapLabelY) {
        this.mBitmapLabelY = mBitmapLabelY;
    }

    public boolean ismIsAmerican() {
        return mIsAmerican;
    }

    public void setmIsAmerican(boolean mIsAmerican) {
        this.mIsAmerican = mIsAmerican;
    }

    public boolean ismIsActive() {
        return mIsActive;
    }

    public void setmIsActive(boolean mIsActive) {
        this.mIsActive = mIsActive;
    }
}
