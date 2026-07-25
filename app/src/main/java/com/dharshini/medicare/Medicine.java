package com.dharshini.medicare;

public class Medicine {

    private String medicineName;
    private String dosage;
    private String id;
    private String frequency;
    private String time;


    public Medicine() {
        // Required empty constructor for Firebase
    }


    public Medicine(String medicineName, String dosage, String frequency, String time) {
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.time = time;
    }


    public String getMedicineName() {
        return medicineName;
    }


    public String getDosage() {
        return dosage;
    }


    public String getFrequency() {
        return frequency;
    }


    public String getTime() {
        return time;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}