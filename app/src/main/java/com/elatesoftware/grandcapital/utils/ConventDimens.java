package com.elatesoftware.grandcapital.utils;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class ConventDimens {

    private final static double DIFFERENCE_X_LINE = 9000d;
    private final static double DIFFERENCE_Y_CLICK = 0.00001d;
    private final static double DIFFERENCE_X_LABEL = 40000d;


    public static float callDistance(float x1, float x2, float y1, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static boolean isClickOnXDealingNoAmerican(double point1X, double point2X, double point1Y, double point2Y) {
        if (Math.abs(point1X - point2X) < DIFFERENCE_X_LINE) {
            return true;
        } else if (Math.abs(point1Y - point2Y) <= DIFFERENCE_Y_CLICK && Math.abs(point1X - point2X) < DIFFERENCE_X_LABEL) {
            return true;
        }else{
            return false;
        }
    }
    public static boolean isClickOnXYDealingAmerican(double point1X, double point2X, double point1Y, double point2Y) {
        if (Math.abs(point1Y - point2Y) <= DIFFERENCE_Y_CLICK && Math.abs(point1X - point2X) < DIFFERENCE_X_LABEL && point1X >= point2X) {
            return true;
        }else{
            return false;
        }
    }
    public static boolean isClickOnYDealingNoAmerican(double point1X, double point2X, double point1Y, double point2Y) {
        if (Math.abs(point1Y - point2Y) <= DIFFERENCE_Y_CLICK && Math.abs(point1X - point2X) < DIFFERENCE_X_LABEL) {
            return true;
        }else{
            return false;
        }
    }
}
