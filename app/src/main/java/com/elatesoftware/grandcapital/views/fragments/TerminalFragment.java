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
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.api.pojo.SocketAnswer;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.services.SymbolHistoryService;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.elatesoftware.grandcapital.views.items.tooltabsview.adapter.OnLoadData;
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
    private static LineDataSet lineDataSet;
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
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_terminal));
        BaseActivity.getToolbar().mTabLayout.setOnLoadData(new OnLoadData() {
            @Override
            public void loadData() {
                BaseActivity.getToolbar().hideTabsByType(ToolbarFragment.TOOLBAR_TERMINALE_FRAGMENT);
                BaseActivity.getToolbar().switchTab(0);
            }
        });
        registrationBroadcasts();
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
        initializationChart();
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
        }else{
            Intent intentMyIntentService = new Intent(getActivity(), InfoUserService.class);
            getActivity().startService(intentMyIntentService);
        }
    }
    private void getSymbolHistory(){
        Intent intentService = new Intent(getActivity(), SymbolHistoryService.class);
        intentService.putExtra(SymbolHistoryService.SYMBOL, tvValueActive.getText().toString());
        getActivity().startService(intentService);
    }
    @Override
    public void onResume() {
        super.onResume();
        setActives();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mSymbolHistoryBroadcastReceiver);
        getActivity().unregisterReceiver(mInfoBroadcastReceiver);
    }

    private void initializationChart() {
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setPinchZoom(true);
        mChart.setScaleYEnabled(false);
        mChart.setScaleXEnabled(true);
        mChart.setPadding(0,0,0,0);
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
        mChart.setDragOffsetX(100f);            /** видимость графика не до конца экрана*/       // TODO norm padding chart in left
        mChart.setScaleMinima(5f, 1f);          /** scale chart*/
        mChart.getLegend().setEnabled(false);   /** Hide the legend */
        /** animation add data in chart*/
        mChart.invalidate();
        mChart.animateX(1500);
    }

    public static void updateChart(SocketAnswer value) {
        /*List<SymbolHistoryAnswer> listPoints = SymbolHistoryAnswer.getInstance();
        listPoints.add(new SymbolHistoryAnswer(value.getHigh(), value.getBid(), value.getAsk(), value.getLow(), value.getTime()));
        SymbolHistoryAnswer.setInstance(listPoints);
        setData(SymbolHistoryAnswer.getInstance());*/
        /*lineDataSet.addEntry(new Entry(value.getTime(), Float.valueOf(String.valueOf(value.getAsk()))));
        //mChart.getData().getDataSetByIndex(0).addEntry(new Entry(value.getTime(), Float.valueOf(String.valueOf(value.getAsk()))));
        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.invalidate();*/
        //******************************
        /*LineData data  = mChart.getData();
        LineDataSet lineDataSet  = (LineDataSet) data.getDataSetByIndex(0);
        if (lineDataSet == null) {
            createLineDataSet(null);
            data.addDataSet(lineDataSet);
        }
        data.addDataSet(lineDataSet);
        data.addEntry(new Entry(value.getTime(), Float.valueOf(String.valueOf(value.getAsk()))), 0);
        mChart.notifyDataSetChanged ();
        mChart.moveViewToX(data.getXMax () - 10);*/
    }
    private static void createLineDataSet(List<Entry> values){
        lineDataSet = new LineDataSet(values, "Base line");    /** set the line*/
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
    }
    private static void setData(List<SymbolHistoryAnswer> list) {
        if(list != null && list.size() > 0){
            values = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                values.add(new Entry(list.get(i).getTime(), Float.valueOf(String.valueOf(list.get(i).getOpen())), 0));
            }
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                lineDataSet = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                lineDataSet.setValues(values);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                createLineDataSet(values);
                /** add the datasets*/
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);
                LineData data = new LineData(dataSets);
                lineDataSet.setHighlightEnabled(false); /** hide Highlight*/
                mChart.setData(data);
            }
        }else{
            mChart.clear();
        }
        /** scroolling in end chart*/
        if( list!= null && list.size() != 0){
            SymbolHistoryAnswer item = list.get(list.size() - 1);
            mChart.centerViewTo(Float.valueOf(item.getTime()), Float.valueOf(String.valueOf(item.getOpen())), YAxis.AxisDependency.RIGHT);
        }
        mChart.invalidate();
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
