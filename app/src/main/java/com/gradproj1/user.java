package com.gradproj1;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

public class    user {
    private Context context;
    private String phoneNumber;
    private String PIN;
    private LatLng currentLocation;
    private String name;
    private String transportation_line;
    private String line;



    public user(Context context,String phoneNumber) {
        FirebaseApp.initializeApp(this.context);
        this.phoneNumber = phoneNumber;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setAllInfo(String name , String phoneNumber, String PIN, LatLng currentLocation, String transportation_line){
        this.phoneNumber = phoneNumber;
        this.PIN = PIN;
        this.name = name;
        this.currentLocation = currentLocation;
        this.transportation_line = transportation_line;

    }
    // Setters Setters Setters Setters Setters Setters Setters Setters Setters

    public void setPhoneNumber(FirebaseFirestore db,String mobileNumber) {

        Map<String,Object> nname=new HashMap<>();
        nname.put("some_message","Hello from the andoroooooid");

        db.collection("users").document(mobileNumber).set(nname);
        this.phoneNumber = mobileNumber;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public void setCurrentLocation(LatLng currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setTransportation_line(String transportation_line) {
        this.transportation_line = transportation_line;
    }
    //Getters Getters Getters Getters Getters Getters Getters Getters Getters

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public String getPIN(FirebaseFirestore db,String mobileNumber) {


        return PIN;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public String getName() {

        return name;
    }

    public String getTransportation_line() {

        return transportation_line;
    }
}
