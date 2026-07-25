package com.dharshini.medicare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MedicineReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String medicineName = intent.getStringExtra("medicineName");

        if (medicineName == null) {
            medicineName = "your medicine";
        }

        NotificationHelper.showNotification(
                context,
                "Medicine Reminder",
                "Time to take " + medicineName
        );
    }
}