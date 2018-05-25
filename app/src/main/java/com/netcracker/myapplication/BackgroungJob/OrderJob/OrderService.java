package com.netcracker.myapplication.BackgroungJob.OrderJob;

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

import com.netcracker.myapplication.Activity.OrderActivity;
import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.Entity.DriverEntity;
import com.netcracker.myapplication.Entity.OrderEntityTO;
import com.netcracker.myapplication.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService extends JobIntentService {
    public static final int UNIQUE_JOB_ID = 1337;
    public static final int INITIAL_DELAY = 5000; // 5 seconds
    public static final int PERIOD = 60000; // 60 seconds
    private static Context contxt;
    public NotificationManager notificationManager;

        static void enqueueWork(Context context) {
            contxt = context;
            enqueueWork(contxt, OrderService.class, 0,
                new Intent(contxt, OrderService.class));
    }

    @Override
    public void onHandleWork(@NonNull Intent i) {

            long idDriver = Long.valueOf(AppDriverAssist.getApplicationPreferences().getString(DriverEntity.ID_DRIVER));

        AppDriverAssist.getApi().getOrderByDriverId(idDriver).enqueue(new Callback<OrderEntityTO>() {
            @Override
            public void onResponse(Call<OrderEntityTO> call, Response<OrderEntityTO> response) {
                if(response.isSuccessful()){

                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    if(response.body()!= null){

                        OrderEntityTO orderEntityTO = response.body();
                        AppDriverAssist.getApplicationPreferences().saveObject(orderEntityTO);
                        AppDriverAssist.getAlarmService().cancelAlarms(contxt,OrderReceiver.class, OrderService.UNIQUE_JOB_ID);


                        Intent intent = new Intent(contxt, OrderActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent resultPendingIntent = PendingIntent.getActivity(contxt, 1, intent ,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        @SuppressWarnings("deprecation") NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(contxt)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("Новый заказ")
                                        .setContentText(orderEntityTO.getName())
                                        .setAutoCancel(true)
                                       // .setOngoing(true)
                                        .setContentIntent(resultPendingIntent);

                        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        builder.setSound(uri);
                        Notification notification = builder.build();

                        if (notificationManager != null) {
                            notificationManager.notify(1, notification);
                        }

                        startActivity(intent);
                    }
                }
            }


            @Override
            public void onFailure(Call<OrderEntityTO> call, Throwable t) {
            }
        });

    }

}