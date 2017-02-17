package com.elatesoftware.grandcapital.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
