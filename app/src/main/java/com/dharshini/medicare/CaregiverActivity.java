package com.dharshini.medicare.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.dharshini.medicare.R;
import com.dharshini.medicare.models.Caregiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CaregiverActivity extends AppCompatActivity {

    private EditText editCaregiverName, editRelationship, editPhone;
    private Button btnSaveCaregiver;
    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);

        editCaregiverName = findViewById(R.id.editCaregiverName);
        editRelationship = findViewById(R.id.editRelationship);
        editPhone = findViewById(R.id.editPhone);
        btnSaveCaregiver = findViewById(R.id.btnSaveCaregiver);

        db = FirebaseFirestore.getInstance();
        com.google.firebase.auth.FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You are not logged in. Please login first.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        uid = currentUser.getUid();

        loadExistingCaregiver();

        btnSaveCaregiver.setOnClickListener(v -> saveCaregiver());
    }

    private void loadExistingCaregiver() {
        db.collection("users").document(uid)
                .collection("caregiver").document("info")
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        editCaregiverName.setText(doc.getString("name"));
                        editRelationship.setText(doc.getString("relationship"));
                        editPhone.setText(doc.getString("phone"));
                    }
                });
    }

    private void saveCaregiver() {
        String name = editCaregiverName.getText().toString().trim();
        String relationship = editRelationship.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Name and phone are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Caregiver caregiver = new Caregiver(name, relationship, phone);

        db.collection("users").document(uid)
                .collection("caregiver").document("info")
                .set(caregiver)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Caregiver saved", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}