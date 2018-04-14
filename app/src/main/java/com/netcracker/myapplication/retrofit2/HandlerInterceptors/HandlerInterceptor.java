package com.netcracker.myapplication.retrofit2.HandlerInterceptors;

import android.content.Context;
import android.content.SharedPreferences;

import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.Security.TokenService;
import com.netcracker.myapplication.SharedPreferences.ApplicationPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class HandlerInterceptor implements Interceptor {

    private Context context;

    public HandlerInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
            String token = AppDriverAssist.getApplicationPreferences().getSharedPreferences().getString(TokenService.AUTH, "");
            Request request = chain.request().newBuilder().header("Authorization", token).build();
            return chain.proceed(request);
    }
}
