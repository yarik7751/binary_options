package com.elatesoftware.grandcapital.views.items.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.ConventImage;
import com.elatesoftware.grandcapital.views.items.animation.CustomAnimationDrawable;

/**
 * Created by Ярослав Левшунов on 31.03.2017.
 */

public class PointAnimation {

    private Context context;
    ImageView imgPointCurrent;
    private Bitmap bitmapIconCurrentPoint;
    private CustomAnimationDrawable rocketAnimation, rocketAnimationBack;

    public PointAnimation(Context _context, ImageView _imgPointCurrent) {
        context = _context;
        imgPointCurrent = _imgPointCurrent;
        bitmapIconCurrentPoint = BitmapFactory.decodeResource(context.getResources(), R.drawable.front_elipsa);
        initRocketAnimation();
    }

    public void start() {
        imgPointCurrent.setImageDrawable(rocketAnimation);
        rocketAnimation.start();
    }

    private void initRocketAnimation() {
        rocketAnimation = new CustomAnimationDrawable();
        rocketAnimation.setOneShot(true);
        for (int i = 10; i >= 3; i--) {
            rocketAnimation.addFrame(new BitmapDrawable(ConventImage.getPaddingImage(bitmapIconCurrentPoint, i)), Const.INTERVAL_ITEM);
        }
        rocketAnimation.setAnimationEndListner(() -> {
            initRocketAnimationBack();
            imgPointCurrent.setImageDrawable(rocketAnimationBack);
            rocketAnimationBack.start();
        });
    }

    private void initRocketAnimationBack() {
        rocketAnimationBack = new CustomAnimationDrawable();
        rocketAnimationBack.setOneShot(true);
        for (int i = 3; i <= 10; i++) {
            rocketAnimationBack.addFrame(new BitmapDrawable(ConventImage.getPaddingImage(bitmapIconCurrentPoint, i)), Const.INTERVAL_ITEM);
        }
        rocketAnimationBack.setAnimationEndListner(() -> {
            initRocketAnimation();
            imgPointCurrent.setImageDrawable(rocketAnimation);
            rocketAnimation.start();
        });
    }
}
