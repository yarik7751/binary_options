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
    private Bitmap mBitmap = null;

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

    public CustomBaseLimitLine(float limit, String label, Bitmap bitmap) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mBitmap = bitmap;
    }

    public void setTypeLimitLine(LimitLinesType type){
        mTypeLimitLine = type;
    }

    public LimitLinesType getTypeLimitLine() {
        return mTypeLimitLine;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
}
