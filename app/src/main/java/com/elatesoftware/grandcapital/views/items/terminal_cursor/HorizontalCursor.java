package com.elatesoftware.grandcapital.views.items.terminal_cursor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 10.03.2017.
 */

public class HorizontalCursor extends View {

    private Path path;

    int color = Color.WHITE;
    String text = "000";

    public HorizontalCursor(Context context) {
        super(context);
        init();
    }

    public HorizontalCursor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalCursor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HorizontalCursor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int wight = canvas.getWidth();
        int height = canvas.getHeight();

        path.moveTo(0, height / 2);
        path.lineTo((float) (wight * 0.15), 0);
        path.lineTo(wight, 0);
        path.lineTo(wight, height);
        path.lineTo((float) (wight * 0.15), height);
        path.close();

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, paint);
    }
}
