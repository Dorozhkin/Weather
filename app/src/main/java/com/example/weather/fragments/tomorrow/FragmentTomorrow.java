package com.example.weather.fragments.tomorrow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.FromJson.Daily;
import com.example.weather.R;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.databinding.FragmentTomorrowBinding;
import com.example.weather.fragments.TodayTomorrowAdapter;
import com.example.weather.fragments.Utils.TextUtil;

public class FragmentTomorrow extends Fragment {

    private static FragmentTomorrow instance;
    private FragmentTomorrowBinding binding;
    private ActivityMainBinding mainBinding;
    private Daily tomorrow;
    private String timezoneOffset;
    public TodayTomorrowAdapter adapter = new TodayTomorrowAdapter();

    private FragmentTomorrow() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTomorrowBinding.inflate(inflater, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.HORIZONTAL);
        binding.tomorrowRecycler.setLayoutManager(llm);
        binding.tomorrowRecycler.setAdapter(adapter);
        binding.tomorrowRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_MOVE: rv.getParent().requestDisallowInterceptTouchEvent(true);  mainBinding.swipe.setEnabled(false); return false;
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

        if (tomorrow != null && timezoneOffset != null) {
            setData();
        }
        return binding.getRoot();
    }

    public static FragmentTomorrow getInstance() {
        if (instance == null) {
            instance = new FragmentTomorrow();
        }
        return instance;
    }

    public void setData() {
        String[] info = TextUtil.forTomorrowFragment(tomorrow, timezoneOffset);
        //String uri = "http://openweathermap.org/img/wn/" +info[3]+ "@2x.png";

        binding.tomorrowRecycler.scrollToPosition(0);
        binding.currentTime.setText(info[0]);
        binding.tomorrowDnemValue.setText(info[1]);
        binding.tomorrowNochUValue.setText(info[2]);
        binding.tomorrowDescription.setText(info[4]);
        binding.tomorrowProbabilityValue.setText(info[5]);

        int forUmbrella = Integer.parseInt(info[5].replace("%", ""));
        binding.Umbrella.setVisibility(forUmbrella >= 30 ? View.VISIBLE : View.GONE);

        int i = 0;
        switch (info[3]) {
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
         binding.tomorrowIcon.setImageResource(i);

      /*  Glide.with(binding.tomorrowIcon)
                .asBitmap()
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.p50d)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap resized = Bitmap.createBitmap(resource, 15,0, 70,70);
                        binding.tomorrowIcon.setImageBitmap(resized);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });*/
    }

    public void setTomorrowAndOffset(Daily tomorrow, String timezoneOffset) {
        this.tomorrow = tomorrow;
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
