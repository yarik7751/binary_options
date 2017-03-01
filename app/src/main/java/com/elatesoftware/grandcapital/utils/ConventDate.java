package com.elatesoftware.grandcapital.utils;

import android.content.Context;

import com.elatesoftware.grandcapital.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Дарья Высокович on 16.02.2017.
 */

public class ConventDate {

    public static String getConventDate(String date){
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date resultDate = null;
        try {
             resultDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(resultDate != null){
            return String.format("%02d", resultDate.getHours()) + ":" + String.format("%02d", resultDate.getMinutes());
        }else{
            return "";
        }
    }

    public static String convertDateFromMilSecHHMM(long date){
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return formatter.format(calendar.getTime());
    }

    public static String getTimeStampCurrentDate(){
        Date date = new Date();
        return String.valueOf(date.getTime()/1000);
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        long i = date.getTime();
        return (String.valueOf(i));*/
        /*Long tsLong = System.currentTimeMillis()/1000;
        return tsLong.toString();*/
/*
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        long unixTime = (calendar.getTime().getTime() / 1000);      // milliseconds to seconds
        return String.valueOf(unixTime);*/
     /*
        Calendar cal =  Calendar.getInstance();
        cal.setTimeZone(TimeZone.);
       // cal.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        long unixTime = (cal.getTime().getTime()/1000);      // milliseconds to seconds
        return String.valueOf(unixTime);
        /*Date date = new Date();
        Date d = new Timestamp(date.getTime());
        return String.valueOf(date.getTime()/1000);*/
    }
    public static String getTimeStampLastDate(){
        /*Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        calendar.getTime().setMinutes(calendar.getTime().getMinutes() -30);
        long unixTime = (calendar.getTime().getTime() / 1000);      // milliseconds to seconds
        return String.valueOf(unixTime);*/
       /* Calendar cal = Calendar.getInstance();
        cal.set(cal.MINUTE, cal.getTime().getMinutes() - 30);
        long unixTime = (cal.getTime().getTime()/1000);      // milliseconds to seconds
        return String.valueOf(unixTime);*/
        Date date = new Date();
        date.setMinutes(date.getMinutes() - 30);
        return String.valueOf(date.getTime()/1000);
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
