package com.elatesoftware.grandcapital.views.items.limitLines;

import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.utils.ConventDate;

/**
 * Created by Дарья Высокович on 12.04.2017.
 */

public class DealingLine extends BaseLimitLine {

    private boolean mIsAmerican = false;
    private boolean mIsActive = false;
    private boolean isRed = false;

    private float mLimit = 0f;
    private String mLabel = "";
    private String mTimer = "";

    DealingLine(float limit, String label, String timer, boolean isAmerican, boolean isActive) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mTimer = timer;
        mIsAmerican = isAmerican;
        mIsActive = isActive;
    }

    public static void updateColorDealingLine(XDealingLine line, OrderAnswer order, double mCurrentValueY){
        if(order.getCmd() == 0 && order.getOpenPrice() <= mCurrentValueY ||
                order.getCmd() == 1 && order.getOpenPrice() >= mCurrentValueY){
            line.setLineColor(colorGreen);
            line.setIsRed(false);
        }else{
            line.setLineColor(colorRed);
            line.setIsRed(true);
        }
        line.setmTimer(String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())));
    }

    public static void updateColorDealingLine(YDealingLine line, OrderAnswer order, double mCurrentValueY){
        if(order.getCmd() == 0 && order.getOpenPrice() <= mCurrentValueY || order.getCmd() == 1 && order.getOpenPrice() >= mCurrentValueY){
            line.setLineColor(colorGreen);
            line.setIsRed(false);
        }else{
            line.setIsRed(true);
            line.setLineColor(colorRed);
        }
        line.setmTimer(String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())));
    }

    public boolean isRed() {
        return isRed;
    }

    public void setIsRed(boolean isRed) {
        this.isRed = isRed;
    }

    public boolean ismIsAmerican() {
        return mIsAmerican;
    }

    public void setmIsAmerican(boolean mIsAmerican) {
        this.mIsAmerican = mIsAmerican;
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public boolean ismIsActive() {
        return mIsActive;
    }

    public void setmIsActive(boolean mIsActive) {
        this.mIsActive = mIsActive;
    }

    public float getmLimit() {
        return mLimit;
    }

    public void setmLimit(float mLimit) {
        this.mLimit = mLimit;
    }

    public String getmTimer() {
        return mTimer;
    }

    public void setmTimer(String mTimer) {
        this.mTimer = mTimer;
    }
}
