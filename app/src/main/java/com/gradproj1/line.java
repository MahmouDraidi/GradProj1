package com.gradproj1;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class line {
    private GeoPoint garage1;
    private GeoPoint garage2;
    private String name;
    private List<String> activeDrivers;
    private List<String> nonActiveDrivers;

    public List<String> getActiveDrivers() {
        return activeDrivers;
    }


    public line() {
    }

    public line(GeoPoint garage1, GeoPoint garage2, String name, List<String> activeDrivers, List<String> nonActiveDrivers) {
        this.garage1 = garage1;
        this.garage2 = garage2;
        this.name = name;
        this.activeDrivers = activeDrivers;
        this.nonActiveDrivers = nonActiveDrivers;
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
}
