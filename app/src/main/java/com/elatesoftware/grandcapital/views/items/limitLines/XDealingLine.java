package com.elatesoftware.grandcapital.views.items.limitLines;

import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.google.gson.Gson;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class XDealingLine extends DealingLine {

    public static final String TAG = "XDealingLine_Logs";

    XDealingLine(float limit, String label,String timer, boolean isAmerican, boolean isActive) {
        super(limit, label, timer, isAmerican, isActive);
        if(isActive){
            super.enableDashedLine(0f, 0f, 0f);
        }else{
            super.enableDashedLine(10f, 10f, 0f);
        }
    }
     static void createXDealingLine(OrderAnswer order, double mCurrentValueY, boolean isAmerican){
        XDealingLine lineX = new XDealingLine(ConventDate.genericTimeForChart(
                ConventDate.getConvertDateInMilliseconds(order.getOptionsData().getExpirationTime()) * 1000),
                new Gson().toJson(order), String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), isAmerican, false);
        updateColorDealingLine(lineX, order, mCurrentValueY);
        xAxis.addLimitLine(lineX);
    }
}
