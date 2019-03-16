package com.gradproj1.driver;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.firestore.GeoPoint;
import com.gradproj1.user.user;

import java.util.Map;

public class driver {
    private String mobileNum;
    private GeoPoint currentLocation;
    private String name;
    private String vehicleNumber;
    private String PIN;
    private String line;
    private boolean isActive;
    private int passengersNum;
    private int maxPassengersNum;

    Map<String, user> myPassengers;



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

    public Map<String, user> getMyPassengers() {
        return myPassengers;
    }

    public void setMyPassengers(Map<String, user> myPassengers) {
        this.myPassengers = myPassengers;
    }

    public int getMaxPassengersNum() {
        return maxPassengersNum;
    }

    public void setMaxPassengersNum(int maxPassengersNum) {
        this.maxPassengersNum = maxPassengersNum;
    }

    public int getPassengersNum() {
        return passengersNum;
    }

    public void setPassengersNum(int passengersNum) {
        this.passengersNum = passengersNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getMobileNumber() {
        return mobileNum;
    }


    public void setMobileNumber(String mobileNum) {
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void drawMarker(GoogleMap googleMap, GeoPoint GP) {
        // driverMarker=new Marker();
    }
}
