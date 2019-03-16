package com.gradproj1.line;

import com.google.firebase.firestore.GeoPoint;
import com.gradproj1.user.user;

import java.util.List;
import java.util.Map;

public class line {
    private GeoPoint garage1;
    private GeoPoint garage2;
    private String name;
    private List<String> activeDrivers;
    private List<String> nonActiveDrivers;
    private Map<String, user> activeUsers;
    private String garage1Discription;

    private String garage2Discription;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;



    public line() {
    }

    public line(GeoPoint garage1, GeoPoint garage2, String name, List<String> activeDrivers, List<String> nonActiveDrivers, Map<String, user> activeUsers) {
        this.garage1 = garage1;
        this.garage2 = garage2;
        this.name = name;
        this.activeDrivers = activeDrivers;
        this.nonActiveDrivers = nonActiveDrivers;
        this.activeUsers = activeUsers;

    }

    public String getGarage1Discription() {
        return garage1Discription;
    }

    public void setGarage1Discription(String garage1Discription) {
        this.garage1Discription = garage1Discription;
    }

    public String getGarage2Discription() {
        return garage2Discription;
    }

    public void setGarage2Discription(String garage2Discription) {
        this.garage2Discription = garage2Discription;
    }

    public GeoPoint getGarage1() {
        return garage1;
    }

    public void setGarage1(GeoPoint garage1) {
        this.garage1 = garage1;
    }

    public GeoPoint getGarage2() {
        return garage2;
    }

    public void setGarage2(GeoPoint garage2) {
        this.garage2 = garage2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActiveDrivers(List<String> activeDrivers) {
        this.activeDrivers = activeDrivers;
    }

    public List<String> getNonActiveDrivers() {
        return nonActiveDrivers;
    }

    public void setNonActiveDrivers(List<String> nonActiveDrivers) {
        this.nonActiveDrivers = nonActiveDrivers;
    }

    public Map<String, user> getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Map<String, user> activeUsers) {
        this.activeUsers = activeUsers;
    }

    public List<String> getActiveDrivers() {
        return activeDrivers;
    }
}
