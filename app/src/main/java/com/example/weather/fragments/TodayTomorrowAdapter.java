package com.example.weather.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.FromJson.Hourly;
import com.example.weather.databinding.HolderTodayTomorrowBinding;
import com.example.weather.fragments.Utils.ForDiagram;
import com.example.weather.fragments.Utils.TextUtil;

import java.util.ArrayList;
import java.util.Map;

public class TodayTomorrowAdapter extends RecyclerView.Adapter<TodayTomorrowHolder> {

    HolderTodayTomorrowBinding binding;
    private ArrayList<Hourly> dataForRecycler;
    private int[] deltaTemp;
    private String timezoneOffset;
    private Map<String, String> leftOrRight;

    @NonNull
    @Override
    public TodayTomorrowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = HolderTodayTomorrowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        TodayTomorrowHolder holder  = new TodayTomorrowHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodayTomorrowHolder holder, int position) {
        float[] top = ForDiagram.indentsTop(dataForRecycler, deltaTemp);
        ArrayList<float[]> leftCenterRight = new ArrayList<>();
        for (int i = 0; i < top.length; i++){
            float[] item = new float[3];
            float center = top[i];
            float left;
            float right;
            if (i == 0) {
                if(leftOrRight.containsKey("left")) {
                    left = (ForDiagram.oneIndent(leftOrRight.get("left"), deltaTemp) + center)/2;
                } else {
                    left = center;
                }
                right = (top[i] + top[i + 1]) / 2;
            } else if (i == dataForRecycler.size() - 1) {
                if (leftOrRight.containsKey("right")) {
                    right = (ForDiagram.oneIndent(leftOrRight.get("right"), deltaTemp) + center)/2;
                } else {
                    right = center;
                }
                left = (top[i] + top[i - 1]) / 2;
            } else {
                right = (top[i] + top[i + 1]) / 2;
                left = (top[i] + top[i - 1]) / 2;
            }

            item[0] = left;
            item[1] = center;
            item[2] = right;
            leftCenterRight.add(item);
        }

        String[] values = TextUtil.todayTomorrowDiagram(dataForRecycler.get(position), timezoneOffset);
        holder.drawAndGradus(leftCenterRight.get(position)[0], leftCenterRight.get(position)[1], leftCenterRight.get(position)[2], values);

    }

    @Override
    public int getItemCount() {
        if (dataForRecycler == null) {
            return 0;
        } else {
            return dataForRecycler.size();
        }
    }

    public void setDataForRecycler(ArrayList<Hourly> dataForRecycler) {
        this.dataForRecycler = dataForRecycler;
    }

    public void setDeltaTemp(int[] delta) {
        this.deltaTemp = delta;
    }

    public void setTimezoneOffset(String timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public void setLeftOrRight(Map<String, String> leftOrRight) {
        this.leftOrRight = leftOrRight;
    }
}
