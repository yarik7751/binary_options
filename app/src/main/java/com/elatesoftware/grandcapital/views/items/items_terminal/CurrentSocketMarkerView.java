package com.elatesoftware.grandcapital.views.items.items_terminal;

import android.content.Context;
import android.graphics.Canvas;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class CurrentSocketMarkerView extends MarkerView {

    public ImageView img;
    private Animation animationMin, animationMax;

    public CurrentSocketMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        img = (ImageView) findViewById(R.id.imgvMarker);
        animationMax = AnimationUtils.loadAnimation(context, R.anim.anim_point_scale_max);
        animationMin = AnimationUtils.loadAnimation(context, R.anim.anim_point_scale_min);
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