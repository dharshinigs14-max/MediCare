package com.dharshini.medicare.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dharshini.medicare.R;
import com.dharshini.medicare.model.MedicineHistory;

import java.util.List;

public class MedicineHistoryAdapter extends RecyclerView.Adapter<MedicineHistoryAdapter.HistoryViewHolder> {

    private final List<MedicineHistory> historyList;

    public MedicineHistoryAdapter(List<MedicineHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        MedicineHistory item = historyList.get(position);

        holder.tvName.setText(item.getMedicineName());
        holder.tvDateTime.setText(item.getDate() + "  •  " + item.getScheduledTime());
        holder.tvStatus.setText(item.getStatus());

        switch (item.getStatus()) {
            case "Taken":
                holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // green
                break;
            case "Missed":
                holder.tvStatus.setTextColor(Color.parseColor("#C62828")); // red
                break;
            case "Snoozed":
                holder.tvStatus.setTextColor(Color.parseColor("#EF6C00")); // orange
                break;
            default:
                holder.tvStatus.setTextColor(Color.parseColor("#616161")); // grey (Pending)
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDateTime, tvStatus;

        HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvHistoryMedicineName);
            tvDateTime = itemView.findViewById(R.id.tvHistoryDateTime);
            tvStatus = itemView.findViewById(R.id.tvHistoryStatus);
        }
    }
}