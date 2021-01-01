package com.handy.test2.Model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.handy.test2.R;
import com.handy.test2.View.MainActivity;

public class NotificationUtils {
    private static final String MAIN_CHANNEL_ID = "mainChannelId";

    public static void createMainNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelName = context.getString(R.string.main_channel);
            String channelDescription = context.getString(R.string.main_channel_description);
            NotificationChannel notificationChannel = new NotificationChannel(
                    MAIN_CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription(channelDescription);
            notificationChannel.setVibrationPattern(new long[] {0, 500, 700, 900});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static void showBasicNotification(Context context, String title, String text){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MAIN_CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setSmallIcon(R.drawable.ic_huf);
        builder.setContentText(text);
        builder.setTicker(title);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setVibrate(new long[] {0, 500, 700, 900});
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, notification);
    }
}
