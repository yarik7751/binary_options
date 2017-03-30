package com.elatesoftware.grandcapital.utils;

import android.content.Context;
import android.util.Log;

import com.elatesoftware.grandcapital.R;
import com.elatesoftware.grandcapital.app.GrandCapitalApplication;

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

    public static final String TAG = "ConventDate_Logs";
    private static final String CLOSE_DEASLING = "1970-01-01T00:00:00";
    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateFormat sdfHHMM = new SimpleDateFormat("HH:mm");
    private final static double DIFFERENSE_FOR_SOCKET = 1.5;
    private final static int DIFFERENCE_FOR_POINTS = 4200;
    private static final String timeZone = "GMT+00:00:00";

    private static final long BIG_DATE_FOR_EQUALS = getBigTimeForEquals();

    public static float genericTimeForChart(long currentTimePoint){
        return (float)(currentTimePoint - BIG_DATE_FOR_EQUALS);
    }
    public static long genericTimeForChartLabels(float currentTimePoint){
        return (BIG_DATE_FOR_EQUALS + (long) currentTimePoint);
    }
    private static long getBigTimeForEquals(){
        Date date = new Date();
        date.setHours(date.getHours() - 12);
        return (date.getTime());
    }
    public static String getConventDate(String date) {
        //dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));//
        sdf.setTimeZone(TimeZone.getDefault());
        //sdf.setTimeZone(TimeZone.getTimeZone("EET"));
        Date resultDate = null;
        try {
            resultDate = sdf.parse(date);
            //resultDate.setHours(resultDate.getHours() - 2);
            //resultDate.setTime(resultDate.getTime() + getIterationTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (resultDate != null) {
            return String.format("%02d", resultDate.getHours()) + ":" + String.format("%02d", resultDate.getMinutes());
        } else {
            return "";
        }
    }

    public static long stringToUnix(String date) {
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date resultDate = null;
        try {
            resultDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (resultDate != null) {
            return resultDate.getTime();
        } else {
            return -1;
        }
    }

    public static long getDifferenceDate(String date) {
        sdf.setTimeZone(TimeZone.getDefault());
        String currDate = sdf.format(new Date());
        return Math.abs(stringToUnix(currDate) - stringToUnix(date)) / 1000;
    }

    public static boolean validationDateTimer(String date) {
        sdf.setTimeZone(TimeZone.getDefault());
        String currDate = sdf.format(new Date());
        if(stringToUnix(date) -(stringToUnix(currDate))/1000 >= 0){
            return true;
        }else {
            return false;
        }
    }

    public static String getDifferenceDateToString(long difSec) {
        if(difSec < 60) {
            return ":" + timeClockFormat(difSec);
        } else if(difSec < 60 * 60) {
            long min = difSec / 60;
            long sec = difSec % 60;
            return timeClockFormat(min) + ":" + timeClockFormat(sec);
        } else if(difSec < 60 * 60 * 24) {
            long hour = difSec / 60 / 60;
            difSec -= hour * 60 * 60;
            long min = difSec / 60;
            long sec = difSec % 60;
            //return hour + "h " + timeClockFormat(min) + ":" + timeClockFormat(sec);
            return hour + "h " + timeClockFormat(min) + "m";
        } else {
            long day = difSec / 60 / 60 / 24;
            difSec -= day * 60 * 60 * 24;
            long hour = difSec / 60 / 60;
            difSec -= hour * 60 * 60;
            long min = difSec / 60;
            long sec = difSec % 60;
            return day + "d " + hour + "h ";
            //return day + "d " + hour + "h " + timeClockFormat(min) + ":" + timeClockFormat(sec);
        }
    }

    public static String timeClockFormat(long time) {
        return (time < 10) ? "0" + time : String.valueOf(time);
    }

    public static long getConvertDateInMilliseconds(String strDate) {
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime()/1000;
    }

    public static String convertDateFromMilSecHHMM(long time) {
        sdfHHMM.setTimeZone(TimeZone.getDefault());
        Date date = new Date(time);
        date.setHours(date.getHours() - 3);
        return sdfHHMM.format(date);
    }

    private static int getIterationTime() {
        long rawOffset = TimeZone.getDefault().getRawOffset();
        Log.d(TAG, "localTimezone: " + rawOffset);
        return (int) rawOffset;
    }

    public static boolean equalsTimeSymbols(long currentTime, long newTime) {
        if(currentTime == 0L){
            return false;
        }else{
             sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
             Date currentDate = new Date(currentTime);
             Date newDate = new Date(newTime);
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
            Date currentDate = new Date(currentTime);
            Date newDate = new Date(newTime);
            if((newDate.getSeconds() - currentDate.getSeconds() <= DIFFERENSE_FOR_SOCKET)){
                return true;
            }else{
                return false;
            }
        }
    }
    public static boolean equalsTimeDealing(String date) {
        long tempLong = ConventDate.getDifferenceDate(date);
        if (tempLong <= 0){
            return true;
        }else{
            return false;
        }
    }
    public static boolean equalsTimePoints(String time1, String time2){
        long timeLong1 = ConventDate.getConvertDateInMilliseconds(time1);
        long timeLong2 = ConventDate.getConvertDateInMilliseconds(time2);
        if ((timeLong1 - timeLong2 <= DIFFERENCE_FOR_POINTS && timeLong1 - timeLong2 >= 0) || (timeLong2 - timeLong1 <= DIFFERENCE_FOR_POINTS && timeLong2 - timeLong1 >= 0)){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isCloseDealing(String time){
        return time.equals(CLOSE_DEASLING);
    }

    public static String getCurrentDate(){
        Date date = new Date();
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }
    public static String getTimeStampCurrentDate() {
        sdf.setTimeZone(TimeZone.getDefault());
        Date date = new Date();
        return String.valueOf(date.getTime() / 1000);
    }
    public static String getTimeStampLastDate() {
        Date date = new Date();
        sdf.setTimeZone(TimeZone.getDefault());
        date.setMinutes(date.getMinutes() - 30);
        return String.valueOf(date.getTime() / 1000);
    }
    public static String getTimeCloseDealing(int expiration) {
        Date date = new Date();
        sdf.setTimeZone(TimeZone.getDefault());
        //date.setHours(date.getHours() + 2);
        date.setMinutes(date.getMinutes() + expiration);
        return sdf.format(date);
    }
    public static long getTimePlusOneSecond(long time) {
        Date date = new Date(time);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        date.setSeconds(date.getSeconds() + 1);
        return date.getTime();
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
