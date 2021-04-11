package com.example.weather.fragments.Utils;

import android.util.Log;

import com.example.weather.FromJson.Hourly;

import java.util.ArrayList;
import java.util.Collections;

public class ForDiagram {
    private static final float TOP_PADDING = 0.17f;
    private static final float BOTTOM_PADDING =  0.17f;

    public static float[] indentsTop(ArrayList<Hourly> day, int[] deltaTemp) {

        float step  = (1 - TOP_PADDING - BOTTOM_PADDING)/deltaTemp[0];

        float[] top = new float[day.size()];
        for (int i = 0; i < day.size(); i++) {
            top[i] = TOP_PADDING - (Math.round(Double.parseDouble(day.get(i).getTemp())) - deltaTemp[1]) * step;
        }
        return top;
    }

    public static float oneIndent(String temp, int[] deltaTemp) {
        float step  = (1 - TOP_PADDING - BOTTOM_PADDING)/deltaTemp[0];
        return  TOP_PADDING - (Math.round(Double.parseDouble(temp)) - deltaTemp[1]) * step;
    }
}
