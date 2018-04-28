package com.netcracker.myapplication.Activity;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.BackgroungJob.OrderJob.OrderService;
import com.netcracker.myapplication.BackgroungJob.OrderJob.OrderReceiver;
import com.netcracker.myapplication.Entity.DriverEntity;
import com.netcracker.myapplication.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingActivity extends MainActivity {
   private Button goToWorkButton;
   private Button leaveWorkButton;
   private Button callOperatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waiting);

        boolean on_shift = AppDriverAssist.getApplicationPreferences().getBoolean(DriverEntity.DRIVER_ON_SHIFT);
        goToWorkButton = findViewById(R.id.go_to_work_button);
        leaveWorkButton = findViewById(R.id.leave_work_button);
        callOperatorButton = findViewById(R.id.call_operators_button);
        // проверяем, пользователь на смене или нет
        if (on_shift) {
            //если пользователь на смене, то необходимо включить фоновое задание
            goToWorkButton.setVisibility(View.GONE);
            AppDriverAssist.getAlarmService().setAlarm(getApplicationContext(),
                    OrderReceiver.class,
                    OrderService.UNIQUE_JOB_ID,
                    OrderService.INITIAL_DELAY,
                    OrderService.PERIOD,
                    AlarmManager.RTC_WAKEUP);
        } else {
            leaveWorkButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void changeOnShiftClick(View view) {

        if (goToWorkButton.getVisibility() == View.VISIBLE) {
            goToWorkButton.setVisibility(View.GONE);
            leaveWorkButton.setVisibility(View.VISIBLE);
        } else {
            leaveWorkButton.setVisibility(View.GONE);
            goToWorkButton.setVisibility(View.VISIBLE);
        }

        long idDriver = Long.valueOf(AppDriverAssist.getApplicationPreferences().getString(DriverEntity.ID_DRIVER));
        AppDriverAssist.getApi().changeOnShift(idDriver).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    if(goToWorkButton.getVisibility()==View.GONE){

                        AppDriverAssist.getAlarmService().setAlarm(getApplicationContext(),
                                OrderReceiver.class,
                                OrderService.UNIQUE_JOB_ID,
                                OrderService.INITIAL_DELAY,
                                OrderService.PERIOD,
                                AlarmManager.RTC_WAKEUP);

                    }else{
                        AppDriverAssist.getAlarmService().cancelAlarms(getApplicationContext(),OrderReceiver.class,OrderService.UNIQUE_JOB_ID);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t)
            {
                t.printStackTrace();
            }
        });
    }

    public void CallOperator(View view) {
        doCall(AppDriverAssist.OPERATOR_PHONE_NAMBER);
    }

    public void doCall(String phoneNumber) {
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intentCall);
    }
}