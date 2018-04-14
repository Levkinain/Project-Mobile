package com.netcracker.myapplication.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.BackgroungJob.PollReceiver;
import com.netcracker.myapplication.R;
import com.netcracker.myapplication.Security.TokenService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingActivity extends AppCompatActivity {
   private Button goToWorkButton;
   private Button leaveWorkButton;
   private Button callOperatorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waiting);

        boolean on_shift = AppDriverAssist.getApplicationPreferences().getSharedPreferences().getBoolean(TokenService.DRIVER_ON_SHIFT, false);
        goToWorkButton = findViewById(R.id.go_to_work_button);
        leaveWorkButton = findViewById(R.id.leave_work_button);
        callOperatorButton = findViewById(R.id.call_operators_button);
        // проверяем, пользователь на смене или нет
        if (on_shift) {
            //если пользователь на смене, то необходимо включить фоновое задание
            goToWorkButton.setVisibility(View.GONE);
               PollReceiver.setAlarm(getApplicationContext());
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

        long idDriver = Long.valueOf(AppDriverAssist.getApplicationPreferences().getSharedPreferences().getString(TokenService.ID_DRIVER, ""));
        AppDriverAssist.getApi().changeOnShift(idDriver).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //статус в поле
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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