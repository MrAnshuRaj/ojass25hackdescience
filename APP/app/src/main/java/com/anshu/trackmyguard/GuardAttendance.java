package com.anshu.trackmyguard;

public class GuardAttendance {
    private String name;
    private double latitude;
    private double longitude;
    private boolean outOfZone;
    private String phone;
    public GuardAttendance() {} // Firestore requires empty constructor

    public GuardAttendance(String name, double latitude, double longitude, boolean outOfZone,String phone) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outOfZone = outOfZone;
        this.phone = phone;
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public boolean isOutOfZone() { return outOfZone; }
    public String getPhone() { return phone; }
}
