package com.netcracker.myapplication.retrofit2.Services;

import android.content.Context;

import com.netcracker.myapplication.retrofit2.Interfaces.ApiService;
import com.netcracker.myapplication.retrofit2.HandlerInterceptors.HandlerInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitService {

   // private static final String BASE_URL = "http://192.168.43.116:8082";
    private static final String BASE_URL = "http://192.168.43.87:8082";
    // private static final String BASE_URL = "http://10.100.36.74:8082";
      // private static final String BASE_URL = "http://10.254.18.248:8082";
    //10.254.18.248
    //вый фай дома
    //private static final String BASE_URL = "http://192.168.88.235:8082/";
   // private static final String BASE_URL = "http://192.168.0.106:8082/";

    private Retrofit retrofit;
    private ApiService apiService;

    public RetrofitService(Context applicationContext) {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(new HandlerInterceptor(applicationContext)).build();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }
}
