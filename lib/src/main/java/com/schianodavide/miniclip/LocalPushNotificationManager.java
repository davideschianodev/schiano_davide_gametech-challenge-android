package com.schianodavide.miniclip;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

public class LocalPushNotificationManager {
    private static final String CHANNEL_ID = "local_push_notifications_channel";

    public static void scheduleNotifications(Context context) {
        long currentTimeMillis = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        createNotificationChannel(context);

        for (int i = 1; i < 6; i++) {
            Intent intent = getIntent(context, i);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, currentTimeMillis + (i * 60 * 1000), pendingIntent);
            }
        }
    }

    @NonNull
    private static Intent getIntent(Context context, int i) {
        Intent intent = new Intent(context, LocalPushNotificationReceiver.class);
        intent.putExtra("notificationId", i);
        intent.putExtra("title", "Notification n°" + i);
        intent.putExtra("message", "This is the notification number " + i);

        int notificationIcon = android.R.drawable.ic_dialog_info;

        switch (i) {
            case 1: notificationIcon = R.drawable.icon1; break;
            case 2: notificationIcon = R.drawable.icon2; break;
            case 3: notificationIcon = R.drawable.icon3; break;
            case 4: notificationIcon = R.drawable.icon4; break;
            case 5: notificationIcon = R.drawable.icon5; break;
        }

        intent.putExtra("notificationIcon", notificationIcon);
        return intent;
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notifications Channel";
            String description = "Channel dedicated for notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}