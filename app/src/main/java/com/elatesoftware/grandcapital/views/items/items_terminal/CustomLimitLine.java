package com.elatesoftware.grandcapital.views.items.items_terminal;

import com.github.mikephil.charting.components.LimitLine;

/**
 * Created by Дарья Высокович on 14.03.2017.
 */

public class CustomLimitLine extends LimitLine {

    /** limit / maximum (the y-value or xIndex) */
    private float mLimit = 0f;

    /** label string that is drawn next to the limit line */
    private String mLabel = "";

    private LimitLinesType mTypeLimitLine = LimitLinesType.LINE_CURRENT_SOCKET;

    public enum LimitLinesType {
        LINE_CURRENT_SOCKET, LINE_VERTICAL_DEALING, LINE_HORIZONTAL_DEALING
    }

    public CustomLimitLine(float limit) {
        super(limit);
        mLimit = limit;
    }

    public CustomLimitLine(float limit, String label) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
    }

    public void setTypeLimetLine(LimitLinesType type){
        mTypeLimitLine = type;
    }
}
