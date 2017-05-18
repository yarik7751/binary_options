package com.elatesoftware.grandcapital.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Дарья Высокович on 20.03.2017.
 */

public class ConventImage {

    public static Bitmap getPaddingImage(Bitmap image, int persent) {
        float width = (float) (image.getWidth() * (persent/10.0));
        float height = (float) (image.getHeight() * (persent/10.0));
        Bitmap smallImage = Bitmap.createScaledBitmap(image, (int) width, (int) height, false);
        Bitmap paddingImage = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(paddingImage);
        canvas.drawBitmap(
                smallImage,
                paddingImage.getWidth() / 2 - smallImage.getWidth() / 2,
                paddingImage.getHeight() / 2 - smallImage.getHeight() / 2,
                null
        );
        smallImage.recycle();
        return paddingImage;
    }

    public static Bitmap loadBitmapFromView(View v) {
        if(v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache(true));
        if(bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmap;
    }

    public static RelativeLayout.LayoutParams getRelativeParams(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = AndroidUtils.dp(16);
        params.leftMargin = AndroidUtils.dp(16);
        return params;
    }
    public static FrameLayout.LayoutParams getFrameParams(boolean isDirection){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                AndroidUtils.dp(80)
        );
        params.topMargin = AndroidUtils.dp(isDirection ? -80 : 0);
        return params;
    }
}
