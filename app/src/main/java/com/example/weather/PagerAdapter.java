package com.example.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weather.fragments.today.FragmentToday;
import com.example.weather.fragments.tomorrow.FragmentTomorrow;
import com.example.weather.fragments.Hours.FragmentHours;

public class PagerAdapter extends FragmentStateAdapter {
    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0: fragment = FragmentToday.getInstance(); break;
            case 1: fragment = FragmentTomorrow.getInstance(); break;
            case 2: fragment = FragmentHours.getInstance(); break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
