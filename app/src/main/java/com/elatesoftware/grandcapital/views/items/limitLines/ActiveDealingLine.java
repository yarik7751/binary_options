package com.elatesoftware.grandcapital.views.items.limitLines;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class ActiveDealingLine extends BaseLimitLine {

    private Bitmap mBitmapLabelY = null;
    private static ActiveDealingLine currentLineDealing = null;

    private ActiveDealingLine(float limit, String label, Bitmap bitmapY) {
        super(limit, label);
        mBitmapLabelY = bitmapY;
        super.setLineColor(Color.TRANSPARENT);
    }

    public static void drawActiveDealingLine(BaseLimitLine line, OrderAnswer order) {
        deleteDealingLine();
        if(line != null){
            line.enableDashedLine(0f, 0f, 0f);
            currentLineDealing = new ActiveDealingLine(Float.valueOf(String.valueOf(order.getOpenPrice())), String.valueOf(order.getOpenPrice()), null);
            if(line.getLineColor() == colorGreen){
                currentLineDealing.setmBitmapLabelY(bitmapIconCurrentDealingGreenYLabel);
                currentLineDealing.setLineColor(colorGreen);
            }else if(line.getLineColor() == colorRed){
                currentLineDealing.setmBitmapLabelY(bitmapIconCurrentDealingRedYLabel);
                currentLineDealing.setLineColor(colorRed);
            }
            rightYAxis.addLimitLine(currentLineDealing);
        }
    }
    public static void deleteDealingLine(){
        if (currentLineDealing != null) {
            rightYAxis.removeLimitLine(currentLineDealing);
            currentLineDealing = null;
        }
    }

    public Bitmap getmBitmapLabelY() {
        return mBitmapLabelY;
    }
    private void setmBitmapLabelY(Bitmap mBitmapLabelY) {
        this.mBitmapLabelY = mBitmapLabelY;
    }
}
