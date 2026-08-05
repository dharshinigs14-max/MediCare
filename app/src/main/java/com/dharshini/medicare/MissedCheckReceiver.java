package com.dharshini.medicare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MissedCheckReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String historyDocId = intent.getStringExtra("historyDocId");
        String medicineName = intent.getStringExtra("medicineName");
        if (historyDocId == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("medicine_history").document(historyDocId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) return;

                    String currentStatus = snapshot.getString("status");
                    if ("Pending".equals(currentStatus)) {
                        db.collection("medicine_history").document(historyDocId)
                                .update("status", "Missed")
                                .addOnSuccessListener(unused ->
                                        notifyCaregiver(context, medicineName));
                    }
                });
    }

    private void notifyCaregiver(Context context, String medicineName) {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (uid == null) return;

        FirebaseFirestore.getInstance()
                .collection("users").document(uid)
                .collection("caregiver").document("info")
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String phone = doc.getString("phone");
                        SmsHelper.sendCaregiverAlert(context, phone, medicineName);
                    }
                });
    }
}