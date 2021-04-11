package com.example.weather.fragments.Hours;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.FromJson.Hourly;
import com.example.weather.R;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.databinding.FragmentHoursBinding;

public class FragmentHours extends Fragment {

    private static FragmentHours instance;
    public FragmentHoursBinding binding;
    public AdapterHours adapterHours = new AdapterHours();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHoursBinding.inflate(inflater, container, false);
        adapterHours.setFragmentHoursBinding(binding);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        binding.recycler.setLayoutManager(llm);
        binding.recycler.setAdapter(adapterHours);
        return binding.getRoot();
    }

    private FragmentHours() {
    }

    public static FragmentHours getInstance() {
        if (instance == null) {
            instance = new FragmentHours();
        }
        return instance;
    }

    public void setAdapterData(Hourly[] data) {
        adapterHours.setData(data);
    }

    public void setTimezoneShift(long timezoneShift) {
        adapterHours.setTimezoneShift(timezoneShift);
    }

}
