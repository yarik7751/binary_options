package com.elatesoftware.grandcapital.views.items.cursors.triangles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.elatesoftware.grandcapital.R;

public class Triangle extends View {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private Path path;
    private int color = Color.WHITE;

    private int orientation = HORIZONTAL;

    int[] set = {
            android.R.attr.src
    };

    public Triangle(Context context, int _orientation) {
        super(context);
        orientation = _orientation;
        init();
    }

    public Triangle(Context context) {
        super(context);
        orientation = HORIZONTAL;
        init();
    }

    public Triangle(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(context, attrs);
        init();
        initColor(context, attrs);
    }

    public Triangle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(context, attrs);
        init();
        initColor(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Triangle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(context, attrs);
        init();
        initColor(context, attrs);
    }

    private void init() {
        path = new Path();
    }

    private void initColor(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, set);
        Drawable backgroundDrawable = typedArray.getDrawable(0);
        color = ((ColorDrawable) backgroundDrawable).getColor();
    }

    private void setOrientation(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Cursor);
        orientation = typedArray.getInt(R.styleable.Cursor_orientation, HORIZONTAL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int wight = canvas.getWidth();
        int height = canvas.getHeight();

        if(orientation == HORIZONTAL) {
            path.moveTo(0, height / 2);
            path.lineTo(wight, 0);
            path.lineTo(wight, height);
            path.close();
        } else if(orientation == VERTICAL) {
            path.moveTo(wight / 2, 0);
            path.lineTo(wight, height);
            path.lineTo(0, height);
            path.close();
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, paint);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }
}
