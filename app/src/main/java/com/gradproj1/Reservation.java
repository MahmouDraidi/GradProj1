package com.gradproj1;

import com.google.firebase.firestore.GeoPoint;
import com.gradproj1.driver.driver;
import com.gradproj1.user.user;

import java.sql.Time;

public class Reservation {
    private String reservationLine;
    private String userMobileNumber;
    private Time reservationTime;
    private String reservationDriver;
    private int reservationSize;
    private String placeDetails;
    private boolean needDriver;
    private GeoPoint currentLocation;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    private String driverName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    private String line;

    public void setCancelRes(boolean cancelRes) {
        this.cancelRes = cancelRes;
    }

    private boolean cancelRes;

    public Reservation(String reservationLine, String userMobileNumber, Time reservationTime, String reservationDriver, int reservationSize, String placeDetails, boolean needDriver) {
        this.reservationLine = reservationLine;
        this.userMobileNumber = userMobileNumber;
        this.reservationTime = reservationTime;
        this.reservationDriver = reservationDriver;
        this.reservationSize = reservationSize;
        this.placeDetails = placeDetails;
        this.needDriver = needDriver;
    }

    public Reservation() {

    }

    public boolean isCancelRes() {
        return cancelRes;
    }

    public boolean isNeedDriver() {
        return needDriver;
    }

    public void setNeedDriver(boolean needDriver) {
        this.needDriver = needDriver;
    }

    public String getPlaceDetails() {
        return placeDetails;
    }

    public void setPlaceDetails(String placeDetails) {
        this.placeDetails = placeDetails;
    }

    public String getReservationLine() {
        return reservationLine;
    }

    public void setReservationLine(String reservationLine) {
        this.reservationLine = reservationLine;
    }


    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public Time getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Time reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getReservationDriver() {
        return reservationDriver;
    }

    public void setReservationDriver(String reservationDriver) {
        this.reservationDriver = reservationDriver;
    }

    public int getReservationSize() {
        return reservationSize;
    }

    public void setReservationSize(int reservationSize) {
        this.reservationSize = reservationSize;
    }


}
