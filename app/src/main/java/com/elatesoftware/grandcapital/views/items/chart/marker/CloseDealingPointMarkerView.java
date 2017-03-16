package com.elatesoftware.grandcapital.views.items.chart.marker;

import android.content.Context;
import android.widget.ImageView;

import com.elatesoftware.grandcapital.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by Darya on 15.03.2017.
 */

public class CloseDealingPointMarkerView extends MarkerView {

    public ImageView img;

    public CloseDealingPointMarkerView(Context context) {
        super(context, R.layout.item_close_dealing_marker);
        img = (ImageView) findViewById(R.id.imgvMarker);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(getWidth(), getHeight());
    }

}
