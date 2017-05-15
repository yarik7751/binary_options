package com.elatesoftware.grandcapital.views.items.tooltabsview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.OnChangePosition;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.OnChooseTab;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.OnLoadData;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.ToolTabsViewAdapter;


public class ToolTabsView extends LinearLayout {

    public static final String TAG = "ToolTabsView_TAG";

    private Context context;
    private int lineColor, selectColor, heightView, widthView;
    private int lineWidth = -1, lineMargin = 1;
    private ToolTabsViewAdapter adapter;
    private boolean isDataSet = false, isDeselectAll = false;

    private LinearLayout llTabs, llLine, llMain;
    private View vIndicator, lastView = null;
    private LayoutParams itemParams;

    private OnChooseTab onChooseTab;
    private OnLoadData onLoadData;
    private OnChangePosition onChangePosition;

    public TranslateAnimation animation;

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
    }

    public void setAdapter(ToolTabsViewAdapter _adapter, boolean _isDeselectAll) {
        adapter = _adapter;
        isDataSet = false;
        itemParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        isDeselectAll = _isDeselectAll;
        //setData();
    }

    public void hideTab(int position) {
        Log.d(TAG, "hideTab");
        llTabs.getChildAt(position).setVisibility(GONE);
        setNumbers();
    }

    public void showTab(int position) {
        Log.d(TAG, "showTab");
        View v = llTabs.getChildAt(position);
        if(v != null){
            if(v.getVisibility() == GONE) {
                v.setVisibility(VISIBLE);
            }
            setNumbers();
        }
    }

    public void showAllTabs() {
        Log.d(TAG, "showAllTabs");
        for(int i = 0; i < llTabs.getChildCount(); i++) {
            View v = llTabs.getChildAt(i);
            v.setVisibility(VISIBLE);
        }
        setNumbers();
    }

    public void selectTab(int logicNumber) {
        deselectAllTabs();
        getNumbersTabsToLog();
        int position = getPositionByLogicNumber(logicNumber);
        setTint(llTabs.getChildAt(position), true);
        vIndicator.setVisibility(VISIBLE);
        moveIndicatior((int) llTabs.getChildAt(position).getTag(R.string.number), false);
        /*if(onChooseTab != null) {
            onChooseTab.onChoose(llTabs.getChildAt(position), logicNumber);
        }*/
    }

    public void deselectAllTabs() {
        for(int i = 0; i < llTabs.getChildCount(); i++) {
            View v = llTabs.getChildAt(i);
            setTint(v, false);
        }
        vIndicator.setVisibility(INVISIBLE);
    }

    public void setIcon(int logicNumber, int res) {
        int position = getPositionByLogicNumber(logicNumber);
        ((ImageView) llTabs.getChildAt(position).findViewById(R.id.img)).setImageResource(res);
    }

    public void setOnChooseTab(OnChooseTab onChooseTab) {
        this.onChooseTab = onChooseTab;
    }

    public void setOnLoadData(OnLoadData onLoadData) {
        this.onLoadData = onLoadData;
    }

    public OnLoadData getOnLoadData() {
        return onLoadData;
    }

    public void setOnChangePosition(OnChangePosition onChangePosition) {
        this.onChangePosition = onChangePosition;
    }

    public int getHideTabsCount() {
        int count = 0;
        for(int i = 0; i < llTabs.getChildCount(); i++) {
            View v = llTabs.getChildAt(i);
            if(v.getVisibility() != VISIBLE) {
                count++;
            }
        }
        return count;
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
                View v = View.inflate(context, R.layout.tool_tabs_view_item2, null);
                ImageView img = (ImageView) v.findViewById(R.id.img);
                img.setImageResource(icons[i]);
                setTint(v, false);
                if (titles != null && titles[i] != null) {
                    TextView tv = (TextView) v.findViewById(R.id.tv);
                    tv.setText(titles[i]);
                }
                v.setTag(R.string.logical_number, i);
                Log.d(TAG, "setData widthItem: " + widthView / adapter.getItemsCount());
                itemParams.width = widthView / adapter.getItemsCount();
                llTabs.addView(v, itemParams);
                v.setOnTouchListener((view, motionEvent) -> {
                    int num = (int) view.getTag(R.string.number);
                    int logicNum = (int) view.getTag(R.string.logical_number);
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d(TAG, "ACTION_DOWN");
                            deselectAllTabs();
                            view.setBackgroundColor(selectColor);
                            vIndicator.setVisibility(VISIBLE);
                            //setTint(lastView, false);
                            setTint(view, true);
                            lastView = view;
                            Log.d(TAG, "num: " + num);
                            Log.d(TAG, "logicNum: " + logicNum);
                            moveIndicatior(num, true);
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
                });
            }
            lastView = llTabs.getChildAt(0);
            setTint(llTabs.getChildAt(0), true);
            isDataSet = true;
            if(isDeselectAll) {
                deselectAllTabs();
            }
            setNumbers();
            if(onLoadData != null) {
                onLoadData.loadData();
            }
        }
    }

    private void setTint(View v, boolean isSelected) {
        if(v != null) {
            ImageView img = (ImageView) v.findViewById(R.id.img);
            if(img != null) {
                if (isSelected) {
                    img.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY);
                } else {
                    img.setColorFilter(Color.parseColor("#55ffffff"), PorterDuff.Mode.MULTIPLY);
                }
            }
        }
    }

    private void moveIndicatior(int position, boolean isAnim) {
        final int way = position * (widthView / adapter.getItemsCount());
        int move = way - ((LayoutParams) vIndicator.getLayoutParams()).leftMargin;
        if(isAnim) {
            animation = new TranslateAnimation(0, move, 0, 0);
            animation.setDuration(200);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    vIndicator.clearAnimation();
                    llLine.removeView(vIndicator);
                    LayoutParams params = new LayoutParams(
                            adapter == null ? 0 : widthView / adapter.getItemsCount(),
                            LayoutParams.MATCH_PARENT
                    );
                    params.leftMargin = way;
                    llLine.addView(vIndicator, params);
                    if(onChangePosition != null) {
                        onChangePosition.changePosition();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            vIndicator.startAnimation(animation);
        } else {
            llLine.removeView(vIndicator);
            LayoutParams params = new LayoutParams(
                    adapter == null ? 0 : widthView / adapter.getItemsCount(),
                    LayoutParams.MATCH_PARENT
            );
            params.leftMargin = way;
            llLine.addView(vIndicator, params);
            /*if(onChangePosition != null) {
                onChangePosition.changePosition();
            }*/
        }
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

    private int getPositionByLogicNumber(int logicNumber) {
        for(int i = 0; i < llTabs.getChildCount(); i++) {
            View v = llTabs.getChildAt(i);
            if(((int) v.getTag(R.string.logical_number)) == logicNumber && v.getVisibility() == VISIBLE) {
                return i;
            }
        }
        return -1;
    }

    private void getNumbersTabsToLog() {
        for(int i = 0; i < llTabs.getChildCount(); i++) {
            View v = llTabs.getChildAt(i);
            Log.d(TAG, "view Ni: " + i);
            Log.d(TAG, "logical number: " + v.getTag(R.string.logical_number));
            Log.d(TAG, "number: " + v.getTag(R.string.number));
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
        llLine.getLayoutParams().height = (int) (heightView * 0.05);
        vIndicator.getLayoutParams().width = adapter == null ? 0 : widthView / adapter.getItemsCount();
        setData();
    }
}
