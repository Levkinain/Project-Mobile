package com.netcracker.myapplication.retrofit2.Interfaces;


import com.netcracker.myapplication.Entity.OrderEntityTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ApiService {

    @GET("login")
    Call<ResponseBody> getAuth();

    @GET("drivers/{id}/IsOnShift")
    Call<ResponseBody> getDriverIsOnShiftById(@Path("id") long id);

    @GET("drivers/{id}/getOrder")
    Call<OrderEntityTO> getOrderByDriverId(@Path("id") long id);

    @PATCH("drivers/changeOnShift")
    Call<ResponseBody> changeOnShift(@Body long id);

    @PATCH("orders/closeorder")
    Call<ResponseBody> closeOrder(@Body OrderEntityTO order);

    @PATCH("orders/pickclient")
    Call<ResponseBody> pickClient(@Body OrderEntityTO order);

    @PATCH("drivers/{id}/changeGeoLocation")
    Call<ResponseBody> changeGeoLocation(@Path("id") long id, @Body String geoData);

}
