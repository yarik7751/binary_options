package com.elatesoftware.grandcapital.utils;

import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;
import com.elatesoftware.grandcapital.models.User;
import com.google.android.gms.analytics.HitBuilders;

/**
 * Created by Darya on 12.03.2017.
 */

public class ConventString {

    public static final String TAG = "ConventString_Log";

    public static void setMaskAmount(EditText v, boolean isBol) {
        String str = v.getText().toString();
        if(isBol) {
            str = str.replace("$", "");
            v.setText(str);
            v.setSelection(v.getText().toString().length());
        } else {
            if(!str.contains("$")) {
                if(TextUtils.isEmpty(str)) {
                    str = "0";
                }
                Double value = Double.parseDouble(str);
                v.setText("$" + value);
            }
        }
    }

    public static void setMaskTime(EditText v, boolean isBol) {
        String str = v.getText().toString();
        if(isBol) {
            str = str.replace(" MIN", "");
            v.setText(str);
            v.setSelection(v.getText().toString().length());
        } else {
            if(!str.contains(" MIN")) {
                if(TextUtils.isEmpty(str)) {
                    str = "0";
                }
                int value = Integer.parseInt(str);
                v.setText(value + " MIN");
            }
        }
    }
    public static void changeTimeValue(EditText v, boolean isAdd, boolean isOpenKeyboard) {
        String str = v.getText().toString();
        str = str.replace(" MIN", "");
        int time = Integer.parseInt(str);
        if(isAdd) {
            time++;
        } else {
            time--;
            if(time < 0) {
                time = 0;
            }
        }
        v.setText(time + (isOpenKeyboard ? "" : " MIN"));
        GoogleAnalyticsUtil.sendEvent(Const.ANALYTICS_TERMINAL_SCREEN, Const.ANALYTICS_BUTTON_CHANGE_TIME, isAdd ? "+" : "-", (long) time);
    }
    public static void changeAmountValue(EditText v, boolean isAdd) {
        String str = v.getText().toString();
        str = str.replace("$", "");
        double amout = Double.parseDouble(str);
        if(isAdd) {
            amout++;
        } else {
            amout--;
            if(amout < 0) {
                amout = 0;
            }
        }
        v.setText("$" + amout);
        GoogleAnalyticsUtil.sendEvent(Const.ANALYTICS_TERMINAL_SCREEN, Const.ANALYTICS_BUTTON_CHANGE_AMOUNT_INVESTMENTS, isAdd ? "+" : "-", (long) amout);
    }
    public static double getAmountValue(EditText v) {
        String valueStr = v.getText().toString();
        if(TextUtils.isEmpty(valueStr)) {
            valueStr = "0";
        }
        valueStr = valueStr.replaceAll("[^0-9.]", "");
        return Double.valueOf(valueStr);
    }

    public static int getTimeValue(EditText v) {
        String valueStr = v.getText().toString();
        if(TextUtils.isEmpty(valueStr)) {
            valueStr = "0";
        }
        valueStr = valueStr.replaceAll("[^0-9]", "");
        return Integer.parseInt(valueStr);
    }

    public static void setBalance(TextView v){
        if(User.getInstance() != null){
            v.setText("$" + String.format("%.2f", User.getInstance().getBalance()).replace('.', ','));
        }
    }

    public static String getActive(TextView v){
        return v.getText().toString().replace("_OP", "");
    }

    public static void parseResponseSignals(Activity activity, TextView v, String summary){
        switch (summary){
            case "s":
                v.setText(activity.getResources().getString(R.string.signal_s));
                v.setTextColor(activity.getResources().getColor(R.color.dealingListDownOrderColor));
                break;
            case "ss":
                v.setText(activity.getResources().getString(R.string.signal_ss));
                v.setTextColor(activity.getResources().getColor(R.color.dealingListDownOrderColor));
                break;
            case "b":
                v.setText(activity.getResources().getString(R.string.signal_b));
                v.setTextColor(activity.getResources().getColor(R.color.dealingListUpOrderColor));
                break;
            case "sb":
                v.setText(activity.getResources().getString(R.string.signal_sb));
                v.setTextColor(activity.getResources().getColor(R.color.dealingListUpOrderColor));
                break;
            default:
                break;
        }
    }

    public static String getRoundNumber(double number) {
        String format = String.format("%.5f", number);
        format = format.replace(",", ".");
        while (format.charAt(format.length() - 1) == '0' || format.charAt(format.length() - 1) == '.') {
            boolean isBreak = format.charAt(format.length() - 1) == '.';
            format = format.substring(0, format.length() - 1);
            if(isBreak) {
                break;
            }
        }
        return format;
    }

    public static String getContentQuestions(String str){
        return Html.fromHtml(str).toString().replace((char) 160, (char) 32).replace((char) 65532, (char) 32).trim().replaceAll("[\\n]{2,}", "\n");
    }

    public static String getStringEarlyClosure(EditText view, int percent){
        double earlyClosure = ConventString.getAmountValue(view) * percent / 100.000;
        return earlyClosure + "(" + (earlyClosure == 0 ? 0 : percent) + "%)";
    }
}
