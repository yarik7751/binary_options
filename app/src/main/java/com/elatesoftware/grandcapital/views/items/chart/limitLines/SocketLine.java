package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Color;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class SocketLine extends BaseLimitLine {

    private float mLimit = 0f;
    private String mLabel = "";

    private static SocketLine lineSocket = null;
    private SocketLine(float limit, String label) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        super.setLineColor(Color.WHITE);
    }
    public static void drawSocketLine(Entry entry) {
        if (entry != null) {
            if (lineSocket != null) {
                rightYAxis.removeLimitLine(lineSocket);
            }
            lineSocket = new SocketLine(entry.getY(), String.valueOf(entry.getY()));
            rightYAxis.addLimitLine(lineSocket);
        }
    }
    public static void deleteSocketLine(){
        if (lineSocket != null) {
            rightYAxis.removeLimitLine(lineSocket);
        }
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
