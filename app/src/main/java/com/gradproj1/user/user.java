package com.gradproj1.user;

import com.google.firebase.firestore.GeoPoint;

public class user {

    private String mobileNumber;
    private String PIN;
    private GeoPoint currentLocation;
    private String name;
    private String line;
    private boolean isActive;

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    private String imgURL;

    public user() {

    }

    public user(String mobileNumber, String name, String PIN, GeoPoint currentLocation, String line) {
        this.mobileNumber = mobileNumber;
        this.PIN = PIN;
        this.currentLocation = currentLocation;
        this.name = name;
        this.line = line;
    }



    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPIN() {
        return PIN;
    }


    public String getLine() {

        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Setters Setters Setters Setters Setters Setters Setters Setters Setters

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Getters Getters Getters Getters Getters Getters Getters Getters Getters

    public String getPIN(String mobileNumber) {


        return PIN;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public String getName() {

        return name;
    }

}
