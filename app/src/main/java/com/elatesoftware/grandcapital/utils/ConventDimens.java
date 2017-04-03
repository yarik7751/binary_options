package com.elatesoftware.grandcapital.utils;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class ConventDimens {

    private final static double DIFFERENCE_POINT_X_CLICK_SELECT = 12000d;
    private final static double DIFFERENCE_POINT_Y_CLICK_SELECT = 0.00001d;
    private final static double DIFFERENCE_POINT_X_CLICK_LABEL = 40000d;


    public static boolean isClickLineForSelect(double point1, double point2) {
        if(Math.abs(point1 - point2) < DIFFERENCE_POINT_X_CLICK_SELECT){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isClickLabelForClose(double point1, double point2) {
        if(Math.abs(point1 - point2) <= DIFFERENCE_POINT_Y_CLICK_SELECT){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isClickYCloseLabel(double point1, double point2) {
        if(point2 - point1 <= DIFFERENCE_POINT_X_CLICK_LABEL && point2 - point1 >= 0){
            return true;
        }else{
            return false;
        }
    }
    public static float callDistance(float x1, float x2, float y1, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
