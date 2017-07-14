package com.elatesoftware.grandcapital.views.items.limitLines;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.api.pojo.OrderAnswer;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventDate;
import com.elatesoftware.grandcapital.views.fragments.TerminalFragment;
import com.google.gson.Gson;

/**
 * Created by Дарья Высокович on 12.04.2017.
 */

public class DealingLine extends BaseLimitLine {

    private boolean mIsAmerican = false;
    private boolean mIsActive = false;
    private boolean isRed = false;

    private float mLimit = 0f;
    private String mLabel = "";
    private String mTimer = "";

    DealingLine(float limit, String label, String timer, boolean isAmerican, boolean isActive) {
        super(limit, label);
        mLimit = limit;
        mLabel = label;
        mTimer = timer;
        mIsAmerican = isAmerican;
        mIsActive = isActive;
    }

    public static void updateColorDealingLine(XDealingLine line, OrderAnswer order){
        if(order.getCmd() == 0 && order.getOpenPrice() <= TerminalFragment.mCurrentValueY ||
                order.getCmd() == 1 && order.getOpenPrice() >= TerminalFragment.mCurrentValueY){
            line.setLineColor(colorGreen);
            line.setIsRed(false);
        }else{
            line.setLineColor(colorRed);
            line.setIsRed(true);
        }
        line.setmTimer(String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())));
        line.setBitmapXLabel(createBitmapX(line));
        line.setBitmapYLabel(createBitmapY(line));
    }

    public static void updateColorDealingLine(YDealingLine line, OrderAnswer order){
        if(order.getCmd() == 0 && order.getOpenPrice() <= TerminalFragment.mCurrentValueY ||
                order.getCmd() == 1 && order.getOpenPrice() >= TerminalFragment.mCurrentValueY){
            line.setLineColor(colorGreen);
            line.setIsRed(false);
        }else{
            line.setIsRed(true);
            line.setLineColor(colorRed);
        }
        line.setmTimer(String.valueOf(ConventDate.getDifferenceDate(order.getOptionsData().getExpirationTime())));
        line.setBitmapY(createBitmapY(line));
    }
    private static Bitmap createBitmapX(XDealingLine line){
        String strLabelX = String.valueOf(ConventDate.convertDateFromMilSecHHMM(ConventDate.genericTimeForChartLabels(line.getLimit())));
        View linearLayoutXLabel;
        if(line.isRed()){
            linearLayoutXLabel = View.inflate(GrandCapitalApplication.getAppContext(), R.layout.incl_chart_label_x_red, null);
        }else{
            linearLayoutXLabel = View.inflate(GrandCapitalApplication.getAppContext(), R.layout.incl_chart_label_x_green, null);
        }
        TextView tvTimeX = (TextView) linearLayoutXLabel.findViewById(R.id.tv_dealing_time);
        tvTimeX.setText(strLabelX);

        linearLayoutXLabel.setDrawingCacheEnabled(true);
        linearLayoutXLabel.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                   View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearLayoutXLabel.layout(0, 0, linearLayoutXLabel.getMeasuredWidth(), linearLayoutXLabel.getMeasuredHeight());
        linearLayoutXLabel.buildDrawingCache(true);
        Bitmap bitmapXLabel = Bitmap.createBitmap(linearLayoutXLabel.getDrawingCache());
        linearLayoutXLabel.setDrawingCacheEnabled(false); // clear drawing cache
        return bitmapXLabel;
    }
    private static Bitmap createBitmapY(XDealingLine line){
        OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
        String strLabelY = ConventDate.getDifferenceDateToString(Long.valueOf(line.getmTimer()));
        View linearLayoutYLabel;
        if(line.isRed()){
            linearLayoutYLabel = View.inflate(GrandCapitalApplication.getAppContext(), R.layout.incl_chart_label_y_red, null);
        }else{
            linearLayoutYLabel = View.inflate(GrandCapitalApplication.getAppContext(), R.layout.incl_chart_label_y_green, null);
        }
        ImageView imgCMD = (ImageView) linearLayoutYLabel.findViewById(R.id.img_dealing_cmd);
        if(order.getCmd() == 1){
            imgCMD.setImageDrawable(GrandCapitalApplication.getAppContext().getResources().getDrawable(R.drawable.down));
        }else{
            imgCMD.setImageDrawable(GrandCapitalApplication.getAppContext().getResources().getDrawable(R.drawable.up));
        }
        TextView tvTimeY = (TextView) linearLayoutYLabel.findViewById(R.id.tv_dealing_time);
        tvTimeY.setText(strLabelY);
        if(line.ismIsAmerican()){
            ImageView imgClose = (ImageView) linearLayoutYLabel.findViewById(R.id.img_dealing_close);
            imgClose.setVisibility(View.VISIBLE);
        }
        linearLayoutYLabel.setDrawingCacheEnabled(true);
        linearLayoutYLabel.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearLayoutYLabel.layout(0, 0, linearLayoutYLabel.getMeasuredWidth(), linearLayoutYLabel.getMeasuredHeight());
        linearLayoutYLabel.buildDrawingCache(true);
        Bitmap bitmapYLabel = Bitmap.createBitmap(linearLayoutYLabel.getDrawingCache());
        linearLayoutYLabel.setDrawingCacheEnabled(false); // clear drawing cache
        return bitmapYLabel;
    }
    private static Bitmap createBitmapY(YDealingLine line){
        View linearLayoutYLabel;
        OrderAnswer order = new Gson().fromJson(line.getLabel(), OrderAnswer.class);
        String strLabelY  = ConventDate.getDifferenceDateToString(Long.valueOf(line.getmTimer()));
        if(line.isRed()){
            linearLayoutYLabel = View.inflate(GrandCapitalApplication.getAppContext(), R.layout.incl_chart_label_y_red, null);
        }else{
            linearLayoutYLabel = View.inflate(GrandCapitalApplication.getAppContext(), R.layout.incl_chart_label_y_green, null);
        }
        ImageView imgCMD = (ImageView) linearLayoutYLabel.findViewById(R.id.img_dealing_cmd);
        if(order.getCmd() == 1){
            imgCMD.setImageDrawable(GrandCapitalApplication.getAppContext().getResources().getDrawable(R.drawable.down));
        }else{
            imgCMD.setImageDrawable(GrandCapitalApplication.getAppContext().getResources().getDrawable(R.drawable.up));
        }
        TextView tvTimeY = (TextView) linearLayoutYLabel.findViewById(R.id.tv_dealing_time);
        tvTimeY.setText(strLabelY);
        if(line.ismIsAmerican()){
            ImageView imgClose = (ImageView) linearLayoutYLabel.findViewById(R.id.img_dealing_close);
            imgClose.setVisibility(View.VISIBLE);
        }
        linearLayoutYLabel.setDrawingCacheEnabled(true);
        linearLayoutYLabel.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearLayoutYLabel.layout(0, 0, linearLayoutYLabel.getMeasuredWidth(), linearLayoutYLabel.getMeasuredHeight());

        linearLayoutYLabel.buildDrawingCache(true);
        Bitmap iconLabelY = Bitmap.createBitmap(linearLayoutYLabel.getDrawingCache());
        linearLayoutYLabel.setDrawingCacheEnabled(false); // clear drawing cache
        return iconLabelY;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setIsRed(boolean isRed) {
        this.isRed = isRed;
    }

    public boolean ismIsAmerican() {
        return mIsAmerican;
    }

    public void setmIsAmerican(boolean mIsAmerican) {
        this.mIsAmerican = mIsAmerican;
    }

    public String getmLabel() {
        return mLabel;
    }

    public void setmLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public boolean ismIsActive() {
        return mIsActive;
    }

    public void setmIsActive(boolean mIsActive) {
        this.mIsActive = mIsActive;
    }

    public float getmLimit() {
        return mLimit;
    }

    public void setmLimit(float mLimit) {
        this.mLimit = mLimit;
    }

    public String getmTimer() {
        return mTimer;
    }

    public void setmTimer(String mTimer) {
        this.mTimer = mTimer;
    }
}
