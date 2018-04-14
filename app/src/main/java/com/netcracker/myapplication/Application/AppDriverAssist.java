package com.netcracker.myapplication.Application;

import android.app.Application;

import com.netcracker.myapplication.SharedPreferences.ApplicationPreferences;
import com.netcracker.myapplication.retrofit2.Interfaces.ApiService;
import com.netcracker.myapplication.retrofit2.Interfaces.MapService;
import com.netcracker.myapplication.retrofit2.Services.RetrofitMapService;
import com.netcracker.myapplication.retrofit2.Services.RetrofitService;


public class AppDriverAssist extends Application {

    private static ApiService apiService;
    private static MapService mapService;
    private static ApplicationPreferences appPreferences;

    public static final String OPERATOR_PHONE_NAMBER = "+79009269218";

    @Override
    public void onCreate(){
        super.onCreate();
        apiService = new RetrofitService(getApplicationContext()).getApiService();
        mapService = new RetrofitMapService(getApplicationContext()).getMapService();
        appPreferences = new ApplicationPreferences(getApplicationContext());
    }

    public static ApiService getApi(){
        return apiService;
    }

    public static MapService getMapApi(){
        return mapService;
    }
    public static  ApplicationPreferences getApplicationPreferences(){
        return appPreferences;
    }
}