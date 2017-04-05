package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.ConventDimens;
import com.elatesoftware.grandcapital.utils.ConventImage;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.MPPointD;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Дарья Высокович on 14.03.2017.
 */

public class BaseLimitLine extends LimitLine {

    static Bitmap bitmapIconRedYLabel;
    static Bitmap bitmapIconGreenYLabel;
    static int colorRed;
    static int colorGreen;
    static Bitmap bitmapIconGreenXLabel;
    static Bitmap bitmapIconRedXLabel;
    static Bitmap bitmapIconCurrentDealingGreenYLabel;
    static Bitmap bitmapIconCurrentDealingRedYLabel;

    static XAxis xAxis;
    static YAxis rightYAxis;
    static LineChart mChart;

    static {
        initialization();
    }

    BaseLimitLine (float limit, String label) {
        super(limit, label);
        colorGreen = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.chat_green);
        colorRed = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.color_red_chart);
        bitmapIconGreenXLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.green_vert);
        bitmapIconRedXLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.red_vert);
        bitmapIconRedYLabel = ConventImage.loadBitmapFromView(LayoutInflater.from(GrandCapitalApplication.getAppContext()).inflate(R.layout.incl_chart_label_red, null));
        bitmapIconGreenYLabel = ConventImage.loadBitmapFromView(LayoutInflater.from(GrandCapitalApplication.getAppContext()).inflate(R.layout.incl_chart_label_green, null));
        bitmapIconCurrentDealingGreenYLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.green_hor);
        bitmapIconCurrentDealingRedYLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.red_hor);
    }
    private static void initialization(){
        if(TerminalFragment.getInstance() != null){
            mChart = TerminalFragment.getInstance().mChart;
            rightYAxis = TerminalFragment.getInstance().rightYAxis;
            xAxis = TerminalFragment.getInstance().xAxis;
        }
    }

    public static List<XDealingLine> getXLimitLines() {
        List<XDealingLine> list = new ArrayList<>();
        if(xAxis.getLimitLines() != null && xAxis.getLimitLines().size() != 0){
            for(LimitLine l: xAxis.getLimitLines()){
                list.add((XDealingLine) l);
            }
            return list;
        }else{
            return null;
        }
    }
    public static List<YDealingLine> getYLimitLines(){
        List<YDealingLine> list = new ArrayList<>();
        if(rightYAxis.getLimitLines() != null && rightYAxis.getLimitLines().size() != 0){
            for(LimitLine l: rightYAxis.getLimitLines()){
                if(l instanceof YDealingLine){
                    list.add((YDealingLine) l);
                }
            }
            return list;
        }else{
            return null;
        }
    }

    public static void redrawScrollXYLinesDealings(float xMax){
        List<XDealingLine> listX = BaseLimitLine.getXLimitLines();
        List<YDealingLine> listY = BaseLimitLine.getYLimitLines();
        if(listX != null && listX.size() != 0){
            for(XDealingLine lineX : listX){
                if(lineX.getLimit() >= xMax){
                    OrderAnswer order = new Gson().fromJson(lineX.getLabel(), OrderAnswer.class);
                    YDealingLine lineY = new YDealingLine(Float.valueOf(String.valueOf(order.getOpenPrice())),
                            lineX.getLabel(), lineX.getmBitmapLabelY(),
                            String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), lineX.ismIsAmerican(), false);
                    if (lineX.ismIsActive()){
                        lineY.setmIsActive(true);
                        makeActiveSelectedDealing(lineY);
                    }
                    rightYAxis.addLimitLine(lineY);
                    xAxis.removeLimitLine(lineX);
                }
            }
        }
        if(listY != null && listY.size() != 0) {
            for (YDealingLine lineY : listY) {
                OrderAnswer order = new Gson().fromJson(lineY.getLabel(), OrderAnswer.class);
                if (ConventDate.genericTimeForChart(ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000) < xMax) {
                    XDealingLine line = new XDealingLine(ConventDate.genericTimeForChart(
                            ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000),
                            new Gson().toJson(order), null, lineY.getmBitmapLabelY(), lineY.getmTimer(), lineY.ismIsAmerican(), false);
                    if(lineY.ismIsActive()){
                        line.setmIsActive(true);
                        makeActiveSelectedDealing(line);
                    }
                    xAxis.addLimitLine(line);
                    rightYAxis.removeLimitLine(lineY);
                }
            }
        }
    }
    public static void makeActiveSelectedDealing(BaseLimitLine line){
        if(line != null){
            if (line instanceof XDealingLine && ((XDealingLine) line).ismIsActive()){
                line.enableDashedLine(10f, 10f, 0f);
                ((XDealingLine) line).setmIsActive(false);
                DealingLine.deleteDealingLine();
            }else if(line instanceof XDealingLine && !((XDealingLine) line).ismIsActive()){
                List<XDealingLine> listX = BaseLimitLine.getXLimitLines();
                List<YDealingLine> listY = BaseLimitLine.getYLimitLines();
                if(listX != null && listX.size() != 0){
                    for(XDealingLine l: listX){
                        if(l.ismIsActive()){
                            l.enableDashedLine(10f, 10f, 0f);
                            l.setmIsActive(false);
                            DealingLine.deleteDealingLine();
                            break;
                        }
                    }
                    if(listY != null && listY.size() != 0){
                        for(YDealingLine l: listY){
                            if(l.ismIsActive()){
                                l.setmIsActive(false);
                                DealingLine.deleteDealingLine();
                                break;
                            }
                        }
                    }
                    line.enableDashedLine(0f, 0f, 0f);
                    ((XDealingLine) line).setmIsActive(true);
                    DealingLine.drawActiveDealingLine(line, (new Gson().fromJson(((XDealingLine) line).getLabel(), OrderAnswer.class)));
                }
            }else if(line instanceof YDealingLine && ((YDealingLine) line).ismIsActive()){
                ((YDealingLine) line).setmIsActive(false);
                DealingLine.deleteDealingLine();
            }else if(line instanceof YDealingLine && !((YDealingLine) line).ismIsActive()){
                List<YDealingLine> listY = BaseLimitLine.getYLimitLines();
                if(listY != null && listY.size() != 0) {
                    List<XDealingLine> listX = BaseLimitLine.getXLimitLines();
                    if(listX != null && listX.size() != 0){
                        for (XDealingLine l : listX) {
                            if (l.ismIsActive()) {
                                l.enableDashedLine(10f, 10f, 0f);
                                l.setmIsActive(false);
                                DealingLine.deleteDealingLine();
                            }
                        }
                    }
                    for (YDealingLine l : listY) {
                        if (l.ismIsActive()) {
                            l.setmIsActive(false);
                            DealingLine.deleteDealingLine();
                        }
                    }
                    ((YDealingLine) line).setmIsActive(true);
                    DealingLine.drawActiveDealingLine(line, (new Gson().fromJson(((YDealingLine) line).getLabel(), OrderAnswer.class)));
                }
            }
        }else{
            List<XDealingLine> listX = BaseLimitLine.getXLimitLines();
            List<YDealingLine> listY = BaseLimitLine.getYLimitLines();
            if(listX != null && listX.size() != 0){
                listX.get(0).setmIsActive(true);
                DealingLine.drawActiveDealingLine(listX.get(0), (new Gson().fromJson(listX.get(0).getLabel(), OrderAnswer.class)));
                return;
            }
            if(listY != null && listY.size() != 0){
                listY.get(0).setmIsActive(true);
                DealingLine.drawActiveDealingLine(listY.get(0), (new Gson().fromJson(listY.get(0).getLabel(), OrderAnswer.class)));
            }
            DealingLine.deleteDealingLine();
        }
    }

    public static OrderAnswer onClickXLimitLines(float x, float y){
        List<XDealingLine> listLimit = BaseLimitLine.getXLimitLines();
        if(listLimit != null && listLimit.size() != 0){
            MPPointD point = mChart.getTransformer(YAxis.AxisDependency.RIGHT).getValuesByTouchPoint(x, y);
            if(point != null){
                for(XDealingLine line: listLimit) {
                    OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                    if (order != null && order.getOpenPrice() != null) {
                        float tappedY = Float.valueOf(String.valueOf(order.getOpenPrice()));
                        if (line.ismIsAmerican() && ConventDimens.isClickOnXYDealingAmerican(point.x, line.getLimit(), point.y, tappedY)) {
                            return order;
                        }else if (ConventDimens.isClickOnXDealingNoAmerican(line.getLimit(), point.x, tappedY, point.y)) {
                            BaseLimitLine.makeActiveSelectedDealing(line);
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }
    public static OrderAnswer onClickYLimitLines(float x, float y){
        float xMax = mChart.getHighestVisibleX();
        List<YDealingLine> listLimit = BaseLimitLine.getYLimitLines();
        if(listLimit != null && listLimit.size() != 0){
            MPPointD point = mChart.getTransformer(YAxis.AxisDependency.RIGHT).getValuesByTouchPoint(x, y);
            if(point != null){
                for(YDealingLine line: listLimit){
                    OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                    if (order != null && order.getOpenPrice() != null) {
                        float tappedY = Float.valueOf(String.valueOf(order.getOpenPrice()));
                        if (line.ismIsAmerican() && ConventDimens.isClickOnXYDealingAmerican(point.x, xMax, tappedY, point.y)) {
                            return order;
                        }else if (ConventDimens.isClickOnYDealingNoAmerican(point.x, xMax, tappedY, point.y)) {
                            BaseLimitLine.makeActiveSelectedDealing(line);
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void drawAllDealingsLimitLines(List<OrderAnswer> list, double mCurrentValueY){
        xAxis.removeAllLimitLines();
        rightYAxis.removeAllLimitLines();
        if(list != null && list.size() != 0){
            for(OrderAnswer orderAnswer : list){
                if(ConventDate.validationDateTimer(orderAnswer.getOptionsData().getExpirationTime())) {
                    if(GrandCapitalApplication.isTypeOptionAmerican && ConventDate.getDifferenceDate(orderAnswer.getOpenTime()) >= 61){
                        drawDealingLimitLine(orderAnswer, true, mCurrentValueY);
                    }else{
                        drawDealingLimitLine(orderAnswer, false, mCurrentValueY);
                    }
                }
            }
            BaseLimitLine.makeActiveSelectedDealing(null);
        }
    }
    public static void drawDealingLimitLine(OrderAnswer order, boolean isAmerican, double mCurrentValueY) {
        if (order != null) {
            if(ConventDate.genericTimeForChart(ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000) >= mChart.getHighestVisibleX()){
                YDealingLine.createYDealingLine(order, mCurrentValueY, isAmerican);
            }else{
                XDealingLine.createXDealingLine(order, mCurrentValueY, isAmerican);
            }
        }
    }
}
