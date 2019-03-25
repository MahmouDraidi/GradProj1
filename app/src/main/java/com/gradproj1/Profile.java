package com.gradproj1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    String path ;
    LayoutInflater layoutInflater ;
    FirebaseFirestore db ;
    private DocumentReference documentReference ;
    String type = "drivers";
    EditText name , line , passenger , mobile ;
    TextView editMyData,textView8;
    ImageView edit ;
    boolean isMe = false ;
    boolean save = true;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_profile);

        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.name);
        line = findViewById(R.id.line);
        passenger = findViewById(R.id.passenger);
        mobile =  findViewById(R.id.mobile);
        textView8 = findViewById(R.id.textView8);

        edit = findViewById(R.id.editIcon);
         editMyData = findViewById(R.id.editMyData);
       // check for data from previous activity
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                path = null;
            } else {
                path = extras.getString("path");
                type = extras.getString("type");
                isMe = extras.getBoolean("isMe");
            }
        } else {
            path = (String) savedInstanceState.getSerializable("path");
        }


        // determine the type
        if(type.equals("drivers")) showDriverInfo();
        else  showPassengerInfo();

        // check if my profile
        if (isMe){edit.setVisibility(View.VISIBLE);
                  } else {
            edit.setVisibility(View.GONE);
            editMyData.setText("");}
    }


    public void  showDriverInfo(){

        layoutInflater = getLayoutInflater();

        documentReference = db.collection("drivers").document(path);
        String N = "";

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){

                            Map<String,Object> note = documentSnapshot.getData();
                            //Toast.makeText(Profile.this,note.get("name").toString(),Toast.LENGTH_SHORT).show();

                            name.setText(note.get("name").toString() );
                            name.setEnabled(false);
                            line.setText(note.get("line").toString());
                            line.setEnabled(false);
                            passenger.setText(note.get("passengersNum").toString() );
                            passenger.setEnabled(false);
                            mobile.setText(note.get("mobileNum").toString());
                            mobile.setEnabled(false);
                            textView8.setText("الحمولة :");

                        }
                        else { Toast.makeText(Profile.this, "not found !",Toast.LENGTH_SHORT).show();}
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "fail !",Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void showPassengerInfo (){

        layoutInflater = getLayoutInflater();

        documentReference = db.collection("users").document(path);
        String N = "";

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){

                            Map<String,Object> note = documentSnapshot.getData();
                            //Toast.makeText(Profile.this,note.get("name").toString(),Toast.LENGTH_SHORT).show();

                            name.setText(note.get("name").toString() );
                            //name.setKeyListener(null);
                            name.setEnabled(false);
                            line.setText(note.get("line").toString());
                            line.setText(note.get("line").toString());
                            line.setEnabled(false);

                      //    passenger.setText(note.get("passengersNum").toString() );
                            passenger.setEnabled(false);

                            mobile.setText(note.get("mobileNumber").toString());
                            mobile.setEnabled(false);

                        }
                        else { Toast.makeText(Profile.this, "not found !",Toast.LENGTH_SHORT).show();}
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "fail !",Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void editMyProfile(View view) {
      //  name.setEnabled();


        if (save && isMe) {
            line.setEnabled(true);
            mobile.setEnabled(true);
            passenger.setEnabled(true);
            editMyData.setText("حفظ التغيرات ");
            edit.setImageResource(0);
            edit.setBackgroundResource(R.drawable.save);
            editMyData.setTextColor(Color.GREEN);
            save = false;

        }
        else if (!save && isMe) {
            line.setEnabled(false);
            mobile.setEnabled(false);
            passenger.setEnabled(false);
            editMyData.setText("تعديل بياناتي ");
            editMyData.setTextColor(Color.BLACK);
            save = true;
            edit.setImageResource(0);
            edit.setBackgroundResource(R.drawable.edit);

            Map<String,Object>  updateData = new HashMap<>();
            updateData.put("name",name.getText().toString());
            updateData.put("line",line.getText().toString());
            if (type.equals("users")){
                updateData.put("mobileNumber",mobile.getText().toString());

            }
            else {
                updateData.put("mobileNum",mobile.getText().toString());
                updateData.put("passengersNum" , passenger.getText().toString());
            }


            db.collection(type).document(path).update(updateData)
                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           Toast.makeText(Profile.this,"تم التعديل !",Toast.LENGTH_SHORT).show();
                       }
                   })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this,"فشل  !",Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }
}
