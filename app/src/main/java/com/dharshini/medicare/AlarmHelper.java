package com.dharshini.medicare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Calendar;
import android.os.Build;
import android.content.Context;

public class AlarmHelper {

    public static void scheduleMedicineReminder(
            Context context,
            String medicineId,
            String medicineName,
            String time
    ) {

        // Convert "08:30" into hour and minute
        String[] parts = time.split(":");

        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // If today's time has already passed,
        // schedule for tomorrow.
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, MedicineReminderReceiver.class);

        intent.putExtra("medicineId", medicineId);
        intent.putExtra("medicineName", medicineName);
        intent.putExtra("scheduledTime", time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                medicineName.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    Log.e("AlarmHelper", "Exact alarms are NOT allowed.");
                    return;
                }
            }

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            Log.d(
                    "AlarmHelper",
                    "Alarm scheduled for: " + calendar.getTime()
            );
        }
    }
    public static void scheduleSnoozeReminder(
            Context context,
            String medicineId,
            String medicineName
    ) {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, 5);
        Intent intent = new Intent(context, MedicineReminderReceiver.class);

        intent.putExtra("medicineId", medicineId);
        intent.putExtra("medicineName", medicineName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                medicineName.hashCode() + 100,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }

    }
    public static void scheduleMissedCheck(
            Context context,
            String historyDocId,
            String medicineName,
            long reminderTimeMillis
    ) {
        Intent intent = new Intent(context, MissedCheckReceiver.class);
        intent.putExtra("historyDocId", historyDocId);
        intent.putExtra("medicineName", medicineName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                historyDocId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long triggerTime = reminderTimeMillis + (1 * 60 * 1000); // +15 minutes

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
            );
        }
    }public static void requestExactAlarmPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(android.net.Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
            }
        }
    }

}