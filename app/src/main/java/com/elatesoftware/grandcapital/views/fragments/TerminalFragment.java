package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.elatesoftware.grandcapital.api.pojo.SummaryAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
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

import java.util.ArrayList;
import java.util.List;

public class TerminalFragment extends Fragment implements OnChartValueSelectedListener{

    private View parentView;

    private static LineChart mChart;
    private static ArrayList<Entry> values;

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

    String temp = "{\"symbol\":\"EURUSD_OP\",\"bid\":1.05379,\"ask\":1.05379,\"digits\":5,\"count\":201,\"point\":0.00001,\"high\":1.05889,\"low\":1.05260,\"time\":1488372836}";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_terminal, container, false);
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
        try {
            BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
            BaseActivity.getToolbar().switchTab(1);
        } catch (Exception e){}
        if(User.getInstance() != null){
            tvBalance.setText("$" + String.format("%.2f", User.getInstance().getBalance()).replace('.', ','));
        }
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
                updateChart(SocketAnswer.getSetInstance(temp));
            }
        });
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mSymbolHistoryBroadcastReceiver);
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
    }
    private void initializationChart() {
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false );
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setPinchZoom(true);
        mChart.setScaleYEnabled(false);
        mChart.setScaleXEnabled(true);
        mChart.setPadding(0,0,0,0);
        mChart.setDrawingCacheEnabled(true);
        mChart.setWillNotCacheDrawing(false);
        mChart.buildDrawingCache(true);
        /** create marker*/
        /*MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.item_marker);
        mv.setChartView(mChart);
        mChart.setMarker(mv);*/
        /** padding limit zone*/
        /*LimitLine llXAxis = new LimitLine(0f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);
        llXAxis.setLineColor(Color.WHITE);*/
        /**Ось Х */
        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        xAxis.setTextColor(getResources().getColor(R.color.chart_values));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter((value, axis) -> ConventDate.convertDateFromMilSecHHMM((((Float)value).longValue())));

        /**Ось Y */
        YAxis yAxis = mChart.getAxisRight();
        yAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        yAxis.setTextColor(getResources().getColor(R.color.chart_values));
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setValueFormatter((value, axis) -> String.format("%.5f", value).replace(',', '.'));
        mChart.getAxisLeft().setEnabled(false); /** hide left Y*/
        /*mChart.setOnTouchListener((v, event) -> {
            return false;        // TODO norm scroll
        });*/
        //mChart.setDragOffsetX(100f);            /** видимость графика не до конца экрана*/       // TODO norm padding chart in left
        mChart.setScaleMinima(3f, 1f);          /** scale chart*/
        mChart.getLegend().setEnabled(false);   /** Hide the legend */
        mChart.invalidate();
        /** animation add data in chart*/
        //mChart.animateXY(2500, 2500);
    }
    public void updateChart(SocketAnswer value) {
        LineData data = mChart.getData();
        if (data != null) {
            ILineDataSet xData = data.getDataSetByIndex(0);
            if (xData == null) {
                xData = createLineDataSet(values);
                data.addDataSet(xData);
            }
            data.addEntry(new Entry(value.getTime() * 1000, Float.valueOf(String.valueOf(value.getAsk()))), 0);
            mChart.setData(data);
            mChart.notifyDataSetChanged();
            mChart.centerViewTo((float) (value.getTime() * 1000), Float.valueOf(String.valueOf(value.getAsk())), YAxis.AxisDependency.RIGHT);
        }
    }

    private static LineDataSet createLineDataSet(List<Entry> values){
        LineDataSet lineDataSet = new LineDataSet(values, "Base line");    /** set the line*/
        lineDataSet.setLineWidth(1f);
        lineDataSet.setDrawValues(false);    /**hide values all points*/
        lineDataSet.setDrawCircles(false);   /**hide  all circle points */
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(9f);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        lineDataSet.setColor(Color.WHITE);          /** color line*/
        lineDataSet.setCircleColor(Color.WHITE);    /** color circles*/
        /** fill color chart*/
        lineDataSet.setFillColor(Color.WHITE);
        lineDataSet.setFillAlpha(50);
        lineDataSet.setHighlightEnabled(false);/** hide Highlight*/
        return lineDataSet;
    }
    private static void setData(List<SymbolHistoryAnswer> list) {
        if(list != null && list.size() > 0){
            values = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                values.add(new Entry(list.get(i).getTime(), Float.valueOf(String.valueOf(list.get(i).getOpen())), 0));
            }
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                LineDataSet lineDataSet = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                lineDataSet.setValues(values);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                LineDataSet lineDataSet = createLineDataSet(values);
                /** add the datasets*/
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);
                LineData data = new LineData(dataSets);
                mChart.setData(data);
                mChart.notifyDataSetChanged();
            }
            /** scroolling in end chart*/
            SymbolHistoryAnswer item = list.get(list.size() - 1);
            mChart.centerViewTo(Float.valueOf(item.getTime()), Float.valueOf(String.valueOf(item.getOpen())), YAxis.AxisDependency.RIGHT);
        }else{
            mChart.clear();
            mChart.notifyDataSetChanged();
        }
    }
    @Override
    protected void finalize() throws Throwable {
        ArrayList<Entry> newList = (ArrayList<Entry>) values.subList(values.size() - 30, values.size() - 1);
        values.clear();
        values = newList;
        mChart.destroyDrawingCache();
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
                        setData(SymbolHistoryAnswer.getInstance());
                        GrandCapitalApplication.openSocket();
                    }
                }
            }
        }
    }
}
