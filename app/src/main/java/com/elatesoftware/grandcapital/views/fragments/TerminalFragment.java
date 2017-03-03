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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.QueueSocketAnswer;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.services.SymbolHistoryService;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
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

import java.util.ArrayList;
import java.util.List;

public class TerminalFragment extends Fragment implements OnChartValueSelectedListener{

    private LineChart mChart;
    private Thread threadSymbolHistory;
    private Thread threadSocket;
    //public QueueSocketAnswer queueSocketAnswer = new QueueSocketAnswer();

    private TextView tvBalance;
    private TextView tvDeposit;
    private TextView tvLowerActive;
    private TextView tvUpperActive;
    private TextView tvValueActive;
    private TextView tvMinusAmount;
    private TextView tvPlusAmount;
    private EditText etValueAmount;
    private TextView tvMinusTime;
    private TextView tvPlusTime;
    private EditText etValueTime;
    private LinearLayout llLowerTerminal;
    private LinearLayout llHigherTerminal;
    private List<String> listActives = new ArrayList<>();
    private GetResponseSymbolHistoryBroadcastReceiver mSymbolHistoryBroadcastReceiver;
    private GetResponseInfoBroadcastReceiver mInfoBroadcastReceiver;

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
        mChart = (LineChart) parentView.findViewById(R.id.chart);
        tvBalance = (TextView) parentView.findViewById(R.id.tvBalanceTerminal);
        tvDeposit = (TextView) parentView.findViewById(R.id.tvDepositTerminal);

        tvLowerActive = (TextView) parentView.findViewById(R.id.tvLowerTabActiveTerminal);
        tvUpperActive = (TextView) parentView.findViewById(R.id.tvUpperTabActiveTerminal);
        tvValueActive = (TextView) parentView.findViewById(R.id.tvValueTabActiveTerminal);

        tvMinusAmount = (TextView) parentView.findViewById(R.id.tvMinusTabAmountTerminal);
        tvPlusAmount = (TextView) parentView.findViewById(R.id.tvPlusTabAmountTerminal);
        etValueAmount = (EditText) parentView.findViewById(R.id.tvValueTabAmountTerminal);

        tvMinusTime = (TextView) parentView.findViewById(R.id.tvMinusTabTimeTerminal);
        tvPlusTime = (TextView) parentView.findViewById(R.id.tvPlusTabTimeTerminal);
        etValueTime = (EditText) parentView.findViewById(R.id.tvValueTabTimeTerminal);

        llLowerTerminal = (LinearLayout) parentView.findViewById(R.id.llLowerTerminal);
        llHigherTerminal = (LinearLayout) parentView.findViewById(R.id.llHigherTerminal);

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
        updateBalance();
        tvLowerActive.setOnClickListener(v -> {
            if(listActives.size() > 0){
                int index = listActives.indexOf(tvValueActive.getText().toString());
                if(index == 0){
                    tvValueActive.setText(listActives.get(listActives.size() -1));
                }else{
                    tvValueActive.setText(listActives.get(index - 1));
                }
            }
        });
        tvUpperActive.setOnClickListener(v -> {
            if(listActives.size() > 0){
                int index = listActives.indexOf(tvValueActive.getText().toString());
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
        llLowerTerminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
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
    }
    private void setActives(){
        if(InfoAnswer.getInstance() != null && InfoAnswer.getInstance().getInstruments() != null &&  InfoAnswer.getInstance().getInstruments().size() > 0){
            listActives.clear();
            for(Instrument instrument: InfoAnswer.getInstance().getInstruments()){
                listActives.add(instrument.getSymbol());
            }
            tvValueActive.setText(listActives.get(0));
            if(!tvValueActive.getText().toString().equals("")){
                getSymbolHistory();
            }
        }
    }
    private void getSymbolHistory(){
        Intent intentService = new Intent(getActivity(), SymbolHistoryService.class);
        //intentService.putExtra(SymbolHistoryService.SYMBOL, tvValueActive.getText().toString()); // TODO
        intentService.putExtra(SymbolHistoryService.SYMBOL, "EURUSD");
        getActivity().startService(intentService);
    }
    private void initializationChart() {
        mChart.setPadding(0,0,0,0);
        mChart.setScaleYEnabled(false);
        mChart.setScaleXEnabled(true);
        mChart.setDoubleTapToZoomEnabled(false);
        // enable description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures жесты
         mChart.setTouchEnabled(true);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        // set an alternative background color
        mChart.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);  // add empty data
        // get the legend (only possible after setting data)
        mChart.getLegend().setEnabled(false);   //Hide the legend

        //Ось Х
        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        xAxis.setTextColor(getResources().getColor(R.color.chart_values));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setValueFormatter((value, axis) -> ConventDate.convertDateFromMilSecHHMM((((Float)value).longValue() * 10000)));
        xAxis.setEnabled(true);
        //Ось Y left
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(false);
        //Ось Y right
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        rightAxis.setTextColor(getResources().getColor(R.color.chart_values));
        rightAxis.setEnabled(true);
        rightAxis.setDrawGridLines(true);
        rightAxis.setValueFormatter((value, axis) -> String.format("%.5f", value).replace(',', '.'));
        //mChart.setDragOffsetX(-10f);            // видимость графика не до конца экрана       // TODO norm padding chart in left
    }
    private LineDataSet createSet() {
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
        set.setDrawValues(false);  //hide values all points
        set.setDrawCircles(true);   //hide  all circle points
        set.setCircleRadius(2f); // TODO hide
        set.setDrawCircleHole(false);
        set.setDrawFilled(true);
        //fill color chart
        set.setFillColor(Color.WHITE);
        set.setFillAlpha(50);
        //set.setHighlightEnabled(false);  //hide Highlight
        return set;
    }
    public void addEntry(SocketAnswer answer) {
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(Float.valueOf(String.valueOf(answer.getTime() / 10000)), Float.valueOf(String.valueOf(answer.getAsk()))), 0);
            Log.d(GrandCapitalApplication.TAG_SOCKET, "in set = " + String.valueOf(set.getEntryCount()));
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            //mChart.setVisibleXRangeMinimum(5f);
            //mChart.setVisibleXRangeMaximum(15f);
            mChart.invalidate();
            Log.d(GrandCapitalApplication.TAG_SOCKET, "chart was invalidate");
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
            data.addEntry(new Entry(Float.valueOf(String.valueOf(answer.getTime() / 10000)), Float.valueOf(String.valueOf(answer.getOpen()))), 0);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            //mChart.setVisibleXRangeMinimum(5f);
            //mChart.setVisibleXRangeMaximum(15f);
            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount()); // this automatically refreshes the chart (calls invalidate())
        }
    }
/*
    public void setDataSocket() {
        if (threadSocket != null){
            threadSocket.interrupt();
        }
        threadSocket = new Thread(() -> {
            while(queueSocketAnswer.size() != 0){
                getActivity().runOnUiThread(() -> addEntry(queueSocketAnswer.pull()));
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        threadSocket.start();
    }*/
    private  void setDataSymbolHistory(List<SymbolHistoryAnswer> list) {
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
            GrandCapitalApplication.openSocket();
        });
        threadSymbolHistory.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (threadSymbolHistory != null) {
            threadSymbolHistory.interrupt();
        }
        if (threadSocket != null) {
            threadSocket.interrupt();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mSymbolHistoryBroadcastReceiver);
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
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
                    tvBalance.setText("$" + String.format("%.2f", User.getInstance().getBalance()).replace('.', ','));
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
                        setDataSymbolHistory(SymbolHistoryAnswer.getInstance());
                    }
                }
            }
        }
    }
}
