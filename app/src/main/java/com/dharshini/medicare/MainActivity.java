package com.dharshini.medicare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import com.google.firebase.firestore.FirebaseFirestore;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import com.dharshini.medicare.activity.MedicineHistoryActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import com.dharshini.medicare.activities.CaregiverActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerMedicines;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationHelper.createNotificationChannel(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        101
                );
            }
        }
        Button btnViewHistory = findViewById(R.id.btnViewHistory);
        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MedicineHistoryActivity.class);
            startActivity(intent);
        });

        db = FirebaseFirestore.getInstance();
        recyclerMedicines = findViewById(R.id.recyclerMedicines);

        medicineList = new ArrayList<>();

        medicineAdapter = new MedicineAdapter(
                medicineList,

                // Delete Listener
                medicine -> {
                    deleteMedicine(medicine);
                },

                // Edit Listener
                medicine -> {

                    Intent intent = new Intent(MainActivity.this, AddMedicineActivity.class);

                    intent.putExtra("medicineId", medicine.getId());
                    intent.putExtra("medicineName", medicine.getMedicineName());
                    intent.putExtra("dosage", medicine.getDosage());
                    intent.putExtra("frequency", medicine.getFrequency());
                    intent.putExtra("time", medicine.getTime());

                    startActivity(intent);
                }
        );
        recyclerMedicines.setLayoutManager(new LinearLayoutManager(this));
        recyclerMedicines.setAdapter(medicineAdapter);

        Button addMedicineButton = findViewById(R.id.addMedicineButton);

        addMedicineButton.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, AddMedicineActivity.class);

            startActivity(intent);

        });
        loadMedicines();
        findViewById(R.id.btnCaregiver).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CaregiverActivity.class)));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 101);
        }
        AlarmHelper.requestExactAlarmPermission(this);
        startActivity(new Intent(this, com.dharshini.medicare.activity.DashboardActivity.class));
    }
    private void loadMedicines() {

        db.collection("medicines")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    medicineList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                        Medicine medicine = document.toObject(Medicine.class);
                        medicine.setId(document.getId());
                        medicineList.add(medicine);
                    }

                    medicineAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,
                            "Failed to load medicines",
                            Toast.LENGTH_SHORT).show();
                });
    }
    private void deleteMedicine(Medicine medicine) {

        db.collection("medicines")
                .document(medicine.getId())
                .delete()
                .addOnSuccessListener(unused -> {

                    Toast.makeText(this,
                            "Medicine deleted successfully",
                            Toast.LENGTH_SHORT).show();

                    loadMedicines();

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(this,
                            "Failed to delete medicine",
                            Toast.LENGTH_SHORT).show();

                });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadMedicines();
    }



}