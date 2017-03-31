package com.elatesoftware.grandcapital.views.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.EarlyClosureAnswer;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.api.pojo.OptionsData;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.api.pojo.SignalAnswer;
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.services.CheckDealingService;
import com.elatesoftware.grandcapital.services.DeleteDealingService;
import com.elatesoftware.grandcapital.services.EarlyClosureService;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.services.MakeDealingService;
import com.elatesoftware.grandcapital.services.OrdersService;
import com.elatesoftware.grandcapital.services.SignalService;
import com.elatesoftware.grandcapital.services.SymbolHistoryService;
import com.elatesoftware.grandcapital.utils.AndroidUtils;
import com.elatesoftware.grandcapital.utils.Const;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.ConventImage;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.items.animation.CustomAnimationDrawable;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.views.items.animation.PointAnimation;
import com.elatesoftware.grandcapital.views.items.chart.CustomBaseLimitLine;
import com.elatesoftware.grandcapital.views.items.chart.ViewInfoHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointD;
import com.google.gson.Gson;
import com.github.mikephil.charting.utils.MPPointF;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;
import java.util.List;

public class TerminalFragment extends Fragment {

    public final static String TAG = "TerminalFragment_Logs";

    private static String sSymbolCurrent = "";
    public static double mCurrentValueY = 0;
    private Thread threadSymbolHistory;
    private OrderAnswer currentDealing = new OrderAnswer();

    private static int typePoint = 0;
    private final static int POINT_SIMPLY = 0;
    private final static int POINT_OPEN_DEALING = 1;
    private final static int POINT_CLOSE_DEALING = 2;

    private static boolean isTypeOptionAmerican = false;
    public static boolean isAddInChart = false;
    public static boolean isOpen = false;
    public boolean isDirection = true;
    private boolean isFirstDrawPoint = true;

    private LineChart mChart;
    private TextView tvBalance;
    private TextView tvDeposit;
    private TextView tvLeftActive;
    private TextView tvRightActive;
    private TextView tvValueActive;
    private TextView tvMinusAmount;
    private TextView tvPlusAmount;
    private EditText etValueAmount;
    private TextView tvMinusTime;
    private TextView tvPlusTime;
    private EditText etValueTime;
    private LinearLayout llLowerTerminal;
    private LinearLayout llHigherTerminal;
    private LinearLayout llButtons, llDeposit;
    private LinearLayout llProgressBar;
    private RelativeLayout rlChart;
    private FrameLayout flMain;
    private LinearLayout llTopPanel;
    private TextView tvCurrentActive;
    private TextView tvCurrentActiveAmount;
    private TextView tvSignalMinutes1;
    private TextView tvSignalMinutes5;
    private TextView tvSignalMinutes15;
    private TextView tvErrorSignal;
    private RelativeLayout rlErrorSignal;
    private TextView tvValueRewardTerminal;

    private Drawable drawableMarkerDealing;
    private Bitmap bitmapIconGreenXLabel;
    private Bitmap bitmapIconRedXLabel;
    private Bitmap bitmapIconRedYLabel;
    private Bitmap bitmapIconGreenYLabel;
    private Bitmap bitmapIconCurrentDealingGreenYLabel;
    private Bitmap bitmapIconCurrentDealingRedYLabel;
    private Bitmap bitmapIconCurrentPoint;
    private int colorRed;
    private int colorGreen;

    private ImageView imgPointCurrent;
    private PointAnimation pointAnimation;
    private Dialog dialogAgreeDeleteDealing;
    private YAxis rightYAxis;
    private XAxis xAxis;
    private CustomBaseLimitLine currentLineSocket;
    private CustomBaseLimitLine currentLineDealing;

    private int numberTemporaryPoint = 1;
    private float divX = 0, divY = 0;
    private Entry entryLast;
    private Handler handler;
    private Entry currEntry;

    private ViewInfoHelper mViewInfoHelper;

    private static List<String> listActives = new ArrayList<>();
    public static List<SocketAnswer> listSocketPointsBackGround = new ArrayList<>();
    public static List<OrderAnswer> listCurrentClosingDealings = new ArrayList<>();
    public static List<OrderAnswer> listOpenDealings = new ArrayList<>();

    private GetResponseSymbolHistoryBroadcastReceiver mSymbolHistoryBroadcastReceiver;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;
    private GetResponseOpenDealingBroadcastReceiver mMakeDealingBroadcastReceiver;
    private GetResponseSignalsBroadcastReceiver mSignalsBroadcastReceiver;
    private GetResponseCheckClosedDealingBroadcastReceiver mCheckClosedDealingBroadcastReceiver;
    private GetResponseOrdersBroadcastReceiver mOrdersBroadcastReceiver;
    private GetResponseEarlyClosureBroadcastReceiver mEarlyClosureBroadcastReceiver;
    private GetResponseDeleteDealingBroadcastReceiver mDeleteDealingBroadcastReceiver;

    private static TerminalFragment fragment = null;
    public static TerminalFragment getInstance() {
        if (fragment == null) {
            fragment = new TerminalFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_terminal, container, false);
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onCreateView Terminal");
        BaseActivity.backToRootFragment = false;
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(false);

        isFirstDrawPoint = true;
        drawableMarkerDealing = getResources().getDrawable(R.drawable.marker_close_dealing);
        bitmapIconGreenXLabel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.green_vert);
        bitmapIconRedXLabel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.red_vert);
        bitmapIconCurrentDealingGreenYLabel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.green_hor);
        bitmapIconCurrentDealingRedYLabel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.red_hor);
        bitmapIconCurrentPoint = BitmapFactory.decodeResource(getResources(), R.drawable.front_elipsa);
        colorGreen = getResources().getColor(R.color.chat_green);
        colorRed = getResources().getColor(R.color.color_red_chart);
        bitmapIconRedYLabel = ConventImage.loadBitmapFromView(LayoutInflater.from(getContext()).inflate(R.layout.incl_chart_label_red, null));
        bitmapIconGreenYLabel = ConventImage.loadBitmapFromView(LayoutInflater.from(getContext()).inflate(R.layout.incl_chart_label_green, null));

        mChart = (LineChart) parentView.findViewById(R.id.chart);
        tvBalance = (TextView) parentView.findViewById(R.id.tvBalanceTerminal);
        tvDeposit = (TextView) parentView.findViewById(R.id.tvDepositTerminal);
        tvLeftActive = (TextView) parentView.findViewById(R.id.tvLeftTabActiveTerminal);
        tvRightActive = (TextView) parentView.findViewById(R.id.tvRightTabActiveTerminal);
        tvValueActive = (TextView) parentView.findViewById(R.id.tvValueTabActiveTerminal);
        tvMinusAmount = (TextView) parentView.findViewById(R.id.tvMinusTabAmountTerminal);
        tvPlusAmount = (TextView) parentView.findViewById(R.id.tvPlusTabAmountTerminal);
        etValueAmount = (EditText) parentView.findViewById(R.id.tvValueTabAmountTerminal);
        tvMinusTime = (TextView) parentView.findViewById(R.id.tvMinusTabTimeTerminal);
        tvPlusTime = (TextView) parentView.findViewById(R.id.tvPlusTabTimeTerminal);
        etValueTime = (EditText) parentView.findViewById(R.id.tvValueTabTimeTerminal);
        llLowerTerminal = (LinearLayout) parentView.findViewById(R.id.llLowerTerminal);
        llHigherTerminal = (LinearLayout) parentView.findViewById(R.id.llHigherTerminal);
        llTopPanel = (LinearLayout) parentView.findViewById(R.id.ll_top_panel);
        llButtons = (LinearLayout) parentView.findViewById(R.id.ll_buttons);
        llDeposit = (LinearLayout) parentView.findViewById(R.id.ll_deposit);
        rlChart = (RelativeLayout) parentView.findViewById(R.id.rl_chart);
        llProgressBar = (LinearLayout) parentView.findViewById(R.id.progress_bar);
        flMain = (FrameLayout) parentView.findViewById(R.id.fl_main);
        tvSignalMinutes1 = (TextView) parentView.findViewById(R.id.tv_time1_value);
        tvSignalMinutes5 = (TextView) parentView.findViewById(R.id.tv_time2_value);
        tvSignalMinutes15 = (TextView) parentView.findViewById(R.id.tv_time3_value);
        tvCurrentActive = (TextView) parentView.findViewById(R.id.tv_currency);
        tvCurrentActiveAmount = (TextView) parentView.findViewById(R.id.tv_amout);
        tvErrorSignal = (TextView) parentView.findViewById(R.id.tvErrorSignal);
        rlErrorSignal = (RelativeLayout) parentView.findViewById(R.id.rlErrorSignal);
        tvValueRewardTerminal = (TextView) parentView.findViewById(R.id.tvValueRewardTerminal);

        return parentView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onViewCreated Terminal");
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_terminal));
        BaseActivity.getToolbar().mTabLayout.setOnLoadData(() -> {
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
            BaseActivity.getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        });
        mViewInfoHelper = new ViewInfoHelper(rlChart);

        etValueAmount.clearFocus();
        etValueTime.clearFocus();

        KeyboardVisibilityEvent.registerEventListener(getActivity(), isOpen1 -> {
            if (etValueAmount.isFocused()) {
                ConventString.setMaskAmount(etValueAmount, isOpen1);
            }
            if (etValueTime.isFocused()) {
                ConventString.setMaskTime(etValueTime, isOpen1);
            }
        });
        etValueAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if(etValueAmount != null) {
                ConventString.setMaskAmount(etValueAmount, hasFocus);
            }
        });
        etValueTime.setOnFocusChangeListener((v, hasFocus) -> {
            if(etValueTime != null) {
                ConventString.setMaskTime(etValueTime, hasFocus);
            }
        });
        etValueAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                requestEarlyClosure();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        etValueTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                requestEarlyClosure();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        tvMinusAmount.setOnClickListener(v -> {
            ConventString.changeAmountValue(etValueAmount, false);
        });
        tvPlusAmount.setOnClickListener(v -> {
            ConventString.changeAmountValue(etValueAmount, true);
        });
        tvPlusTime.setOnClickListener(v -> {
            ConventString.changeTimeValue(etValueTime, true);
        });
        tvMinusTime.setOnClickListener(v -> {
            ConventString.changeTimeValue(etValueTime, false);
        });
        tvLeftActive.setOnClickListener(v -> {
            if (!ConventString.getActive(tvValueActive).isEmpty() && listActives.size() > 0) {
                int index = listActives.indexOf(ConventString.getActive(tvValueActive));
                if (index == 0) {
                    sSymbolCurrent = listActives.get(listActives.size() - 1);
                } else {
                    sSymbolCurrent = listActives.get(index - 1);
                }
                changeActive();
                parseResponseSignals(ConventString.getActive(tvValueActive));
            } else {
                sSymbolCurrent = Const.SYMBOL;
            }
        });
        tvRightActive.setOnClickListener(v -> {
            if (!ConventString.getActive(tvValueActive).isEmpty() && listActives.size() > 0) {
                int index = listActives.indexOf(ConventString.getActive(tvValueActive));
                if (index == (listActives.size() - 1)) {
                    sSymbolCurrent = listActives.get(0);
                } else {
                    sSymbolCurrent = listActives.get(index + 1);
                }
                changeActive();
                parseResponseSignals(ConventString.getActive(tvValueActive));
            } else {
                sSymbolCurrent = Const.SYMBOL;
            }
        });
        tvDeposit.setOnClickListener(v -> {
            BaseActivity.changeMainFragment(new DepositFragment());
        });
        llLowerTerminal.setOnClickListener(v -> {
            requestMakeDealing(Const.CMD_LOWER);
        });
        llHigherTerminal.setOnClickListener(v -> {
            requestMakeDealing(Const.CMD_HEIGHT);
        });
        initializationCurrentPoint();
        initializationChart();
        setSizeHeight();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onResume Terminal");
        handler = new Handler();
        llProgressBar.setVisibility(View.VISIBLE);
        isOpen = true;
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(false);
        registerBroadcasts();
        clearChart();
        ConventString.updateBalance(tvBalance);
        if (sSymbolCurrent != null && !sSymbolCurrent.equals("")) {
            tvValueActive.setText(sSymbolCurrent);
            parseResponseSymbolHistory();
            llProgressBar.setVisibility(View.GONE);
        } else {
            changeActive();
        }
        requestGetAllOrders();
        setEnabledBtnChooseActive(true);
    }
    @Override
    public void onPause() {
        if (threadSymbolHistory != null) {
            threadSymbolHistory.interrupt();
        }
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onPause() Terminal");
        isAddInChart = false;
        isFirstDrawPoint = true;
        imgPointCurrent.setVisibility(View.INVISIBLE);
        clearChart();
        unregisterBroadcasts();
        isOpen = false;
        super.onPause();
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(true);
    }
    @Override
    public void onDestroy() {
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onDestroy() Terminal");
        GrandCapitalApplication.closeSocket();
        super.onDestroy();
    }

    private void registerBroadcasts() {
        mInfoBroadcastReceiver = new GetResponseInfoBroadcastReceiver();
        IntentFilter intentFilterInfo = new IntentFilter(InfoUserService.ACTION_SERVICE_GET_INFO);
        intentFilterInfo.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mInfoBroadcastReceiver, intentFilterInfo);

        mSymbolHistoryBroadcastReceiver = new GetResponseSymbolHistoryBroadcastReceiver();
        IntentFilter intentFilterSymbolHistory = new IntentFilter(SymbolHistoryService.ACTION_SERVICE_SYMBOL_HISTORY);
        intentFilterSymbolHistory.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mSymbolHistoryBroadcastReceiver, intentFilterSymbolHistory);

        mMakeDealingBroadcastReceiver = new GetResponseOpenDealingBroadcastReceiver();
        IntentFilter intentFilterMakeDealing = new IntentFilter(MakeDealingService.ACTION_SERVICE_MAKE_DEALING);
        intentFilterMakeDealing.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mMakeDealingBroadcastReceiver, intentFilterMakeDealing);

        mSignalsBroadcastReceiver = new GetResponseSignalsBroadcastReceiver();
        IntentFilter intentFilterSignal = new IntentFilter(SignalService.ACTION_SERVICE_SIGNAL);
        intentFilterSignal.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mSignalsBroadcastReceiver, intentFilterSignal);

        mCheckClosedDealingBroadcastReceiver = new GetResponseCheckClosedDealingBroadcastReceiver();
        IntentFilter intentFilterCloseDealing = new IntentFilter(CheckDealingService.ACTION_SERVICE_CHECK_DEALINGS);
        intentFilterCloseDealing.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mCheckClosedDealingBroadcastReceiver, intentFilterCloseDealing);

        mOrdersBroadcastReceiver = new GetResponseOrdersBroadcastReceiver();
        IntentFilter intentFilterOrders = new IntentFilter(OrdersService.ACTION_SERVICE_ORDERS);
        intentFilterOrders.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mOrdersBroadcastReceiver, intentFilterOrders);

        mEarlyClosureBroadcastReceiver = new GetResponseEarlyClosureBroadcastReceiver();
        IntentFilter intentFilterOrdersEarlyClosure = new IntentFilter(EarlyClosureService.ACTION_SERVICE_EARLY_CLOSURE);
        intentFilterOrdersEarlyClosure.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mEarlyClosureBroadcastReceiver, intentFilterOrdersEarlyClosure);

        mDeleteDealingBroadcastReceiver = new GetResponseDeleteDealingBroadcastReceiver();
        IntentFilter intentFilterDeleteDealing = new IntentFilter(DeleteDealingService.ACTION_SERVICE_DELETE_FEALING);
        intentFilterDeleteDealing.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mDeleteDealingBroadcastReceiver, intentFilterDeleteDealing);
    }
    private void unregisterBroadcasts() {
        getActivity().unregisterReceiver(mSymbolHistoryBroadcastReceiver);
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
        getActivity().unregisterReceiver(mMakeDealingBroadcastReceiver);
        getActivity().unregisterReceiver(mSignalsBroadcastReceiver);
        getActivity().unregisterReceiver(mCheckClosedDealingBroadcastReceiver);
        getActivity().unregisterReceiver(mOrdersBroadcastReceiver);
        getActivity().unregisterReceiver(mEarlyClosureBroadcastReceiver);
        getActivity().unregisterReceiver(mDeleteDealingBroadcastReceiver);
    }

    private void initializationChart() {
        mChart.setNoDataText(getResources().getString(R.string.request_error_title));
        mChart.setDragDecelerationFrictionCoef(0.3f);
        mChart.setDragDecelerationEnabled(true);
        mChart.setHighlightPerDragEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setPadding(0, 0, 0, 0);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.setScaleYEnabled(false);
        mChart.setScaleMinima(0.4f, 1f);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.getLegend().setEnabled(false);
        mChart.setDrawMarkers(true);
        mChart.getViewPortHandler().setMaximumScaleX(10f);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        xAxis = mChart.getXAxis();
        xAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        xAxis.setTextColor(getResources().getColor(R.color.chart_values));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setValueFormatter((value, axis) -> ConventDate.convertDateFromMilSecHHMM(ConventDate.genericTimeForChartLabels(value)));
        xAxis.setEnabled(true);
        xAxis.setTextSize(9);
        xAxis.disableAxisLineDashedLine();
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceMax(600000f);

        YAxis leftYAxis = mChart.getAxisLeft();
        leftYAxis.setEnabled(false);
        rightYAxis = mChart.getAxisRight();
        rightYAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        rightYAxis.setTextColor(getResources().getColor(R.color.chart_values));
        rightYAxis.setEnabled(true);
        rightYAxis.setTextSize(9);
        rightYAxis.setDrawGridLines(true);
        rightYAxis.disableAxisLineDashedLine();
        rightYAxis.setValueFormatter((value, axis) -> String.format("%.5f", value).replace(',', '.'));
        rightYAxis.setStartAtZero(false);

        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }
            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }
            @Override
            public void onChartLongPressed(MotionEvent me) {

            }
            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }
            @Override
            public void onChartSingleTapped(MotionEvent event) {
                onClickXLimitLines(event.getX(), event.getY());
                onClickYLimitLines(event.getX(), event.getY());
            }
            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {}

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                if (imgPointCurrent != null || currEntry != null) {
                    MPPointF point = mChart.getPosition(currEntry, YAxis.AxisDependency.RIGHT);
                    if(point != null){
                        imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
                        imgPointCurrent.setY(point.getY() - imgPointCurrent.getHeight() / 2);
                    }
                }
                redrawScrollLinesDealings(mChart.getHighestVisibleX());
            }
            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                if (imgPointCurrent != null || currEntry != null) {
                    MPPointF point = mChart.getPosition(currEntry, YAxis.AxisDependency.RIGHT);
                    if(point != null) {
                        Log.d(TAG, "maxim x: " + mChart.getHighestVisibleX());
                        Log.d(TAG, "point x: " + (point.getX() - imgPointCurrent.getWidth() / 2));
                        if(currEntry.getX() >= mChart.getHighestVisibleX()) {
                            imgPointCurrent.setVisibility(View.INVISIBLE);
                        } else {
                            imgPointCurrent.setVisibility(View.VISIBLE);
                        }
                        imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
                        imgPointCurrent.setY(point.getY() - imgPointCurrent.getHeight() / 2 - dY);
                    }
                }
                redrawScrollLinesDealings(mChart.getHighestVisibleX());
            }
        });
    }
    private void initializationCurrentPoint() {
        imgPointCurrent = new ImageView(getContext());
        imgPointCurrent.setId(R.id.img);
        pointAnimation = new PointAnimation(getContext(), imgPointCurrent);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(AndroidUtils.dp(40), AndroidUtils.dp(40));
        rlChart.addView(imgPointCurrent, params);
        imgPointCurrent.setVisibility(View.INVISIBLE);
        pointAnimation.start();
    }

    private void setEnabledBtnTerminal(boolean enabled) {
        llHigherTerminal.setEnabled(enabled);
        llLowerTerminal.setEnabled(enabled);
    }
    private void setEnabledBtnChooseActive(boolean enabled) {
        tvLeftActive.setEnabled(enabled);
        tvRightActive.setEnabled(enabled);
    }
    private void setSizeHeight() {
        int height = AndroidUtils.getWindowsSizeParams(getContext())[1] - AndroidUtils.getStatusBarHeight(getContext()) - AndroidUtils.dp(60);
        rlChart.getLayoutParams().height = (int) (height * 0.6);
        llDeposit.getLayoutParams().height = (int) (height * 0.28);
        llButtons.getLayoutParams().height = (int) (height * 0.11);
    }

    private List<CustomBaseLimitLine> getXLimitLines() {
        List<CustomBaseLimitLine> list = new ArrayList<>();
        if(xAxis.getLimitLines() != null && xAxis.getLimitLines().size() != 0){
            for(LimitLine l: xAxis.getLimitLines()){
                list.add((CustomBaseLimitLine) l);
            }
            return list;
        }else{
            return null;
        }
    }
    private List<CustomBaseLimitLine> getYLimitLines(){
        List<CustomBaseLimitLine> list = new ArrayList<>();
        if(rightYAxis.getLimitLines() != null && rightYAxis.getLimitLines().size() != 0){
            for(LimitLine l: rightYAxis.getLimitLines()){
                list.add((CustomBaseLimitLine) l);
            }
            if(currentLineSocket != null){
                list.remove(currentLineSocket);
            }
            if(currentLineDealing != null){
                list.remove(currentLineDealing);
            }
            return list;
        }else{
            return null;
        }
    }
    private synchronized LineDataSet createSetDataChart() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.WHITE);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(1.7f);
        set.setFillAlpha(50);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawIcons(true);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setDrawCircleHole(false);
        set.setDrawFilled(true);
        set.setFillColor(Color.WHITE);
        set.setFillAlpha(50);
        set.setHighlightEnabled(true);
        set.setDrawHighlightIndicators(false);
        return set;
    }
    public LineData getLineData(){
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSetDataChart();
                data.addDataSet(set);
            }
        }
        return data;
    }

    private void clearChart() {
        currEntry = null;
        entryLast = null;
        currentLineDealing = null;
        currentLineSocket = null;
        currentDealing = null;
        mCurrentValueY = 0;
        typePoint = POINT_SIMPLY;
        mChart.highlightValues(null);
        rightYAxis.removeAllLimitLines();
        xAxis.removeAllLimitLines();
        mChart.getLineData().clearValues();
        mChart.clearValues();
        mChart.invalidate();
    }
    private void changeActive() {
        setEnabledBtnChooseActive(false);
        if (sSymbolCurrent == null || sSymbolCurrent.equals("")) {
            sSymbolCurrent = Const.SYMBOL;
        }
        isFirstDrawPoint = true;
        imgPointCurrent.setVisibility(View.INVISIBLE);
        llProgressBar.setVisibility(View.VISIBLE);
        tvValueActive.setText(sSymbolCurrent);
        clearChart();
        SymbolHistoryAnswer.nullInstance();
        SocketAnswer.nullInstance();
        if (threadSymbolHistory != null) {
            threadSymbolHistory.interrupt();
        }
        listOpenDealings.clear();
        listCurrentClosingDealings.clear();
        listSocketPointsBackGround.clear();
        GrandCapitalApplication.closeSocket();
        requestSymbolHistory(ConventString.getActive(tvValueActive));
        requestGetAllOrders();
    }

    private void onClickXLimitLines(float x, float y){
        List<CustomBaseLimitLine> listLimit = getXLimitLines();
        if(listLimit != null && listLimit.size() != 0){
            MPPointD point = mChart.getTransformer(YAxis.AxisDependency.RIGHT).getValuesByTouchPoint(x, y);
            if(point != null){
                for(CustomBaseLimitLine line: listLimit){
                    OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                    if(order != null && order.getOpenPrice() != null){
                        float tappedY = Float.valueOf(String.valueOf(order.getOpenPrice()));
                        if(line.ismIsAmerican()) {
                            if (line.ismIsAmerican() && (point.x - line.getLimit() <= 1200000 && point.x - line.getLimit() >= 0)) {
                                if ((tappedY - point.y >= 0 && tappedY - point.y <= 0.00001) || (point.y - tappedY >= 0 && point.y - tappedY <= 0.00001)) {
                                    showViewCloseDealing(order);
                                    break;
                                }
                            }
                        }
                    }
                    if ((line.getLimit() - point.x <= 9000 && line.getLimit() - point.x >= 0) ||
                            (point.x - line.getLimit() <= 9000 && point.x - line.getLimit() >= 0)) {
                        makeActiveSelectedDealing(line);
                        break;
                    }
                }
            }
        }
    }
    private void onClickYLimitLines(float x, float y){
        float xMax = mChart.getHighestVisibleX();
        List<CustomBaseLimitLine> listLimit = getYLimitLines();
        if(listLimit != null && listLimit.size() != 0){
            MPPointD point = mChart.getTransformer(YAxis.AxisDependency.RIGHT).getValuesByTouchPoint(x, y);
            if(point != null){
                for(CustomBaseLimitLine line: listLimit){
                    OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                    if((order != null && order.getOpenPrice() != null && line.ismIsAmerican()) && (line.ismIsAmerican() && (point.x - xMax <= 1200000 && point.x - xMax >= 0))) {
                        float tappedY = Float.valueOf(String.valueOf(order.getOpenPrice()));
                        if ((tappedY - point.y >= 0 && tappedY - point.y <= 0.00001) || (point.y - tappedY >= 0 && point.y - tappedY <= 0.00001)) {
                            showViewCloseDealing(order);
                            break;
                        }
                    }
                    if ((xMax - point.x <= 9000 && xMax - point.x >= 0) || (point.x - xMax <= 9000 && point.x - xMax >= 0)) {
                       // makeActiveSelectedDealing(line);
                        break;
                    }
                }
            }
        }
    }

    private void requestEarlyClosure() {
        Intent intent = new Intent(getActivity(), EarlyClosureService.class);
        intent.putExtra(EarlyClosureService.SYMBOL, tvValueActive.getText().toString());
        intent.putExtra(EarlyClosureService.TIME, ConventString.getTimeValue(etValueTime));
        getActivity().startService(intent);
    }
    private void requestSymbolHistory(String symbol) {
        Intent intentService = new Intent(getActivity(), SymbolHistoryService.class);
        intentService.putExtra(SymbolHistoryService.SYMBOL, symbol);
        getActivity().startService(intentService);
    }
    private void requestSignals() {
        Intent intentService = new Intent(getActivity(), SignalService.class);
        getActivity().startService(intentService);
    }
    private void requestMakeDealing(String lowerOrHeight) {
        llProgressBar.setVisibility(View.VISIBLE);
        setEnabledBtnTerminal(false);
        if (ConventString.getAmountValue(etValueAmount) != 0 && ConventString.getTimeValue(etValueTime) != 0 && !ConventString.getActive(tvValueActive).isEmpty()) {
            if(ConventString.getTimeValue(etValueTime) > Const.MAX_TIME_MIN) {
                CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.error_max_time));
                llProgressBar.setVisibility(View.GONE);
                setEnabledBtnTerminal(true);
                return;
            }
            Intent intentService = new Intent(getActivity(), MakeDealingService.class);
            intentService.putExtra(MakeDealingService.CMD, lowerOrHeight);
            intentService.putExtra(MakeDealingService.SYMBOL, ConventString.getActive(tvValueActive));
            intentService.putExtra(MakeDealingService.VOLUME, String.valueOf(ConventString.getAmountValue(etValueAmount)));
            intentService.putExtra(MakeDealingService.EXPIRATION, String.valueOf(ConventString.getTimeValue(etValueTime)));
            getActivity().startService(intentService);
        } else {
            CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.no_correct_values));
            llProgressBar.setVisibility(View.GONE);
            setEnabledBtnTerminal(true);
        }
    }
    private void requestGetAllOrders() {
        if(isAdded()) {
            Intent intentService = new Intent(getActivity(), OrdersService.class);
            intentService.putExtra(OrdersService.FUNCTION, OrdersService.GET_ALL_ORDERS);
            getActivity().startService(intentService);
        }
    }
    private void requestGetTicketOrder(OrderAnswer order) {
        if(isAdded()) {
            Intent intentService = new Intent(getActivity(), OrdersService.class);
            intentService.putExtra(OrdersService.FUNCTION, OrdersService.GET_TICKET_ORDER);
            intentService.putExtra(OrdersService.ORDER, new Gson().toJson(order));
            getActivity().startService(intentService);
        }
    }
    private void requestDeleteDealing(OrderAnswer order){
        if(order.getTicket() != null && order.getTicket() != 0 ){
            Intent intentService = new Intent(getActivity(), DeleteDealingService.class);
            getActivity().startService(intentService.putExtra(DeleteDealingService.TICKET, order.getTicket()));
        }else{
            requestGetTicketOrder(order);
        }
    }

    private void parseResponseSymbolHistory() {
        if (SymbolHistoryAnswer.getInstance() != null && SymbolHistoryAnswer.getInstance().size() != 0) {
            List<SocketAnswer> list;
            if (listSocketPointsBackGround != null && listSocketPointsBackGround.size() != 0) {
                list = listSocketPointsBackGround.subList(0, listSocketPointsBackGround.size()-1);
                Log.d(GrandCapitalApplication.TAG_SOCKET, "add from background in list socketanswer size = " + (list.size() - 1));
                for (SocketAnswer item : list) {
                    SymbolHistoryAnswer.addSocketAnswerInSymbol(item);
                }
                listSocketPointsBackGround.clear();
            }
            drawDataSymbolHistory(SymbolHistoryAnswer.getInstance(), ConventString.getActive(tvValueActive));
        } else {
            listSocketPointsBackGround.clear();
            isAddInChart = true;
        }
    }
    private void parseResponseSignals(String symbol) {
        if (SignalAnswer.getInstance() != null) {
            List<SignalAnswer> list = SignalAnswer.getSignalsActive(symbol);
            if (list.size() != 0) {
                rlErrorSignal.setVisibility(View.GONE);
                tvCurrentActive.setText(symbol);
                tvCurrentActiveAmount.setText(list.get(0).getCost());
                for (SignalAnswer answer : list) {
                    if (answer.getTime() == 60) {
                        ConventString.parseResponseSignals(getActivity(), tvSignalMinutes1, answer.getSummary());
                    } else if (answer.getTime() == 300) {
                        ConventString.parseResponseSignals(getActivity(), tvSignalMinutes5, answer.getSummary());
                    } else if (answer.getTime() == 900) {
                        ConventString.parseResponseSignals(getActivity(), tvSignalMinutes15, answer.getSummary());
                    }
                }
            } else {
                rlErrorSignal.setVisibility(View.VISIBLE);
                tvErrorSignal.setText(getResources().getString(R.string.error_find_signal));
            }
        }
    }
    private void parseClosingDealings(List<OrderAnswer> listAllClosedDealings){
        if(listCurrentClosingDealings != null && listCurrentClosingDealings.size() != 0){
            for(OrderAnswer order : listCurrentClosingDealings){
                for(OrderAnswer orderClosed : listAllClosedDealings){
                    if ((int) order.getTicket() == (int) orderClosed.getTicket()) {
                        mViewInfoHelper.updateSettingsCloseDealing(orderClosed, getActivity());
                        if(order.getSymbol().equals(ConventString.getActive(tvValueActive))) {
                            drawAllDealingsLimitLines(listOpenDealings);
                            //redrawPointsDealings(orderClosed);
                        }
                        break;
                    }
                }
            }
            listCurrentClosingDealings.clear();
        }
    }

    public synchronized void addEntry(final SocketAnswer answer) {
        if (answer != null && answer.getTime() != null && answer.getAsk() != null) {
            LineData data = getLineData();
            if (data != null) {
                SymbolHistoryAnswer.addSocketAnswerInSymbol(answer);
                mCurrentValueY = answer.getAsk();
                currEntry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getAsk())), null, null);
                entryLast = data.getDataSetByIndex(0).getEntryForIndex(data.getDataSetByIndex(0).getEntryCount()-1);

                divX = (currEntry.getX() - entryLast.getX()) / 10.f;
                divY = (currEntry.getY() - entryLast.getY()) / 10.f;

                numberTemporaryPoint = 1;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(entryLast != null && currEntry != null) {
                            float x = entryLast.getX();
                            float y = entryLast.getY();
                            x += divX;
                            y += divY;
                            Entry newEntry;
                            if (numberTemporaryPoint == 9) {
                                switch (typePoint) {
                                    case POINT_CLOSE_DEALING:
                                        newEntry = new Entry(x, y, drawableMarkerDealing, null);
                                        break;
                                    case POINT_OPEN_DEALING:
                                        OrderAnswer order = currentDealing;
                                        order.setOpenPrice(answer.getAsk());
                                        String dataEntry = new Gson().toJson(order);
                                        newEntry = new Entry(x, y, drawableMarkerDealing, dataEntry);
                                        drawDealingLimitLine(order, false);
                                        break;
                                    case POINT_SIMPLY:
                                    default:
                                        newEntry = new Entry(x, y, null, null);
                                        break;
                                }
                            }else{
                                newEntry = new Entry(x, y, null, null);
                            }
                            numberTemporaryPoint++;
                            LineData data = mChart.getData();
                            data.addEntry(newEntry, 0);
                            data.notifyDataChanged();
                            mChart.notifyDataSetChanged();
                            mChart.invalidate();
                            if (numberTemporaryPoint == 9) {
                                typePoint = POINT_SIMPLY;
                                redrawXYDealingLimitLines();
                            }
                            drawSocketCurrentYLimitLine(newEntry);
                            entryLast = newEntry;
                            if (numberTemporaryPoint < 10) {
                                handler.postDelayed(this, 50);
                            }
                        }
                    }
                }, 50);
            }
        }
    }
    private void addEntry(final SymbolHistoryAnswer answer) {
        if (answer != null && answer.getTime() != null && answer.getOpen() != null) {
            LineData data = getLineData();
            if (data != null) {
                Entry entry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getOpen())), null, null);
                data.addEntry(entry, 0);
                data.notifyDataChanged();
                mChart.notifyDataSetChanged();
            }
        }
    }
    private void drawDataSymbolHistory(List<SymbolHistoryAnswer> listSymbol, final String symbol) {
        if (threadSymbolHistory != null) {
            threadSymbolHistory.interrupt();
        }
        threadSymbolHistory = new Thread(() -> {
            Log.d(GrandCapitalApplication.TAG_SOCKET, "drawDataSymbolHistory size = " + listSymbol.size());
            if (listSymbol.size() != 0) {
                for (int i = 0; i < listSymbol.size() - 1; i++) {
                    int finalI = i;
                    getActivity().runOnUiThread(() -> {
                        addEntry(listSymbol.get(finalI));
                    });
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (mChart.getLineData() != null) {
                mCurrentValueY = Double.valueOf(String.valueOf(listSymbol.get(listSymbol.size() - 1).getOpen()));
                Entry entry = new Entry(ConventDate.genericTimeForChart(listSymbol.get(listSymbol.size() - 1).getTime()),
                        Float.valueOf(String.valueOf(mCurrentValueY)), null, null);
                mChart.zoom(10f, 0f, entry.getX(), 0f, YAxis.AxisDependency.RIGHT);
                currEntry = entry;
                getActivity().runOnUiThread(() -> {
                    drawSocketCurrentYLimitLine(entry);
                    if (isFirstDrawPoint) {
                        imgPointCurrent.setVisibility(View.VISIBLE);
                        isFirstDrawPoint = false;
                    }
                    if (imgPointCurrent != null || currEntry != null) {
                        MPPointF point = mChart.getPosition(entry, YAxis.AxisDependency.RIGHT);
                        if (point != null) {
                            Log.d(TAG, "setPoint position");
                            imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
                            imgPointCurrent.setY(point.getY() - imgPointCurrent.getHeight() / 2);
                        }
                    }
                });
            }
            GrandCapitalApplication.closeAndOpenSocket(symbol);
            isAddInChart = true;
        });
        threadSymbolHistory.start();
    }

    private void redrawPointsDealings(OrderAnswer order){
        if (mChart.getData() != null) {
            ILineDataSet set = mChart.getData().getDataSetByIndex(0);
            typePoint = POINT_CLOSE_DEALING;
            for (int i = set.getEntryCount() - 1; i >= 0; i--) {
                Entry entry = set.getEntryForIndex(i);
                if (entry.getData() != null && entry.getData() instanceof String) {
                    OrderAnswer dataPoint = new Gson().fromJson(String.valueOf(entry.getData()) , OrderAnswer.class);
                    if(ConventDate.equalsTimePoints(dataPoint.getOptionsData().getExpirationTime(), order.getOptionsData().getExpirationTime())){
                        Log.d(GrandCapitalApplication.TAG_SOCKET, "closeDealing");
                        entry.setIcon(null);
                        entry.setData(null);
                        mChart.getData().notifyDataChanged();
                        mChart.notifyDataSetChanged();
                        mChart.invalidate();
                        break;
                    }
                }
            }
        }
    }
    private void drawCurrentPoint(Entry entry) {
        if(isFirstDrawPoint) {
            //hideCurrentPoint();
        } else {
            //imgPointCurrent.setVisibility(View.VISIBLE);
        }
        if (imgPointCurrent != null) {
            MPPointF point = mChart.getPosition(entry, YAxis.AxisDependency.RIGHT);
            imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
            imgPointCurrent.setY(point.getY() - imgPointCurrent.getHeight() / 2);
        }
    }
    private void drawSocketCurrentYLimitLine(Entry entry) {
        if (entry != null) {
            if (currentLineSocket != null) {
                rightYAxis.removeLimitLine(currentLineSocket);
            }
            currentLineSocket = new CustomBaseLimitLine(entry.getY(), String.valueOf(entry.getY()));
            currentLineSocket.setLineColor(Color.WHITE);
            currentLineSocket.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_HORIZONTAL_CURRENT_SOCKET);
            rightYAxis.addLimitLine(currentLineSocket);
            drawCurrentPoint(entry);
        }
    }
    private void drawCurrentDealingYLimitLine(CustomBaseLimitLine lineX, OrderAnswer order) {
        if (currentLineDealing != null) {
            rightYAxis.removeLimitLine(currentLineDealing);
        }
        if(getXLimitLines() != null  && lineX != null){
            lineX.enableDashedLine(0f, 0f, 0f);
            lineX.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE);
            currentLineDealing = new CustomBaseLimitLine(Float.valueOf(String.valueOf(order.getOpenPrice())), String.valueOf(order.getOpenPrice()), null);
            if(lineX.getLineColor() == colorGreen){
                currentLineDealing.setmBitmapLabelY(bitmapIconCurrentDealingGreenYLabel);
                currentLineDealing.setLineColor(colorGreen);
            }else if (lineX.getLineColor() == colorRed){
                currentLineDealing.setmBitmapLabelY(bitmapIconCurrentDealingRedYLabel);
                currentLineDealing.setLineColor(colorRed);
            }
            currentLineDealing.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_HORIZONTAL_CURRENT_DEALING);
            rightYAxis.addLimitLine(currentLineDealing);
        }
    }

    private void redrawScrollLinesDealings(float xMax){
        List<CustomBaseLimitLine> listX = getXLimitLines();
        List<CustomBaseLimitLine> listY = getYLimitLines();
        if(listX != null && listX.size() != 0){
            for(CustomBaseLimitLine lineX : listX){
                if(lineX.getLimit() >= xMax){
                    OrderAnswer order = new Gson().fromJson(lineX.getLabel(), OrderAnswer.class);
                    CustomBaseLimitLine lineY = new CustomBaseLimitLine(Float.valueOf(String.valueOf(order.getOpenPrice())),
                            lineX.getLabel(), lineX.getmBitmapLabelY(),
                            String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), lineX.ismIsAmerican());
                    lineY.setLineColor(Color.TRANSPARENT);
                    lineY.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_HORIZONTAL_DEALING);
                    rightYAxis.addLimitLine(lineY);
                    if (lineX.getTypeLimitLine() == CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE){
                        drawCurrentDealingYLimitLine(lineX, order);
                    }
                    xAxis.removeLimitLine(lineX);
                }
            }
        }
        if(listY != null && listY.size() != 0) {
            for (CustomBaseLimitLine lineY : listY) {
                OrderAnswer order = new Gson().fromJson(lineY.getLabel(), OrderAnswer.class);
                if (ConventDate.genericTimeForChart(ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000) < xMax) {
                    CustomBaseLimitLine line = new CustomBaseLimitLine(ConventDate.genericTimeForChart(
                            ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000),
                            new Gson().toJson(order), lineY.getmBitmapLabelX(), lineY.getmBitmapLabelY(), lineY.getmTimer(), lineY.ismIsAmerican());
                    line.enableDashedLine(10f, 10f, 0f);
                    line.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_PASS);
                    xAxis.addLimitLine(line);
                    rightYAxis.removeLimitLine(lineY);
                }
            }
        }
    }
    private void drawAllDealingsLimitLines(List<OrderAnswer> list){
        xAxis.removeAllLimitLines();
        rightYAxis.removeAllLimitLines();
        if(list != null && list.size() != 0){
            for(OrderAnswer orderAnswer : list){
                if(ConventDate.validationDateTimer(orderAnswer.getOptionsData().getExpirationTime())) {
                     if(isTypeOptionAmerican && ConventDate.getDifferenceDate(orderAnswer.getOpenTime()) >= 61){
                        drawDealingLimitLine(orderAnswer, isTypeOptionAmerican);
                     }else{
                        drawDealingLimitLine(orderAnswer, false);
                    }
                }
            }
            makeActiveSelectedDealing(null);
        }
    }
    private void makeActiveSelectedDealing(CustomBaseLimitLine line){
        List<CustomBaseLimitLine> list = getXLimitLines();
        if(list != null){
            if(line != null){
                if(line.getTypeLimitLine() == CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE){
                    line.enableDashedLine(10f, 10f, 0f);
                    line.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_PASS);
                    if (currentLineDealing != null) {
                        rightYAxis.removeLimitLine(currentLineDealing);
                    }
                }else{
                    for(CustomBaseLimitLine l: list){
                        if(l.getTypeLimitLine() == CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE){
                            l.enableDashedLine(10f, 10f, 0f);
                            l.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_PASS);
                            break;
                        }
                    }
                    drawCurrentDealingYLimitLine(line, (new Gson().fromJson(line.getLabel(), OrderAnswer.class)));
                }
            }else{
                drawCurrentDealingYLimitLine(list.get(0),(new Gson().fromJson(list.get(0).getLabel(), OrderAnswer.class)));
            }
        }else{
            if(currentLineDealing != null){
                rightYAxis.removeLimitLine(currentLineDealing);
            }
        }
    }
    public void redrawXYDealingLimitLines(){
        redrawXLimitLines();
        redrawYLimitLines();
        mChart.invalidate();
    }
    private void drawDealingLimitLine(OrderAnswer order, boolean isAmerican) {
        if (order != null) {
            if(ConventDate.genericTimeForChart(ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000) >= mChart.getHighestVisibleX()){
                CustomBaseLimitLine lineY = new CustomBaseLimitLine(Float.valueOf(String.valueOf(order.getOpenPrice())),
                        new Gson().toJson(order), null,
                        String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), isAmerican);
                updateColorYLimitLine(lineY, order);
                lineY.setLineColor(Color.TRANSPARENT);
                lineY.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_HORIZONTAL_DEALING);
                rightYAxis.addLimitLine(lineY);
            }else{
                CustomBaseLimitLine lineX = new CustomBaseLimitLine(ConventDate.genericTimeForChart(
                        ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000),
                        new Gson().toJson(order), null, null, String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), isAmerican);
                updateColorXLimitLine(lineX, order);
                lineX.enableDashedLine(10f, 10f, 0f);
                lineX.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_PASS);
                xAxis.addLimitLine(lineX);
            }
        }
    }
    public void redrawXLimitLines(){
        List<CustomBaseLimitLine> list = getXLimitLines();
        if(list != null && list.size() != 0){
            for(CustomBaseLimitLine line : list){
                OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                if(Long.parseLong(line.getmTimer()) <= 1.5) {
                    redrawPointsDealings(order);
                    xAxis.removeLimitLine(line);
                    makeActiveSelectedDealing(null);
                }else {
                    if(!ConventDate.validationDateTimer(order.getOptionsData().getExpirationTime())){
                        xAxis.removeLimitLine(line);
                    }else{
                        if(isTypeOptionAmerican && ConventDate.getDifferenceDate(order.getOpenTime()) >= 61){
                            line.setmIsAmerican(true);
                        }
                        updateColorXLimitLine(line, order);
                        if (line.getTypeLimitLine() == CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE){
                            drawCurrentDealingYLimitLine(line, order);
                        }
                    }
                }
            }
        }else{
            if(currentLineDealing != null) {
                rightYAxis.removeLimitLine(currentLineDealing);
            }
        }
    }
    public void redrawYLimitLines(){
        List<CustomBaseLimitLine> list = getYLimitLines();
        if(list != null && list.size() != 0){
            for(CustomBaseLimitLine line : list){
                OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                if(Long.parseLong(line.getmTimer()) <= 1.5) {
                    redrawPointsDealings(order);
                    rightYAxis.removeLimitLine(line);
                }else {
                    if(!ConventDate.validationDateTimer(order.getOptionsData().getExpirationTime())){
                        rightYAxis.removeLimitLine(line);
                    }else{
                        if(isTypeOptionAmerican && ConventDate.getDifferenceDate(order.getOpenTime()) >= 61){
                            line.setmIsAmerican(true);
                        }
                        updateColorYLimitLine(line, order);
                    }
                }
            }
        }
    }
    private void updateColorXLimitLine(CustomBaseLimitLine line, OrderAnswer order){
        if(order.getCmd() == 0 && order.getOpenPrice() <= mCurrentValueY ||
                order.getCmd() == 1 && order.getOpenPrice() >= mCurrentValueY){
            line.setmBitmapLabelX(bitmapIconGreenXLabel);
            line.setLineColor(colorGreen);
            line.setmBitmapLabelY(bitmapIconGreenYLabel);
        }else{
            line.setmBitmapLabelX(bitmapIconRedXLabel);
            line.setLineColor(colorRed);
            line.setmBitmapLabelY(bitmapIconRedYLabel);
        }
        line.setmTimer(String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())));
    }
    private void updateColorYLimitLine(CustomBaseLimitLine line, OrderAnswer order){
        if(order.getCmd() == 0 && order.getOpenPrice() <= mCurrentValueY ||
                order.getCmd() == 1 && order.getOpenPrice() >= mCurrentValueY){
            line.setmBitmapLabelY(bitmapIconGreenYLabel);
        }else{
            line.setmBitmapLabelY(bitmapIconRedYLabel);
        }
        line.setmTimer(String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())));
    }
    private void deleteDealingLimitLine(final int ticket){
        if(OrderAnswer.getInstance() != null && ticket != 0){
            if(listOpenDealings != null && listOpenDealings.size() != 0){
                for(OrderAnswer order: listOpenDealings){
                    if(order.getTicket() == ticket){
                        redrawPointsDealings(order);
                        listOpenDealings.remove(order);
                        CheckDealingService.setListOrderAnswer(listOpenDealings);
                        mViewInfoHelper.updateSettingsCloseDealing(order, getActivity());
                        drawAllDealingsLimitLines(listOpenDealings);
                        break;
                    }
                }
            }
        }
    }

    public void showSignalsPanel() {
        parseResponseSignals(ConventString.getActive(tvValueActive));
        if(!isDirection) {
            BaseActivity.getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        }
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, AndroidUtils.dp(isDirection ? 60 : -60));
        animation.setDuration(Const.INTERVAL_ANIM_PANEL);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llTopPanel.clearAnimation();
                flMain.removeView(llTopPanel);
                flMain.addView(llTopPanel, ConventImage.getFrameParams(isDirection));
                if (!isDirection) {
                    new Handler().postDelayed(() -> {
                        if (!isDirection) {
                            BaseActivity.getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
                            showSignalsPanel();
                        }
                    }, 3000);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        llTopPanel.startAnimation(animation);
        isDirection = !isDirection;
    }
    private void showViewCloseDealing(OrderAnswer order){
        if(CustomSharedPreferences.getAgreeCloseDealing(getContext())){
            dialogAgreeDeleteDealing = CustomDialog.showDialogCloseDealing(getActivity(), v12 -> {
                requestDeleteDealing(order);
                dialogAgreeDeleteDealing.cancel();
            }, v1 -> {
                requestDeleteDealing(order);
                CustomSharedPreferences.setAgreeCloseDealing(getContext(), false);
                dialogAgreeDeleteDealing.cancel();
            });
        }else{
            requestDeleteDealing(order);
        }
    }

    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(InfoUserService.RESPONSE_INFO) != null && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY) != null &&
                intent.getStringExtra(InfoUserService.RESPONSE_INFO).equals(Const.RESPONSE_CODE_SUCCESS) &&
                intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY).equals(Const.RESPONSE_CODE_SUCCESS) && InfoAnswer.getInstance() != null &&
                InfoAnswer.getInstance().getInstruments() != null && InfoAnswer.getInstance().getInstruments().size() > 0) {
                listActives.clear();
                for (Instrument instrument : InfoAnswer.getInstance().getInstruments()) {
                    listActives.add(instrument.getSymbol());
                }
                if (listActives != null && listActives.size() > 0 && listActives.contains(Const.SYMBOL) && !ConventString.getActive(tvValueActive).equals(Const.SYMBOL)) {
                    sSymbolCurrent = listActives.get(listActives.indexOf(Const.SYMBOL));
                    changeActive();
                }
                ConventString.updateBalance(tvBalance);
            }
            requestSignals();
         }
    }
    public class GetResponseSymbolHistoryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(SymbolHistoryService.RESPONSE) != null && intent.getStringExtra(SymbolHistoryService.RESPONSE).equals(Const.RESPONSE_CODE_SUCCESS) &&
                    SymbolHistoryAnswer.getInstance() != null) {
                parseResponseSymbolHistory();
            } else {
                GrandCapitalApplication.closeAndOpenSocket(sSymbolCurrent);
            }
            llProgressBar.setVisibility(View.GONE);
            setEnabledBtnChooseActive(true);
        }
    }
    public class GetResponseOpenDealingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(MakeDealingService.RESPONSE);
            if (response != null && response.equals("true")) {
                requestGetAllOrders();
                if (currentDealing == null) {
                    currentDealing = new OrderAnswer();
                }
                currentDealing.setOpenPrice(mCurrentValueY);
                currentDealing.setSymbol(intent.getStringExtra(MakeDealingService.SYMBOL));
                currentDealing.setCmd(Integer.valueOf(intent.getStringExtra(MakeDealingService.CMD)));
                OptionsData optionsData = new OptionsData();
                optionsData.setExpirationTime(ConventDate.getTimeCloseDealing(Double.valueOf(intent.getStringExtra(MakeDealingService.EXPIRATION)).intValue()));
                currentDealing.setOptionsData(optionsData);
                currentDealing.setOpenTime(ConventDate.getCurrentDate());
                currentDealing.setVolume(Double.valueOf(intent.getStringExtra(MakeDealingService.VOLUME)).intValue());
                typePoint = POINT_OPEN_DEALING;
                mViewInfoHelper.showViewOpenDealing(intent.getStringExtra(MakeDealingService.SYMBOL),
                        intent.getStringExtra(MakeDealingService.VOLUME),
                        intent.getStringExtra(MakeDealingService.EXPIRATION));
                etValueAmount.setText(getResources().getString(R.string.zero_dollars));
                etValueTime.setText(getResources().getString(R.string.zero_min));
            } else {
                CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.request_error_request));
            }
            llProgressBar.setVisibility(View.GONE);
            setEnabledBtnTerminal(true);
        }
    }
    public class GetResponseCheckClosedDealingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            listCurrentClosingDealings.add(new Gson().fromJson(intent.getStringExtra(CheckDealingService.RESPONSE), OrderAnswer.class));
            new Handler().postDelayed(() -> requestGetAllOrders(), 4500);
        }
    }
    public class GetResponseOrdersBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (OrderAnswer.getInstance() != null) {
                switch (intent.getIntExtra(OrdersService.FUNCTION, 0)){
                    case OrdersService.GET_ALL_ORDERS:
                        List<OrderAnswer> listAllOpenDealings = OrderAnswer.filterOrders(OrderAnswer.getInstance(), DealingFragment.OPEN_TAB_POSITION);
                        listOpenDealings = OrderAnswer.filterOrdersCurrentActive(listAllOpenDealings, DealingFragment.OPEN_TAB_POSITION, ConventString.getActive(tvValueActive));
                        List<OrderAnswer> listAllClosedDealings = OrderAnswer.filterOrders(OrderAnswer.getInstance(), DealingFragment.CLOSE_TAB_POSITION);
                        CheckDealingService.setListOrderAnswer(listAllOpenDealings);

                        parseClosingDealings(listAllClosedDealings);
                        if (listOpenDealings != null && listOpenDealings.size() != 0 && getXLimitLines() == null) {
                            drawAllDealingsLimitLines(listOpenDealings);
                        }
                        break;
                    case OrdersService.GET_TICKET_ORDER:
                        listOpenDealings = OrderAnswer.filterOrdersCurrentActive(OrderAnswer.getInstance(), DealingFragment.OPEN_TAB_POSITION, ConventString.getActive(tvValueActive));
                        requestDeleteDealing(new Gson().fromJson(intent.getStringExtra(OrdersService.RESPONSE), OrderAnswer.class));
                        break;
                    default:
                        break;
                }
            }
        }
    }
    public class GetResponseSignalsBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(SignalService.RESPONSE);
            if (response == null || !response.equals(Const.RESPONSE_CODE_SUCCESS)) {
                CustomDialog.showDialogInfo(getActivity(),
                        getResources().getString(R.string.error),
                        getResources().getString(R.string.request_error_text));
            }
        }
    }
    public class GetResponseEarlyClosureBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(EarlyClosureService.RESPONSE);
            if (response != null && response.equals(Const.RESPONSE_CODE_SUCCESS) && EarlyClosureAnswer.getInstance() != null &&
                    InfoAnswer.getInstance() != null && InfoAnswer.getInstance().getGroup() != null) {
                isTypeOptionAmerican = intent.getBooleanExtra(EarlyClosureService.IS_AMERICAN, false);
                tvValueRewardTerminal.setText(ConventString.getStringEarlyClosure(etValueAmount, intent.getIntExtra(EarlyClosureService.PERCENT, 0)));
            } else {
                CustomDialog.showDialogInfo(getActivity(),
                        getResources().getString(R.string.error),
                        getResources().getString(R.string.request_error_text));
            }
        }
    }
    public class GetResponseDeleteDealingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(DeleteDealingService.RESPONSE);
            if (response == null || !response.equals(Const.CODE_SUCCESS_DELETE_DEALING)) {
                CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.request_error_title), getResources().getString(R.string.request_error_request));
            }else{
                deleteDealingLimitLine(intent.getIntExtra(DeleteDealingService.TICKET, 0));
            }
        }
    }
}
