package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.api.pojo.SignalAnswer;
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.Dealing;
import com.elatesoftware.grandcapital.services.CheckDealingService;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.services.MakeDealingService;
import com.elatesoftware.grandcapital.services.OrdersService;
import com.elatesoftware.grandcapital.services.SignalService;
import com.elatesoftware.grandcapital.services.SymbolHistoryService;
import com.elatesoftware.grandcapital.utils.AndroidUtils;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.ConventString;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.elatesoftware.grandcapital.views.items.chart.limit_lines.CustomBaseLimitLine;
import com.elatesoftware.grandcapital.views.items.chart.marker.CloseDealingPointMarkerView;
import com.elatesoftware.grandcapital.views.items.chart.marker.CurrentSocketPointMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.ArrayList;
import java.util.List;

import static com.elatesoftware.grandcapital.R.id.rl_chart;

public class TerminalFragment extends Fragment implements OnChartValueSelectedListener {

    public final static String TAG = "TerminalFragment_Logs";

    private final static int INTERVAL_SHOW_LABEL = 3000;
    private final static int INTERVAL_SHOW_LABEL_CLOSE = 9000;

    private final static String SYMBOL = "EURUSD";
    private static String sSymbolCurrent = "";

    public static boolean isAddInChart = false;
    public static boolean isOpen = false;
    public boolean isDirection = true;
    private LineChart mChart;
    private YAxis rightYAxis;
    private XAxis xAxis;
    private Thread threadSymbolHistory;

    private static List<String> listActives = new ArrayList<>();
    public static List<SocketAnswer> listBackGroundSocketAnswer = new ArrayList<>();
    private static List<Highlight> listSelectedEntries = new ArrayList<>();

    private String currActive, currAmount, currTime;
    private View openDealingView;

    private String activeClose, amountClose;
    private long mCurrTimeUnix;
    private long mCloseTime;
    private Handler handler = new Handler();

    private View closeDealingView;
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

    private GetResponseSymbolHistoryBroadcastReceiver mSymbolHistoryBroadcastReceiver;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;
    private GetResponseMakeDealingBroadcastReceiver mMakeDealingBroadcastReceiver;
    private GetResponseSignalsBroadcastReceiver mSignalsBroadcastReceiver;
    private CloseDealingBroadcastReceiver closeDealingBroadcastReceiver;
    private GetResponseOrdersBroadcastReceiver mOrdersBroadcastReceiver;

    private CurrentSocketPointMarkerView currentSocketPointMarkerView;
    private CloseDealingPointMarkerView dealingPointMarkerView;

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
        rlChart = (RelativeLayout) parentView.findViewById(rl_chart);
        llProgressBar = (LinearLayout) parentView.findViewById(R.id.progress_bar);
        flMain = (FrameLayout) parentView.findViewById(R.id.fl_main);

        tvSignalMinutes1 = (TextView) parentView.findViewById(R.id.tv_time1_value);
        tvSignalMinutes5 = (TextView) parentView.findViewById(R.id.tv_time2_value);
        tvSignalMinutes15 = (TextView) parentView.findViewById(R.id.tv_time3_value);
        tvCurrentActive = (TextView) parentView.findViewById(R.id.tv_currency);
        tvCurrentActiveAmount = (TextView) parentView.findViewById(R.id.tv_amout);
        tvErrorSignal = (TextView) parentView.findViewById(R.id.tvErrorSignal);
        rlErrorSignal = (RelativeLayout) parentView.findViewById(R.id.rlErrorSignal);

        etValueAmount.clearFocus();
        etValueTime.clearFocus();

        setSizeHeight();
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
        KeyboardVisibilityEvent.registerEventListener(getActivity(), isOpen1 -> {
            if (etValueAmount.isFocused()) {
                ConventString.setMaskAmount(etValueAmount, isOpen1);
            }
            if (etValueTime.isFocused()) {
                ConventString.setMaskTime(etValueTime, isOpen1);
            }
        });
        initializationChart();
        etValueAmount.setOnFocusChangeListener((v, hasFocus) -> ConventString.setMaskAmount(etValueAmount, hasFocus));
        etValueTime.setOnFocusChangeListener((v, hasFocus) -> ConventString.setMaskTime(etValueTime, hasFocus));
        tvMinusAmount.setOnClickListener(v -> ConventString.changeAmountValue(etValueAmount, false));
        tvPlusAmount.setOnClickListener(v -> ConventString.changeAmountValue(etValueAmount, true));
        tvPlusTime.setOnClickListener(v -> ConventString.changeTimeValue(etValueTime, true));
        tvMinusTime.setOnClickListener(v -> ConventString.changeTimeValue(etValueTime, false));
        tvLeftActive.setOnClickListener(v -> {
            if (!ConventString.getActive(tvValueActive).equals("") && listActives.size() > 0) {
                int index = listActives.indexOf(ConventString.getActive(tvValueActive));
                if (index == 0) {
                    sSymbolCurrent = listActives.get(listActives.size() - 1);
                    changeActive();
                } else {
                    sSymbolCurrent = listActives.get(index - 1);
                    changeActive();
                }
            } else {
                sSymbolCurrent = SYMBOL;
                changeActive();
            }
            parseResponseSignals(ConventString.getActive(tvValueActive));
        });
        tvRightActive.setOnClickListener(v -> {
            if (!ConventString.getActive(tvValueActive).equals("") && listActives.size() > 0) {
                int index = listActives.indexOf(ConventString.getActive(tvValueActive));
                if (index == (listActives.size() - 1)) {
                    sSymbolCurrent = listActives.get(0);
                    changeActive();
                } else {
                    sSymbolCurrent = listActives.get(index + 1);
                    changeActive();
                }
            } else {
                sSymbolCurrent = SYMBOL;
                changeActive();
            }
            parseResponseSignals(ConventString.getActive(tvValueActive));
        });
        tvDeposit.setOnClickListener(v -> {
            BaseActivity.changeMainFragment(new DepositFragment());
        });
        llLowerTerminal.setOnClickListener(v -> {
            //CustomDialog.showDialogOpenAccount(getActivity(), null);
            requestMakeDealing("1");

        });
        llHigherTerminal.setOnClickListener(v -> {
            requestMakeDealing("0");
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onResume Terminal");
        isOpen = true;
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(false);
        registerBroadcasts();
        ConventString.updateBalance(tvBalance);
        if (sSymbolCurrent != null && !sSymbolCurrent.equals("")) {
            tvValueActive.setText(sSymbolCurrent);
            parseResponseSymbolHistory(false);
        } else {
            changeActive();
        }
        tvLeftActive.setEnabled(true);
        tvRightActive.setEnabled(true);
    }

    @Override
    public void onPause() {
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onPause() Terminal");
        mChart.clearValues();
        listBackGroundSocketAnswer.clear();
        unregisterBroadcasts();
        isAddInChart = false;
        isOpen = false;
        super.onPause();
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(true);
        if (threadSymbolHistory != null) {
            threadSymbolHistory.interrupt();
        }
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

        mMakeDealingBroadcastReceiver = new GetResponseMakeDealingBroadcastReceiver();
        IntentFilter intentFilterMakeDealing = new IntentFilter(MakeDealingService.ACTION_SERVICE_MAKE_DEALING);
        intentFilterMakeDealing.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mMakeDealingBroadcastReceiver, intentFilterMakeDealing);

        mSignalsBroadcastReceiver = new GetResponseSignalsBroadcastReceiver();
        IntentFilter intentFilterSignal = new IntentFilter(SignalService.ACTION_SERVICE_SIGNAL);
        intentFilterSignal.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mSignalsBroadcastReceiver, intentFilterSignal);

        closeDealingBroadcastReceiver = new CloseDealingBroadcastReceiver();
        IntentFilter intentFilterCloseDealing = new IntentFilter(CheckDealingService.ACTION_SERVICE_CHECK_DEALINGS);
        intentFilterCloseDealing.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(closeDealingBroadcastReceiver, intentFilterCloseDealing);

        mOrdersBroadcastReceiver = new GetResponseOrdersBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(OrdersService.ACTION_SERVICE_ORDERS + OrdersService.FRAGMENT_TERMINAL);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mOrdersBroadcastReceiver, intentFilter);
    }

    private void unregisterBroadcasts() {
        getActivity().unregisterReceiver(mSymbolHistoryBroadcastReceiver);
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
        getActivity().unregisterReceiver(mMakeDealingBroadcastReceiver);
        getActivity().unregisterReceiver(mSignalsBroadcastReceiver);
        getActivity().unregisterReceiver(closeDealingBroadcastReceiver);
        getActivity().unregisterReceiver(mOrdersBroadcastReceiver);
    }

    private void initializationChart() {
        mChart.setNoDataText(getResources().getString(R.string.request_error_title));
        mChart.setDragDecelerationFrictionCoef(0.95f); // задержка при перетаскивании
        mChart.setHighlightPerDragEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.setPadding(0, 0, 0, 0);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.setScaleYEnabled(false);
        mChart.setScaleMinima(0.5f, 1f);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.getDescription().setEnabled(false);// enable description text
        mChart.setTouchEnabled(true);      // enable touch gestures жесты
        mChart.setDragEnabled(true);    // enable scaling and dragging
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);      // if disabled, scaling can be done on x- and y-axis separately
        mChart.setBackgroundColor(Color.TRANSPARENT); // set an alternative background color
        mChart.getLegend().setEnabled(false);   //Hide the legend
        mChart.setDragOffsetX(30f);// TODO norm padding chart in left
        mChart.setDrawMarkers(true);
        mChart.getViewPortHandler().setMaximumScaleX(7f);

        currentSocketPointMarkerView = new CurrentSocketPointMarkerView(getContext());
        currentSocketPointMarkerView.setChartView(mChart);
        mChart.setMarker(currentSocketPointMarkerView);

//        dealingPointMarkerView = new CloseDealingPointMarkerView(getContext());
//        dealingPointMarkerView.setChartView(mChart);
//        mChart.setMarker(dealingPointMarkerView);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        xAxis = mChart.getXAxis();  //Ось Х
        xAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        xAxis.setTextColor(getResources().getColor(R.color.chart_values));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setValueFormatter((value, axis) -> ConventDate.convertDateFromMilSecHHMM(ConventDate.genericTimeForChartLabels(value)));
        xAxis.setEnabled(true);
        xAxis.disableAxisLineDashedLine();
        xAxis.setDrawGridLines(true);

        //xAxis.setGranularity(0.000000001f);
        //xAxis.setGranularityEnabled(true);

        YAxis leftYAxis = mChart.getAxisLeft();      //Ось Y left
        leftYAxis.setEnabled(false);
        rightYAxis = mChart.getAxisRight();    //Ось Y right
        rightYAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        rightYAxis.setTextColor(getResources().getColor(R.color.chart_values));
        rightYAxis.setEnabled(true);
        rightYAxis.setDrawGridLines(true);
        rightYAxis.disableAxisLineDashedLine();
        rightYAxis.setValueFormatter((value, axis) -> String.format("%.5f", value).replace(',', '.'));
        rightYAxis.setStartAtZero(false);
    }

    private void setSizeHeight() {
        int height = AndroidUtils.getWindowsSizeParams(getContext())[1] - AndroidUtils.getStatusBarHeight(getContext()) - AndroidUtils.dp(60);
        rlChart.getLayoutParams().height = (int) (height * 0.6);
        llDeposit.getLayoutParams().height = (int) (height * 0.28);
        llButtons.getLayoutParams().height = (int) (height * 0.11);
    }

    public void showSignalsPanel() {
        parseResponseSignals(ConventString.getActive(tvValueActive));
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

    private void showLabelCloseDealing(OrderAnswer answer) {
        closeDealingView = LayoutInflater.from(getContext()).inflate(R.layout.label_close_dealing, null);
        ((TextView) closeDealingView.findViewById(R.id.tvPrice))
                .setText(getResources().getString(R.string.of_price) + " " + answer.getClosePrice() +
                        ",\n" + getResources().getString(R.string.profit) + " " + answer.getProfitStr());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = AndroidUtils.dp(16);
        params.leftMargin = AndroidUtils.dp(16);
        rlChart.addView(closeDealingView, params);

        /*addIconCloseDealing(new Entry(ConventDate.genericTimeForChart(answer.getCloseTimeUnix()),
                Float.valueOf(String.valueOf(answer.getClosePrice()))));*/
        handler.postDelayed(() -> rlChart.removeView(closeDealingView), INTERVAL_SHOW_LABEL_CLOSE);
    }

    private View initialViewOpenDealing() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.label_open_dealing, null);
        ((TextView) v.findViewById(R.id.tvActive)).setText(currActive);
        ((TextView) v.findViewById(R.id.tvAmount)).setText(currAmount);
        ((TextView) v.findViewById(R.id.tvTime)).setText(currTime);
        return v;
    }

    private void clearChart() {
        mChart.highlightValues(null); // delete all markers
        rightYAxis.removeAllLimitLines();
        mChart.getData().clearValues();
        mChart.getLineData().clearValues();
        mChart.invalidate();
        SymbolHistoryAnswer.nullInstance();
        SocketAnswer.nullInstance();
    }

    private void changeActive() {
        if (sSymbolCurrent == null || sSymbolCurrent.equals("")) {
            sSymbolCurrent = SYMBOL;
        }
        tvValueActive.setText(sSymbolCurrent);
        clearChart();
        GrandCapitalApplication.closeSocket();
        requestSymbolHistory(ConventString.getActive(tvValueActive));
        tvLeftActive.setEnabled(false);
        tvRightActive.setEnabled(false);
    }

    private void parseResponseSymbolHistory(boolean isMakeOpenSocket) {
        if (SymbolHistoryAnswer.getInstance() != null && SymbolHistoryAnswer.getInstance().size() != 0) {
            List<SocketAnswer> list;
            if (listBackGroundSocketAnswer != null && listBackGroundSocketAnswer.size() != 0) {
                list = listBackGroundSocketAnswer.subList(0, listBackGroundSocketAnswer.size());
                Log.d(GrandCapitalApplication.TAG_SOCKET, "add from background in list socketanswer size = " + (list.size() - 1));
                for (SocketAnswer item : list) {
                    addSocketAnswerInSymbol(item);
                }
                listBackGroundSocketAnswer.clear();
            }
            drawDataSymbolHistory(SymbolHistoryAnswer.getInstance(), ConventString.getActive(tvValueActive), isMakeOpenSocket);
        }
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

    private void requestSignals() {
        Intent intentService = new Intent(getActivity(), SignalService.class);
        getActivity().startService(intentService);
    }

    private void requestSymbolHistory(String symbol) {
        Intent intentService = new Intent(getActivity(), SymbolHistoryService.class);
        intentService.putExtra(SymbolHistoryService.SYMBOL, symbol);
        getActivity().startService(intentService);
        llProgressBar.setVisibility(View.VISIBLE);
    }

    private void requestMakeDealing(String cmd) {
        currActive = tvValueActive.getText().toString();
        currAmount = etValueAmount.getText().toString();
        currTime = etValueTime.getText().toString();
        if (ConventString.getAmountValue(etValueAmount) != 0 && ConventString.getTimeValue(etValueTime) != 0 && !ConventString.getActive(tvValueActive).equals("")) {
            Intent intentService = new Intent(getActivity(), MakeDealingService.class);
            intentService.putExtra(MakeDealingService.CMD, cmd);
            intentService.putExtra(MakeDealingService.SYMBOL, ConventString.getActive(tvValueActive));
            intentService.putExtra(MakeDealingService.VOLUME, String.valueOf(ConventString.getAmountValue(etValueAmount)));
            intentService.putExtra(MakeDealingService.EXPIRATION, String.valueOf(ConventString.getTimeValue(etValueTime)));
            getActivity().startService(intentService);
        } else {
            CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.no_correct_values));
        }
    }

    private synchronized LineDataSet createSetDataChart() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.WHITE);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(1.3f);
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
        set.setHighLightColor(Color.TRANSPARENT);
        return set;
    }

    private void drawDataSymbolHistory(List<SymbolHistoryAnswer> listSymbol, final String symbol, boolean isMAkeOpenSocket) {
        if (threadSymbolHistory != null) {
            threadSymbolHistory.interrupt();
        }
        threadSymbolHistory = new Thread(() -> {
            Log.d(GrandCapitalApplication.TAG_SOCKET, "drawDataSymbolHistory size = " + listSymbol.size());
            if (listSymbol != null && listSymbol.size() != 0) {
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
                mChart.moveViewToX(mChart.getData().getXMax());
                mChart.zoom(10f, 0f, mChart.getData().getXMax(), 0f, YAxis.AxisDependency.RIGHT);
                Entry entry = new Entry(ConventDate.genericTimeForChart(listSymbol.get(listSymbol.size() - 1).getTime()),
                        Float.valueOf(String.valueOf(listSymbol.get(listSymbol.size() - 1).getOpen())));
                getActivity().runOnUiThread(() -> drawCurrentYLimitLine(entry));
            }
            if (isMAkeOpenSocket) {
                GrandCapitalApplication.closeAndOpenSocket(symbol);
            } else {
                isAddInChart = true;
            }
        });
        threadSymbolHistory.start();
    }

    public void addSocketAnswerInSymbol(SocketAnswer item) {
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
                Entry entry = new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getAsk())));
                data.addEntry(entry, 0);
                data.notifyDataChanged();
                drawCurrentYLimitLine(entry);
                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        }
    }

    public void addEntry(SymbolHistoryAnswer answer) {
        if (answer != null && answer.getTime() != null && answer.getOpen() != null) {
            LineData data = mChart.getData();
            if (data != null) {
                ILineDataSet set = data.getDataSetByIndex(0);
                if (set == null) {
                    set = createSetDataChart();
                    data.addDataSet(set);
                }
                data.addEntry(new Entry(ConventDate.genericTimeForChart(answer.getTime()),
                        Float.valueOf(String.valueOf(answer.getOpen()))), 0);
                data.notifyDataChanged();
                mChart.notifyDataSetChanged();
            }
        }
    }

    private void drawCurrentYLimitLine(Entry entry) {
        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.whitevert);
        CustomBaseLimitLine ll1 = new CustomBaseLimitLine(entry.getY(), String.valueOf(entry.getY()), icon);
        ll1.setLineWidth(1.5f);
        ll1.setLineColor(Color.WHITE);
        ll1.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_CURRENT_SOCKET);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);

        rightYAxis.removeAllLimitLines();
        rightYAxis.addLimitLine(ll1);
        /*if(listSelectedEntries != null && listSelectedEntries.size() != 0){
            List<Highlight> listTemp = new ArrayList<>();
            listTemp = listSelectedEntries;
            Highlight current = new Highlight(entry.getX(), entry.getY(), 0);
            listTemp.add(current);
            mChart.highlightValues();
        }else{*/
            mChart.highlightValue(entry.getX(), entry.getY(), 0);
        //}
    }

    private void drawXLimitLine(Entry entry) {
        CustomBaseLimitLine line = new CustomBaseLimitLine(entry.getX());
        line.setLineWidth(1.5f);
        line.setTextColor(Color.BLACK);
        line.setLineColor(Color.WHITE);
        line.setTypeLimitLine(CustomBaseLimitLine.LimitLinesType.LINE_CURRENT_SOCKET);
        line.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        xAxis.addLimitLine(line);
        xAxis.removeAllLimitLines();
        xAxis.addLimitLine(line);
    }

    private void addIconCloseDealing(Entry entry) {
        if(listSelectedEntries != null){
            listSelectedEntries.add(new Highlight(entry.getX(), entry.getY(), 0));
        }
        //mChart.highlightValue(entry.getX(), entry.getY(), 0);
        //entry.setIcon(getResources().getDrawable(R.drawable.marker_close_dealing));
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
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
                parseResponseSymbolHistory(true);
            } else {
                GrandCapitalApplication.closeAndOpenSocket(sSymbolCurrent);
            }
            tvLeftActive.setEnabled(true);
            tvRightActive.setEnabled(true);
            llProgressBar.setVisibility(View.GONE);
        }
    }

    public class GetResponseMakeDealingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(MakeDealingService.RESPONSE);
            mCurrTimeUnix = Long.valueOf(ConventDate.getTimeStampCurrentDate());
            Log.d(TAG, "getTimeStampCurrentDate :" + ConventDate.getTimeStampCurrentDate());
            if (response != null && response.equals("true")) {
                openDealingView = initialViewOpenDealing();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.topMargin = AndroidUtils.dp(16);
                params.leftMargin = AndroidUtils.dp(16);
                rlChart.addView(openDealingView, params);
                handler.postDelayed(() -> rlChart.removeView(openDealingView), INTERVAL_SHOW_LABEL);
                Dealing dealing = new Dealing(currActive, currAmount, ConventString.getTimeValue(etValueTime) * 60, mCurrTimeUnix);
                CheckDealingService.dealings.add(dealing);
                etValueAmount.setText("$0");
                etValueTime.setText("0 MIN");
            } else {
                CustomDialog.showDialogInfo(getActivity(),
                        getResources().getString(R.string.error),
                        getResources().getString(R.string.request_error_text));
            }
        }
    }

    public class CloseDealingBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((BaseActivity) getActivity()).setDealings();
            BaseActivity.getToolbar().setDealingSelectIcon();
            activeClose = intent.getStringExtra(CheckDealingService.ACTIVE);
            amountClose = intent.getStringExtra(CheckDealingService.AMOUNT);
            String closeTime = ConventDate.getConvertDateFromUnix(intent.getLongExtra(CheckDealingService.CLOSE_DATE, -1) * 1000);
            TerminalFragment.this.mCloseTime = intent.getLongExtra(CheckDealingService.CLOSE_DATE, -1) * 1000;
            Log.d(TAG, "active: " + activeClose + ", amount: " + amountClose + ", mCloseTime: " + closeTime);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intentService = new Intent(getActivity(), OrdersService.class);
                    intentService.putExtra(OrdersService.FRAGMENT, OrdersService.FRAGMENT_TERMINAL);
                    getActivity().startService(intentService);
                }
            }, 4000);
        }
    }

    public class GetResponseOrdersBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(OrdersService.RESPONSE);
            if (response != null) {
                if (response.equals("200")) {
                    if (OrderAnswer.getInstance() != null) {
                        List<OrderAnswer> orders = OrderAnswer.getInstance();
                        List<OrderAnswer> closeOrders = DealingFragment.findOrders(orders, DealingFragment.CLOSE_TAB_POSITION);
                        Log.d(TAG, "closeOrders.size(): " + closeOrders.size());
                        Log.d(TAG, "closeOrders: " + closeOrders);
                        for (OrderAnswer closeDealing : closeOrders) {
                            if (closeDealing != null &&
                                    closeDealing.getSymbol().contains(activeClose) &&
                                    closeDealing.getVolumeStr().contains(amountClose) &&
                                    Math.abs(mCloseTime - 3600000 - closeDealing.getCloseTimeUnix()) <= 5000) {
                                showLabelCloseDealing(closeDealing);
                                Log.d(TAG, "closeDealing: " + closeDealing);
                                break;
                            }
                        }
                    }
                }
            } else {
                Log.d(TAG, "response = null");
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
}
