package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Bitmap;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class XDealingLine extends BaseLimitLine {

    /** limit / maximum (the y-value or xIndex) */
    private float mLimit = 0f;

    /** label string that is drawn next to the limit line */
    private String mLabel = "";
    private String mTimer = "";
    private Bitmap mBitmapLabelX = null;
    private Bitmap mBitmapLabelY = null;
    private boolean mIsAmerican = false;
    private boolean mIsActive = false;

    /** for x dealing*/
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
