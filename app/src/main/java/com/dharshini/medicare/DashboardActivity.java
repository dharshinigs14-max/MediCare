package com.dharshini.medicare.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dharshini.medicare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvCompleted, tvPending, tvMissed, tvAdherence;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbarDashboard);
        setSupportActionBar(toolbar);

        tvCompleted = findViewById(R.id.tvCompletedCount);
        tvPending = findViewById(R.id.tvPendingCount);
        tvMissed = findViewById(R.id.tvMissedCount);
        tvAdherence = findViewById(R.id.tvAdherencePercent);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTodayStats();
    }

    private void loadTodayStats() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (uid == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new java.util.Date());

        db.collection("medicine_history")
                .whereEqualTo("uid", uid)
                .whereEqualTo("date", today)
                .get()
                .addOnSuccessListener(this::onStatsLoaded)
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load dashboard: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    private void onStatsLoaded(QuerySnapshot snapshots) {
        int completed = 0, pending = 0, missed = 0, total = 0;

        for (var doc : snapshots) {
            String status = doc.getString("status");
            if (status == null) continue;
            total++;
            switch (status) {
                case "Taken":
                    completed++;
                    break;
                case "Pending":
                case "Snoozed":
                    pending++;
                    break;
                case "Missed":
                    missed++;
                    break;
            }
        }

        int adherencePercent = total == 0 ? 0 : Math.round((completed * 100f) / total);

        tvCompleted.setText(String.valueOf(completed));
        tvPending.setText(String.valueOf(pending));
        tvMissed.setText(String.valueOf(missed));
        tvAdherence.setText(adherencePercent + "%");
    }
}