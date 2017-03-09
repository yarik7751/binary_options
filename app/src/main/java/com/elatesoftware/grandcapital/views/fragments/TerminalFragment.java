package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    private static final String TAG = "TerminalFragment_Logs";
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
    private RelativeLayout rlChart;

    private GetResponseSymbolHistoryBroadcastReceiver mSymbolHistoryBroadcastReceiver;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;
    private GetResponseMakeDealingBroadcastReceiver mMakeDealingBroadcastReceiver;

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

        llButtons = (LinearLayout) parentView.findViewById(R.id.ll_buttons);
        llDeposit = (LinearLayout) parentView .findViewById(R.id.ll_deposit);
        rlChart = (RelativeLayout) parentView.findViewById(R.id.rl_chart);
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
        } catch (Exception e){}
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
        tvValueActive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(InfoAnswer.getInstance() != null){
                    GrandCapitalApplication.closeSocket();
                    clearChart();
                    getSymbolHistory(getActive());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tvLeftActive.setOnClickListener(v -> {
            if(!getActive().equals("") && listActives.size() > 0){
                int index = listActives.indexOf(getActive());
                if(index == 0){
                    tvValueActive.setText(listActives.get(listActives.size() - 1));
                }else{
                    tvValueActive.setText(listActives.get(index - 1));
                }
            }
        });
        tvRightActive.setOnClickListener(v -> {
            if(!getActive().equals("") && listActives.size() > 0){
                int index = listActives.indexOf(getActive());
                if(index == (listActives.size() - 1)){
                    tvValueActive.setText(listActives.get(0));
                }else{
                    tvValueActive.setText(listActives.get(index  + 1));
                }
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
    private void clearChart(){
        mChart.getData().clearValues();
        mChart.getLineData().clearValues();
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
            if(listActives != null && listActives.size() > 0 ){
                tvValueActive.setText(listActives.get(listActives.indexOf("EURUSD")));
            }
        }
    }
    private void getSymbolHistory(String symbol){
        Intent intentService = new Intent(getActivity(), SymbolHistoryService.class);
        intentService.putExtra(SymbolHistoryService.SYMBOL, symbol);
        getActivity().startService(intentService);
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
        set.setDrawValues(false);  // TODO hide values all points
        set.setDrawCircles(true);   // TODO hide  all circle points
        set.setCircleRadius(2f); // TODO hide
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
               // mChart.invalidate();
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
            for (int i = 0; i < list.size() - 1; i++) {
                int finalI = i;
                getActivity().runOnUiThread(() -> addEntry(list.get(finalI)));
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(mChart.getLineData() != null){
                mChart.zoom(10f, 0f, mChart.getData().getXMax(), 0f, YAxis.AxisDependency.RIGHT);
            }
            GrandCapitalApplication.openSocket(symbol);
        });
        threadSymbolHistory.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        isOpen = true;
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(false);
        Log.d(TAG, "isScrolling: " + ((BaseActivity) getActivity()).mResideMenu.isScrolling());
        Log.d(TAG, "TerminalFragment.isOpen: " + TerminalFragment.isOpen);
    }
    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        isOpen = false;
        ((BaseActivity) getActivity()).mResideMenu.setScrolling(true);
        if (threadSymbolHistory != null) {
            threadSymbolHistory.interrupt();
        }
        super.onPause();
    }
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
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
                        setDataSymbolHistory(SymbolHistoryAnswer.getInstance(), intent.getStringExtra(SymbolHistoryService.SYMBOL));
                    }else{
                        GrandCapitalApplication.openSocket(intent.getStringExtra(SymbolHistoryService.SYMBOL));
                    }
                }
            }
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
