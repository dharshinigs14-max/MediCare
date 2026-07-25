package com.dharshini.medicare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private OnDeleteClickListener deleteListener;
    private OnEditClickListener editListener;

    // Edit button click interface
    public interface OnEditClickListener {
        void onEditClick(Medicine medicine);
    }

    // Delete button click interface
    public interface OnDeleteClickListener {
        void onDeleteClick(Medicine medicine);
    }

    // Constructor
    public MedicineAdapter(List<Medicine> medicineList,
                           OnDeleteClickListener deleteListener,
                           OnEditClickListener editListener) {

        this.medicineList = medicineList;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);

        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {

        Medicine medicine = medicineList.get(position);

        holder.tvMedicineName.setText(medicine.getMedicineName());
        holder.tvDosage.setText("Dosage: " + medicine.getDosage());
        holder.tvFrequency.setText("Frequency: " + medicine.getFrequency());
        holder.tvTime.setText("Time: " + medicine.getTime());

        // Edit button click
        holder.btnEdit.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onEditClick(medicine);
            }
        });

        // Delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(medicine);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    // ViewHolder
    public static class MedicineViewHolder extends RecyclerView.ViewHolder {

        TextView tvMedicineName;
        TextView tvDosage;
        TextView tvFrequency;
        TextView tvTime;

        Button btnEdit;
        Button btnDelete;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvDosage = itemView.findViewById(R.id.tvDosage);
            tvFrequency = itemView.findViewById(R.id.tvFrequency);
            tvTime = itemView.findViewById(R.id.tvTime);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}