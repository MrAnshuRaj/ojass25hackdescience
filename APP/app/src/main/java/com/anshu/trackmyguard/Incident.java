package com.anshu.trackmyguard;
public class Incident {
    private String title, description;
    private double latitude, longitude;


    public Incident() { }

    public Incident(String title, String description, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
