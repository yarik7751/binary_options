package com.elatesoftware.grandcapital.views.items.cursors;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.items.cursors.triangles.Triangle;

public class Cursor extends FrameLayout {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private View vLine;
    private FrameLayout flNumber;
    private Triangle ht;
    private TextView tvNumber;

    private int color = Color.WHITE;

    private int orientation = HORIZONTAL;

    int[] set = {
            android.R.attr.src,
            android.R.attr.orientation
    };

    public Cursor(Context context, int orientation) {
        super(context);
        this.orientation = orientation;
        init(context);
    }

    public Cursor(Context context) {
        super(context);
        init(context);
    }

    public Cursor(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(context, attrs);
        init(context);
        initAttrs(context, attrs);
    }

    public Cursor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(context, attrs);
        init(context);
        initAttrs(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Cursor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(context, attrs);
        init(context);
        initAttrs(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = null;
        /*if(orientation == HORIZONTAL) {
            v = inflater.inflate(R.layout.horizontal_cursor, this, true);
        } else if(orientation == VERTICAL) {
            v = inflater.inflate(R.layout.vertical_cursor, this, true);
        }*/
        v = inflater.inflate(R.layout.cursor_horizontal_current, this, true);

        vLine = v.findViewById(R.id.v_line);
        flNumber = (FrameLayout) v.findViewById(R.id.fl_number);
        ht = (Triangle) v.findViewById(R.id.ht);
        tvNumber = (TextView) v.findViewById(R.id.tv_number);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, set);
        Drawable backgroundDrawable = typedArray.getDrawable(0);
        if(backgroundDrawable != null) {
            color = ((ColorDrawable) backgroundDrawable).getColor();
        }

        vLine.setBackgroundColor(color);
        ht.setColor(color);
        flNumber.setBackgroundColor(color);
    }

    private void setOrientation(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Cursor);
        orientation = typedArray.getInt(R.styleable.Cursor_orientation, HORIZONTAL);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public FrameLayout getFlNumber() {
        return flNumber;
    }

    public void setFlNumber(FrameLayout flNumber) {
        this.flNumber = flNumber;
    }

    public Triangle getHt() {
        return ht;
    }

    public void setHt(Triangle ht) {
        this.ht = ht;
    }

    public TextView getTvNumber() {
        return tvNumber;
    }

    public void setTvNumber(TextView tvNumber) {
        this.tvNumber = tvNumber;
    }

    public View getvLine() {
        return vLine;
    }

    public void setvLine(View vLine) {
        this.vLine = vLine;
    }
}
