package com.example.weather.retro;

import com.example.weather.FromJson.MainClassFromJson;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RetrofitInterface {

    @GET("onecall?exclude=minutely,alerts&units=metric&lang=ru")
    Call<MainClassFromJson> getFromNetByCoordinates(@QueryMap Map<String, String> coordinates);
}
