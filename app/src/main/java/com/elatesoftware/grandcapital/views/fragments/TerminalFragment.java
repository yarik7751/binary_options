package com.elatesoftware.grandcapital.views.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import com.elatesoftware.grandcapital.api.socket.WebSocketApi;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.User;
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
import com.elatesoftware.grandcapital.utils.ConventDimens;
import com.elatesoftware.grandcapital.utils.ConventImage;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.utils.CustomSharedPreferences;
import com.elatesoftware.grandcapital.utils.GoogleAnalyticsUtil;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.views.items.animation.PointAnimation;
import com.elatesoftware.grandcapital.views.items.chart.ViewInfoHelper;
import com.elatesoftware.grandcapital.views.items.chart.limitLines.ActiveDealingLine;
import com.elatesoftware.grandcapital.views.items.chart.limitLines.BaseLimitLine;
import com.elatesoftware.grandcapital.views.items.chart.limitLines.DealingLine;
import com.elatesoftware.grandcapital.views.items.chart.limitLines.SocketLine;
import com.elatesoftware.grandcapital.views.items.chart.limitLines.XDealingLine;
import com.elatesoftware.grandcapital.views.items.chart.limitLines.YDealingLine;
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
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.Gson;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TerminalFragment extends Fragment {

    public final static String TAG = "TerminalFragment_Logs";
    private static int TIME_ANIMATION_DRAW_POINT = 100;
    public static float MAX_X_SCALE = 9f;

    private static String sSymbolCurrent = "";
    public static double mCurrentValueY = 0;
    private Timer mTimerRedraw;
    private OrderAnswer currentDealing = new OrderAnswer();

    private static int typePoint = 0;
    private final static int POINT_SIMPLY = 0;
    private final static int POINT_OPEN_DEALING = 1;
    private final static int POINT_CLOSE_DEALING = 2;

    public static boolean isAddInChart = false;
    public static boolean isOpen = false;
    public boolean isDirection = true;
    private boolean isFirstDrawPoint = true;
    private boolean isFirstZoom = true;
    private boolean isFirstLoopPoint = true;
    private boolean isTimeIterator, isValueIterator, isOpenKeyboard = false;
    private static boolean isFinishedDrawPoint = true;

    public LineChart mChart;
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
    private View vCurtain ;

    private Drawable drawableMarkerDealing;
    private ImageView imgPointCurrent;
    private Dialog dialogAgreeDeleteDealing;
    public YAxis rightYAxis;
    public XAxis xAxis;

    private int numberTemporaryPoint = 1;
    private float divX = 0, divY = 0;
    private Entry entryLast;
    private Handler handler;
    private Entry currEntry;
    private float x1 = -1, y1 = -1, x2 = -1, y2 = -1, distance1 = -1, distance2 = -1;

    private ViewInfoHelper mViewInfoHelper;
    private WebSocketApi mWebSocket;

    private List<String> listActives = new ArrayList<>();
    public  List<OrderAnswer> listCurrentClosingDealings = new ArrayList<>();
    private List<SocketAnswer> listSocketAnswerQueue = new ArrayList<>();

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
        parentView.setOnClickListener(v -> Log.d(TAG, "parentView CLICK"));
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onCreateView Terminal");
        BaseActivity.backToRootFragment = false;
        BaseActivity.getResideMenu().setScrolling(false);

        isFirstDrawPoint = true;
        drawableMarkerDealing = getResources().getDrawable(R.drawable.marker_close_dealing);

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
        etValueAmount.setText(getResources().getString(R.string.zero_dollars));
        etValueTime.setText(getResources().getString(R.string.zero_min));

        tvValueRewardTerminal.setText(getResources().getString(R.string.reward) + " $0.0(0%)");
        ConventString.formatReward(tvValueRewardTerminal);

        KeyboardVisibilityEvent.registerEventListener(getActivity(), isOpen1 -> {
            isValueIterator = true;
            isTimeIterator = true;
            isOpenKeyboard = isOpen1;
            if (etValueAmount.isFocused()) {
                ConventString.setMaskAmount(etValueAmount, isOpen1);
            }
            if (etValueTime.isFocused()) {
                ConventString.setMaskTime(etValueTime, isOpen1);
            }
        });
        etValueAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if(etValueAmount != null) {
                if(!hasFocus) {
                    isValueIterator = true;
                }
                ConventString.setMaskAmount(etValueAmount, hasFocus);
            }
        });
        etValueTime.setOnFocusChangeListener((v, hasFocus) -> {
            if(etValueTime != null) {
                if(!hasFocus) {
                    isTimeIterator = true;
                }
                ConventString.setMaskTime(etValueTime, hasFocus);
            }
        });
        etValueAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() > 7 && !isValueIterator) {
                    etValueAmount.setText(s.toString().substring(0, 7));
                    etValueAmount.setSelection(etValueAmount.getText().toString().length());
                }
                requestEarlyClosure();
            }
            @Override
            public void afterTextChanged(Editable s) {
                isValueIterator = false;
            }
        });
        etValueTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged");
                if(s.toString().length() > 4 && !isTimeIterator) {
                    etValueTime.setText(s.toString().substring(0, 4));
                    etValueTime.setSelection(etValueTime.getText().toString().length());
                }
                requestEarlyClosure();
            }
            @Override
            public void afterTextChanged(Editable s) {
                isTimeIterator = false;
                Log.d(TAG, "afterTextChanged");
            }
        });

        tvMinusAmount.setOnClickListener(v -> {
            isValueIterator = true;
            ConventString.changeAmountValue(etValueAmount, false, isOpenKeyboard && etValueAmount.isFocused());
        });
        tvPlusAmount.setOnClickListener(v -> {
            isValueIterator = true;
            ConventString.changeAmountValue(etValueAmount, true, isOpenKeyboard && etValueAmount.isFocused());
        });
        tvPlusTime.setOnClickListener(v ->{
                isTimeIterator = true;
            //if(!isOpenKeyboard) {
                ConventString.changeTimeValue(etValueTime, true, isOpenKeyboard && etValueTime.isFocused());
            //}
        });
        tvMinusTime.setOnClickListener(v -> {
            isTimeIterator = true;
            //if(!isOpenKeyboard) {
                ConventString.changeTimeValue(etValueTime, false, isOpenKeyboard && etValueTime.isFocused());
            //}
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
        tvDeposit.setOnClickListener(v -> BaseActivity.changeMainFragment(new DepositFragment()));

        llLowerTerminal.setOnClickListener(v -> {
            llLowerTerminal.setEnabled(false);
            requestMakeDealing(Const.CMD_LOWER);
        });
        llHigherTerminal.setOnClickListener(v -> {
            llHigherTerminal.setEnabled(false);
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
        mTimerRedraw = new Timer();
        startTimerRedrawLimitLines();
        llProgressBar.setVisibility(View.VISIBLE);
        isOpen = true;
        BaseActivity.getResideMenu().setScrolling(false);
        registerBroadcasts();
        clearChart();
        updateBalance(0);
        if (sSymbolCurrent != null && !sSymbolCurrent.equals("")) {
            tvValueActive.setText(sSymbolCurrent);
            drawDataSymbolHistory(ConventString.getActive(tvValueActive));
        } else {
            changeActive();
        }
        requestGetAllOrders();
    }
    @Override
    public void onPause() {
        stopTimerRedrawLimitLines();
        isFinishedDrawPoint = false;
        isAddInChart = false;
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onPause() Terminal");
        isFirstDrawPoint = true;
        listSocketAnswerQueue.clear();
        imgPointCurrent.setVisibility(View.INVISIBLE);
        isOpen = false;
        clearChart();
        super.onPause();
        unregisterBroadcasts();
        BaseActivity.getResideMenu().setScrolling(true);
    }
    @Override
    public void onDestroy() {
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onDestroy() Terminal");
        if(mWebSocket != null){
            mWebSocket.closeSocket();
        }
        GrandCapitalApplication.isTypeOptionAmerican = false;
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
        IntentFilter intentFilterDeleteDealing = new IntentFilter(DeleteDealingService.ACTION_SERVICE_DELETE_DEALING);
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

    private void setXVisibilityPoint() {
        if (currEntry.getX() >= mChart.getHighestVisibleX()) {
            imgPointCurrent.setVisibility(View.INVISIBLE);
        } else {
            imgPointCurrent.setVisibility(View.VISIBLE);
        }
    }
    private void setPoints(MotionEvent event) {
        x1 = event.getX(0);
        y1 = event.getY(0);
        x2 = event.getX(1);
        y2 = event.getY(1);
    }
    private void initializationCurrentPoint() {
        imgPointCurrent = new ImageView(getContext());
        imgPointCurrent.setId(R.id.img);
        PointAnimation pointAnimation = new PointAnimation(getContext(), imgPointCurrent);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(AndroidUtils.dp(40), AndroidUtils.dp(40));
        rlChart.addView(imgPointCurrent, params);
        imgPointCurrent.setVisibility(View.INVISIBLE);
        pointAnimation.start();
    }
    private void updateBalance(double volume){
        if(volume != 0){
            User.getInstance().updateBalance(volume);
        }
        ConventString.setBalance(tvBalance);
    }
    public void setEnabled(boolean enabled) {
        llLowerTerminal.setEnabled(enabled);
        llHigherTerminal.setEnabled(enabled);
        mChart.setEnabled(enabled);
        if(vCurtain  == null) {
            vCurtain  = LayoutInflater.from(getContext()).inflate(R.layout.fragment_support, null);
        }
        if(enabled && vCurtain .getParent() != null) {
            flMain.removeView(vCurtain );
        } else if(!enabled && vCurtain .getParent() == null) {
            flMain.addView(vCurtain );
        }
    }
    private void startProgress(){
        llProgressBar.setVisibility(View.VISIBLE);
        setEnabledBtnTerminal(false);
        setEnabledBtnChooseActive(false);
    }
    private void stopProgress(){
        llProgressBar.setVisibility(View.GONE);
        setEnabledBtnTerminal(true);
        setEnabledBtnChooseActive(true);
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

    private void initializationChart() {
        mChart.setNoDataText("Loading Data...");
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

        mChart.setAnimationCacheEnabled(true);
        mChart.buildDrawingCache(true);
        mChart.setDrawingCacheEnabled(true);
        mChart.getViewPortHandler().setMaximumScaleX(MAX_X_SCALE);

        setLineDataChart();

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
        xAxis.setSpaceMax(70f);
        xAxis.setGranularityEnabled(true);

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
        rightYAxis.setDrawLimitLinesBehindData(true);

        BaseLimitLine.initialization();

        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }
            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {}
            @Override
            public void onChartLongPressed(MotionEvent me) {

            }
            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }
            @Override
            public void onChartSingleTapped(MotionEvent event) {
                OrderAnswer orderClick = BaseLimitLine.onClickLimitLines(event.getX(), event.getY());
                if(orderClick != null){
                    showViewCloseDealing(orderClick);
                }
            }
            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                Log.d(TAG, "scaleX: " + mChart.getViewPortHandler().getScaleX());
                if (imgPointCurrent != null && currEntry != null) {
                    MPPointF point = mChart.getPosition(currEntry, YAxis.AxisDependency.RIGHT);
                    if(point != null) {
                        setXVisibilityPoint();
                        imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
                        imgPointCurrent.setY(point.getY() - imgPointCurrent.getHeight() / 2);
                    }
                }
                BaseLimitLine.scrollXYLinesDealings(mChart.getHighestVisibleX());
            }
            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                if (imgPointCurrent != null && currEntry != null) {
                    MPPointF point = mChart.getPosition(currEntry, YAxis.AxisDependency.RIGHT);
                    if(point != null) {
                        if(mChart.getViewPortHandler().getScaleX() > 1.3277f) {
                            setXVisibilityPoint();
                        }
                        if(mChart.getViewPortHandler().getScaleX() > 4.3f) {
                            imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
                        } else if (mChart.getHighestVisibleX() != mChart.getXChartMax() && mChart.getLowestVisibleX() != mChart.getXChartMin()) {
                            imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
                        }
                        imgPointCurrent.setY(point.getY() - imgPointCurrent.getHeight() / 2 - dY);
                    }
                }
                BaseLimitLine.scrollXYLinesDealings(mChart.getHighestVisibleX());
            }
        });
        mChart.setOnTouchListener((v, event) -> {
            if(event.getPointerCount() >= 3) {
                return false;
            }
            if(event.getActionMasked() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 2) {
                if(x1 < 0 && y1 < 0 && x2 < 0 && y2 < 0) {
                    setPoints(event);
                    distance1 = ConventDimens.callDistance(x1, x2, y1, y2);
                    return false;
                } else {
                    setPoints(event);
                    distance2 = ConventDimens.callDistance(x1, x2, y1, y2);
                    if(Math.abs(distance1 - distance2) >= 5) {
                        if (distance1 < distance2) {
                            if(mChart.getViewPortHandler().getScaleX() >= MAX_X_SCALE) {
                                mChart.setScaleXEnabled(false);
                            }
                        } else if (distance1 > distance2) {
                            mChart.setScaleXEnabled(true);
                        }
                        distance1 = distance2;
                    }
                }
            }
            return false;
        });
    }
    private void setLineDataChart(){
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
    }
    private synchronized LineDataSet createSetDataChart() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        Collections.sort(set.getValues(), new EntryXComparator());
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
    private synchronized LineData getLineDataChart(){
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
        currentDealing = null;
        mCurrentValueY = 0;
        typePoint = POINT_SIMPLY;
        mChart.clearDisappearingChildren();
        mChart.highlightValues(null);
        ActiveDealingLine.deleteDealingLine();
        SocketLine.deleteSocketLine();
        rightYAxis.removeAllLimitLines();
        xAxis.removeAllLimitLines();
        if(mChart.getLineData() != null){
           mChart.getLineData().clearValues();
           mChart.clearValues();
        }
        mChart.invalidate();
    }
    private void changeActive(){
        startProgress();
        llDeposit.setBackgroundColor(getResources().getColor(R.color.dialog_bg));
        imgPointCurrent.setVisibility(View.INVISIBLE);
        if(mWebSocket != null){
            mWebSocket.closeSocket();
        }
        if (sSymbolCurrent == null || sSymbolCurrent.equals("")) {
            sSymbolCurrent = Const.SYMBOL;
        }
        isFirstDrawPoint = true;
        isFirstLoopPoint = true;
        tvValueActive.setText(sSymbolCurrent);
        Log.d(TAG, "sSymbolCurrent: " + sSymbolCurrent);
        GoogleAnalyticsUtil.sendEvent(
                Const.ANALYTICS_TERMINAL_SCREEN,
                Const.ANALYTICS_BUTTON_CHANGE_ACTIVE,
                sSymbolCurrent,
                null
        );

        SymbolHistoryAnswer.nullInstance();
        SocketAnswer.nullInstance();
        OrderAnswer.nullInstance();

        clearChart();
        listSocketAnswerQueue.clear();
        listCurrentClosingDealings.clear();
        requestSymbolHistory(ConventString.getActive(tvValueActive));
        requestGetAllOrders();
    }

    private void startTimerRedrawLimitLines(){
        mTimerRedraw.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() -> {
                     if(isFinishedDrawPoint && listSocketAnswerQueue != null && listSocketAnswerQueue.size() != 0) {
                         SocketAnswer socketAnswer;
                         if(listSocketAnswerQueue.size() == 1){
                             socketAnswer = listSocketAnswerQueue.get(0);
                         }else{
                             Collections.sort(listSocketAnswerQueue, (o1, o2) -> o1.getTime().compareTo(o2.getTime()));
                             socketAnswer = listSocketAnswerQueue.get(listSocketAnswerQueue.size()-1);
                         }
                         listSocketAnswerQueue.clear();
                         if(socketAnswer != null){
                             addEntry(socketAnswer);
                         }
                     }else{
                         redrawXLimitLines();
                         redrawYLimitLines();
                         mChart.invalidate();
                     }
                 });
            }
        }, 1000, 1000);
    }
    private void stopTimerRedrawLimitLines(){
        if(mTimerRedraw != null){
            mTimerRedraw.purge();
            mTimerRedraw.cancel();
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
        startProgress();
        if (ConventString.getAmountValue(etValueAmount) != 0 && ConventString.getTimeValue(etValueTime) != 0 && !ConventString.getActive(tvValueActive).isEmpty()) {
            if(ConventString.getTimeValue(etValueTime) > Const.MAX_TIME_MIN) {
                CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.error_max_time));
                stopProgress();
                return;
            }
            Intent intentService = new Intent(getActivity(), MakeDealingService.class);
            intentService.putExtra(MakeDealingService.CMD, lowerOrHeight);
            intentService.putExtra(MakeDealingService.SYMBOL, ConventString.getActive(tvValueActive));
            intentService.putExtra(MakeDealingService.VOLUME, String.valueOf(ConventString.getAmountValue(etValueAmount)));
            intentService.putExtra(MakeDealingService.EXPIRATION, String.valueOf(ConventString.getTimeValue(etValueTime)));
            getActivity().startService(intentService);
            GoogleAnalyticsUtil.sendEvent(
                    Const.ANALYTICS_TERMINAL_SCREEN,
                    lowerOrHeight.equals(Const.CMD_HEIGHT) ? Const.ANALYTICS_BUTTON_UP : Const.ANALYTICS_BUTTON_DOWN,
                    null,
                    null
            );

        } else {
            CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.no_correct_values));
            stopProgress();
        }
    }
    private void requestGetAllOrders() {
        if(isAdded()) {
            Intent intentService = new Intent(getActivity(), OrdersService.class);
            intentService.putExtra(OrdersService.FUNCTION, OrdersService.GET_ALL_ORDERS_TERMINAL);
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
            llProgressBar.setVisibility(View.VISIBLE);
        }else{
            requestGetTicketOrder(order);
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
                    if ((int)order.getTicket() == orderClosed.getTicket()) {
                        updateBalance(orderClosed.getProfit());
                        mViewInfoHelper.updateSettingsCloseDealing(orderClosed, getActivity());
                        if(order.getSymbol().equals(ConventString.getActive(tvValueActive))) {
                            BaseLimitLine.deleteDealingLimitLine(orderClosed.getTicket());
                            redrawPointsDealings(orderClosed.getTicket());
                        }
                        break;
                    }
                }
            }
            listCurrentClosingDealings.clear();
        }
    }

    public void answerSocket(final SocketAnswer answer) {
        if (answer != null) {
            if (isAddInChart) {
                isFirstLoopPoint = false;
                if(listSocketAnswerQueue == null){
                    listSocketAnswerQueue = new ArrayList<>();
                }
                listSocketAnswerQueue.add(answer);
            } else {
                SymbolHistoryAnswer.addSocketAnswerInSymbol(answer);
                Log.d(GrandCapitalApplication.TAG_SOCKET, "add in list background");
            }
        }
    }
    public synchronized void addEntry(final SocketAnswer answer) {
        if (answer != null && answer.getTime() != null && answer.getAsk() != null) {
            LineData data = getLineDataChart();
            currEntry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getAsk())), null, null);
            isFinishedDrawPoint = false;
            if (data != null && data.getEntryCount() != 0) {
                mCurrentValueY = answer.getAsk();
                if (data.getDataSetByIndex(0).getEntryCount() != 0) {
                    entryLast = data.getDataSetByIndex(0).getEntryForIndex(data.getDataSetByIndex(0).getEntryCount() - 1);
                    if (entryLast != null) {
                        if (entryLast.getX() > currEntry.getX()) {
                            return;
                        }
                        switch (typePoint) {
                            case POINT_CLOSE_DEALING:
                                entryLast.setIcon(drawableMarkerDealing);
                                entryLast.setData(null);
                                typePoint = POINT_SIMPLY;
                                break;
                            case POINT_OPEN_DEALING:
                                OrderAnswer order = currentDealing;
                                order.setOpenPrice((double) entryLast.getY());
                                String dataEntry = new Gson().toJson(order);
                                entryLast.setIcon(drawableMarkerDealing);
                                entryLast.setData(dataEntry);
                                BaseLimitLine.drawDealingLimitLine(order, false, mCurrentValueY);
                                currentDealing = null;
                                typePoint = POINT_SIMPLY;
                                break;
                            default:
                                break;
                        }
                        divX = (currEntry.getX() - entryLast.getX()) / 10.f;
                        divY = (currEntry.getY() - entryLast.getY()) / 10.f;
                        numberTemporaryPoint = 0;
                        final Entry simplyEntry = new Entry(entryLast.getX(), entryLast.getY(), null, null);
                            handler.postAtTime(new Runnable() {
                                @Override
                                public void run() {
                                    if (entryLast != null && currEntry != null) {
                                        float x = simplyEntry.getX();
                                        float y = simplyEntry.getY();
                                        simplyEntry.setX(x + divX);
                                        simplyEntry.setY(y + divY);
                                        if (numberTemporaryPoint == 0) {
                                            data.getDataSetByIndex(0).addEntry(simplyEntry);
                                            SymbolHistoryAnswer.addSocketAnswerInSymbol(answer);
                                        }
                                        data.notifyDataChanged();
                                        mChart.invalidate();
                                        numberTemporaryPoint++;

                                        redrawItemsChart(simplyEntry);

                                        if(numberTemporaryPoint == 10){
                                            isFinishedDrawPoint = true;
                                        }
                                        if (numberTemporaryPoint < 10) {
                                            handler.postDelayed(this, TIME_ANIMATION_DRAW_POINT);
                                        }
                                    }
                                }
                            }, TIME_ANIMATION_DRAW_POINT);
                        }
                }
            } else if (data != null && currEntry != null) {
                    data.getDataSetByIndex(0).addEntry(currEntry);
                    data.notifyDataChanged();
                    mChart.invalidate();
                    redrawItemsChart(currEntry);
                    SymbolHistoryAnswer.addSocketAnswerInSymbol(answer);
                    isFinishedDrawPoint = true;
            }
        }
    }
    private synchronized void addEntry(final SymbolHistoryAnswer answer) {
        if (answer != null && answer.getTime() != null && answer.getOpen() != null) {
            LineData data = getLineDataChart();
            if (data != null) {
                Entry entry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getOpen())), null, null);
                data.getDataSetByIndex(0).addEntry(entry);
                data.notifyDataChanged();
                mChart.notifyDataSetChanged();
            }
        }
    }
    private synchronized void drawDataSymbolHistory(final String symbol) {
        if (SymbolHistoryAnswer.getInstance() == null || SymbolHistoryAnswer.getInstance().size() == 0) {
            isAddInChart = true;
            return;
        }
        final List<SymbolHistoryAnswer> listSymbol = SymbolHistoryAnswer.getInstance();
        if (listSymbol != null && listSymbol.size() != 0) {
            Log.d(GrandCapitalApplication.TAG_SOCKET, "drawDataSymbolHistory size = " + listSymbol.size());
            getActivity().runOnUiThread(() -> {
                if (listSymbol.size() != 0) {
                    for (int i = 0; i < listSymbol.size(); i++) {
                        addEntry(listSymbol.get(i));
                    }
                    mChart.invalidate();
                }
            });
            if (mChart.getLineData() != null && listSymbol.size() != 0) {
                mCurrentValueY = Double.valueOf(String.valueOf(listSymbol.get(listSymbol.size() - 1).getOpen()));
                Entry entry = new Entry(ConventDate.genericTimeForChart(listSymbol.get(listSymbol.size() - 1).getTime()), Float.valueOf(String.valueOf(mCurrentValueY)), null, null);
                currEntry = entry;
                getActivity().runOnUiThread(() -> new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (imgPointCurrent != null && currEntry != null) {
                            redrawItemsChart(currEntry);
                            MPPointF point = mChart.getPosition(currEntry, YAxis.AxisDependency.RIGHT);
                            imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
                            imgPointCurrent.setY(point.getY() - imgPointCurrent.getHeight() / 2);
                        }
                        if(isFirstZoom) {
                            mChart.zoom(MAX_X_SCALE - 1, 0f, entry.getX(), 0f, YAxis.AxisDependency.RIGHT);
                            isFirstZoom = false;
                        }
                        if (isFirstDrawPoint && imgPointCurrent != null) {
                            imgPointCurrent.setVisibility(View.VISIBLE);
                            isFirstDrawPoint = false;
                        }
                        if(isFirstLoopPoint) {
                            new Handler().postDelayed(this, 50);
                        }
                        stopProgress();
                    }
                }, 50));
            }
        }
        if(WebSocketApi.getmSymbolCurrent() == null || !WebSocketApi.getmSymbolCurrent().equals(symbol)){
            mWebSocket = new WebSocketApi(symbol);
        }
        isAddInChart = true;
        isFinishedDrawPoint = true;
        stopProgress();
    }

    private void drawCurrentPoint(Entry entry) {
        if (imgPointCurrent != null) {
            MPPointF point = mChart.getPosition(entry, YAxis.AxisDependency.RIGHT);
            imgPointCurrent.setX(point.getX() - imgPointCurrent.getWidth() / 2);
            float y = point.getY();
            if(y > mChart.getHeight() - mChart.getHeight() * 0.1f) {
                y = mChart.getHeight() - mChart.getHeight() * 0.1f;
            }
            if(y < mChart.getHeight() * 0.1f) {
                y = mChart.getHeight() * 0.1f;
            }
            imgPointCurrent.setY(y - imgPointCurrent.getHeight() / 2);
            setXVisibilityPoint();
        }
    }
    public void redrawItemsChart(Entry entry){
        SocketLine.drawSocketLine(entry);
        drawCurrentPoint(entry);
        redrawYLimitLines();
        redrawXLimitLines();
    }
    private void redrawXLimitLines(){
        List<XDealingLine> list = BaseLimitLine.getXLimitLines();
        if(list != null && list.size() != 0){
            for(XDealingLine line : list){
            OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                if(!ConventDate.validationDateTimer(order.getOptionsData().getExpirationTime()) || Long.parseLong(line.getmTimer()) <= 0.5){
                    xAxis.removeLimitLine(line);
                    if(line.ismIsActive()){
                        BaseLimitLine.activationSelectedDealing(null);
                    }
                    typePoint = POINT_CLOSE_DEALING;
                }else{
                    if(GrandCapitalApplication.isTypeOptionAmerican && ConventDate.getDifferenceDate(order.getOpenTime()) >= 61){
                        line.setmIsAmerican(true);
                    }
                    DealingLine.updateColorDealingLine(line, order, mCurrentValueY);
                    if(line.ismIsActive()){
                        ActiveDealingLine.drawActiveDealingLine(line, order);
                    }
                }
            }
        }
    }
    private void redrawYLimitLines(){
        List<YDealingLine> list = BaseLimitLine.getYLimitLines();
        if(list != null && list.size() != 0){
            for(YDealingLine line : list){
                OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                if(!ConventDate.validationDateTimer(order.getOptionsData().getExpirationTime()) || Long.parseLong(line.getmTimer()) < 0.5){
                    rightYAxis.removeLimitLine(line);
                    if(line.ismIsActive()){
                        BaseLimitLine.activationSelectedDealing(null);
                    }
                    typePoint = POINT_CLOSE_DEALING;
                }else{
                    if(GrandCapitalApplication.isTypeOptionAmerican && ConventDate.getDifferenceDate(order.getOpenTime()) >= 61){
                        line.setmIsAmerican(true);
                    }
                    DealingLine.updateColorDealingLine(line, order, mCurrentValueY);
                    if(line.ismIsActive()){
                        ActiveDealingLine.drawActiveDealingLine(line, order);
                    }
                }
            }
        }
    }
    private void redrawPointsDealings(final int ticket){
        if (mChart.getData() != null && mChart.getData().getEntryCount() != 0 ) {
            ILineDataSet set = mChart.getData().getDataSetByIndex(0);
            if(set.getEntryCount() != 0){
                for (int i = set.getEntryCount() - 1; i >= 0; i--) {
                    Entry entry = set.getEntryForIndex(i);
                    if (entry.getData() != null && entry.getData() instanceof String) {
                        OrderAnswer dataPoint = new Gson().fromJson(String.valueOf(entry.getData()) , OrderAnswer.class);
                        if(ticket == dataPoint.getTicket()){
                            entry.setIcon(null);
                            entry.setData(null);
                            mChart.getData().notifyDataChanged();
                            break;
                        }
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
    public void showViewCloseDealing(OrderAnswer order){
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
                    if(instrument.getSymbol().length() == 6            // TODO COMMIT BUGS
                        && !instrument.getSymbol().contains("JPY")
                        && !instrument.getSymbol().equals("NZDUSD")
                        && !instrument.getSymbol().equals("SILVER")){
                        listActives.add(instrument.getSymbol());
                    }
                }
                if (listActives != null && listActives.size() > 0 && listActives.contains(Const.SYMBOL) && !ConventString.getActive(tvValueActive).equals(Const.SYMBOL)) {
                    sSymbolCurrent = listActives.get(listActives.indexOf(Const.SYMBOL));
                    changeActive();
                }
                updateBalance(0);
            }
            requestSignals();
         }
    }
    public class GetResponseSymbolHistoryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(SymbolHistoryService.RESPONSE) != null && intent.getStringExtra(SymbolHistoryService.RESPONSE).equals(Const.RESPONSE_CODE_SUCCESS) &&
                SymbolHistoryAnswer.getInstance() != null){
                    drawDataSymbolHistory(ConventString.getActive(tvValueActive));
            } else {
                isAddInChart = true;
                mWebSocket = new WebSocketApi(sSymbolCurrent);
                stopProgress();
            }
        }
    }
    public class GetResponseOpenDealingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(MakeDealingService.RESPONSE);
            if (response != null && response.equals("true")) {
                if (currentDealing == null) {
                    currentDealing = new OrderAnswer();
                }
                isTimeIterator = true;
                isValueIterator = true;

                currentDealing.setOpenPrice(mCurrentValueY);
                currentDealing.setSymbol(intent.getStringExtra(MakeDealingService.SYMBOL));
                currentDealing.setCmd(Integer.valueOf(intent.getStringExtra(MakeDealingService.CMD)));
                OptionsData optionsData = new OptionsData();
                optionsData.setExpirationTime(ConventDate.getTimeCloseDealing(Double.valueOf(intent.getStringExtra(MakeDealingService.EXPIRATION)).intValue()));
                currentDealing.setOptionsData(optionsData);
                currentDealing.setOpenTime(ConventDate.getCurrentDate());
                currentDealing.setVolume(Double.valueOf(intent.getStringExtra(MakeDealingService.VOLUME)).intValue());
                typePoint = POINT_OPEN_DEALING;
                new Handler().postDelayed(() -> requestGetAllOrders(), 5000);
                updateBalance(-1 * Double.valueOf(currentDealing.getVolume()));
                mViewInfoHelper.showViewOpenDealing(intent.getStringExtra(MakeDealingService.SYMBOL),
                        intent.getStringExtra(MakeDealingService.VOLUME),
                        intent.getStringExtra(MakeDealingService.EXPIRATION));
                stopProgress();
            } else {
                CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.request_error_request));
                stopProgress();
            }
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
                    case OrdersService.GET_ALL_ORDERS_TERMINAL:
                        List<OrderAnswer> listAllOpenDealings = OrderAnswer.filterOrders(OrderAnswer.getInstance(), DealingFragment.OPEN_TAB_POSITION);
                        List<OrderAnswer> listOpenDealings = OrderAnswer.filterOrdersCurrentActive(listAllOpenDealings, DealingFragment.OPEN_TAB_POSITION, ConventString.getActive(tvValueActive));
                        List<OrderAnswer> listAllClosedDealings = OrderAnswer.filterOrders(OrderAnswer.getInstance(), DealingFragment.CLOSE_TAB_POSITION);

                        CheckDealingService.setListOrderAnswer(listAllOpenDealings);
                        parseClosingDealings(listAllClosedDealings);

                        if(listOpenDealings != null && listOpenDealings.size() != 0){
                            int i = 0;
                            if(BaseLimitLine.getYLimitLines() != null){
                                i = BaseLimitLine.getYLimitLines().size();
                            }
                            if(BaseLimitLine.getXLimitLines() != null){
                                i = BaseLimitLine.getXLimitLines().size();
                            }
                            if(i < listOpenDealings.size()){
                                BaseLimitLine.drawAllDealingsLimitLines(listOpenDealings, mCurrentValueY);
                                if (currEntry != null) {
                                    SocketLine.drawSocketLine(currEntry);
                                }
                            }else{
                                BaseLimitLine.addTicketsInLines(listOpenDealings);
                            }
                        }
                        OrderAnswer.setInstance(null);
                        break;
                    case OrdersService.GET_TICKET_ORDER:
                        requestDeleteDealing(new Gson().fromJson(intent.getStringExtra(OrdersService.RESPONSE), OrderAnswer.class));
                        OrderAnswer.setInstance(null);
                        break;
                    case OrdersService.GET_ALL_ORDERS_DEALING:
                        break;
                    default:
                        break;
                }
                stopProgress();
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
                GrandCapitalApplication.isTypeOptionAmerican = intent.getBooleanExtra(EarlyClosureService.IS_AMERICAN, false);
                tvValueRewardTerminal.setText(getResources().getString(R.string.reward) + " $" + ConventString.getStringEarlyClosure(etValueAmount, intent.getIntExtra(EarlyClosureService.PERCENT, 0)));
                ConventString.formatReward(tvValueRewardTerminal);
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
                llProgressBar.setVisibility(View.GONE);
                int ticket = intent.getIntExtra(DeleteDealingService.TICKET, 0);
                if(ticket != 0){
                    OrderAnswer order = CheckDealingService.deleteOrder(ticket);
                    if(order != null){
                        redrawPointsDealings(ticket);
                        updateBalance(order.getProfit());
                        mViewInfoHelper.updateSettingsCloseDealing(order, getActivity());
                        BaseLimitLine.deleteDealingLimitLine(ticket);
                        typePoint = POINT_CLOSE_DEALING;
                        GoogleAnalyticsUtil.sendEvent(Const.ANALYTICS_TERMINAL_SCREEN, Const.ANALYTICS_BUTTON_CLOSE_DEALINGS, null, null);
                    }
                }
            }
        }
    }
}
