package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Color;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class SocketLine extends BaseLimitLine {

    /** limit / maximum (the y-value or xIndex) */
    private float mLimit = 0f;

    /** label string that is drawn next to the limit line */
    private String mLabel = "";

    /** for socket*/
    public SocketLine(float limit, String label) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        super.setLineColor(Color.WHITE);
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public float getmLimit() {
        return mLimit;
    }

    public void setmLimit(float mLimit) {
        this.mLimit = mLimit;
    }
}
