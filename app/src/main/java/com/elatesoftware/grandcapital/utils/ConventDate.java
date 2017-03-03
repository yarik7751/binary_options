package com.elatesoftware.grandcapital.utils;

import android.content.Context;

import com.elatesoftware.grandcapital.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Дарья Высокович on 16.02.2017.
 */

public class ConventDate {

    public static String getConventDate(String date) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date resultDate = null;
        try {
            resultDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (resultDate != null) {
            return String.format("%02d", resultDate.getHours()) + ":" + String.format("%02d", resultDate.getMinutes());
        } else {
            return "";
        }
    }

    public static String convertDateFromMilSecHHMM(long time) {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date(Long.parseLong(String.valueOf(time)));
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00:00"));
        return formatter.format(date);
    }
    public static boolean equalsTimeSymbols(long currentTime, long newTime) {
        if(currentTime == 0L){
            return false;
        }else{
             Date currentDate = new Date(Long.parseLong(String.valueOf(currentTime)));
             Date newDate = new Date(Long.parseLong(String.valueOf(newTime)));
            if((currentDate.getSeconds() == newDate.getSeconds()) ||
                    (((newDate.getSeconds() - currentDate.getSeconds()))) < 2){
                return true;
            }else{
                return false;
            }
        }
    }
    public static boolean equalsTimeSocket(long currentTime, long newTime) {
        if(currentTime == 0L){
            return false;
        }else{
            Date currentDate = new Date(Long.parseLong(String.valueOf(currentTime)));
            Date newDate = new Date(Long.parseLong(String.valueOf(newTime)));
            if((newDate.getSeconds() - currentDate.getSeconds() <= 2)){
                return true;
            }else{
                return false;
            }
        }
    }
    public static String getTimeStampCurrentDate() {
        /*DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        long unixtime = 0;
        Date date = new Date();
        String time = dfm.format(date);
        dfm.setTimeZone(TimeZone.getTimeZone("GMT+00:00:00"));
        try {
            unixtime = dfm.parse(time).getTime();
            unixtime = unixtime / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(unixtime);*/

        Date date = new Date();
        return String.valueOf(date.getTime() / 1000);
    }
    public static String getTimeStampLastDate() {
       /* DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        long unixtime = 0;
        Date date = new Date();
        date.setMinutes(date.getMinutes() -30);
        String time = dfm.format(date);
        dfm.setTimeZone(TimeZone.getTimeZone("GMT+00:00:00"));
        try {
            unixtime = dfm.parse(time).getTime();
            unixtime = unixtime / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(unixtime);*/
        Date date = new Date();
        date.setMinutes(date.getMinutes() -30);
        return String.valueOf(date.getTime() / 1000);
    }

    public static String getChatDateByUnix(Context context, long unix) {
        Calendar calendarChat = Calendar.getInstance();
        calendarChat.setTimeInMillis(unix);

        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeInMillis(System.currentTimeMillis());

        int hour = calendarChat.get(Calendar.HOUR_OF_DAY);
        int min = calendarChat.get(Calendar.MINUTE);

        String date = "";
        if(calendarChat.get(Calendar.MONTH) - calendarNow.get(Calendar.MONTH) == -1) {
            date = context.getResources().getString(R.string.yesterday);
        } else if(calendarChat.get(Calendar.MONTH) - calendarNow.get(Calendar.MONTH) < -1) {
            int day = calendarChat.get(Calendar.DAY_OF_MONTH);
            String month = context.getResources().getStringArray(R.array.months)[calendarChat.get(Calendar.MONTH)];
            date += (day <= 9 ? "0" + day : day) + " " + month + ", ";
        }
        date += (hour <= 9 ? "0" + hour : hour) + ":" + (min <= 9 ? "0" + min : min);

        return date;
    }


}
