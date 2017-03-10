package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
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
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.services.MakeDealingService;
import com.elatesoftware.grandcapital.services.SymbolHistoryService;
import com.elatesoftware.grandcapital.utils.AndroidUtils;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.CustomDialog;
import com.github.mikephil.charting.charts.LineChart;
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

public class TerminalFragment extends Fragment implements OnChartValueSelectedListener{

    private static final String SYMBOL = "EURUSD";
    public static boolean isOpen = false;
    private LineChart mChart;
    private Thread threadSymbolHistory;
    private List<String> listActives = new ArrayList<>();

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

    private GetResponseSymbolHistoryBroadcastReceiver mSymbolHistoryBroadcastReceiver;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;
    private GetResponseMakeDealingBroadcastReceiver mMakeDealingBroadcastReceiver;

    public boolean direction = true;

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
        llDeposit = (LinearLayout) parentView .findViewById(R.id.ll_deposit);
        rlChart = (RelativeLayout) parentView.findViewById(R.id.rl_chart);
        llProgressBar = (LinearLayout) parentView.findViewById(R.id.progress_bar);
        flMain = (FrameLayout) parentView.findViewById(R.id.fl_main);
        setSizeHeight();

        registrationBroadcasts();
        initializationChart();
        return parentView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_terminal));
        BaseActivity.getToolbar().mTabLayout.setOnLoadData(() -> {
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
            BaseActivity.getToolbar().switchTab(1);
        });
        try {
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
            BaseActivity.getToolbar().switchTab(1);
        } catch (Exception ignored){
            ignored.printStackTrace();
        }
        updateBalance();
        KeyboardVisibilityEvent.registerEventListener(getActivity(), isOpen1 -> {
            if(etValueAmount.isFocused()) {
                setMaskAmount(isOpen1);
            }
            if(etValueTime.isFocused()) {
                setMaskTime(isOpen1);
            }
        });
        etValueAmount.setOnFocusChangeListener((v, hasFocus) -> setMaskAmount(hasFocus));
        etValueTime.setOnFocusChangeListener((v, hasFocus) -> setMaskTime(hasFocus));
        tvMinusAmount.setOnClickListener(v -> changeAmountValue(false));
        tvPlusAmount.setOnClickListener(v -> changeAmountValue(true));
        tvPlusTime.setOnClickListener(v -> changeTimeValue(true));
        tvMinusTime.setOnClickListener(v -> changeTimeValue(false));
        tvLeftActive.setOnClickListener(v -> {
            if(!getActive().equals("") && listActives.size() > 0){
                int index = listActives.indexOf(getActive());
                if(index == 0){
                    setSelectedActive(listActives.get(listActives.size() - 1));
                }else{
                    setSelectedActive(listActives.get(index - 1));
                }
            }else{
                setSelectedActive(SYMBOL);
            }
        });
        tvRightActive.setOnClickListener(v -> {
            if(!getActive().equals("") && listActives.size() > 0){
                int index = listActives.indexOf(getActive());
                if(index == (listActives.size() - 1)){
                    setSelectedActive(listActives.get(0));
                }else{
                    setSelectedActive(listActives.get(index  + 1));
                }
            }else{
                setSelectedActive(SYMBOL);
            }
        });
        tvDeposit.setOnClickListener(v -> {
            BaseActivity.changeMainFragment(new DepositFragment());
        });
        llLowerTerminal.setOnClickListener(v -> {
            //CustomDialog.showDialogOpenAccount(getActivity(), null);
            makeDealing("1");

        });
        llHigherTerminal.setOnClickListener(v -> {
            makeDealing("0");
        });
    }
    private void makeDealing(String cmd){
        if(getAmountValue()!= 0 && getTimeValue() != 0 && !getActive().equals("")){
            Intent intentService = new Intent(getActivity(), MakeDealingService.class);
            intentService.putExtra(MakeDealingService.CMD, cmd);
            intentService.putExtra(MakeDealingService.SYMBOL, getActive());
            intentService.putExtra(MakeDealingService.VOLUME, String.valueOf(getAmountValue()));
            intentService.putExtra(MakeDealingService.EXPIRATION, String.valueOf(getTimeValue()));
            getActivity().startService(intentService);
        }else{
            CustomDialog.showDialogInfo(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.no_correct_values));
        }
    }
    private String getActive(){
        return tvValueActive.getText().toString().replace("_OP", "");
    }
    private void newActive(){
        clearChart();
        GrandCapitalApplication.closeSocket();
        getSymbolHistory(getActive());
        tvLeftActive.setEnabled(false);
        tvRightActive.setEnabled(false);
    }
    private void clearChart(){
        mChart.getData().clearValues();
        mChart.getLineData().clearValues();
        mChart.invalidate();
        SymbolHistoryAnswer.nullInstance();
        SocketAnswer.nullInstance();
    }
    private void setMaskAmount(boolean isBol) {
        if(isBol) {
            String str = etValueAmount.getText().toString();
            str = str.replace("$", "");
            etValueAmount.setText(str);
        } else {
            String str = etValueAmount.getText().toString();
            if(!str.contains("$")) {
                etValueAmount.setText("$" + str);
            }
        }
    }
    private void setMaskTime(boolean isBol) {
        if(isBol) {
            String str = etValueTime.getText().toString();
            str = str.replace(" MIN", "");
            etValueTime.setText(str);
        } else {
            String str = etValueTime.getText().toString();
            if(!str.contains(" MIN")) {
                etValueTime.setText(str + " MIN");
            }
        }
    }
    private void changeTimeValue(boolean isAdd) {
        String str = etValueTime.getText().toString();
        str = str.replace(" MIN", "");
        int time = Integer.parseInt(str);
        if(isAdd) {
            time++;
        } else {
            time--;
            if(time < 0) {
                time = 0;
            }
        }
        etValueTime.setText(time + " MIN");
    }
    private void changeAmountValue(boolean isAdd) {
        String str = etValueAmount.getText().toString();
        str = str.replace("$", "");
        int amout = Integer.parseInt(str);
        if(isAdd) {
            amout++;
        } else {
            amout--;
            if(amout < 0) {
                amout = 0;
            }
        }
        etValueAmount.setText("$" + amout);
    }
    private double getAmountValue() {
        String valueStr = etValueAmount.getText().toString();
        valueStr = valueStr.replaceAll("[^0-9.]", "");
        return Double.valueOf(valueStr);
    }
    public void showTopPanel() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, AndroidUtils.dp(direction ? 60 : -60));
        animation.setDuration(200);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                llTopPanel.clearAnimation();
                flMain.removeView(llTopPanel);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        AndroidUtils.dp(60)
                );
                params.topMargin = AndroidUtils.dp(direction ? -60 : 0);
                flMain.addView(llTopPanel, params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        llTopPanel.startAnimation(animation);
        direction = !direction;
    }
    private int getTimeValue() {
        String valueStr = etValueTime.getText().toString();
        valueStr = valueStr.replaceAll("[^0-9]", "");
        return Integer.parseInt(valueStr);
    }
    private void setSizeHeight() {
        int height = AndroidUtils.getWindowsSizeParams(getContext())[1] - AndroidUtils.getStatusBarHeight(getContext()) - AndroidUtils.dp(60);
        rlChart.getLayoutParams().height = (int) (height * 0.6);
        llDeposit.getLayoutParams().height = (int) (height * 0.28);
        llButtons.getLayoutParams().height = (int) (height * 0.11);
    }
    private void updateBalance(){
        if(User.getInstance() != null){
            tvBalance.setText("$" + String.format("%.2f", User.getInstance().getBalance()).replace('.', ','));
        }
    }
    private void registrationBroadcasts(){
        mInfoBroadcastReceiver = new  GetResponseInfoBroadcastReceiver();
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
    }
    private void setActives(){
        if(InfoAnswer.getInstance() != null && InfoAnswer.getInstance().getInstruments() != null &&  InfoAnswer.getInstance().getInstruments().size() > 0){
            listActives.clear();
            for(Instrument instrument: InfoAnswer.getInstance().getInstruments()){
                listActives.add(instrument.getSymbol());
            }
            if(listActives != null && listActives.size() > 0 && listActives.contains(SYMBOL) && !tvValueActive.getText().equals(SYMBOL)){
                setSelectedActive(listActives.get(listActives.indexOf(SYMBOL)));
            }
        }
    }
    private void setSelectedActive(String symbol){
        tvValueActive.setText(symbol);
        newActive();
    }

    private void getSymbolHistory(String symbol){
        Intent intentService = new Intent(getActivity(), SymbolHistoryService.class);
        intentService.putExtra(SymbolHistoryService.SYMBOL, symbol);
        getActivity().startService(intentService);
        llProgressBar.setVisibility(View.VISIBLE);
    }
    private void initializationChart() {
        mChart.setNoDataText(getResources().getString(R.string.request_error_title));
        mChart.setDragDecelerationFrictionCoef(0.95f); // задержка при перетаскивании
        mChart.setPadding(0,0,0,0);
        mChart.setScaleYEnabled(false);
        mChart.setScaleXEnabled(true);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.getDescription().setEnabled(false);// enable description text
        mChart.setTouchEnabled(true);      // enable touch gestures жесты
        mChart.setDragEnabled(true);    // enable scaling and dragging
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);      // if disabled, scaling can be done on x- and y-axis separately
        mChart.setBackgroundColor(Color.TRANSPARENT); // set an alternative background color
        mChart.getLegend().setEnabled(false);   //Hide the legend
        mChart.setDragOffsetX(30f);// TODO norm padding chart in left

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        XAxis xAxis = mChart.getXAxis();  //Ось Х
        xAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        xAxis.setTextColor(getResources().getColor(R.color.chart_values));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setValueFormatter((value, axis) -> ConventDate.convertDateFromMilSecHHMM(ConventDate.genericTimeForChartLabels(value)));
        xAxis.setEnabled(true);
        xAxis.disableAxisLineDashedLine();
        xAxis.setDrawGridLines(true);

        YAxis leftAxis = mChart.getAxisLeft();      //Ось Y left
        leftAxis.setEnabled(false);
        YAxis rightAxis = mChart.getAxisRight();    //Ось Y right
        rightAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        rightAxis.setTextColor(getResources().getColor(R.color.chart_values));
        rightAxis.setEnabled(true);
        rightAxis.setDrawGridLines(true);
        rightAxis.disableAxisLineDashedLine();
        rightAxis.setValueFormatter((value, axis) -> String.format("%.5f", value).replace(',', '.'));
    }
    private synchronized LineDataSet createSet() {
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
        set.setDrawValues(false);
        set.setDrawCircles(false);
        //set.setCircleRadius(2f);
        set.setDrawCircleHole(false);
        set.setDrawFilled(true);
        set.setFillColor(Color.WHITE);    //fill color chart
        set.setFillAlpha(50);
        //set.setHighlightEnabled(false);  //  TODO hide Highlight
        return set;
    }
    public synchronized void addEntry(final SocketAnswer answer) {
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            if(answer.getTime() != null){
                data.addEntry(new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getAsk()))), 0);
                data.notifyDataChanged();
                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        }
    }
    public void addEntry(SymbolHistoryAnswer answer) {
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(ConventDate.genericTimeForChart(answer.getTime()), Float.valueOf(String.valueOf(answer.getOpen()))), 0);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            //mChart.moveViewToX(data.getEntryCount()); // this automatically refreshes the chart (calls invalidate())
        }
    }
    private void setDataSymbolHistory(List<SymbolHistoryAnswer> list, final String symbol) {
        if (threadSymbolHistory != null){
            threadSymbolHistory.interrupt();
        }
        threadSymbolHistory = new Thread(() -> {
            Log.d(GrandCapitalApplication.TAG_SOCKET, "setDataSymbolHistory size = " + list.size());
            for (int i = 0; i < list.size() - 1; i++) {
                int finalI = i;
                getActivity().runOnUiThread(() -> addEntry(list.get(finalI)));
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            GrandCapitalApplication.openSocket(symbol);
            if(mChart.getLineData() != null){
                mChart.zoom(10f, 0f, mChart.getData().getXMax(), 0f, YAxis.AxisDependency.RIGHT);
            }
        });
        threadSymbolHistory.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        isOpen = true;
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(false);
        if(tvValueActive.getText().equals("") || !tvValueActive.getText().equals(SYMBOL)){
            setSelectedActive(SYMBOL);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onPause() Terminal");
        isOpen = false;
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(true);
        if (threadSymbolHistory != null) {
            threadSymbolHistory.interrupt();
        }

    }
    @Override
    public void onDestroy() {
        Log.d(GrandCapitalApplication.TAG_SOCKET, "onDestroy() Terminal");
        GrandCapitalApplication.closeSocket();
        getActivity().unregisterReceiver(mSymbolHistoryBroadcastReceiver);
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
        getActivity().unregisterReceiver(mMakeDealingBroadcastReceiver);
        super.onDestroy();
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
            if(intent.getStringExtra(InfoUserService.RESPONSE_INFO) != null && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY) != null){
                if(intent.getStringExtra(InfoUserService.RESPONSE_INFO).equals("200") && intent.getStringExtra(InfoUserService.RESPONSE_SUMMARY).equals("200")){
                    setActives();
                    updateBalance();
                }
            }
        }
    }
    public class GetResponseSymbolHistoryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String response = intent.getStringExtra(SymbolHistoryService.RESPONSE);
            if (response != null) {
                if (response.equals("200")) {
                    if(SymbolHistoryAnswer.getInstance() != null){
                        setDataSymbolHistory(SymbolHistoryAnswer.getInstance(), getActive());
                    }else{
                        GrandCapitalApplication.openSocket(SYMBOL);
                    }
                }
            }
            else{
                GrandCapitalApplication.openSocket(SYMBOL);
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
            if (response != null) {
                if (response.equals("200")) {

                }
            }
        }
    }
}
