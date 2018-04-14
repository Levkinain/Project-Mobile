package com.netcracker.myapplication.BackgroungJob;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.netcracker.myapplication.Activity.OrderActivity;
import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.Entity.OrderEntityTO;
import com.netcracker.myapplication.R;
import com.netcracker.myapplication.Security.TokenService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.netcracker.myapplication.Application.AppDriverAssist.getApi;

public class ScheduledService extends JobIntentService {
    private static final int UNIQUE_JOB_ID = 1337;
    private static Context ctxt;

        static void enqueueWork(Context context) {
            System.out.println("");
            ctxt = context;
            enqueueWork(context, ScheduledService.class, UNIQUE_JOB_ID,
                new Intent(context, ScheduledService.class));
    }

    @Override
    public void onHandleWork(@NonNull Intent i) {

        System.out.println("onHandleWork");

            long idDriver = Long.valueOf(AppDriverAssist.getApplicationPreferences().getSharedPreferences().getString(TokenService.ID_DRIVER, ""));

        getApi().getOrderByDriverId(idDriver).enqueue(new Callback<OrderEntityTO>() {
            @Override
            public void onResponse(Call<OrderEntityTO> call, Response<OrderEntityTO> response) {
                if(response.isSuccessful()){
                    if(response.body()!= null){
                        OrderEntityTO orderEntityTO = response.body();
                        AppDriverAssist.getApplicationPreferences().saveObject(orderEntityTO);
                        //не уверена, что будет правильно работать
                        // создаем уведомление
                        // Create PendingIntent
                        PollReceiver.cancelAlarms(ctxt);
                        Intent intent = new Intent(ctxt, OrderActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent resultPendingIntent = PendingIntent.getActivity(ctxt, 0, intent ,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        @SuppressWarnings("deprecation") NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(ctxt)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("Новый заказ")
                                        .setContentText(orderEntityTO.getName())
                                        .setAutoCancel(true)
                                       // .setOngoing(true)
                                        .setContentIntent(resultPendingIntent);

                        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        builder.setSound(uri);
                        Notification notification = builder.build();

                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        if (notificationManager != null) {
                            notificationManager.notify(1, notification);
                        }

                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderEntityTO> call, Throwable t) {
               // Здесь ничего не делаем t.printStackTrace();
            }
        });

    }
}