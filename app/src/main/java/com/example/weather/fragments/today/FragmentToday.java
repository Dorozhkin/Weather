package com.example.weather.fragments.today;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.FontStyle;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.weather.FromJson.Current;
import com.example.weather.FromJson.Daily;
import com.example.weather.MainActivity;
import com.example.weather.R;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.databinding.FragmentTodayBinding;
import com.example.weather.fragments.TodayTomorrowAdapter;
import com.example.weather.fragments.Utils.TextUtil;

public class FragmentToday extends Fragment {

    private static FragmentToday instance;
    private FragmentTodayBinding binding;
    private ActivityMainBinding mainBinding;
    private Daily today;
    private Current current;
    private String timezoneOffset;
    public TodayTomorrowAdapter adapter = new TodayTomorrowAdapter();

    private FragmentToday() {}

    public static FragmentToday getInstance() {
        if (instance == null) {
            instance = new FragmentToday();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTodayBinding.inflate(inflater, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        binding.todayRecycler.setLayoutManager(llm);
        binding.todayRecycler.setAdapter(adapter);
        binding.todayRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_MOVE: rv.getParent().requestDisallowInterceptTouchEvent(true); mainBinding.swipe.setEnabled(false); return false;
                        default: mainBinding.swipe.setEnabled(true);
                    }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        if(today != null && current != null) {
            setData();
        }
        return binding.getRoot();
    }

    public void setData() {
        String[] info = TextUtil.forTodayFragment(current, today, timezoneOffset);
        //String uri = "http://openweathermap.org/img/wn/" +info[5]+ "@2x.png";

        binding.todayRecycler.scrollToPosition(0);
        binding.todayConstraint.setVisibility(View.VISIBLE);
        binding.currentTime.setText(info[0]);
        binding.todayDnemValue.setText(info[1]);
        binding.todayNochUValue.setText(info[2]);
        binding.currentGradus.setText(info[3]);
        binding.currentFeelsLike.setText(info[4]);
        binding.currentDescription.setText(info[6]);
        binding.todayProbabilityValue.setText(info[7]);

        int i = 0;
        switch (info[5]) {
            case "01d": i = R.drawable.p01dwhite; break;
            case "01n": i = R.drawable.p01nwhite; break;
            case "02d": i = R.drawable.p02dwhite; break;
            case "02n": i = R.drawable.p02nwhite; break;
            case "03d": i = R.drawable.p03dwhite; break;
            case "03n": i = R.drawable.p03nwhite; break;
            case "04d": i = R.drawable.p04dwhite; break;
            case "04n": i = R.drawable.p04nwhite; break;
            case "09d": i = R.drawable.p09dwhite; break;
            case "09n": i = R.drawable.p09nwhite; break;
            case "10d": i = R.drawable.p10dwhite; break;
            case "10n": i = R.drawable.p10nwhite; break;
            case "11d": i = R.drawable.p11dwhite; break;
            case "11n": i = R.drawable.p11nwhite; break;
            case "13d": i = R.drawable.p13dwhite; break;
            case "13n": i = R.drawable.p13nwhite; break;
            case "50d": i = R.drawable.p50dwhite; break;
            case "50n": i = R.drawable.p50nwhite; break;
        }
        binding.todayIcon.setImageResource(i);


        int forUmbrella = Integer.parseInt(info[7].replace("%", ""));
        binding.Umbrella.setVisibility(forUmbrella >= 30 ? View.VISIBLE : View.GONE);
/*        Glide.with(binding.todayIcon)
                .asBitmap()
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.p50d)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap resized = Bitmap.createBitmap(resource, 15,0, 70,70);
                        binding.todayIcon.setImageBitmap(resized);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });*/
    }

    public void setTodayAndCurrent(Daily today, Current current, String timezoneOffset) {
        this.today = today;
        this.current = current;
        this.timezoneOffset = timezoneOffset;
        adapter.setTimezoneOffset(timezoneOffset);
        if (binding != null) {
            setData();
        }
    }

    public void setMainBinding(ActivityMainBinding mainBinding) {
        this.mainBinding = mainBinding;
    }
}
