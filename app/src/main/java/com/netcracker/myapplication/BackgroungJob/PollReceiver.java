package com.netcracker.myapplication.BackgroungJob;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

public class PollReceiver extends BroadcastReceiver {

    private static final int INITIAL_DELAY = 5000; // 5 seconds
    private static final int PERIOD = 60000; // 60 seconds

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) {
           //Здесь проверка, какой сервис выполнять
            ScheduledService.enqueueWork(context);
        }
        else {
            setAlarm(context);
        }

    }

   public static void setAlarm(Context context) {
        //получаем доступ к сервису
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // создаем интент на основе класса
        Intent intent = new Intent(context, PollReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //говорим, что этот интент будет посвторяться каждые 10 секунд
       if (mgr != null) {
           mgr.setRepeating(AlarmManager.RTC_WAKEUP,
                   INITIAL_DELAY,
                   PERIOD, pi);
       }
   }

    public static void cancelAlarms(Context context) {
        System.out.println("Фоновое завеншилось");
        //получаем доступ к сервису
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // создаем интент на основе класса
        Intent intent = new Intent(context, PollReceiver.class);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mgr != null) {
            mgr.cancel(pi);
        }

    }
}
