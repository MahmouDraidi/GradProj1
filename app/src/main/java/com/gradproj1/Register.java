package com.gradproj1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView  mobileNumber;
    //    GeoPoint location;
    LayoutInflater layoutInflater;
    private FirebaseFirestore db;
    Location location = null;


    View view;

    public Location getLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            @SuppressLint("MissingPermission") Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                System.out.print(lastKnownLocationGPS.toString());
                return lastKnownLocationGPS;
            } else {
                @SuppressLint("MissingPermission") Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                System.out.println("1::"+loc);
                System.out.println("2::"+loc.getLatitude());
                return loc;
            }
        } else {
            return null;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_number);
        layoutInflater = getLayoutInflater();
        db = FirebaseFirestore.getInstance();


    }

    public void saveNewUser(View v) {
        view = layoutInflater.inflate(R.layout.activity_client_register, (ViewGroup) findViewById(R.id.ClientRigisterLayout));
        // db.initializeApp(this);

        //for location




        // end location

            TextView fName = view.findViewById(R.id.fNameInput);
            TextView sName = view.findViewById(R.id.sNameInput);
            TextView lName = view.findViewById(R.id.lastNameInput);
            TextView pin = view.findViewById(R.id.pinInput);
            TextView Line = view.findViewById(R.id.lineInput);

            Map<String, Object> newUser = new HashMap<>();
            // TODO put correct position
            newUser.put("currentLocation", new GeoPoint(getLocation().getLatitude(), getLocation().getLongitude()));
            newUser.put("line", Line.getText().toString());
            newUser.put("mobileNumber", "+97" + mobileNumber.getText().toString());
            newUser.put("name", fName.getText().toString() + " " + sName.getText().toString() + " " + lName.getText().toString());
            newUser.put("pin", pin.getText().toString());
            db.collection("users").document(mobileNumber.getText().toString()).set(newUser);


        // back to login activity
        Intent intent = new Intent (this, login.class);
        startActivity(intent);

    }



    public void gotoRegisterPage(View v) {

        mobileNumber = findViewById(R.id.phoneNum);
        if (mobileNumber.getText().toString().equals("")) mobileNumber.setBackgroundColor(Color.RED);
        else setContentView(R.layout.activity_client_driver);
    }

    public void gotoClientRegister(View view) {
        setContentView(R.layout.activity_client_register);
    }

    public void gotoDriverRegister(View view) {
        setContentView(R.layout.activity_driver_register);
    }

    public void addNewDriver(View v) {
        view = layoutInflater.inflate(R.layout.activity_driver_register, (ViewGroup) findViewById(R.id.driverRegisterLayout));

        // db.initializeApp(this);

        //for location


        // end location


            TextView fName = view.findViewById(R.id.drFName);
            TextView sName = view.findViewById(R.id.drSName);
            TextView lName = view.findViewById(R.id.drLastName);
            TextView pin = view.findViewById(R.id.drPIN);
            TextView Line = view.findViewById(R.id.drLine);
            TextView vehicleNum = view.findViewById(R.id.vehicNum);

            Map<String, Object> newDriver = new HashMap<>();
            // TODO put correct position
            newDriver.put("currentLocation", new GeoPoint(getLocation().getLatitude(), getLocation().getLongitude()));
            newDriver.put("isActive", true);
            newDriver.put("line", Line.getText().toString());
            newDriver.put("mobileNumber", "+97" + mobileNumber.getText().toString());
            newDriver.put("name", fName.getText().toString() + " " + sName.getText().toString() + " " + lName.getText().toString());
            newDriver.put("pin", pin.getText().toString());
            newDriver.put("vehicleNumber", vehicleNum.getText().toString());


            db.collection("drivers").document(mobileNumber.getText().toString()).set(newDriver);

        Intent intent = new Intent (this, login.class);
        startActivity(intent);


    }

}


