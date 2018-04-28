package com.netcracker.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.netcracker.myapplication.BackgroungJob.AlarmService;
import com.netcracker.myapplication.BackgroungJob.LocationJob.LocationService;

public class MainActivity extends AppCompatActivity {
   private AlarmService alarmService;
   private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        serviceIntent = new Intent(this, LocationService.class);
    }

    @Override
    protected void onStart(){
         super.onStart();
        this.startService(serviceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.stopService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
