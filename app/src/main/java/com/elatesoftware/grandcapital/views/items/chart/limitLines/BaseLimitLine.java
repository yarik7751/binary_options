package com.elatesoftware.grandcapital.views.items.chart.limitLines;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.utils.ConventImage;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

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

    BaseLimitLine(float limit, String label) {
        super(limit, label);
        colorGreen = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.chat_green);
        colorRed = GrandCapitalApplication.getAppContext().getResources().getColor(R.color.color_red_chart);
        bitmapIconGreenXLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.green_vert);
        bitmapIconRedXLabel = BitmapFactory.decodeResource(GrandCapitalApplication.getAppContext().getResources(), R.drawable.red_vert);
        bitmapIconRedYLabel = ConventImage.loadBitmapFromView(LayoutInflater.from(GrandCapitalApplication.getAppContext()).inflate(R.layout.incl_chart_label_red, null));
        bitmapIconGreenYLabel = ConventImage.loadBitmapFromView(LayoutInflater.from(GrandCapitalApplication.getAppContext()).inflate(R.layout.incl_chart_label_green, null));
    }

    public static List<XDealingLine> getXLimitLines(XAxis xAxis) {
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
    public static List<YDealingLine> getYLimitLines(YAxis rightYAxis){
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
}
