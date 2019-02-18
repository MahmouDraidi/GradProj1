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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {

    Button loginButton;
    boolean mLocationPermissionGranted = false;
    TextView moblile_num_textview;
    String entered_moblie_num;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String verification_code = "";
    TextView signUpTextView;
    SharedPreferences SP;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        // final SharedPreferences.Editor SPE = SP.edit();
        getLocationPermission();

        if (!SP.getString("number", "").equals("")) {
            startActivity(new Intent(this, MainActivity.class));

            return;
        }

        auth = FirebaseAuth.getInstance();
        loginButton = (Button) findViewById(R.id.loginButton);
        moblile_num_textview = (TextView) findViewById(R.id.mobile_number);
        signUpTextView = (TextView) findViewById(R.id.signUp);

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code = s;
                toastMessage("Code is sent to your device, \ncheck your messages. ");
            }

        };


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (loginButton.getText().toString().equalsIgnoreCase("Log in")) {
                    send_sms();
                    loginButton.setText("Verify");
                } else if (loginButton.getText().toString().equals("Verify")) {

                    verifyMobileNumber();
                }
            }
        });
        //end of button onClick listener

    }

    public void send_sms() {
        entered_moblie_num = "+970" + moblile_num_textview.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(entered_moblie_num, 60, TimeUnit.SECONDS, this, mCallback);
        moblile_num_textview.setText("");

    }

    private void signWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_LONG).show();
                            move();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    public void verifyMobileNumber() {
        String inputCode = moblile_num_textview.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code, inputCode);
        signWithPhone(credential);
    }

    public void move() {
        SharedPreferences.Editor SPE = SP.edit();
        SPE.putString("number", "+972" + moblile_num_textview.getText().toString().trim());
        SPE.apply();

        Intent i = new Intent(this, MainActivity.class);
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
                            mLocationPermissionGranted = false;
                            System.exit(0);
                        }
                    }
                    mLocationPermissionGranted = true;

                }
        }
    }
}
