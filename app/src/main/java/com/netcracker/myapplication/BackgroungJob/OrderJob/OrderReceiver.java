package com.netcracker.myapplication.BackgroungJob.OrderJob;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.BackgroungJob.AlarmService;

public class OrderReceiver extends BroadcastReceiver {

    private AlarmService alarmService;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.alarmService = AppDriverAssist.getAlarmService();
        if (intent.getAction() == null) {
                OrderService.enqueueWork(context);
        } else {
            AppDriverAssist.getAlarmService().setAlarm(context,
                    OrderReceiver.class,
                    OrderService.UNIQUE_JOB_ID,
                    OrderService.INITIAL_DELAY,
                    OrderService.PERIOD,
                    AlarmManager.RTC_WAKEUP);
        }
    }
}
