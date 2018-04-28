package com.netcracker.myapplication.BackgroungJob;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.netcracker.myapplication.BackgroungJob.OrderJob.OrderReceiver;

public class AlarmService {

    private static android.app.AlarmManager mgr;

    public AlarmService(Context context) {
        this.mgr = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);;
    }


    public static void setAlarm(Context context,Class clazz,int idRequestcode, int initialDelay,int period,int allarmOption ) {

        // создаем интент на основе класса
        Intent intent = new Intent(context, clazz);
        PendingIntent pi = PendingIntent.getBroadcast(context, idRequestcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //говорим, что этот интент будет посвторяться каждые initialDelay секунд
        if (mgr != null) {
            mgr.setRepeating(allarmOption,
                    initialDelay,
                    period, pi);
        }
    }

    public static void cancelAlarms(Context context, Class clazz, int idRequestcode ) {
        // создаем интент на основе класса
        Intent intent = new Intent(context, clazz);

        PendingIntent pi = PendingIntent.getBroadcast(context, idRequestcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (mgr != null) {
            mgr.cancel(pi );
        }
    }
}
