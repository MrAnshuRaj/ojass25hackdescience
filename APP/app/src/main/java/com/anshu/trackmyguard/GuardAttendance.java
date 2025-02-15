package com.anshu.trackmyguard;

public class GuardAttendance {
    private String name;
    private double latitude;
    private double longitude;
    private boolean outOfZone;

    public GuardAttendance() {} // Firestore requires empty constructor

    public GuardAttendance(String name, double latitude, double longitude, boolean outOfZone) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outOfZone = outOfZone;
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public boolean isOutOfZone() { return outOfZone; }
}
