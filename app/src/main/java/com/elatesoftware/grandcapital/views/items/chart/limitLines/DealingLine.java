package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.github.mikephil.charting.components.YAxis;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class DealingLine extends BaseLimitLine {

    private float mLimit = 0f;
    private String mLabel = "";
    private Bitmap mBitmapLabelY = null;

    private static DealingLine currentLineDealing = null;

    private DealingLine(float limit, String label, Bitmap bitmapY) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mBitmapLabelY = bitmapY;
        super.setLineColor(Color.TRANSPARENT);
    }

    public static void drawActiveDealingLine(BaseLimitLine line, OrderAnswer order) {
        deleteDealingLine();
        if(line != null){
            line.enableDashedLine(0f, 0f, 0f);
            currentLineDealing = new DealingLine(Float.valueOf(String.valueOf(order.getOpenPrice())), String.valueOf(order.getOpenPrice()), null);
            if(line.getLineColor() == colorGreen){
                currentLineDealing.setmBitmapLabelY(bitmapIconCurrentDealingGreenYLabel);
                currentLineDealing.setLineColor(colorGreen);
            }else{
                currentLineDealing.setmBitmapLabelY(bitmapIconCurrentDealingRedYLabel);
                currentLineDealing.setLineColor(colorRed);
            }
            rightYAxis.addLimitLine(currentLineDealing);
        }
    }
    public static void deleteDealingLine(){
        if (currentLineDealing != null) {
            rightYAxis.removeLimitLine(currentLineDealing);
        }
    }

    public float getmLimit() {
        return mLimit;
    }
    public void setmLimit(float mLimit) {
        this.mLimit = mLimit;
    }
    public String getmLabel() {
        return mLabel;
    }
    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }
    public Bitmap getmBitmapLabelY() {
        return mBitmapLabelY;
    }
    public void setmBitmapLabelY(Bitmap mBitmapLabelY) {
        this.mBitmapLabelY = mBitmapLabelY;
    }
}
