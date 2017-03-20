package com.elatesoftware.grandcapital.views.items.chart.marker;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.elatesoftware.grandcapital.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by Дарья Высокович on 13.03.2017.
 */

public class CurrentSocketPointMarkerView extends MarkerView {

    public final static String TAG = "CurrentSocketPointMarkerView_Logs";

    public ImageView img;
    //private Animation animationMin, animationMax;

    public CurrentSocketPointMarkerView(Context context) {
        super(context, R.layout.item_current_marker);
        img = (ImageView) findViewById(R.id.imgvMarker);
        //animationMax = AnimationUtils.loadAnimation(context, R.anim.anim_point_scale_max);
        //animationMin = AnimationUtils.loadAnimation(context, R.anim.anim_point_scale_min);
    }
    /*
    public void startAnimate(){
        animationMin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                img.clearAnimation();
                img.startAnimation(animationMax);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        img.startAnimation(animationMin);

        animationMax.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                img.clearAnimation();
                img.startAnimation(animationMin);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
*/

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight()/2);
    }
}