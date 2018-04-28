package com.netcracker.myapplication.Application;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.netcracker.myapplication.BackgroungJob.AlarmService;
import com.netcracker.myapplication.SharedPreferences.ApplicationPreferences;
import com.netcracker.myapplication.retrofit2.Interfaces.ApiService;
import com.netcracker.myapplication.retrofit2.Interfaces.MapService;
import com.netcracker.myapplication.retrofit2.Services.RetrofitMapService;
import com.netcracker.myapplication.retrofit2.Services.RetrofitService;


public class AppDriverAssist extends Application {

    private static ApiService apiService;
    private static MapService mapService;
    private static ApplicationPreferences appPreferences;
    private static FusedLocationProviderClient mFusedLocationClient;
    private static AlarmService alarmService;

    public static final String OPERATOR_PHONE_NAMBER = "+79204185103";

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        apiService = new RetrofitService(context).getApiService();
        mapService = new RetrofitMapService(context).getMapService();
        appPreferences = new ApplicationPreferences(context);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        alarmService = new AlarmService(context);
    }

    public static ApiService getApi() {
        return apiService;
    }

    public static MapService getMapApi() {
        return mapService;
    }

    public static ApplicationPreferences getApplicationPreferences() {
        return appPreferences;
    }

    public static FusedLocationProviderClient getFusedLocationProviderClient() {
        return mFusedLocationClient;
    }

    public static AlarmService getAlarmService() {
        return alarmService;
    }
}


///////////////////Нужно отработать отказ от заказа.