package com.example.weather.fragments;

import android.graphics.Canvas;
import android.util.DisplayMetrics;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.databinding.HolderTodayTomorrowBinding;

public class TodayTomorrowHolder extends RecyclerView.ViewHolder {

    HolderTodayTomorrowBinding binding;

    public TodayTomorrowHolder(HolderTodayTomorrowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void drawAndGradus(float left, float center, float right, String[] values) {
        DisplayMetrics displayMetrics = binding.myDiagram.getContext().getResources().getDisplayMetrics();

        int height = Math.round(200 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));


        int topMargin = (int) (height*(center-0.17f));
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, topMargin, 0, 0);
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;

        binding.gradus.setLayoutParams(params);

        binding.myDiagram.setLeft(left);
        binding.myDiagram.setCenter(center);
        binding.myDiagram.setRight(right);
        binding.hourTime.setText(values[0]);

        binding.myDiagram.draw(new Canvas());

        binding.gradus.setText(values[1]);
    }
}
