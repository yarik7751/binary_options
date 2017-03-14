package com.elatesoftware.grandcapital.views.items.items_terminal;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/**
 * Created by Дарья Высокович on 13.03.2017.
 */

public class CurrentMarkerView extends MarkerView {

    private ImageView imgvMarker;

    public CurrentMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        imgvMarker = (ImageView) findViewById(R.id.imgvMarker);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight()/2);
    }
}