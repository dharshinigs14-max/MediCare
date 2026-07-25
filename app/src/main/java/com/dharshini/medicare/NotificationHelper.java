package com.dharshini.medicare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    public static final String CHANNEL_ID = "medicine_reminder_channel";
    public static final String CHANNEL_NAME = "Medicine Reminders";
    public static final String CHANNEL_DESCRIPTION = "Notifications for medicine reminders";

    public static void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription(CHANNEL_DESCRIPTION);

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Displays a notification with Take Now / Snooze action buttons
     */
    public static void showNotification(Context context,
                                        String title,
                                        String message,
                                        String medicineHistoryId,
                                        String medicineId,
                                        String medicineName,
                                        String scheduledTime
    ) {

        Intent takeNowIntent = new Intent(context, NotificationActionReceiver.class);
        takeNowIntent.setAction("ACTION_TAKE_NOW");
        takeNowIntent.putExtra("medicineHistoryId", medicineHistoryId);
        takeNowIntent.putExtra("medicineId", medicineId);
        takeNowIntent.putExtra("medicineName", medicineName);
        takeNowIntent.putExtra("scheduledTime", scheduledTime);

        PendingIntent takeNowPendingIntent = PendingIntent.getBroadcast(
                context,
                medicineHistoryId.hashCode() + 1,
                takeNowIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent snoozeIntent = new Intent(context, NotificationActionReceiver.class);
        snoozeIntent.setAction("ACTION_SNOOZE");
        snoozeIntent.putExtra("medicineHistoryId", medicineHistoryId);
        snoozeIntent.putExtra("medicineId", medicineId);
        snoozeIntent.putExtra("medicineName", medicineName);
        snoozeIntent.putExtra("scheduledTime", scheduledTime);

        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(
                context,
                medicineHistoryId.hashCode() + 2,
                snoozeIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .addAction(0, "Take Now", takeNowPendingIntent)
                        .addAction(0, "Snooze", snoozePendingIntent);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        notificationManager.notify(1001, builder.build());
    }
}