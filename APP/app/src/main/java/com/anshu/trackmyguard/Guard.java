package com.anshu.trackmyguard;

public class Guard {
    private String id;
    private String username;
    private String phone;
    private String organization;
    private int age;
    private boolean isVerified;

    // Empty constructor required for Firestore
    public Guard() { }

    public Guard(String id, String username, int age, String phone, String organization,boolean verified) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.phone = phone;
        this.organization = organization;
        this.isVerified = verified;
    }

    // Getter and Setter for ID
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // Getters and Setters for other fields
    public String getUserName() { return username; }
    public void setUserName(String name) { this.username = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    public boolean isVerified() { return isVerified; }

    public void setVerified(boolean verified) { this.isVerified = verified; }
}
