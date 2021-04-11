package com.example.weather.fragments.Hours;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.FromJson.Hourly;
import com.example.weather.R;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.databinding.FragmentHoursBinding;
import com.example.weather.databinding.HolderHoursBinding;
import com.example.weather.fragments.Utils.TextUtil;

public class AdapterHours extends RecyclerView.Adapter<ViewHolderHours> {

    HolderHoursBinding binding;
    private Hourly[] data;
    private long timezoneShift;
    private FragmentHoursBinding fragmentHoursBinding;

    public void setData(Hourly[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolderHours onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = HolderHoursBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolderHours holder = new ViewHolderHours(binding);

        View.OnClickListener listener = v -> {
            Hourly hour = data[holder.getAdapterPosition()];
            hour.isSelected = !hour.isSelected;
            this.notifyItemChanged(holder.getAdapterPosition());

            Log.d("Click", "YES");
        };
        holder.binding.alwaysVisiblePart.setOnClickListener(listener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderHours holder, int position) {
        Hourly current = data[position];
        String[] text = TextUtil.textForItem(current, timezoneShift);
        holder.putInfo(text);

        boolean isExpanded = data[position].isSelected;

        holder.binding.expandablePart.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        if (holder.binding.expandablePart.getVisibility() == View.VISIBLE) {
            fragmentHoursBinding.recycler.smoothScrollToPosition(holder.getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.length;
        } else return 0;
    }

    public void setTimezoneShift(long timezoneShift) {
        this.timezoneShift = timezoneShift;
    }


    public void setFragmentHoursBinding(FragmentHoursBinding fragmentHoursBinding) {
        this.fragmentHoursBinding = fragmentHoursBinding;
    }
}
