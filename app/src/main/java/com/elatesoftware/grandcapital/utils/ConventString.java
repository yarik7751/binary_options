package com.elatesoftware.grandcapital.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.models.User;

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
                int value = Integer.parseInt(str);
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

    public static void setTimeValue(EditText v, int value, boolean isOpenKeyboard) {
        v.setText(value + (isOpenKeyboard ? "" : " MIN"));
    }

    public static void setAmountValue(EditText v, double value, boolean isOpenKeyboard) {
        v.setText((isOpenKeyboard ? "" : "$") + value);
    }

    public static void changeAmountValue(EditText v, boolean isAdd, boolean isOpenKeyboard) {
        String str = v.getText().toString();
        str = str.replace("$", "");
        int amout = Integer.parseInt(str);
        if(isAdd) {
            amout++;
        } else {
            amout--;
            if(amout < 0) {
                amout = 0;
            }
        }
        v.setText((isOpenKeyboard ? "" : "$") + amout);
        GoogleAnalyticsUtil.sendEvent(Const.ANALYTICS_TERMINAL_SCREEN, Const.ANALYTICS_BUTTON_CHANGE_AMOUNT_INVESTMENTS, isAdd ? "+" : "-", (long) amout);
    }
    public static double getAmountValue(EditText v) {
        String valueStr = v.getText().toString();
        if(TextUtils.isEmpty(valueStr)) {
            valueStr = "0";
        }
        valueStr = valueStr.replaceAll("[^0-9.]", "");
        double result = 0d;
        try {
            result = Double.valueOf(valueStr);
            return result;
        } catch (Exception e) {
            return result;
        }
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

    public static String getRoundNumber(int signs, double number) {
        String format = String.format("%." + signs + "f", number);
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
        return getRoundNumber(4, earlyClosure) + "(" + (earlyClosure == 0 ? 0 : percent) + "%)";
    }

    public static void formatReward(TextView tv) {
        String str = tv.getText().toString();
        int startPosition = str.indexOf("$");

        SpannableString text = new SpannableString(str);
        text.setSpan(new StyleSpan(Typeface.BOLD), startPosition, str.length() - 1,  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new RelativeSizeSpan(1.1f), startPosition, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(text, TextView.BufferType.SPANNABLE);
    }
}
