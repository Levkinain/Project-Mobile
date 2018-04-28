package com.netcracker.myapplication.retrofit2.Services;

import android.content.Context;

import com.netcracker.myapplication.retrofit2.Interfaces.MapService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitMapService {
    private static final String BASE_URL = "https://maps.googleapis.com/";
    private Retrofit retrofit;
    private MapService mapService;

    public RetrofitMapService(Context applicationContext) {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder().build();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();
        mapService = retrofit.create(MapService.class);
    }

    public MapService getMapService() {
        return mapService;
    }
}

