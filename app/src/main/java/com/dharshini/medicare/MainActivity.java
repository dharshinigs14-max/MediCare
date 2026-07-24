package com.dharshini.medicare; // match your actual package name

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addMedicineButton = findViewById(R.id.addMedicineButton);

        addMedicineButton.setOnClickListener(v -> {
            Toast.makeText(this, "Add Medicine screen coming soon!", Toast.LENGTH_SHORT).show();
        });
    }
}