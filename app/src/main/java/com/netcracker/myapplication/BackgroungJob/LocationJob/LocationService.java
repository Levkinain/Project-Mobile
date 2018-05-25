package com.netcracker.myapplication.BackgroungJob.LocationJob;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.netcracker.myapplication.Activity.MainActivity;
import com.netcracker.myapplication.Activity.OrderActivity;
import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.Entity.DriverEntity;
import com.netcracker.myapplication.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private boolean currentlyProcessingLocation = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    public static final int UNIQUE_JOB_ID = 1339;
    public static final int INITIAL_DELAY = 5000; // 5 seconds
    public static final int PERIOD = 60000; // 60 seconds

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!currentlyProcessingLocation) {
            currentlyProcessingLocation = true;
            startTracking();
        }

        return START_NOT_STICKY;
    }

    private void startTracking() {
        googleApiClient = new GoogleApiClient.Builder(LocationService.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(LocationService.this)
                .addOnConnectionFailedListener(LocationService.this)
                .build();

        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
        if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
               googleApiClient.connect();
        }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000*10);//время в милисекундах
        locationRequest.setFastestInterval(1000*10);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException se) {
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        stopLocationUpdates();
        stopSelf();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            if (location.getAccuracy() < 500.0f) {
                StringBuilder stringBuilder = new StringBuilder();
                //String  driverLocatoin = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                String driverLocatoin = stringBuilder.append(location.getLatitude()).append(",").append(location.getLongitude()).toString();
                AppDriverAssist.getApplicationPreferences().putString(DriverEntity.DRIVER_GEO_LOCATION, driverLocatoin);
                sendLocationDataToServer();
            }
        }
    }

    private void stopLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void sendLocationDataToServer() {
        long Driver = Long.valueOf(AppDriverAssist.getApplicationPreferences().getString(DriverEntity.ID_DRIVER));
        String geoData = AppDriverAssist.getApplicationPreferences().getString(DriverEntity.DRIVER_GEO_LOCATION);
        //этот код предназначен для отладки
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 2, intent ,
                PendingIntent.FLAG_UPDATE_CURRENT);

        @SuppressWarnings("deprecation") NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Изменилось местоположение:" + geoData)
                        .setAutoCancel(true)
                        .setContentIntent(resultPendingIntent);
        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }

        AppDriverAssist.getApi().changeGeoLocation(Driver, geoData).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean res = response.isSuccessful();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
