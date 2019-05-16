package com.gradproj1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.gradproj1.driver.driverMap;
import com.gradproj1.user.userMap;

import java.util.concurrent.TimeUnit;

public class verification extends AppCompatActivity {
    Button verifyButton;
    TextView vCode_editText;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String verification_code = "";
    TextView signUpTextView;
    SharedPreferences SP;
    LinearLayout LinLay1, LinLay2;
    String mob_num = "";
    String dest = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);

        auth = FirebaseAuth.getInstance();
        verifyButton = (Button) findViewById(R.id.verifyButton);
        vCode_editText = (TextView) findViewById(R.id.editText);
        signUpTextView = (TextView) findViewById(R.id.signUp);
        verifyButton = (Button) (findViewById(R.id.verifyButton));
        LinLay1 = (LinearLayout) findViewById(R.id.linLay);
        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);


        Bundle data = getIntent().getExtras();
        if (data != null) {
            if (data.containsKey("numberFromReg")) {
                dest = "reg";
                mob_num = data.getString("numberFromReg");

            } else if (data.containsKey("numberFromLogin")) {
                dest = "login";
                mob_num = data.getString("numberFromLogin");
            }
        }

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO uncomment verify and remove the uncommented when enabling verification
                //verifyMobileNumber();
                SP.edit().putString("number", mob_num).apply();
                move();

            }
        });


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
        //TODO uncomment send_sms
        // send_sms(mob_num);
    }

    public void send_sms(String s) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(s, 60, TimeUnit.SECONDS, this, mCallback);
    }

    public void verifyMobileNumber() {
        String inputCode = vCode_editText.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_code, inputCode);
        signWithPhone(credential);
    }

    private void signWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            if (dest.equals("log"))
                                SP.edit().putString("number", mob_num).apply();
                            //TODO uncommect move
                            //move();
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    public void move() {
        Intent i;
        String type = "";
        if (dest.equals("reg")) {
            i = new Intent(this, Registration.class);
            i.putExtra("numberFromVerf", mob_num);
            startActivity(i);

        } else {
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
        }
        finish();
    }

    public void toastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

    }


}
