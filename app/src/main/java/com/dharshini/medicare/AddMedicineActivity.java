package com.dharshini.medicare;

import com.google.firebase.firestore.FirebaseFirestore;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.TimePickerDialog;
import java.util.Calendar;
import java.util.Locale;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class AddMedicineActivity extends AppCompatActivity {
    private boolean isEditMode = false;
    private String medicineId = "";
    EditText etMedicineName;
    EditText etDosage;
    EditText etFrequency;

    Button btnSelectTime;
    Button btnSaveMedicine;

    TextView tvSelectedTime;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        db = FirebaseFirestore.getInstance();


        etMedicineName = findViewById(R.id.etMedicineName);
        etDosage = findViewById(R.id.etDosage);
        etFrequency = findViewById(R.id.etFrequency);

        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSaveMedicine = findViewById(R.id.btnSaveMedicine);

        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        Intent intent = getIntent();

        if (intent.hasExtra("medicineId")) {

            isEditMode = true;

            medicineId = intent.getStringExtra("medicineId");

            etMedicineName.setText(intent.getStringExtra("medicineName"));
            etDosage.setText(intent.getStringExtra("dosage"));
            etFrequency.setText(intent.getStringExtra("frequency"));
            tvSelectedTime.setText(intent.getStringExtra("time"));

            btnSaveMedicine.setText("Update Medicine");
        }
        btnSelectTime.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);


            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    AddMedicineActivity.this,
                    (view, selectedHour, selectedMinute) -> {

                        String time = String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                selectedHour,
                                selectedMinute
                        );

                        tvSelectedTime.setText(time);

                    },
                    hour,
                    minute,
                    true
            );


            timePickerDialog.show();

        });
        btnSaveMedicine.setOnClickListener(v -> {

            String medicineName = etMedicineName.getText().toString();
            String dosage = etDosage.getText().toString();
            String frequency = etFrequency.getText().toString();
            String time = tvSelectedTime.getText().toString();


            Medicine medicine = new Medicine(
                    medicineName,
                    dosage,
                    frequency,
                    time
            );


            if (isEditMode) {

                db.collection("medicines")
                        .document(medicineId)
                        .set(medicine)
                        .addOnSuccessListener(unused -> {

                            AlarmHelper.scheduleMedicineReminder(
                                    AddMedicineActivity.this,
                                    medicineName,
                                    time
                            );

                            Toast.makeText(
                                    AddMedicineActivity.this,
                                    "Medicine Updated Successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();

                        })
                        .addOnFailureListener(e -> {

                            Toast.makeText(
                                    AddMedicineActivity.this,
                                    "Update Failed: " + e.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                        });

            } else {

                db.collection("medicines")
                        .add(medicine)
                        .addOnSuccessListener(documentReference -> {

                            AlarmHelper.scheduleMedicineReminder(
                                    AddMedicineActivity.this,
                                    medicineName,
                                    time
                            );

                            Toast.makeText(
                                    AddMedicineActivity.this,
                                    "Medicine Saved Successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();

                        })
                        .addOnFailureListener(e -> {

                            Toast.makeText(
                                    AddMedicineActivity.this,
                                    "Save Failed: " + e.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();

                        });

            }

        });


    }


}