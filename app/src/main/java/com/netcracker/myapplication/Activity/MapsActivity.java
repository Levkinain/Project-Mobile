package com.netcracker.myapplication.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.Entity.MapEntity.EndLocation;
import com.netcracker.myapplication.Entity.MapEntity.Leg;
import com.netcracker.myapplication.Entity.MapEntity.MapResult;
import com.netcracker.myapplication.Entity.MapEntity.Route;
import com.netcracker.myapplication.Entity.MapEntity.StartLocation;
import com.netcracker.myapplication.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showPoute();
    }

    public void showPoute() {


        AppDriverAssist.getMapApi().getRoutes("51.678858,39.159507", "51.676180,39.150355", "ru").enqueue(new Callback<MapResult>() {
            @Override
            public void onResponse(Call<MapResult> call, Response<MapResult> response) {
                MapResult routes = response.body();
                if (routes != null && !routes.getRoutes().isEmpty()) {
                    Route route = routes.getRoutes().get(0);
                    List<LatLng> mPoints = PolyUtil.decode(route.getOverviewPolyline().getPoints());
                    //Строим полилинию
                    PolylineOptions line = new PolylineOptions();
                    line.width(10f).color(R.color.colorAccent);
                    LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
                    for (int i = 0; i < mPoints.size(); i++) {

                        line.add((LatLng) mPoints.get(i));
                        latLngBuilder.include((LatLng) mPoints.get(i));
                    }

                    Leg leg                     = route.getLegs().get(0);
                    StartLocation startLocation = leg.getStartLocation();
                    EndLocation endLocation     = leg.getEndLocation();
                    mMap.addMarker(new MarkerOptions().position(new LatLng(startLocation.getLat(),startLocation.getLng())).title(leg.getStartAddress()));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(endLocation.getLat(),endLocation.getLng())).title(leg.getEndAddress()));
                    mMap.addPolyline(line);
                    int size = getResources().getDisplayMetrics().widthPixels;
                    LatLngBounds latLngBounds = latLngBuilder.build();
                    CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 25);
                    mMap.moveCamera(track);
                }
            }

            @Override
            public void onFailure(Call<MapResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

