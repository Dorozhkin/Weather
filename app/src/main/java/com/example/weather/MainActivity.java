package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import android.view.Window;

import com.example.weather.FromJson.Current;
import com.example.weather.FromJson.Daily;
import com.example.weather.FromJson.Hourly;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.fragments.today.FragmentToday;
import com.example.weather.fragments.tomorrow.FragmentTomorrow;
import com.example.weather.fragments.Hours.FragmentHours;
import com.example.weather.fragments.search.SearchFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;

public class MainActivity  extends AppCompatActivity implements SearchFragment.OnFragmentSendDataListener {

    private ActivityMainBinding binding;
    private FragmentHours fragmentHours = FragmentHours.getInstance();
    private FragmentToday fragmentToday = FragmentToday.getInstance();
    private FragmentTomorrow fragmentTomorrow = FragmentTomorrow.getInstance();

    private int todayColor = R.color.orange;
    private int tomorrowColor = R.color.orange;
    private static final int HOURS_COLOR = R.color.forHours;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener swipeListener;
    private ArrayList<Hourly> forTodayAdapter = new ArrayList<>();
    private ArrayList<Hourly> forTomorrowAdapter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("On create ", "Activity");
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        swipeRefreshLayout = binding.swipe;
        setContentView(binding.getRoot());


        fragmentToday.adapter.setDataForRecycler(forTodayAdapter);
        fragmentTomorrow.adapter.setDataForRecycler(forTomorrowAdapter);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), getLifecycle());

        binding.pager2.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabs, binding.pager2, true, (tab, position) -> {
                 switch (position) {
                     case 0: tab.setText("Сегодня"); break;
                     case 1: tab.setText("Завтра"); break;
                     case 2: tab.setText("По часам"); break;
                 }

                 tab.view.setOnLongClickListener(v -> true);

        }).attach();

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeColors(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("Reselected", "Yes");
                changeColors(tab.getPosition());
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        binding.pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                binding.swipe.setEnabled(state == ViewPager2.SCROLL_STATE_IDLE);
            }
        });


        fragmentToday.setMainBinding(binding);
        fragmentTomorrow.setMainBinding(binding);
    }


    @Override
    protected void onStart() {
        Log.d("On Start ", "Activity");
        super.onStart();
        binding.swipe.setOnRefreshListener(swipeListener);
    }

    @Override
    public void pickColor(String currentWeatherId, String tomorrowWeatherID, String currentTime, String sunSet, String sunRise) {
        long current = Long.parseLong(currentTime);
        long sunset = Long.parseLong(sunSet);
        long sunrise = Long.parseLong(sunRise);

        if( current > sunset || current < sunrise ) {
            todayColor = R.color.night;
        } else {
            switch (currentWeatherId.substring(0, 1)) {
                case "2": todayColor = R.color.thunderstorm; break;
                case "3": todayColor = R.color.drizzle; break;
                case "5": todayColor = R.color.rain; break;
                case "6": todayColor = R.color.darkGrey; break;
                case "7": todayColor = R.color.atmosphere; break;
                case "8":
                    switch (currentWeatherId) {
                        case "800": todayColor = R.color.clear; break;
                        default: todayColor = R.color.clouds; break;
                    }
            }
        }
        switch (tomorrowWeatherID.substring(0, 1)) {
            case "2": tomorrowColor = R.color.thunderstorm; break;
            case "3": tomorrowColor = R.color.drizzle; break;
            case "5": tomorrowColor = R.color.rain; break;
            case "6": tomorrowColor = R.color.darkGrey; break;
            case "7": tomorrowColor = R.color.atmosphere; break;
            case "8":
                switch (tomorrowWeatherID) {
                    case "800": tomorrowColor = R.color.clear; break;
                    default: tomorrowColor = R.color.clouds; break;
                }
        }
    }
    public void changeColors(int position) {
        Window window = getWindow();

        switch (position) {
            case 0: int colorToday = getApplicationContext().getColor(todayColor); binding.tabs.setBackgroundColor(colorToday); window.setStatusBarColor(colorToday); binding.linear.setBackgroundColor(colorToday); Log.d("TODAY COLOR", String.valueOf(colorToday)); break;
            case 1: int colorTomorrow = getApplicationContext().getColor(tomorrowColor);  binding.tabs.setBackgroundColor(colorTomorrow); window.setStatusBarColor(colorTomorrow); binding.linear.setBackgroundColor(colorTomorrow); break;
            case 2: int hoursColor = getApplicationContext().getColor(HOURS_COLOR); binding.tabs.setBackgroundColor(hoursColor); window.setStatusBarColor(hoursColor); binding.linear.setBackgroundColor(hoursColor); break;
        }
    }

    @Override
    public void onSendDataForHours(Hourly[] data, String timezone) {
        fragmentHours.setAdapterData(data);
        fragmentHours.setTimezoneShift(Long.parseLong(timezone));
        fragmentHours.adapterHours.notifyDataSetChanged();

        binding.tabs.selectTab(binding.tabs.getTabAt(binding.pager2.getCurrentItem()));

        if (fragmentHours.binding != null) {
            fragmentHours.binding.recycler.scrollToPosition(0);
        }
    }

    @Override
    public void onSendDataForToday(Current current, Daily today, String timezone) {
        fragmentToday.setTodayAndCurrent(today, current, timezone);
    }

    @Override
    public void onSendDataForTomorrow(Daily tomorrow, String timezoneOffset) {
        fragmentTomorrow.setTomorrowAndOffset(tomorrow, timezoneOffset);
    }

    @Override
    public void onSendListener(SwipeRefreshLayout.OnRefreshListener swipeListener) {
        this.swipeListener = swipeListener;
    }
    @Override
    public void onSendDataForAdapters(ArrayList<Hourly> forToday, ArrayList<Hourly> forTomorrow, int[] delta, Map<String,String> today, Map<String,String> tomorrow){
            forTodayAdapter.clear();
            forTomorrowAdapter.clear();

            fragmentToday.adapter.setDeltaTemp(delta);
            fragmentTomorrow.adapter.setDeltaTemp(delta);

            fragmentToday.adapter.setLeftOrRight(today);
            fragmentTomorrow.adapter.setLeftOrRight(tomorrow);

            forTodayAdapter.addAll(forToday);
            forTomorrowAdapter.addAll(forTomorrow);
            fragmentTomorrow.adapter.notifyDataSetChanged();
            fragmentToday.adapter.notifyDataSetChanged();
    }
}

