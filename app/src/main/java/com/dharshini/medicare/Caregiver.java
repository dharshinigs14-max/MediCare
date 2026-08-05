package com.dharshini.medicare.models;

public class Caregiver {
    private String name;
    private String relationship;
    private String phone;
    private String fcmToken; // needed later for push notifications

    public Caregiver() {
        // Empty constructor required for Firestore deserialization
    }

    public Caregiver(String name, String relationship, String phone) {
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFcmToken() { return fcmToken; }
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
}