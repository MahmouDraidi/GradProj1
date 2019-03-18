package com.gradproj1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.gradproj1.user.user;

import java.util.HashMap;
import java.util.Map;

public class reservationPopup extends AppCompatActivity {
    Button confirm;
    SeekBar seekBar;
    TextView resSize;
    user User;
    String phone_num;
    String userName;
    String mobileNum;
    SharedPreferences SP;
    EditText PIN;
    EditText posDisc;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_popup);

        confirm = (Button) findViewById(R.id.confirmReservation);
        seekBar = (SeekBar) findViewById(R.id.popUpseekbar);
        resSize = (TextView) findViewById(R.id.reservationSize);
        posDisc = findViewById(R.id.placeDiscription);
        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        PIN = findViewById(R.id.popupPIN);
        initUser();


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .7));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PIN.getText().toString().equals(User.getPIN())) {
                    Reservation newRes = new Reservation();
                    newRes.setNeedDriver(true);
                    newRes.setUserMobileNumber(User.getMobileNumber());
                    newRes.setPlaceDetails(posDisc.getText().toString());
                    newRes.setReservationLine(User.getLine());
                    newRes.setReservationSize(Integer.valueOf(resSize.getText().toString()));
                    newRes.setReservationDriver("none");
                    newRes.setLine(User.getLine());
                    newRes.setUserName(User.getName());

                    db.collection("users").document(User.getMobileNumber()).update("isActive", true);
                    db.collection("reservations").document(User.getMobileNumber()).set(newRes);
                    Toast.makeText(getApplicationContext(), "Done!, wait for a driver request", Toast.LENGTH_LONG).show();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Wrong PIN, try again", Toast.LENGTH_LONG).show();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String s = Integer.toString(i + 1);
                resSize.setText(s);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void initUser() {
        User = new user();
        User.setMobileNumber(SP.getString("number", ""));
        User.setName(SP.getString("name", ""));
        User.setLine(SP.getString("line", ""));
        User.setPIN(SP.getString("PIN", ""));

    }

}
