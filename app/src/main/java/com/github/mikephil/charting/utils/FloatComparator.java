package com.github.mikephil.charting.utils;

import com.github.mikephil.charting.data.Entry;

import java.util.Comparator;

/**
 * Created by Дарья Высокович on 04.04.2017.
 */

public class FloatComparator implements Comparator<Float> {
    @Override
    public int compare(Float float1, Float float2) {
        float diff = float1 - float2;
        if (diff == 0f) return 0;
        else {
            if (diff > 0f) return 1;
            else return -1;
        }
    }
}