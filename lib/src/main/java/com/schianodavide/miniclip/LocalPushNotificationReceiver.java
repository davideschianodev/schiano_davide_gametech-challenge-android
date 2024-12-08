package com.schianodavide.miniclip;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class LocalPushNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        int notificationIcon = intent.getIntExtra("notificationIcon", android.R.drawable.ic_dialog_info);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "local_push_notifications_channel")
                .setSmallIcon(notificationIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(notificationId, builder.build());
        }
    }
}