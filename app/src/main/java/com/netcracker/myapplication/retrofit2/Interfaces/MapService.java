package com.netcracker.myapplication.retrofit2.Interfaces;

import com.netcracker.myapplication.Entity.MapEntity.MapResult;
import com.netcracker.myapplication.Entity.MapEntity.RouteResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {

    @GET("/maps/api/directions/json")
    Call<MapResult> getRoutes(
            @Query(value = "origin") String position,
            @Query(value = "destination") String destination,
            @Query("language") String language);
}
