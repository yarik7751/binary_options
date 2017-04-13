package com.elatesoftware.grandcapital.utils;

/**
 * Created by Дарья Высокович on 03.04.2017.
 */

public class ConventDimens {

    private final static double DIFFERENCE_Y_CLICK = 0.00001d;

    public static float callDistance(float x1, float x2, float y1, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    public static boolean isClickOnXDealingNoAmerican(final double point1X, final double point2X, final double point1Y, final double point2Y, final int maxX) {
        return maxX != 0 && Math.abs(point1Y - point2Y) <= DIFFERENCE_Y_CLICK && Math.abs(point1X - point2X) < maxX / 4;
    }
    public static boolean isClickOnXYDealingAmerican(final double point1X, final double point2X, final double point1Y, final double point2Y, final int maxX) {
        return maxX != 0 && Math.abs(point1Y - point2Y) <= DIFFERENCE_Y_CLICK && Math.abs(point1X - point2X) < maxX / 4 && point1X >= point2X;
    }
    public static boolean isClickOnYDealingNoAmerican(double point1X, double point2X, double point1Y, double point2Y, int maxX) {
        return maxX != 0 && Math.abs(point1Y - point2Y) <= DIFFERENCE_Y_CLICK && Math.abs(point1X - point2X) < maxX / 4;
    }
}
