package com.gradproj1;

import com.google.firebase.firestore.GeoPoint;

public class driver {
    private String mobileNum;
    private GeoPoint currentLocation;
    private String name;
    private String vehicleNumber;
    private String PIN;
    private String line;
    boolean isActive;

    public driver() {
    }

    public driver(String mobileNum, GeoPoint currentLocation, String name, String vehicleNumber, String PIN, String line, boolean isActive) {
        this.mobileNum = mobileNum;
        this.currentLocation = currentLocation;
        this.name = name;
        this.vehicleNumber = vehicleNumber;
        this.PIN = PIN;
        this.line = line;
        this.isActive = isActive;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
