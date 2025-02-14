package com.anshu.trackmyguard;


public class Guard {
    private String name, pastWork, residence, currentDeployment, photoUrl;
    private int age;

    public Guard() { } // Required for Firestore

    public Guard(String name, int age, String pastWork, String residence, String currentDeployment, String photoUrl) {
        this.name = name;
        this.age = age;
        this.pastWork = pastWork;
        this.residence = residence;
        this.currentDeployment = currentDeployment;
        this.photoUrl = photoUrl;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPastWork() { return pastWork; }
    public String getResidence() { return residence; }
    public String getCurrentDeployment() { return currentDeployment; }
    public String getPhotoUrl() { return photoUrl; }
}
