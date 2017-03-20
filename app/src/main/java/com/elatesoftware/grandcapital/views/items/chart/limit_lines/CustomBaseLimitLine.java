package com.elatesoftware.grandcapital.views.items.chart.limit_lines;

import android.graphics.Bitmap;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;

/**
 * Created by Дарья Высокович on 14.03.2017.
 */

public class CustomBaseLimitLine extends LimitLine {

    /** limit / maximum (the y-value or xIndex) */
    private float mLimit = 0f;

    /** label string that is drawn next to the limit line */
    private String mLabel = "";
    private Bitmap mBitmapIconLabel = null;

    private LimitLinesType mTypeLimitLine = LimitLinesType.LINE_CURRENT_SOCKET;

    public enum LimitLinesType {
        LINE_CURRENT_SOCKET,
        LINE_HORIZONTAL_DEALING_ACTIVE,
        LINE_VERTICAL_DEALING_ACTIVE,
        LINE_VERTICAL_DEALING_PASS,
    }

    public CustomBaseLimitLine(float limit) {
        super(limit);
        mLimit = limit;
    }

    public CustomBaseLimitLine(float limit, String label, Bitmap bitmapLabel/*, Bitmap bitmapIconPoint, Entry entry*/) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mBitmapIconLabel = bitmapLabel;
        //mBitmapIconPoint = bitmapIconPoint;
        //mEntry = entry;
    }

    public void setTypeLimitLine(LimitLinesType type){
        mTypeLimitLine = type;
    }

    public LimitLinesType getTypeLimitLine() {
        return mTypeLimitLine;
    }

    public Bitmap getBitmapIconLabel() {
        return mBitmapIconLabel;
    }

    public void setBitmapIconLabel(Bitmap bitmapIcon) {
        this.mBitmapIconLabel = bitmapIcon;
    }
}
