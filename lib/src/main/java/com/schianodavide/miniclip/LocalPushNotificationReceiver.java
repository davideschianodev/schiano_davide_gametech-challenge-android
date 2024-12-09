package com.schianodavide.miniclip;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.List;

public class LocalPushNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        int notificationIcon = intent.getIntExtra("notificationIcon", android.R.drawable.ic_dialog_info);

        Intent unityIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (unityIntent != null) {
            unityIntent.putExtra("notificationTitle", title);
            unityIntent.putExtra("notificationMessage", message);
            unityIntent.putExtra("notificationIcon", notificationIcon);
            unityIntent.setAction("OPEN_APP_FROM_NOTIFICATION");

            if (isAppRunning(context)) {
                unityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            } else {
                unityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, notificationId, unityIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "local_push_notifications_channel")
                    .setSmallIcon(notificationIcon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.notify(notificationId, builder.build());
            }
        }
    }

    private boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (context.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}