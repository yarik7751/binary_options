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

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ярослав Левшунов on 31.03.2017.
 */

public class PointAnimation {

    private ImageView imgPointCurrent;
    private Bitmap bitmapIconCurrentPoint;
    private CustomAnimationDrawable rocketAnimation, rocketAnimationBack;
    private List<Bitmap> bitmapCache;

    public PointAnimation(Context context, ImageView imgPoint) {
        imgPointCurrent = imgPoint;
        bitmapCache = new LinkedList<>();
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
        for (int i = 7; i >= 3; i--) {
            Bitmap bitmap = ConventImage.getPaddingImage(bitmapIconCurrentPoint, i);
            rocketAnimation.addFrame(new BitmapDrawable(bitmap), Const.INTERVAL_ITEM);
            bitmapCache.add(bitmap);
        }
        rocketAnimation.setAnimationEndListner(() -> {
            recycleAnimation();
            initRocketAnimationBack();
            imgPointCurrent.setImageDrawable(rocketAnimationBack);
            rocketAnimationBack.start();
        });
    }

    private void initRocketAnimationBack() {
        rocketAnimationBack = new CustomAnimationDrawable();
        rocketAnimationBack.setOneShot(true);
        for (int i = 3; i <= 7; i++) {
            Bitmap bitmap = ConventImage.getPaddingImage(bitmapIconCurrentPoint, i);
            rocketAnimationBack.addFrame(new BitmapDrawable(bitmap), Const.INTERVAL_ITEM);
            bitmapCache.add(bitmap);
        }
        rocketAnimationBack.setAnimationEndListner(() -> {
            recycleAnimation();
            initRocketAnimation();
            imgPointCurrent.setImageDrawable(rocketAnimation);
            rocketAnimation.start();
        });
    }

    private void recycleAnimation() {
        for(Bitmap bitmap : bitmapCache) {
            bitmap.recycle();
        }
        bitmapCache.clear();
    }
}
