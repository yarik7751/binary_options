package com.elatesoftware.grandcapital.utils;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.models.User;

/**
 * Created by Darya on 12.03.2017.
 */

public class ConventString {

    public static void setMaskAmount(EditText v, boolean isBol) {
        String str = v.getText().toString();
        if(isBol) {
            str = str.replace("$", "");
            v.setText(str);
        } else {
            if(!str.contains("$")) {
                v.setText("$" + str);
            }
        }
    }
    public static void setMaskTime(EditText v, boolean isBol) {
        String str = v.getText().toString();
        if(isBol) {
            str = str.replace(" MIN", "");
            v.setText(str);
        } else {
            if(!str.contains(" MIN")) {
                v.setText(str + " MIN");
            }
        }
    }
    public static void changeTimeValue(EditText v, boolean isAdd) {
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
        v.setText(time + " MIN");
    }
    public static void changeAmountValue(EditText v, boolean isAdd) {
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
        v.setText("$" + amout);
    }
    public static double getAmountValue(EditText v) {
        String valueStr = v.getText().toString();
        valueStr = valueStr.replaceAll("[^0-9.]", "");
        return Double.valueOf(valueStr);
    }

    public static int getTimeValue(EditText v) {
        String valueStr = v.getText().toString();
        valueStr = valueStr.replaceAll("[^0-9]", "");
        return Integer.parseInt(valueStr);
    }

    public static void updateBalance(TextView v){
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
}