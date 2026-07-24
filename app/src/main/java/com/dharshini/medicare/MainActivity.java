package com.dharshini.medicare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        Button addMedicineButton = findViewById(R.id.addMedicineButton);

        addMedicineButton.setOnClickListener(v -> {
            saveTestMedicine();
        });
    }

    private void saveTestMedicine() {
        Map<String, Object> medicine = new HashMap<>();
        medicine.put("name", "Paracetamol");
        medicine.put("time", "08:00");

        db.collection("medicines")
                .add(medicine)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Saved! ID: " + documentReference.getId(), Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}