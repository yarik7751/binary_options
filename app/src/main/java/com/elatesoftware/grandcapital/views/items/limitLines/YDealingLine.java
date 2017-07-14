package com.elatesoftware.grandcapital.views.items.limitLines;

import android.graphics.Bitmap;

import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.google.gson.Gson;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class YDealingLine extends DealingLine {

    private Bitmap bitmapY;

    YDealingLine(float limit, String label, String timer, boolean isAmerican, boolean isActive) {
        super(limit, label, timer, isAmerican, isActive);
    }

    static void createYDealingLine(OrderAnswer order, boolean isAmerican){
        YDealingLine lineY = new YDealingLine(Float.valueOf(String.valueOf(order.getOpenPrice())),
                                            new Gson().toJson(order),
                                            String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())), isAmerican, false);
        updateColorDealingLine(lineY, order);
        rightYAxis.addLimitLine(lineY);
    }

    public Bitmap getBitmapY() {
        return bitmapY;
    }

    public void setBitmapY(Bitmap bitmapY) {
        this.bitmapY = bitmapY;
    }
}
