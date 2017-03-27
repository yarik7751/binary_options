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
import android.net.Uri;
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
import com.elatesoftware.grandcapital.services.EarlyClosureService;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.services.MakeDealingService;
import com.elatesoftware.grandcapital.services.OrdersService;
import com.elatesoftware.grandcapital.services.SignalService;
import com.elatesoftware.grandcapital.services.SymbolHistoryService;
import com.elatesoftware.grandcapital.utils.AndroidUtils;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.ConventImage;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.views.CustomAnimationDrawable;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.views.items.chart.limit_lines.CustomBaseLimitLine;
import com.github.mikephil.charting.charts.LineChart;
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
    public final static String TAG_OPEN_DEALING = "openDealingView";
    public final static String TAG_CLOSE_DEALING = "closeDealingView";

    private final static int INTERVAL_SHOW_LABEL = 5000;
    private final int INTERVAL_ITEM = 100;

    private final static String SYMBOL = "EURUSD";
    private static String sSymbolCurrent = "";

    public static double mCurrentValueY = 0;
    private OrderAnswer currentDealing = new OrderAnswer();

    private static int typePoint = 0;
    private final static int POINT_SIMPLY = 0;
    private final static int POINT_OPEN_DEALING = 1;
    private final static int POINT_CLOSE_DEALING = 2;

    public static boolean isAddInChart = false;
    public static boolean isOpen = false;
    public boolean isDirection = true;

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
    private Bitmap bitmapIconCurrentLimitLabel;
    private Bitmap bitmapIconGreenXLabel;
    private Bitmap bitmapIconRedXLabel;
    private Bitmap bitmapIconRedYLabel;
    private Bitmap bitmapIconGreenYLabel;
    private Bitmap bitmapIconCurrentDealingGreenYLabel;
    private Bitmap bitmapIconCurrentDealingRedYLabel;
    private int colorRed;
    private int colorGreen;

    private ImageView imgPointCurrent;
    private CustomAnimationDrawable rocketAnimation, rocketAnimationBack;
    private Dialog dialogOpenAccount;
    private View closeDealingView;
    private View openDealingView;
    private YAxis rightYAxis;
    private XAxis xAxis;
    private CustomBaseLimitLine currentLineSocket;
    private CustomBaseLimitLine currentLineDealing;

    private Thread threadSymbolHistory;

    private static List<String> listActives = new ArrayList<>();
    public static List<SocketAnswer> listSocketPointsBackGround = new ArrayList<>();
    public static List<OrderAnswer> listCurrentClosingDealings = new ArrayList<>();
    private List<CustomBaseLimitLine> mListDealingXLine = new ArrayList<>();

    private GetResponseSymbolHistoryBroadcastReceiver mSymbolHistoryBroadcastReceiver;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;
    private GetResponseOpenDealingBroadcastReceiver mMakeDealingBroadcastReceiver;
    private GetResponseSignalsBroadcastReceiver mSignalsBroadcastReceiver;
    private GetResponseCloseDealingBroadcastReceiver mCloseDealingBroadcastReceiver;
    private GetResponseOrdersBroadcastReceiver mOrdersBroadcastReceiver;
    private GetResponseEarlyClosureBroadcastReceiver mEarlyClosureBroadcastReceiver;

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

        drawableMarkerDealing = getResources().getDrawable(R.drawable.marker_close_dealing);
        bitmapIconCurrentLimitLabel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.whitevert);
        bitmapIconGreenXLabel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.green_vert);
        bitmapIconRedXLabel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.red_vert);
        bitmapIconCurrentDealingGreenYLabel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.green_hor);
        bitmapIconCurrentDealingRedYLabel = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.red_hor);
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
        openDealingView = LayoutInflater.from(getContext()).inflate(R.layout.label_open_dealing, null);
        closeDealingView = LayoutInflater.from(getContext()).inflate(R.layout.label_close_dealing, null);

        openDealingView.setTag(TAG_OPEN_DEALING);
        closeDealingView.setTag(TAG_CLOSE_DEALING);

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
        try {
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
            BaseActivity.getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        etValueAmount.clearFocus();
        etValueTime.clearFocus();

        KeyboardVisibilityEvent.registerEventListener(getActivity(), isOpen1 -> {
            if (etValueAmount.isFocused()) {
                ConventString.setMaskAmount(etValueAmount, isOpen1);
            }
            if (etValueTime.isFocused()) {
                ConventString.setMaskTime(etValueTime, isOpen1);
            }
            /*if(!isOpen1) {
                requestEarlyClosure();
            }*/
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
            if (!ConventString.getActive(tvValueActive).equals("") && listActives.size() > 0) {
                int index = listActives.indexOf(ConventString.getActive(tvValueActive));
                if (index == 0) {
                    sSymbolCurrent = listActives.get(listActives.size() - 1);
                } else {
                    sSymbolCurrent = listActives.get(index - 1);
                }
                changeActive();
                parseResponseSignals(ConventString.getActive(tvValueActive));
            } else {
                sSymbolCurrent = SYMBOL;
            }
        });
        tvRightActive.setOnClickListener(v -> {
            if (!ConventString.getActive(tvValueActive).equals("") && listActives.size() > 0) {
                int index = listActives.indexOf(ConventString.getActive(tvValueActive));
                if (index == (listActives.size() - 1)) {
                    sSymbolCurrent = listActives.get(0);
                } else {
                    sSymbolCurrent = listActives.get(index + 1);
                }
                changeActive();
                parseResponseSignals(ConventString.getActive(tvValueActive));
            } else {
                sSymbolCurrent = SYMBOL;
            }
        });
        tvDeposit.setOnClickListener(v -> {
            BaseActivity.changeMainFragment(new DepositFragment());
        });
        llLowerTerminal.setOnClickListener(v -> {
            requestMakeDealing("1");
        });
        llHigherTerminal.setOnClickListener(v -> {
            requestMakeDealing("0");
        });
        initializationChart();
        initializationCurrentPoint();
        setSizeHeight();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onResume Terminal");
        llProgressBar.setVisibility(View.VISIBLE);
        isOpen = true;
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(false);
        registerBroadcasts();
        clearChart();
        ConventString.updateBalance(tvBalance);
        if (sSymbolCurrent != null && !sSymbolCurrent.equals("")) {
            tvValueActive.setText(sSymbolCurrent);
            parseResponseSymbolHistory();
        } else {
            changeActive();
        }
        requestOrders();
        tvLeftActive.setEnabled(true);
        tvRightActive.setEnabled(true);
    }
    @Override
    public void onPause() {
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onPause() Terminal");
        isAddInChart = false;
        clearChart();
        if (threadSymbolHistory != null) {
            threadSymbolHistory.interrupt();
        }
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

    private void setEnabledBtnTerminal(boolean enabled) {
        llHigherTerminal.setEnabled(enabled);
        llLowerTerminal.setEnabled(enabled);
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

        mCloseDealingBroadcastReceiver = new GetResponseCloseDealingBroadcastReceiver();
        IntentFilter intentFilterCloseDealing = new IntentFilter(CheckDealingService.ACTION_SERVICE_CHECK_DEALINGS);
        intentFilterCloseDealing.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mCloseDealingBroadcastReceiver, intentFilterCloseDealing);

        mOrdersBroadcastReceiver = new GetResponseOrdersBroadcastReceiver();
        IntentFilter intentFilterOrders = new IntentFilter(OrdersService.ACTION_SERVICE_ORDERS);
        intentFilterOrders.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mOrdersBroadcastReceiver, intentFilterOrders);

        mEarlyClosureBroadcastReceiver = new GetResponseEarlyClosureBroadcastReceiver();
        IntentFilter intentFilterOrdersEarlyClosure = new IntentFilter(EarlyClosureService.ACTION_SERVICE_EARLY_CLOSURE);
        intentFilterOrdersEarlyClosure.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mEarlyClosureBroadcastReceiver, intentFilterOrdersEarlyClosure);
    }
    private void unregisterBroadcasts() {
        getActivity().unregisterReceiver(mSymbolHistoryBroadcastReceiver);
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
        getActivity().unregisterReceiver(mMakeDealingBroadcastReceiver);
        getActivity().unregisterReceiver(mSignalsBroadcastReceiver);
        getActivity().unregisterReceiver(mCloseDealingBroadcastReceiver);
        getActivity().unregisterReceiver(mOrdersBroadcastReceiver);
        getActivity().unregisterReceiver(mEarlyClosureBroadcastReceiver);
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
        mChart.getDescription().setEnabled(false);// enable description text
        mChart.setTouchEnabled(true);      // enable touch gestures жесты
        mChart.setDragEnabled(true);    // enable scaling and dragging
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);      // if disabled, scaling can be done on x- and y-axis separately
        mChart.setBackgroundColor(Color.TRANSPARENT); // set an alternative background color
        mChart.getLegend().setEnabled(false);   //Hide the legend
        mChart.setDrawMarkers(true);
        mChart.getViewPortHandler().setMaximumScaleX(8.6f);

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
        xAxis.setSpaceMax(600000f); // space free on x

        //xAxis.setXOffset(300f);
        //xAxis.setSpaceMax(600f);
        //xAxis.setGranularityEnabled(true);
        //xAxis.setGranularity(36000f);
        //xAxis.setGranularity(0.00000000001f);
        //xAxis.setGranularityEnabled(true);

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

        mChart.setOnTouchListener((v, event) -> {
            if(mListDealingXLine != null && mListDealingXLine.size() != 0){
                float tappedX = event.getX();
                float tappedY = event.getY();
                MPPointD point = mChart.getTransformer(YAxis.AxisDependency.RIGHT).getValuesByTouchPoint(tappedX, tappedY);
                for(CustomBaseLimitLine line: mListDealingXLine){
                    if((line.getLimit() - point.x <= 7000 && line.getLimit() - point.x >= 0) ||
                            (point.x - line.getLimit() <= 7000 && point.x - line.getLimit() >= 0)){
                        makeActiveSelectedDealing(line);
                        break;
                    }
                }
            }
            return false;
        });

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
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });
    }
    private void initializationCurrentPoint() {
        initRocketAnimation();
        imgPointCurrent = new ImageView(getContext());
        imgPointCurrent.setVisibility(View.GONE);
        imgPointCurrent.setId(R.id.img);
        imgPointCurrent.setImageDrawable(rocketAnimation);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(AndroidUtils.dp(40), AndroidUtils.dp(40));
        rlChart.addView(imgPointCurrent, params);
        rocketAnimation.start();
    }
    private void initRocketAnimation() {
        if(isAdded()) {
            rocketAnimation = new CustomAnimationDrawable();
            rocketAnimation.setOneShot(true);
            //Log.d(TAG, "initRocketAnimation()");
            for (int i = 10; i >= 3; i--) {
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.front_elipsa);
                rocketAnimation.addFrame(new BitmapDrawable(ConventImage.getPaddingImage(image, i)), INTERVAL_ITEM);
            }

            rocketAnimation.setAnimationEndListner(() -> {
                initRocketAnimationBack();
                imgPointCurrent.setBackgroundDrawable(rocketAnimationBack);
                rocketAnimationBack.start();
            });
        }
    }
    private void initRocketAnimationBack() {
        if(isAdded()) {
            rocketAnimationBack = new CustomAnimationDrawable();
            rocketAnimationBack.setOneShot(true);
            for (int i = 3; i <= 10; i++) {
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.front_elipsa);
                rocketAnimationBack.addFrame(new BitmapDrawable(ConventImage.getPaddingImage(image, i)), INTERVAL_ITEM);
            }
            rocketAnimationBack.setAnimationEndListner(() -> {
                initRocketAnimation();
                imgPointCurrent.setBackgroundDrawable(rocketAnimation);
                rocketAnimation.start();
            });
        }
    }

    private void setSizeHeight() {
        int height = AndroidUtils.getWindowsSizeParams(getContext())[1] - AndroidUtils.getStatusBarHeight(getContext()) - AndroidUtils.dp(60);
        rlChart.getLayoutParams().height = (int) (height * 0.6);
        llDeposit.getLayoutParams().height = (int) (height * 0.28);
        llButtons.getLayoutParams().height = (int) (height * 0.11);
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
        set.setHighlightEnabled(true); // selected point
        set.setDrawHighlightIndicators(false);//
        return set;
    }
    private void clearChart() {
        listSocketPointsBackGround.clear();
        mListDealingXLine.clear();
        typePoint = POINT_SIMPLY;
        mListDealingXLine.clear();
        mChart.highlightValues(null);
        mChart.clearValues();
        rightYAxis.removeAllLimitLines();
        xAxis.removeAllLimitLines();
    }
    private void changeActive() {
        if (sSymbolCurrent == null || sSymbolCurrent.equals("")) {
            sSymbolCurrent = SYMBOL;
        }
        llProgressBar.setVisibility(View.VISIBLE);
        tvValueActive.setText(sSymbolCurrent);
        clearChart();
        SymbolHistoryAnswer.nullInstance();
        SocketAnswer.nullInstance();
        GrandCapitalApplication.closeSocket();
        requestSymbolHistory(ConventString.getActive(tvValueActive));
        requestOrders();
        tvLeftActive.setEnabled(false);
        tvRightActive.setEnabled(false);
    }
    private void updateViewCloseDealing(OrderAnswer order){
        CustomSharedPreferences.setAmtCloseDealings(getContext(), CustomSharedPreferences.getAmtCloseDealings(getContext()) + 1);
        showViewCloseDealing(order);
        ((BaseActivity) getActivity()).setDealings();
        BaseActivity.getToolbar().setDealingSelectIcon();
        showViewOpenRealAccount();
    }

    private void requestEarlyClosure() {
        Intent intent = new Intent(getActivity(), EarlyClosureService.class);
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
        if (ConventString.getAmountValue(etValueAmount) != 0 && ConventString.getTimeValue(etValueTime) != 0 && !ConventString.getActive(tvValueActive).equals("")) {
            if(ConventString.getTimeValue(etValueTime) > 2880) {
                CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.error_max_time));
                return;
            }
            Intent intentService = new Intent(getActivity(), MakeDealingService.class);
            intentService.putExtra(MakeDealingService.CMD, lowerOrHeight);
            intentService.putExtra(MakeDealingService.SYMBOL, ConventString.getActive(tvValueActive));
            intentService.putExtra(MakeDealingService.VOLUME, String.valueOf(ConventString.getAmountValue(etValueAmount)));
            intentService.putExtra(MakeDealingService.EXPIRATION, String.valueOf(ConventString.getTimeValue(etValueTime)));
            getActivity().startService(intentService);
            llProgressBar.setVisibility(View.VISIBLE);
            setEnabledBtnTerminal(false);
        } else {
            CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.no_correct_values));
        }
    }
    private void requestOrders() {
        if(isAdded()) {
            Intent intentService = new Intent(getActivity(), OrdersService.class);
            getActivity().startService(intentService);
        }
    }

    private void parseResponseSymbolHistory() {
        if (SymbolHistoryAnswer.getInstance() != null && SymbolHistoryAnswer.getInstance().size() != 0) {
            List<SocketAnswer> list;
            if (listSocketPointsBackGround != null && listSocketPointsBackGround.size() != 0) {
                list = listSocketPointsBackGround.subList(0, listSocketPointsBackGround.size());
                Log.d(GrandCapitalApplication.TAG_SOCKET, "add from background in list socketanswer size = " + (list.size() - 1));
                for (SocketAnswer item : list) {
                    addSocketAnswerInSymbol(item);
                }
                listSocketPointsBackGround.clear();
            }
            drawDataSymbolHistory(SymbolHistoryAnswer.getInstance(), ConventString.getActive(tvValueActive));
        } else {
            listSocketPointsBackGround.clear();
            isAddInChart = true;
        }
        llProgressBar.setVisibility(View.GONE);
    }
    private void parseResponseSignals(String symbol) {
        if (SignalAnswer.getInstance() != null) {
            List<SignalAnswer> list = new ArrayList<>();
            for (SignalAnswer answer : SignalAnswer.getInstance()) {
                if (answer.getInstrument().replace("/", "").equals(symbol)) {
                    list.add(answer);
                }
            }
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
    private void parseClosingDealings(List<OrderAnswer> listAllClosedDealings, List<OrderAnswer> listOpenDealingsCurrentActive){
        if(listCurrentClosingDealings != null && listCurrentClosingDealings.size() != 0){
            for(OrderAnswer order : listCurrentClosingDealings){
                for(OrderAnswer orderClosed : listAllClosedDealings){
                    if ((int) order.getTicket() == (int) orderClosed.getTicket()) {
                        updateViewCloseDealing(orderClosed);
                        if(orderClosed.getSymbol().equals(ConventString.getActive(tvValueActive))){
                            redrawPointsDealings(order);
                            drawAllDealingsXLimitLines(listOpenDealingsCurrentActive);
                        }
                        break;
                    }
                }
            }
            listCurrentClosingDealings.clear();
        }
    }

    private void addSocketAnswerInSymbol(SocketAnswer item) {
        if (SymbolHistoryAnswer.getInstance() != null) {
            SymbolHistoryAnswer.getInstance().add(new SymbolHistoryAnswer(item.getHigh(), item.getBid(), item.getAsk(), item.getLow(), item.getTime()));
        }
    }
    public synchronized void addEntry(final SocketAnswer answer) {
        if (answer != null && answer.getTime() != null && answer.getAsk() != null) {
            LineData data = mChart.getData();

            if (data != null) {
                ILineDataSet set = data.getDataSetByIndex(0);
                if (set == null) {
                    set = createSetDataChart();
                    data.addDataSet(set);
                }
                addSocketAnswerInSymbol(answer);
                Entry entry;
                switch (typePoint) {
                    case POINT_CLOSE_DEALING:
                        entry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getAsk())), drawableMarkerDealing, null);
                        break;
                    case POINT_OPEN_DEALING:
                        OrderAnswer order = currentDealing;
                        currentDealing = null;
                        entry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getAsk())), drawableMarkerDealing, answer.getTime());
                        //entry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(order.getOpenPrice())), drawableMarkerDealing, answer.getTime());
                        order.setOpenPrice(Double.valueOf(String.valueOf(entry.getY())));
                        drawXLimitLine(order);
                        break;
                    case POINT_SIMPLY:
                        entry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getAsk())), null, null);
                        break;
                    default:
                        entry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getAsk())), null, null);
                        break;
                }
                mCurrentValueY = answer.getAsk();
                redrawXLimitLines();
                data.addEntry(entry, 0);
                data.notifyDataChanged();
                mChart.notifyDataSetChanged();
                mChart.invalidate();
                drawSocketCurrentYLimitLine(entry);
                typePoint = POINT_SIMPLY;
            }
        }
    }
    private void addEntry(SymbolHistoryAnswer answer) {
        if (answer != null && answer.getTime() != null && answer.getOpen() != null) {
            LineData data = mChart.getData();
            if (data != null) {
                ILineDataSet set = data.getDataSetByIndex(0);
                if (set == null) {
                    set = createSetDataChart();
                    data.addDataSet(set);
                }
                Entry entry = new Entry(ConventDate.genericTimeForChart(answer.getTime()),
                        Float.valueOf(String.valueOf(answer.getOpen())), null, null);
                data.addEntry(entry, 0);
                data.notifyDataChanged();
                mChart.notifyDataSetChanged();
            }
        }
    }

    private void makeActiveSelectedDealing(CustomBaseLimitLine line){
        if(mListDealingXLine != null && mListDealingXLine.size() != 0){
            if(line != null){
                if(line.getTypeLimitLine() == CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE){
                    line.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_PASS);
                    line.enableDashedLine(10f, 10f, 0f);
                    if (currentLineDealing != null) {
                         rightYAxis.removeLimitLine(currentLineDealing);
                    }
                }else{
                    for(CustomBaseLimitLine l: mListDealingXLine){
                        l.enableDashedLine(10f, 10f, 0f);
                        l.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_PASS);
                    }
                    line.enableDashedLine(0f, 0f, 0f);
                    line.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE);
                    drawCurrentDealingYLimitLine(line, (new Gson().fromJson(line.getLabel(), OrderAnswer.class)));
                }
            }else{
                mListDealingXLine.get(0).enableDashedLine(0f, 0f, 0f);
                mListDealingXLine.get(0).setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE);
                drawCurrentDealingYLimitLine(mListDealingXLine.get(0),(new Gson().fromJson(mListDealingXLine.get(0).getLabel(), OrderAnswer.class)));
            }
        }else{
            if(currentLineDealing != null){
                rightYAxis.removeLimitLine(currentLineDealing);
            }
            xAxis.removeAllLimitLines();
        }
    }
    private void redrawPointsDealings(OrderAnswer order){
        if (mChart.getData() != null) {
            ILineDataSet set = mChart.getData().getDataSetByIndex(0);
            for (int i = set.getEntryCount() - 1; i >= 0; i--) {
                Entry entry = set.getEntryForIndex(i);
                if (entry.getData() != null && entry.getData() instanceof Long) {
                    Long timeDataOpenPoint = Long.valueOf(String.valueOf(entry.getData()));
                    if (ConventDate.equalsTimeDealingPoint(timeDataOpenPoint, order.getOpenTime())) {
                        Log.d(GrandCapitalApplication.TAG_SOCKET, "closeDealing");
                        entry.setIcon(null);
                        entry.setData(null);
                        mChart.getData().notifyDataChanged();
                        mChart.notifyDataSetChanged();
                        mChart.invalidate();
                        typePoint = POINT_CLOSE_DEALING;
                        break;
                    }
                }
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
                mChart.zoom(8.6f, 0f, entry.getX(), 0f, YAxis.AxisDependency.RIGHT);
                getActivity().runOnUiThread(() -> {
                    drawSocketCurrentYLimitLine(entry);
                });
            }
            GrandCapitalApplication.closeAndOpenSocket(symbol);
            isAddInChart = true;
        });
        threadSymbolHistory.start();
    }
    private void drawSocketCurrentYLimitLine(Entry entry) {
        if (currentLineSocket != null) {
            rightYAxis.removeLimitLine(currentLineSocket);
        }
        currentLineSocket = new CustomBaseLimitLine(entry.getY(), String.valueOf(entry.getY()), bitmapIconCurrentLimitLabel);
        currentLineSocket.setLineColor(Color.WHITE);
        currentLineSocket.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_HORIZONTAL_CURRENT_SOCKET);
        rightYAxis.addLimitLine(currentLineSocket);
        drawCurrentPoint(entry);
    }
    private void redrawColorTimerXLimitLine(CustomBaseLimitLine line, OrderAnswer order){
        if(Long.parseLong(line.getmTimer()) <= 1){
            if(line.getTypeLimitLine() == CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE){
               rightYAxis.removeLimitLine(currentLineDealing);
                if(mListDealingXLine != null && mListDealingXLine.size() != 0){
                    mListDealingXLine.get(0).enableDashedLine(0f, 0f, 0f);
                    mListDealingXLine.get(0).setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE);
                    drawCurrentDealingYLimitLine(mListDealingXLine.get(0),(new Gson().fromJson(mListDealingXLine.get(0).getLabel(), OrderAnswer.class)));
                }
            }
            xAxis.removeLimitLine(line);
        }else{
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
    }
    private void drawCurrentDealingYLimitLine(CustomBaseLimitLine lineX, OrderAnswer order) {
        if (currentLineDealing != null) {
            rightYAxis.removeLimitLine(currentLineDealing);
        }
        if(mListDealingXLine != null && mListDealingXLine.size() != 0 && lineX != null){
            currentLineDealing = new CustomBaseLimitLine(Float.valueOf(String.valueOf(order.getOpenPrice())), String.valueOf(order.getOpenPrice()), null);
            if(lineX.getLineColor() == colorGreen){
                currentLineDealing.setmBitmapLabelX(bitmapIconCurrentDealingGreenYLabel);
                currentLineDealing.setLineColor(colorGreen);
            }else if (lineX.getLineColor() == colorRed){
                currentLineDealing.setmBitmapLabelX(bitmapIconCurrentDealingRedYLabel);
                currentLineDealing.setLineColor(colorRed);
            }
            currentLineDealing.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_HORIZONTAL_CURRENT_DEALING);
            rightYAxis.addLimitLine(currentLineDealing);
        }
    }
    private void drawCurrentPoint(Entry entry) {
        if (imgPointCurrent != null) {
            imgPointCurrent.setVisibility(View.VISIBLE);
            MPPointF point = mChart.getPosition(entry, YAxis.AxisDependency.RIGHT);
            imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
            imgPointCurrent.setY(point.getY() - imgPointCurrent.getHeight() / 2);
        }
    }
    private void drawAllDealingsXLimitLines(List<OrderAnswer> list){
        xAxis.removeAllLimitLines();
        rightYAxis.removeAllLimitLines();
        if(list != null && list.size() != 0){
            if(mListDealingXLine == null){
                mListDealingXLine = new ArrayList<>();
            }
            mListDealingXLine.clear();
            for(OrderAnswer orderAnswer : list){
                drawXLimitLine(orderAnswer);
            }
            makeActiveSelectedDealing(null);
        }
    }
    private void redrawXLimitLines(){
        if(mListDealingXLine != null && mListDealingXLine.size() != 0){
            for(CustomBaseLimitLine line : mListDealingXLine){
                OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                redrawColorTimerXLimitLine(line, order);
                if (line.getTypeLimitLine() == CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_ACTIVE){
                    drawCurrentDealingYLimitLine(line, order);
                }
            }
        }else{
            if(currentLineDealing != null){
                rightYAxis.removeLimitLine(currentLineDealing);
            }
            xAxis.removeAllLimitLines();
        }
    }
    private void drawXLimitLine(OrderAnswer order) {
        if (order != null) {
            CustomBaseLimitLine line = new CustomBaseLimitLine(ConventDate.genericTimeForChart(
                    ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000),
                    new Gson().toJson(order), null, null, String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())));
            redrawColorTimerXLimitLine(line, order);
            line.enableDashedLine(10f, 10f, 0f);
            line.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_VERTICAL_DEALING_PASS);
            xAxis.addLimitLine(line);
            mListDealingXLine.add(line);
        }
    }

    private void showViewOpenRealAccount(){
        if (CustomSharedPreferences.getIntervalAdvertising(getContext()) >= 0) {
            CustomSharedPreferences.setIntervalAdvertising(getContext(), CustomSharedPreferences.getIntervalAdvertising(getContext()) + 1);
            Log.d(TAG, "IntervalAdvertising1: " + CustomSharedPreferences.getIntervalAdvertising(getContext()));
            if (CustomSharedPreferences.getIntervalAdvertising(getContext()) >= 3) {
                CustomSharedPreferences.setIntervalAdvertising(getContext(), 0);
                dialogOpenAccount = CustomDialog.showDialogOpenAccount(getActivity(), v -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://grand.capital/4"));
                    startActivity(browserIntent);
                    dialogOpenAccount.cancel();
                });
            }
        }
    }
    private void showViewOpenDealing(String active, String amount, String time) {
        ((TextView) openDealingView.findViewById(R.id.tvActive)).setText(active);
        ((TextView) openDealingView.findViewById(R.id.tvAmount)).setText(amount);
        ((TextView) openDealingView.findViewById(R.id.tvTime)).setText(time);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = AndroidUtils.dp(16);
        params.leftMargin = AndroidUtils.dp(16);

        View vCloseDealings = rlChart.findViewWithTag(TAG_CLOSE_DEALING);
        if(vCloseDealings != null) {
            rlChart.removeView(vCloseDealings);
        }
        if (rlChart.findViewWithTag(TAG_OPEN_DEALING) != null) {
            rlChart.updateViewLayout(openDealingView, params);
        } else {
            rlChart.addView(openDealingView, params);
        }
        new Handler().postDelayed(() -> rlChart.removeView(openDealingView), INTERVAL_SHOW_LABEL);
    }
    private void showViewCloseDealing(OrderAnswer answer) {
        if (answer != null) {
            ((TextView) closeDealingView.findViewById(R.id.tvActiveValue)).setText(String.valueOf(answer.getSymbol()));
            ((TextView) closeDealingView.findViewById(R.id.tvPriceValue)).setText(ConventString.getRoundNumber(answer.getClosePrice()));
            ((TextView) closeDealingView.findViewById(R.id.tvProfitValue)).setText(String.valueOf(answer.getProfitStr()));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.topMargin = AndroidUtils.dp(16);
            params.leftMargin = AndroidUtils.dp(16);

            View vOpenDealings = rlChart.findViewWithTag(TAG_OPEN_DEALING);
            if(vOpenDealings != null) {
                rlChart.removeView(vOpenDealings);
            }
            if (rlChart.findViewWithTag(TAG_CLOSE_DEALING) != null) {
                rlChart.updateViewLayout(closeDealingView, params);
            } else {
                rlChart.addView(closeDealingView, params);
            }
            new Handler().postDelayed(() -> rlChart.removeView(closeDealingView), INTERVAL_SHOW_LABEL);
        }
    }
    public void  showSignalsPanel() {
        parseResponseSignals(ConventString.getActive(tvValueActive));
        if(!isDirection) {
            BaseActivity.getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        }
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, AndroidUtils.dp(isDirection ? 60 : -60));
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llTopPanel.clearAnimation();
                flMain.removeView(llTopPanel);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        AndroidUtils.dp(60)
                );
                params.topMargin = AndroidUtils.dp(isDirection ? -60 : 0);
                flMain.addView(llTopPanel, params);

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

    public class GetResponseInfoBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(InfoUserService.RESPONSE_INFO) != null && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY) != null) {
                if (intent.getStringExtra(InfoUserService.RESPONSE_INFO).equals("200") && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY).equals("200")) {
                    if (InfoAnswer.getInstance() != null && InfoAnswer.getInstance().getInstruments() != null && InfoAnswer.getInstance().getInstruments().size() > 0) {
                        listActives.clear();
                        for (Instrument instrument : InfoAnswer.getInstance().getInstruments()) {
                            listActives.add(instrument.getSymbol());
                        }
                        if (listActives != null && listActives.size() > 0 && listActives.contains(SYMBOL) && !ConventString.getActive(tvValueActive).equals(SYMBOL)) {
                            sSymbolCurrent = listActives.get(listActives.indexOf(SYMBOL));
                            changeActive();
                        }
                    }
                    ConventString.updateBalance(tvBalance);
                    requestSignals();
                }
            }
        }
    }
    public class GetResponseSymbolHistoryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(SymbolHistoryService.RESPONSE);
            if (response != null && response.equals("200") && SymbolHistoryAnswer.getInstance() != null) {
                parseResponseSymbolHistory();
            } else {
                GrandCapitalApplication.closeAndOpenSocket(sSymbolCurrent);
            }
            llProgressBar.setVisibility(View.GONE);
            tvLeftActive.setEnabled(true);
            tvRightActive.setEnabled(true);
        }
    }
    public class GetResponseOpenDealingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(MakeDealingService.RESPONSE);
            if (response != null && response.equals("true")) {
                llProgressBar.setVisibility(View.GONE);
                setEnabledBtnTerminal(true);
                Log.d(GrandCapitalApplication.TAG_SOCKET, "openDealing");
                requestOrders();
                if (currentDealing == null) {
                    currentDealing = new OrderAnswer();
                }
                currentDealing.setOpenPrice(mCurrentValueY);
                currentDealing.setSymbol(intent.getStringExtra(MakeDealingService.SYMBOL));
                currentDealing.setCmd(Integer.valueOf(intent.getStringExtra(MakeDealingService.CMD)));
                OptionsData optionsData = new OptionsData();
                optionsData.setExpirationTime(ConventDate.getTimeCloseDealing(Double.valueOf(intent.getStringExtra(MakeDealingService.EXPIRATION)).intValue()));
                currentDealing.setOptionsData(optionsData);
                currentDealing.setVolume(Double.valueOf(intent.getStringExtra(MakeDealingService.VOLUME)).intValue());
                showViewOpenDealing(intent.getStringExtra(MakeDealingService.SYMBOL),
                        intent.getStringExtra(MakeDealingService.VOLUME),
                        intent.getStringExtra(MakeDealingService.EXPIRATION));
                etValueAmount.setText(getResources().getString(R.string.zero_dollars));
                etValueTime.setText(getResources().getString(R.string.zero_min));
                typePoint = POINT_OPEN_DEALING;
            } else {
                CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.request_error_request));
            }
        }
    }
    public class GetResponseCloseDealingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            listCurrentClosingDealings.add(new Gson().fromJson(intent.getStringExtra(CheckDealingService.RESPONSE), OrderAnswer.class));
            new Handler().postDelayed(() -> requestOrders(), 2000);
        }
    }
    public class GetResponseOrdersBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (OrderAnswer.getInstance() != null) {
                List<OrderAnswer> listAllOpenDealings = OrderAnswer.filterOrders(OrderAnswer.getInstance(), DealingFragment.OPEN_TAB_POSITION);
                List<OrderAnswer> listOpenDealingsCurrentActive = OrderAnswer.filterOrdersCurrentActive(listAllOpenDealings,
                        DealingFragment.OPEN_TAB_POSITION, ConventString.getActive(tvValueActive));
                List<OrderAnswer> listAllClosedDealings = OrderAnswer.filterOrders(OrderAnswer.getInstance(), DealingFragment.CLOSE_TAB_POSITION);
                CheckDealingService.setListOrderAnswer(listAllOpenDealings);
                parseClosingDealings(listAllClosedDealings, listOpenDealingsCurrentActive);
                if(listOpenDealingsCurrentActive != null && listOpenDealingsCurrentActive.size() != 0 &&
                        (mListDealingXLine == null || mListDealingXLine.size() == 0)){
                    drawAllDealingsXLimitLines(listOpenDealingsCurrentActive);
                }
            }
        }
    }
    public class GetResponseSignalsBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(SignalService.RESPONSE);
            if (response == null || !response.equals("200")) {
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
            if (response != null && response.equals("200") && EarlyClosureAnswer.getInstance() != null && InfoAnswer.getInstance() != null && InfoAnswer.getInstance().getGroup() != null) {
                for(int i = 0; i < EarlyClosureAnswer.getInstance().getInstruments().size(); i++) {
                    Instrument instrument = EarlyClosureAnswer.getInstance().getInstruments().get(i);
                    if(instrument.getSymbol().contains(tvValueActive.getText().toString())) {
                        /*if(InfoAnswer.getInstance().getGroup() == null) {
                            return;
                        }*/
                        String typeOption = InfoAnswer.getInstance().getGroup().getOptionsStyle();
                        int percent = 100;
                        Log.d(TAG, "typeOption: " + typeOption);
                        if(typeOption.contains("american")) {
                            percent = instrument.getWinFull();
                        } else if(typeOption.contains("european")) {
                            int timeValue = ConventString.getTimeValue(etValueTime);
                            if(timeValue < 5) {
                                percent = instrument.getWinLt5();
                            } else if(timeValue >= 5 && timeValue < 15) {
                                percent = instrument.getWin5();
                            } else if(timeValue >= 15 && timeValue < 30) {
                                percent = instrument.getWin15();
                            } else {
                                percent = instrument.getWin30();
                            }
                        }
                        double earlyClosure = ConventString.getAmountValue(etValueAmount) * percent / 100.000;
                        tvValueRewardTerminal.setText(earlyClosure + "(" + (earlyClosure == 0 ? 0 : percent) + "%)");
                    }
                }
            }
        }
    }
}
