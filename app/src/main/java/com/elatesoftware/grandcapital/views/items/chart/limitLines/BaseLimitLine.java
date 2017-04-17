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
    public static Bitmap iconClose;
    public static Bitmap iconCMDDown;
    public static Bitmap iconCMDUp;
    public static Bitmap bitmapLabel;

    static XAxis xAxis;
    static YAxis rightYAxis;
    private static LineChart mChart;

    private int maxWeightCanvasLabel;

    static {
        initialization();
    }

    BaseLimitLine (float limit, String label) {
        super(limit, label);
    }

    private static void initialization(){
        if(TerminalFragment.getInstance() != null){
            mChart = TerminalFragment.getInstance().mChart;
            rightYAxis = TerminalFragment.getInstance().rightYAxis;
            xAxis = TerminalFragment.getInstance().xAxis;

            colorGreen = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.chat_green);
            colorRed = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.color_red_chart);
            bitmapIconGreenXLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.green_vert);
            bitmapIconRedXLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.red_vert);
            bitmapIconRedYLabel = ConventImage.loadBitmapFromView(LayoutInflater.from(GrandCapitalApplication.getAppContext()).inflate(R.layout.incl_chart_label_red, null));
            bitmapIconGreenYLabel = ConventImage.loadBitmapFromView(LayoutInflater.from(GrandCapitalApplication.getAppContext()).inflate(R.layout.incl_chart_label_green, null));
            bitmapIconCurrentDealingGreenYLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.green_hor);
            bitmapIconCurrentDealingRedYLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.red_hor);
            iconClose = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.close_button);
            iconCMDDown = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.down);
            iconCMDUp = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.up);
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
                            lineX.getLabel(), lineX.getmBitmapLabelY(),
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
                            new Gson().toJson(order), null, lineY.getmBitmapLabelY(), lineY.getmTimer(), lineY.ismIsAmerican(), lineY.ismIsActive());
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
    private static DealingLine selectOnClickLine(MPPointD pointClick, float xMax){
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
                if(line instanceof XDealingLine && ConventDimens.isClickOnXDealingNoAmerican(line.getLimit(), pointClick.x, tappedY, pointClick.y, line.getMaxWeightCanvasLabel())){
                    listSelectedLines.add(line);
                }else if(ConventDimens.isClickOnYDealingNoAmerican(pointClick.x, xMax, tappedY, pointClick.y, line.getMaxWeightCanvasLabel())){
                    listSelectedLines.add(line);
                }
            }
            if(listSelectedLines.size() != 0){
                if(listSelectedLines.size() == 1){
                    return listSelectedLines.get(0);
                }else{
                    DealingLine activeDealingLine = null;
                    for(DealingLine l : listSelectedLines){
                        if(l.ismIsActive()){
                            activeDealingLine = l;
                            break;
                        }
                    }
                    if(activeDealingLine != null){
                        return activeDealingLine;
                    }else{
                        return listSelectedLines.get(0);
                    }
                }
            }
        }
        return null;
    }

    private static OrderAnswer makeOnClickLabel(DealingLine line, MPPointD point, float xMax){
        OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
        if (order != null && order.getOpenPrice() != null) {
            float tappedY = Float.valueOf(String.valueOf(order.getOpenPrice()));
            if (line.ismIsAmerican() && ((line instanceof XDealingLine && ConventDimens.isClickOnXYDealingAmerican(point.x, line.getLimit(), point.y, tappedY, line.getMaxWeightCanvasLabel()))
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
                    break;
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
                    break;
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
