package com.elatesoftware.grandcapital.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.InfoAnswer;
import com.elatesoftware.grandcapital.api.pojo.Instrument;
import com.elatesoftware.grandcapital.api.pojo.SymbolHistoryAnswer;
import com.elatesoftware.grandcapital.models.User;
import com.elatesoftware.grandcapital.services.InfoUserService;
import com.elatesoftware.grandcapital.services.SymbolHistoryService;
import com.elatesoftware.grandcapital.views.activities.BaseActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class TerminalFragment extends Fragment implements OnChartValueSelectedListener {

    private View parentView;
    private LineChart mChart;
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

    public TerminalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        BaseActivity.getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.toolbar_name_terminal));
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
        });/*
        etValueAmount.setOnClickListener(v -> {
            String value = etValueAmount.getText().toString();
            etValueAmount.setText(value.substring(1, value.length()-1));
        });
        etValueAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                CharSequence text;
                if(s.length() > 0){
                    text = s.subSequence(0, s.length()-1);
                    text = "&" + text;
                }else{
                    text = "$0";
                }
                etValueAmount.setText(text);
            }
        });
*/
        /*tvMinusAmount.setOnClickListener(v -> {
            try{
                String value = etValueAmount.getText().toString().replace(',', '.').replace('$', ' ');
                value = value.substring(1, value.length()-1);
                double currentAmount = Double.valueOf(value);
                if(currentAmount > 0){
                    currentAmount = currentAmount --;
                    etValueAmount.setText("$" + String.format("%.2f", currentAmount).replace('.', ','));
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        tvPlusAmount.setOnClickListener(v -> {
            try{
                String value = etValueAmount.getText().toString().replace(',', '.');
                value = value.substring(1, value.length()-1);
                double currentAmount = Double.valueOf(value);
                if(currentAmount >= 0){
                    currentAmount = currentAmount ++;
                    etValueAmount.setText("$" + String.format("%.2f", currentAmount).replace('.', ','));
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        tvMinusTime.setOnClickListener(v -> {
            String valueTime = etValueTime.getText().toString();

        });
        tvPlusTime.setOnClickListener(v -> {

        });
*/
        initializationChart();
        tvDeposit.setOnClickListener(v -> {
            BaseActivity.changeMainFragment(new DepositFragment());
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
        if(InfoAnswer.getInstance() != null &&  InfoAnswer.getInstance().getInstruments() != null &&  InfoAnswer.getInstance().getInstruments().size() > 0){
            listActives.clear();
            for(Instrument instrument: InfoAnswer.getInstance().getInstruments()){
                listActives.add(instrument.getSymbol());
            }
            tvValueActive.setText(listActives.get(0));
            if(tvValueActive.getText().toString() != null && !tvValueActive.getText().toString().equals("")){
                getOrders();
            }
        }
    }

    private void getOrders(){
        Intent intentService = new Intent(getActivity(), SymbolHistoryService.class);
        intentService.putExtra(SymbolHistoryService.SYMBOL, tvValueActive.getText().toString());
        getActivity().startService(intentService);
    }
    public void getInfoUser(){
        Intent intentMyIntentService = new Intent(getActivity(), InfoUserService.class);
        getActivity().startService(intentMyIntentService);
    }
    @Override
    public void onStart() {
        super.onStart();
        getInfoUser();
    }

    @Override
    public void onStop() {
        super.onStop();
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
        /**Ось Y */
        YAxis yAxis = mChart.getAxisRight();
        yAxis.setAxisLineColor(getResources().getColor(R.color.chart_values));
        yAxis.setTextColor(getResources().getColor(R.color.chart_values));
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        mChart.getAxisLeft().setEnabled(false); /** hide left Y*/
        /** animation add data in chart*/
        mChart.animateXY(1500, 2000);
        mChart.setOnTouchListener((v, event) -> {
            return false;        // TODO
        });
        mChart.setDragOffsetX(20f);  /** видимость графика не до конца экрана*/       // TODO
        mChart.getLegend().setEnabled(false);   /** Hide the legend */
        mChart.invalidate();
    }

    private void setData(List<SymbolHistoryAnswer> list) {
        if(list != null && list.size() > 0){
            ArrayList<Entry> values = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                values.add(new Entry(list.get(i).getTime(), Float.valueOf(String.valueOf(list.get(i).getOpen())), getResources().getDrawable(R.drawable.front_elipsa)));
            }
            LineDataSet set1;
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(values, "Base line");    /** set the line*/
                set1.setColor(Color.WHITE);
                set1.setCircleColor(Color.WHITE);
                set1.setLineWidth(1f);
                set1.setDrawValues(false);    /**hide values all points*/
                set1.setDrawCircles(false);   /**hide  all circle points */
                set1.setCircleRadius(3f);
                set1.setDrawCircleHole(false);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(true);
                set1.setFormLineWidth(1f);
                set1.setFormSize(15.f);
                /** fill color chart*/
                set1.setFillColor(Color.WHITE);
                set1.setFillAlpha(50);
                /** add the datasets*/
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                LineData data = new LineData(dataSets);
                set1.setHighlightEnabled(false); /** hide Highlight*/
                mChart.setData(data);
            }
        }else{
            mChart.clear();
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
                    List<SymbolHistoryAnswer> listSymbolHistory = SymbolHistoryAnswer.getInstance();
                    setData(listSymbolHistory);
                }
            }
        }
    }
}
