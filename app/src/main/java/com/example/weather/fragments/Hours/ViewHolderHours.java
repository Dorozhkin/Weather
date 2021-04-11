package com.example.weather.fragments.Hours;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.weather.R;
import com.example.weather.databinding.HolderHoursBinding;

public class ViewHolderHours extends RecyclerView.ViewHolder {

    HolderHoursBinding binding;

    public ViewHolderHours(HolderHoursBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void putInfo(String[] data)  {
        binding.tvTime.setText(data[0]);
        binding.tvMainWeather.setText(data[1]);
        //String uri = "http://openweathermap.org/img/wn/" +data[2]+ "@2x.png";
        binding.temperature.setText(data[3]);

        binding.feelsLikeValue.setText(data[4]);
        binding.windValue.setText(data[5]);
        binding.humidityValue.setText(data[6]);
        binding.precipitationValue.setText(data[7]);
        binding.probability.setText(data[8]);

        int i = 0;
        switch (data[2]) {
            case "01d": i = R.drawable.p01d; break;
            case "01n": i = R.drawable.p01n; break;
            case "02d": i = R.drawable.p02d; break;
            case "02n": i = R.drawable.p02n; break;
            case "03d": i = R.drawable.p03d; break;
            case "03n": i = R.drawable.p03n; break;
            case "04d": i = R.drawable.p04d; break;
            case "04n": i = R.drawable.p04n; break;
            case "09d": i = R.drawable.p09d; break;
            case "09n": i = R.drawable.p09n; break;
            case "10d": i = R.drawable.p10d; break;
            case "10n": i = R.drawable.p10n; break;
            case "11d": i = R.drawable.p11d; break;
            case "11n": i = R.drawable.p11n; break;
            case "13d": i = R.drawable.p13d; break;
            case "13n": i = R.drawable.p13n; break;
            case "50d": i = R.drawable.p50d; break;
            case "50n": i = R.drawable.p50n; break;
        }

        if (i == R.drawable.p01d || i == R.drawable.p01n || i == R.drawable.p09d || i == R.drawable.p09n || i == R.drawable.p11d || i ==  R.drawable.p11n) {
            binding.image.setPadding(0, 25, 0, 25);
        }
        binding.image.setImageResource(i);
/*        Glide.with(binding.image)
                .asBitmap()
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.p50d)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap resized = Bitmap.createBitmap(resource, 15,0, 70,100);
                        binding.image.setImageBitmap(resized);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });*/
    }
}
