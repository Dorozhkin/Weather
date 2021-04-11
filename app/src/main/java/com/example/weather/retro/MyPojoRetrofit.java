package com.example.weather.retro;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyPojoRetrofit {

    private final static String SCHEME = "http";
    private final static String HOST = "api.openweathermap.org";
    private final static String PATH = "data/2.5/";

    private static MyPojoRetrofit myPojoRetrofit;
    private final RetrofitInterface retrofitInterface;

    private MyPojoRetrofit() {

        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME)
                .host(HOST)
                .addPathSegments(PATH)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public static RetrofitInterface getRetrofitInterface() {
        if (myPojoRetrofit == null) {
            myPojoRetrofit = new MyPojoRetrofit();
        }
        return myPojoRetrofit.retrofitInterface;
    }
}
