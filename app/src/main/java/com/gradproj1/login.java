package com.gradproj1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    Button loginButton;
    boolean mLocationPermissionGranted=false;
    TextView moblile_num_textview;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences SP=getSharedPreferences("mobile_number", MODE_PRIVATE);
        final SharedPreferences.Editor SPE=SP.edit();
       // FirebaseApp.initializeApp(this);

        //if user already logged in then continue to app
        if(!SP.getString("number","").equals("")){
            startActivity(new Intent(this,MainActivity.class));
            getLocationPermission();
            return;
        }

        loginButton=(Button) findViewById(R.id.loginButton);
        moblile_num_textview=(TextView)findViewById(R.id.mobile_number);

        //continue to app if location permission is garanteed
        getLocationPermission();


        //((TextView)findViewById(R.id.textView5)).setText(SP.getString("number",""));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!moblile_num_textview.getText().toString().equals(""))
                move(SPE);
            }
        });

    }

    public void move(SharedPreferences.Editor SPE){

        SPE.putString("number",moblile_num_textview.getText().toString());
        SPE.apply();

        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }





    private void getLocationPermission(){
        String[] permission= {Manifest.permission.ACCESS_FINE_LOCATION};
        if(ContextCompat.checkSelfPermission(this,permission[0])== PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted=true;
        }
        else {
            ActivityCompat.requestPermissions(this,permission,1234);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted=false;
        switch (requestCode){
            case 1234:
                if(grantResults.length>0 ){

                    for (int i=0;i<grantResults.length;i++){
                        if(grantResults[0]!= PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted=false;
                            System.exit(0);
                        }
                    }
                    mLocationPermissionGranted=true;

                }
        }
    }
}
