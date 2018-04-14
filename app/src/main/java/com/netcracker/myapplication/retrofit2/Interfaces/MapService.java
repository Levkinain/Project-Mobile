package com.netcracker.myapplication.retrofit2.Interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MapService {
    @GET("login")
    Call<ResponseBody> getAuth();
}
