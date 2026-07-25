package com.dharshini.medicare.model;

public class MedicineHistory {

    private String medicineId;
    private String medicineName;
    private String scheduledTime;
    private String date;
    private String takenTime;
    private String status;

    // Required empty constructor for Firestore
    public MedicineHistory() {
    }

    public MedicineHistory(String medicineId, String medicineName, String scheduledTime,
                           String date, String takenTime, String status) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.scheduledTime = scheduledTime;
        this.date = date;
        this.takenTime = takenTime;
        this.status = status;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(String takenTime) {
        this.takenTime = takenTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}