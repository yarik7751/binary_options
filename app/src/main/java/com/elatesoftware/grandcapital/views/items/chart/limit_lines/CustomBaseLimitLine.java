package com.elatesoftware.grandcapital.views.items.chart.limit_lines;

import android.graphics.Bitmap;

import com.github.mikephil.charting.components.LimitLine;

/**
 * Created by Дарья Высокович on 14.03.2017.
 */

public class CustomBaseLimitLine extends LimitLine {

    /** limit / maximum (the y-value or xIndex) */
    private float mLimit = 0f;

    /** label string that is drawn next to the limit line */
    private String mLabel = "";
    private String mTimer = "";
    private Bitmap mBitmapLabelX = null;
    private Bitmap mBitmapLabelY = null;

    private LimitLinesType mTypeLimitLine = LimitLinesType.LINE_HORIZONTAL_CURRENT_SOCKET;

    public enum LimitLinesType {
        LINE_HORIZONTAL_CURRENT_SOCKET,
        LINE_HORIZONTAL_CURRENT_DEALING,

        LINE_VERTICAL_DEALING_ACTIVE,
        LINE_VERTICAL_DEALING_PASS,

        LINE_VERTICAL_DEALING_ACTIVE_CLOSING,
        LINE_VERTICAL_DEALING_PASS_CLOSING,
    }

    public CustomBaseLimitLine(float limit, String label, Bitmap bitmapX, Bitmap bitmapY, String timer) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mBitmapLabelX = bitmapX;
        mBitmapLabelY = bitmapY;
        mTimer = timer;
    }
    public CustomBaseLimitLine(float limit, String label, Bitmap bitmapX) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mBitmapLabelX = bitmapX;
    }
    public void setTypeLimitLine(LimitLinesType type){
        mTypeLimitLine = type;
    }

    public LimitLinesType getTypeLimitLine() {
        return mTypeLimitLine;
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
}
