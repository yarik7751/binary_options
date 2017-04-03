package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class DealingLine extends BaseLimitLine {

    /** limit / maximum (the y-value or xIndex) */
    private float mLimit = 0f;

    /** label string that is drawn next to the limit line */
    private String mLabel = "";
    private Bitmap mBitmapLabelY = null;

    /** for current y dealing*/
    public DealingLine(float limit, String label, Bitmap bitmapY) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mBitmapLabelY = bitmapY;
        super.setLineColor(Color.TRANSPARENT);
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

    public Bitmap getmBitmapLabelY() {
        return mBitmapLabelY;
    }

    public void setmBitmapLabelY(Bitmap mBitmapLabelY) {
        this.mBitmapLabelY = mBitmapLabelY;
    }
}
