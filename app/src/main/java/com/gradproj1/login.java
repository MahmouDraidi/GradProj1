package com.gradproj1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.zip.Inflater;

public class login extends AppCompatActivity {

    Button loginButton;
    boolean mLocationPermissionGranted = false;
    TextView mobile_num_editText;
    TextView signUpTextView;
    SharedPreferences SP;
    LinearLayout LinLay1;
    FirebaseFirestore db;
    String entered_number = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        getLocationPermission();

        //if user or driver already logged pass log in activity
        if (!SP.getString("number", "").equals("")) {
            //TODO redirect to user or driver
            Intent i;
            String type = "";
            type = SP.getString("type", "");
            switch (type) {
                case "user":
                    i = new Intent(this, userMap.class);
                    startActivity(i);
                    break;
                case "driver":

                    i = new Intent(this, driverMap.class);
                    startActivity(i);
                    break;
            }
            finish();
        }


        loginButton = (Button) findViewById(R.id.loginButton);
        mobile_num_editText = (TextView) findViewById(R.id.mobile_number);
        signUpTextView = (TextView) findViewById(R.id.signUp);
        LinLay1 = (LinearLayout) findViewById(R.id.linLay);
        db = FirebaseFirestore.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (mobile_num_editText.getText().toString().length() != 10)
                    toastMessage("Please enter valid number");
                else {
                    entered_number = "+97" + mobile_num_editText.getText().toString();
                    defineUser(entered_number);
                }
            }
        });
        //end of button onClick listener
    }

    public void defineUser(final String mobNumber) {
        db.collection("users").document(mobNumber).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            user User = documentSnapshot.toObject(user.class);


                            SP.edit().putString("line", User.getLine()).apply();
                            SP.edit().putString("type", "user").apply();
                            SP.edit().putString("name", User.getName()).apply();
                            SP.edit().putString("PIN", User.getPIN()).apply();
                            move();

                        } else {
                            defineDriver(mobNumber);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastMessage("Something went wrong!");
                    }
                });


    }

    public void defineDriver(String mobNum) {

        db.collection("drivers").document(mobNum).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            toastMessage("I'm driver");

                            driver Driver = documentSnapshot.toObject(driver.class);

                            SP.edit().putString("number", Driver.getMobileNumber()).apply();
                            SP.edit().putString("line", Driver.getLine()).apply();
                            SP.edit().putString("type", "driver").apply();
                            SP.edit().putString("name", Driver.getName()).apply();
                            SP.edit().putString("PIN", Driver.getPIN()).apply();
                            move();

                        } else toastMessage("This number is not registered, you need to sign up");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastMessage("Something went wrong!");
                    }
                });
    }


    public void move() {
        Intent i = new Intent(this, verification.class);
        i.putExtra("number", entered_number);
        startActivity(i);

    }

    public void toastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

    }


    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, permission[0]) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, permission, 1234);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case 1234:
                if (grantResults.length > 0) {

                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            System.exit(0);
                        }
                    }
                    mLocationPermissionGranted = true;
                }
        }
    }


    public void registerNow(View v) {

        Intent intent = new Intent (this, Register.class);
        startActivity(intent);
    //    setContentView(R.layout.activity_enter_number);

    }
}
