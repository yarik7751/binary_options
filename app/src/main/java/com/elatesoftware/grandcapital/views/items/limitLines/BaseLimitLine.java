package com.elatesoftware.grandcapital.views.items.limitLines;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.utils.ConventDimens;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.MPPointD;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Дарья Высокович on 14.03.2017.
 */

public class BaseLimitLine extends LimitLine {

    public static final String TAG = "BaseLimitLine_Log";

    static int colorRed;
    static int colorGreen;

    static Bitmap bitmapIconCurrentDealingGreenYLabel;
    static Bitmap bitmapIconCurrentDealingRedYLabel;

    public static Bitmap bitmapLabel;

    static XAxis xAxis;
    static YAxis rightYAxis;
    private static LineChart mChart;

    private int maxWeightCanvasLabel;

    BaseLimitLine (float limit, String label) {
        super(limit, label);
    }

    public static void initialization(){
        if(TerminalFragment.getInstance() != null){
            mChart = TerminalFragment.getInstance().mChart;
            rightYAxis = TerminalFragment.getInstance().rightYAxis;
            xAxis = TerminalFragment.getInstance().xAxis;

            colorGreen = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.chat_green);
            colorRed = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.color_red_chart);

            bitmapIconCurrentDealingGreenYLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.green_hor);
            bitmapIconCurrentDealingRedYLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.red_hor);
            bitmapLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.whitevert);
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
        if(rightYAxis.getLimitLines() != null && rightYAxis.getLimitLines().size() == 1 && rightYAxis.getLimitLines().get(0) instanceof SocketLine){
            return null;
        }
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

    public static void scrollXYLinesDealings(float xMax){
        List<XDealingLine> listX = BaseLimitLine.getXLimitLines();
        List<YDealingLine> listY = BaseLimitLine.getYLimitLines();
        if(listX != null && listX.size() != 0){
            for(XDealingLine lineX : listX){
                if(lineX.getLimit() >= xMax){
                    OrderAnswer order = new Gson().fromJson(lineX.getLabel(), OrderAnswer.class);
                    YDealingLine lineY = new YDealingLine(Float.valueOf(String.valueOf(order.getOpenPrice())),
                            lineX.getLabel(),
                            String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), lineX.ismIsAmerican(), lineX.ismIsActive());
                    if (lineX.ismIsActive()){
                        ActiveDealingLine.deleteDealingLine();
                        ActiveDealingLine.drawActiveDealingLine(lineY, (new Gson().fromJson(lineY.getLabel(), OrderAnswer.class)));
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
                    XDealingLine lineX = new XDealingLine(ConventDate.genericTimeForChart(
                            ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000),
                            new Gson().toJson(order),lineY.getmTimer(), lineY.ismIsAmerican(), lineY.ismIsActive());
                    if(lineY.ismIsActive()){
                        lineX.enableDashedLine(0f, 0f, 0f);
                        ActiveDealingLine.deleteDealingLine();
                        ActiveDealingLine.drawActiveDealingLine(lineX, (new Gson().fromJson(lineX.getLabel(), OrderAnswer.class)));
                    }
                    xAxis.addLimitLine(lineX);
                    rightYAxis.removeLimitLine(lineY);
                }
            }
        }
    }
    public static void activationSelectedDealing(BaseLimitLine line){
        if(line != null){
            if (line instanceof XDealingLine && ((XDealingLine) line).ismIsActive()){
                line.enableDashedLine(10f, 10f, 0f);
                ((XDealingLine) line).setmIsActive(false);
                ActiveDealingLine.deleteDealingLine();
            }else if(line instanceof XDealingLine && !((XDealingLine) line).ismIsActive()){
                List<XDealingLine> listX = BaseLimitLine.getXLimitLines();
                List<YDealingLine> listY = BaseLimitLine.getYLimitLines();
                if(listX != null && listX.size() != 0){
                    for(XDealingLine l: listX){
                        if(l.ismIsActive()){
                            l.enableDashedLine(10f, 10f, 0f);
                            l.setmIsActive(false);
                            ActiveDealingLine.deleteDealingLine();
                            break;
                        }
                    }
                    if(listY != null && listY.size() != 0){
                        for(YDealingLine l: listY){
                            if(l.ismIsActive()){
                                l.setmIsActive(false);
                                ActiveDealingLine.deleteDealingLine();
                                break;
                            }
                        }
                    }
                    line.enableDashedLine(0f, 0f, 0f);
                    ((XDealingLine) line).setmIsActive(true);
                    ActiveDealingLine.drawActiveDealingLine(line, (new Gson().fromJson(line.getLabel(), OrderAnswer.class)));
                }
            }else if(line instanceof YDealingLine && ((YDealingLine) line).ismIsActive()){
                ((YDealingLine) line).setmIsActive(false);
                ActiveDealingLine.deleteDealingLine();
            }else if(line instanceof YDealingLine && !((YDealingLine) line).ismIsActive()){
                List<YDealingLine> listY = BaseLimitLine.getYLimitLines();
                if(listY != null && listY.size() != 0) {
                    List<XDealingLine> listX = BaseLimitLine.getXLimitLines();
                    if(listX != null && listX.size() != 0){
                        for (XDealingLine l : listX) {
                            if (l.ismIsActive()) {
                                l.enableDashedLine(10f, 10f, 0f);
                                l.setmIsActive(false);
                                ActiveDealingLine.deleteDealingLine();
                            }
                        }
                    }
                    for (YDealingLine l : listY) {
                        if (l.ismIsActive()) {
                            l.setmIsActive(false);
                            ActiveDealingLine.deleteDealingLine();
                        }
                    }
                    ((YDealingLine) line).setmIsActive(true);
                    ActiveDealingLine.drawActiveDealingLine(line, (new Gson().fromJson(line.getLabel(), OrderAnswer.class)));
                }
            }
        }else{
            List<XDealingLine> listX = BaseLimitLine.getXLimitLines();
            List<YDealingLine> listY = BaseLimitLine.getYLimitLines();
            if(listX != null && listX.size() != 0){
                listX.get(0).setmIsActive(true);
                ActiveDealingLine.drawActiveDealingLine(listX.get(0), (new Gson().fromJson(listX.get(0).getLabel(), OrderAnswer.class)));
                return;
            }
            if(listY != null && listY.size() != 0){
                listY.get(0).setmIsActive(true);
                ActiveDealingLine.drawActiveDealingLine(listY.get(0), (new Gson().fromJson(listY.get(0).getLabel(), OrderAnswer.class)));
                return;
            }
            ActiveDealingLine.deleteDealingLine();
        }
    }

    public static OrderAnswer onClickLimitLines(float x, float y){
        float xMax = mChart.getHighestVisibleX();
        MPPointD point = mChart.getTransformer(YAxis.AxisDependency.RIGHT).getValuesByTouchPoint(x, y);
        if(point != null){
            DealingLine selectedLine = selectOnClickLine(point, xMax);
            if(selectedLine != null){
                return makeOnClickLabel(selectedLine, point, xMax);
            }
        }
        return null;
    }
    private static DealingLine selectOnClickLine(MPPointD pointClick, float xMax) {
        List<XDealingLine> listLimitX = BaseLimitLine.getXLimitLines();
        List<YDealingLine> listLimitY = BaseLimitLine.getYLimitLines();
        List<DealingLine> listLimits = new ArrayList<>();
        if(listLimitX != null && listLimitX.size() != 0){
            listLimits.addAll(listLimitX);
        }
        if(listLimitY != null && listLimitY.size() != 0){
            listLimits.addAll(listLimitY);
        }
        if(listLimits.size() != 0){
            List<DealingLine> listSelectedLines = new ArrayList<>();
            for(DealingLine line : listLimits){
                OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                float tappedY = Float.valueOf(String.valueOf(order.getOpenPrice()));
                if(line instanceof XDealingLine &&
                        ConventDimens.isClickOnXDealingNoAmerican(line.getLimit(), pointClick.x, tappedY, pointClick.y, line.getMaxWeightCanvasLabel())){
                    listSelectedLines.add(line);
                }else if(line instanceof YDealingLine &&
                        ConventDimens.isClickOnYDealingNoAmerican(pointClick.x, xMax, tappedY, pointClick.y, line.getMaxWeightCanvasLabel())){
                    listSelectedLines.add(line);
                }
            }
            Log.d(TAG, "listSelectedLines.size: " + listSelectedLines.size());
            if(listSelectedLines.size() != 0){
                if(listSelectedLines.size() == 1){
                    return listSelectedLines.get(0);
                }else {
                    for (DealingLine l : listSelectedLines){
                        if (l.ismIsActive()) {
                            return l;
                        }
                    }
                    return listSelectedLines.get(0);
                }
            }
        }
        return null;
    }

    private static OrderAnswer makeOnClickLabel(DealingLine line, MPPointD point, float xMax){
        OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
        if (order != null && order.getOpenPrice() != null) {
            float tappedY = Float.valueOf(String.valueOf(order.getOpenPrice()));
            if (line.ismIsAmerican()
                    && ((line instanceof XDealingLine && ConventDimens.isClickOnXYDealingAmerican(point.x, line.getLimit(), point.y, tappedY, line.getMaxWeightCanvasLabel()))
                    || (line instanceof YDealingLine && ConventDimens.isClickOnXYDealingAmerican(point.x, xMax, tappedY, point.y, line.getMaxWeightCanvasLabel())))) {
                return order;
            }else{
                BaseLimitLine.activationSelectedDealing(line);
                return null;
            }
        }
        return null;
    }
    public static void drawAllDealingsLimitLines(List<OrderAnswer> list, double mCurrentValueY){
        if(getXLimitLines() != null && getXLimitLines().size() != 0){
            xAxis.removeAllLimitLines();
        }
        if(getYLimitLines() != null && getYLimitLines().size() != 0){
            for(YDealingLine line : getYLimitLines()){
                rightYAxis.removeLimitLine(line);
            }
            ActiveDealingLine.deleteDealingLine();
        }
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
            BaseLimitLine.activationSelectedDealing(null);
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
    public static void deleteDealingLimitLine(int ticket) {
        if(getXLimitLines() != null && getXLimitLines().size() != 0){
            for(XDealingLine line : getXLimitLines()){
                OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                if(ticket == order.getTicket()){
                    xAxis.removeLimitLine(line);
                    if(line.ismIsActive()){
                        ActiveDealingLine.deleteDealingLine();
                        if(getXLimitLines() != null && getXLimitLines().size() != 0){
                            BaseLimitLine.activationSelectedDealing(null);
                        }
                    }
                   return;
                }
            }
        }
        if(getYLimitLines() != null && getYLimitLines().size() != 0){
            for(YDealingLine line : getYLimitLines()){
                OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
                if(ticket == order.getTicket()){
                    rightYAxis.removeLimitLine(line);
                    if(line.ismIsActive()){
                        ActiveDealingLine.deleteDealingLine();
                        if(getYLimitLines() != null && getYLimitLines().size() != 0){
                            BaseLimitLine.activationSelectedDealing(null);
                        }
                    }
                    return;
                }
            }
        }
    }
    public synchronized static void addTicketsInLines(List<OrderAnswer> listOrders){
        List<XDealingLine> listLimitX = BaseLimitLine.getXLimitLines();
        List<YDealingLine> listLimitY = BaseLimitLine.getYLimitLines();
        List<DealingLine> listLines = new ArrayList<>();
        if(listLimitX != null && listLimitX.size() != 0){
            listLines.addAll(listLimitX);
        }
        if(listLimitY != null && listLimitY.size() != 0){
            listLines.addAll(listLimitY);
        }
        if(listLines.size() != 0){
            Collections.sort(listLines,(p1, p2) -> (new Gson().fromJson(p1.getLabel(), OrderAnswer.class).getOpenTime().compareTo(
                                                   (new Gson().fromJson(p2.getLabel(), OrderAnswer.class).getOpenTime()))));
            Collections.sort(listOrders,(o1, o2) -> o1.getOpenTime().compareTo(o2.getOpenTime()));
            //TODO xz....bug was fixed or not? Error: java.lang.IndexOutOfBoundsException: Invalid index 1, size is 1
           if(listLines.size() > 0 && listOrders.size() > 0 && listLines.size() > listOrders.size()){
                if(listOrders.size() > 1){
                    listLines = listLines.subList(0, listOrders.size()-1);
                }else if(listOrders.size() == 1){
                    listLines = listLines.subList(0, 1);
                }
            }
            if(listOrders.size() == listLines.size() && listLines.size() > 0 && listOrders.size() > 0 && listLines.size() >= listOrders.size()){
                for(int i = 0; i < listLines.size(); i++){
                    OrderAnswer orderAnswer = new Gson().fromJson(listLines.get(i).getLabel(), OrderAnswer.class);
                    orderAnswer.setTicket(listOrders.get(i).getTicket());
                    listLines.get(i).setLabel(new Gson().toJson(orderAnswer));
                }
            }
        }
    }

    public int getMaxWeightCanvasLabel() {
        return maxWeightCanvasLabel;
    }
    public void setMaxWeightCanvasLabel(int maxWeightCanvasLabel) {
        this.maxWeightCanvasLabel = maxWeightCanvasLabel;
    }
}
