package com.elatesoftware.grandcapital.views.items.tooltabsview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.test_tooltabsview.Logs;
import com.example.lenovo.test_tooltabsview.R;
import com.example.lenovo.test_tooltabsview.tooltabsview.adapter.OnChooseTab;
import com.example.lenovo.test_tooltabsview.tooltabsview.adapter.OnLoadData;
import com.example.lenovo.test_tooltabsview.tooltabsview.adapter.ToolTabsViewAdapter;

public class ToolTabsView extends LinearLayout {

    public static final String TAG = "ToolTabsView_TAG";

    private Context context;
    private int lineColor, selectColor, heightView, widthView;
    private int lineWidth = -1, lineMargin = 1;
    private ToolTabsViewAdapter adapter;
    private boolean isDataSet = false;

    private LinearLayout llTabs, llLine, llMain;
    private View vIndicator;
    private LayoutParams itemParams;

    private OnChooseTab onChooseTab;
    private OnLoadData onLoadData;

    public ToolTabsView(Context context) {
        super(context);
        init(context);
    }

    public ToolTabsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setAttr(attrs);
    }

    public ToolTabsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setAttr(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ToolTabsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        setAttr(attrs);
    }

    public void setAdapter(ToolTabsViewAdapter _adapter) {
        adapter = _adapter;
        isDataSet = false;
        itemParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        itemParams.weight = 1;
        //setData();
    }

    public void hideTab(int position) {
        llTabs.getChildAt(position).setVisibility(GONE);
        setNumbers();
    }

    public void setOnChooseTab(OnChooseTab onChooseTab) {
        this.onChooseTab = onChooseTab;
    }

    public void setOnLoadData(OnLoadData onLoadData) {
        this.onLoadData = onLoadData;
    }

    private void init(Context _context) {
        context = _context;

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.tool_tabs_view, this, true);
        llTabs = (LinearLayout) v.findViewById(R.id.ll_tabs);
        llLine = (LinearLayout) v.findViewById(R.id.ll_line);
        llMain = (LinearLayout) v.findViewById(R.id.ll_main);
        vIndicator = v.findViewById(R.id.indicator);
        vIndicator.getLayoutParams().width = 0;
    }

    private void setAttr(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolTabsView);
        lineColor = typedArray.getColor(R.styleable.ToolTabsView_line_color, Color.TRANSPARENT);
        selectColor = typedArray.getColor(R.styleable.ToolTabsView_select_color, Color.TRANSPARENT);
        vIndicator.setBackgroundColor(lineColor);
    }

    private void setData() {
        if(!isDataSet) {
            int[] icons = adapter.getIcons();
            String[] titles = adapter.getTitles();
            for (int i = 0; i < adapter.getItemsCount(); i++) {
                View v;
                if (titles == null || titles[i] == null) {
                    v = View.inflate(context, R.layout.tool_tabs_view_item2, null);
                    ImageView img = (ImageView) v.findViewById(R.id.img);
                    img.setImageResource(icons[i]);
                } else {
                    v = View.inflate(context, R.layout.tool_tabs_view_item1, null);
                    ImageView img = (ImageView) v.findViewById(R.id.img);
                    img.setImageResource(icons[i]);
                    TextView tv = (TextView) v.findViewById(R.id.tv);
                    tv.setText(titles[i]);
                }
                v.setTag(R.string.logical_number, i);
                Log.d(TAG, "setData widthItem: " + widthView / adapter.getItemsCount());
                itemParams.width = widthView / adapter.getItemsCount();
                llTabs.addView(v, itemParams);
                v.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        int num = (int) view.getTag(R.string.number);
                        int logicNum = (int) view.getTag(R.string.logical_number);
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                Log.d(TAG, "ACTION_DOWN");
                                view.setBackgroundColor(selectColor);
                                Log.d(TAG, "num: " + num);
                                Log.d(TAG, "logicNum: " + logicNum);
                                moveIndicatior(num);
                                if (onChooseTab != null) {
                                    onChooseTab.onChoose(view, logicNum);
                                }
                                return true;

                            case MotionEvent.ACTION_UP:
                                Log.d(TAG, "ACTION_UP");
                                view.setBackgroundColor(Color.TRANSPARENT);
                                return true;
                        }
                        return false;
                    }
                });
            }
            isDataSet = true;
            setNumbers();
            if(onLoadData != null) {
                onLoadData.loadData();
            }
        }
    }

    private void moveIndicatior(int position) {
        final int way = position * (widthView / adapter.getItemsCount());
        int move = way - ((LayoutParams) vIndicator.getLayoutParams()).leftMargin;
        TranslateAnimation animation = new TranslateAnimation(0, move, 0, 0);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                llLine.removeView(vIndicator);
                LayoutParams params = new LayoutParams(
                        adapter == null ? 0 : widthView / adapter.getItemsCount(),
                        LayoutParams.MATCH_PARENT
                );
                params.leftMargin = way;
                llLine.addView(vIndicator, params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        vIndicator.startAnimation(animation);
    }

    private void setNumbers() {
        int k = 0;
        for(int i = 0; i < llTabs.getChildCount(); i++) {
            View v = llTabs.getChildAt(i);
            if(v.getVisibility() == VISIBLE) {
                v.setTag(R.string.number, k);
                k++;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "widthMeasureSpec :" + widthMeasureSpec);
        Log.d(TAG, "heightMeasureSpec :" + heightMeasureSpec);
        Log.d(TAG, "width :" + MeasureSpec.getSize(widthMeasureSpec));
        Log.d(TAG, "height :" + MeasureSpec.getSize(heightMeasureSpec));
        heightView = MeasureSpec.getSize(heightMeasureSpec);
        widthView = MeasureSpec.getSize(widthMeasureSpec);
        llLine.getLayoutParams().height = (int) (heightView * 0.1);
        vIndicator.getLayoutParams().width = adapter == null ? 0 : widthView / adapter.getItemsCount();
        setData();
    }
}
