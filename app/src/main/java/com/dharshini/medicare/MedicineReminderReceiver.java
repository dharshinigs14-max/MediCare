package com.dharshini.medicare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MedicineReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String medicineId = intent.getStringExtra("medicineId");
        String medicineName = intent.getStringExtra("medicineName");
        String scheduledTime = intent.getStringExtra("scheduledTime");

        if (medicineName == null) {
            medicineName = "your medicine";
        }

        NotificationHelper.showNotification(
                context,
                "Medicine Reminder",
                "Time to take " + medicineName
        );

        saveHistoryRecord(medicineId, medicineName, scheduledTime);
    }

    private void saveHistoryRecord(String medicineId, String medicineName, String scheduledTime) {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new java.util.Date());

        Map<String, Object> history = new HashMap<>();
        history.put("medicineId", medicineId);
        history.put("medicineName", medicineName);
        history.put("scheduledTime", scheduledTime);
        history.put("date", today);
        history.put("takenTime", null);
        history.put("status", "Pending");

        FirebaseFirestore.getInstance()
                .collection("medicine_history")
                .add(history);
    }
}