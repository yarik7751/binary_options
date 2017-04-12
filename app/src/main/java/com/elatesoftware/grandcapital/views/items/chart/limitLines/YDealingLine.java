package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Bitmap;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.google.gson.Gson;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class YDealingLine extends DealingLine {

    YDealingLine(float limit, String label, Bitmap bitmapY, String timer, boolean isAmerican, boolean isActive) {
        super(limit, label, bitmapY, timer, isAmerican, isActive);
    }

    static void createYDealingLine(OrderAnswer order, double mCurrentValueY, boolean isAmerican){
        YDealingLine lineY = new YDealingLine(Float.valueOf(String.valueOf(order.getOpenPrice())),
                                            new Gson().toJson(order), null,
                                            String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), isAmerican, false);
        updateColorDealingLine(lineY, order, mCurrentValueY);
        rightYAxis.addLimitLine(lineY);
    }
}
