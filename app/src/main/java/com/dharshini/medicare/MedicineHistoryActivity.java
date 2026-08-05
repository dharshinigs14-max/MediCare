package com.dharshini.medicare.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dharshini.medicare.R;
import com.dharshini.medicare.adapter.MedicineHistoryAdapter;
import com.dharshini.medicare.model.MedicineHistory;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MedicineHistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private TextView tvEmpty;
    private MedicineHistoryAdapter adapter;
    private final List<MedicineHistory> historyList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_history);

        Toolbar toolbar = findViewById(R.id.toolbarHistory);
        setSupportActionBar(toolbar);

        rvHistory = findViewById(R.id.rvMedicineHistory);
        tvEmpty = findViewById(R.id.tvEmptyHistory);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineHistoryAdapter(historyList);
        rvHistory.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadHistory();
    }

    private void loadHistory() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (uid == null) {
            android.widget.Toast.makeText(this, "Not logged in", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("medicine_history")
                .whereEqualTo("uid", uid)
                .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(this::onHistoryLoaded)
                .addOnFailureListener(e ->
                        android.widget.Toast.makeText(this,
                                "Failed to load history: " + e.getMessage(),
                                android.widget.Toast.LENGTH_SHORT).show());
    }

    private void onHistoryLoaded(QuerySnapshot snapshots) {
        historyList.clear();
        for (var doc : snapshots) {
            MedicineHistory item = doc.toObject(MedicineHistory.class);
            historyList.add(item);
        }
        adapter.notifyDataSetChanged();

        tvEmpty.setVisibility(historyList.isEmpty() ? View.VISIBLE : View.GONE);
        rvHistory.setVisibility(historyList.isEmpty() ? View.GONE : View.VISIBLE);
    }
}