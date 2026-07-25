package com.dharshini.medicare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        String medicineHistoryId = intent.getStringExtra("medicineHistoryId");
        String medicineId = intent.getStringExtra("medicineId");
        String medicineName = intent.getStringExtra("medicineName");
        String scheduledTime = intent.getStringExtra("scheduledTime");

        if (medicineHistoryId == null) {
            return;
        }

        if ("ACTION_TAKE_NOW".equals(action)) {

            String now = new SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(new java.util.Date());

            Map<String, Object> update = new HashMap<>();
            update.put("status", "Taken");
            update.put("takenTime", now);

            FirebaseFirestore.getInstance()
                    .collection("medicine_history")
                    .document(medicineHistoryId)
                    .update(update);

            Toast.makeText(context, "Marked as Taken", Toast.LENGTH_SHORT).show();

        } else if ("ACTION_SNOOZE".equals(action)) {

            Map<String, Object> update = new HashMap<>();
            update.put("status", "Snoozed");

            FirebaseFirestore.getInstance()
                    .collection("medicine_history")
                    .document(medicineHistoryId)
                    .update(update);
            AlarmHelper.scheduleSnoozeReminder(
                    context,
                    medicineId,
                    medicineName
            );
            Toast.makeText(context, "Snoozed", Toast.LENGTH_SHORT).show();
        }

        // Dismiss the notification after either action
        NotificationManagerCompat.from(context).cancel(1001);
    }
}