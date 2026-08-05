package com.dharshini.medicare;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsHelper {
    public static void sendCaregiverAlert(Context context, String caregiverPhone, String medicineName) {
        if (caregiverPhone == null || caregiverPhone.trim().isEmpty()) {
            Log.d("SmsHelper", "No caregiver phone set, skipping SMS");
            return;
        }
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String message = "MediCare Alert: Patient missed dose of " + medicineName + ". Please check on them.";
            smsManager.sendTextMessage(caregiverPhone, null, message, null, null);
            Log.d("SmsHelper", "SMS sent to " + caregiverPhone);
        } catch (Exception e) {
            Log.e("SmsHelper", "SMS failed: " + e.getMessage());
        }
    }
}