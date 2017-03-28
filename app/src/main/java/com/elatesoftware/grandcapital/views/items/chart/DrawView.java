package com.elatesoftware.grandcapital.views.items.chart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class DrawView extends View {

    public static final String TAG = "DrawView_Log";

    private Context context;
    private int interval;

    //private ArrayList<Line> way;
    private int startI = 3;
    private float startX = 0;
    private float startY = 0;
    private float endX = 0;
    private float endY = 0;
    private float endXValue = 0;
    private float endYValue = 0;

    private float divX, divY;
    private boolean isClear = false;

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    //private int amtLine;



    private void init(Context context) {
        this.context = context;
        interval = 15;

        divX = Math.abs(startX - endXValue) / 10 * (startX < endXValue ? 1 : -1);
        divY = Math.abs(startY - endYValue) / 10 * (startY < endYValue ? 1 : -1);
        //Log.d(TAG, "divX = " + divX + ", divY = " + divY);
        //way = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(0, 74, 68, 106);

        if(isClear) {
            isClear = false;
            return;
        }

        /*for(int i = 0; i < way.size(); i++) {
            canvas.drawLine(way.get(i).x1, way.get(i).y1, way.get(i).x2, way.get(i).y2, getLinePaint());
        }*/

        canvas.drawLine(startX, startY, endX, endY, getLinePaint());

        boolean xCondition = startX < endXValue ? endX >= endXValue : endX <= endXValue;
        boolean yCondition = startY < endYValue ? endY >= endYValue : endY <= endYValue;

        if (!(xCondition && yCondition)) {
            //Log.d(TAG, "divX = " + divX + ", divY = " + divY);
            endX += divX;
            endY += divY;
            //Log.d(TAG, "endX = " + endX + ", endY = " + endY);
            postInvalidateDelayed(interval);
        } else {
            //Line line = new Line(startX, startY, endX, endY);
            //way.add(line);
            startX = endXValue;
            startY = endYValue;
            //amtLine++;
        }
    }

    public void addPoint(float x1, float y1, float x2, float y2) {
        Log.d(TAG, "addPoint");
        Log.d(TAG, "x1: " + x1 + ", y1: " + y1 + ", x2: " + x2 + ", y2: " + y2);
        startX = x1;
        startY = y1;
        endX = x1;
        endY = y1;
        endXValue = x2;
        endYValue = y2;
        divX = Math.abs(startX - endXValue) / 10 * (startX < endXValue ? 1 : -1);
        divY = Math.abs(startY - endYValue) / 10 * (startY < endYValue ? 1 : -1);
        Log.d(TAG, "divX = " + divX + ", divY = " + divY);
        invalidate();
    }

    public void clear() {
        isClear = true;
        invalidate();
    }

    private Paint getLinePaint() {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(15);
        p.setColor(Color.RED);
        return p;
    }
}
