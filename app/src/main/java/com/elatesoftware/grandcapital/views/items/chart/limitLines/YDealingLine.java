package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Bitmap;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.google.gson.Gson;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class YDealingLine extends BaseLimitLine {

    private float mLimit = 0f;
    private String mLabel = "";
    private String mTimer = "";
    private Bitmap mBitmapLabelY = null;
    private boolean mIsAmerican = false;
    private boolean mIsActive = false;

    YDealingLine(float limit, String label, Bitmap bitmapY, String timer, boolean isAmerican, boolean isActive) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mBitmapLabelY = bitmapY;
        mTimer = timer;
        mIsAmerican = isAmerican;
        mIsActive = isActive;
    }
    public static void updateColorYLimitLine(YDealingLine line, OrderAnswer order, double mCurrentValueY){
        if(order.getCmd() == 0 && order.getOpenPrice() <= mCurrentValueY || order.getCmd() == 1 && order.getOpenPrice() >= mCurrentValueY){
            line.setmBitmapLabelY(bitmapIconGreenYLabel);
            line.setLineColor(colorGreen);
        }else{
            line.setmBitmapLabelY(bitmapIconRedYLabel);
            line.setLineColor(colorRed);
        }
        line.setmTimer(String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())));
    }
    static void createYDealingLine(OrderAnswer order, double mCurrentValueY, boolean isAmerican){
        YDealingLine line = new YDealingLine(Float.valueOf(String.valueOf(order.getOpenPrice())),
                new Gson().toJson(order), null,
                String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), isAmerican, false);
        YDealingLine.updateColorYLimitLine(line, order, mCurrentValueY);
        rightYAxis.addLimitLine(line);
    }

    public Bitmap getmBitmapLabelY() {
        return mBitmapLabelY;
    }

    public void setmBitmapLabelY(Bitmap mBitmapLabelY) {
        this.mBitmapLabelY = mBitmapLabelY;
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
