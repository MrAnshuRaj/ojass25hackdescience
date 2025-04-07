package com.anshu.trackmyguard;

import java.util.Date;

public class Incident {
    private String title, description;
    private double latitude, longitude;
    private Date timestamp;

    public Incident() { }

    public Incident(String title, String description, double latitude, double longitude, Date timestamp) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public Date getTimestamp() {
        return timestamp;
    }
}
