package com.elatesoftware.grandcapital.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.GrandCapitalApi;
import com.elatesoftware.grandcapital.api.pojo.Order;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TerminalFragment extends Fragment implements OnChartValueSelectedListener {

    private View parentView;
    private LineChart mChart;

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
        mChart.setDrawGridBackground(false);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseActivity.getToolbar().switchTab(BaseActivity.TERMINAL_POSITION);
        BaseActivity.getToolbar().setPageTitle(getResources().getString(R.string.menu_item_terminal));

        Call<ResponseBody> ordersCall = GrandCapitalApi.getSymbolHistory();
        ordersCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        initializationChart();
    }

    private void initializationChart(){
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setPinchZoom(true);
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
        setData(15, 20);    /** add data*/
        /** animation add data in chart*/
        mChart.animateXY(1500, 1500);
        mChart.setDragOffsetX(20f);  /** видимость графика не до конца экрана*/       // TODO
        mChart.getLegend().setEnabled(false);   /** Hide the legend */
        mChart.invalidate();
    }

    private void setData(int count, float range) {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.front_elipsa)));
        }
        LineDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
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
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
