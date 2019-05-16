package com.gradproj1;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.gradproj1.driver.driver;
import com.gradproj1.user.user;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    RadioButton rbD;
    EditText name, family, PIN, regMobNum, vehicleNumber;
    Button chooseImg, doRegistration, finishReg, checkNumButton;
    ProgressBar progressBar;
    ImageView userImg;
    Uri mImageUri;
    FirebaseFirestore db;
    StorageReference ref;
    StorageTask mUploadTask;
    TextView tv;
    LinearLayout linearLayout, additionalDriverInfo;
    LinearLayout constraintLayout;
    String mobileNumber = "";
    String fromVerfication;
    List<String> linesOfFirestore, vehicleCapOptions;
    Spinner spinner, vCap;
    RadioGroup rg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        spinner = (Spinner) findViewById(R.id.spinner);
        vCap = findViewById(R.id.regVehicleCapacity);
        name = findViewById(R.id.user_reg_name);
        family = findViewById(R.id.user_reg_family);
        PIN = findViewById(R.id.user_reg_pin);
        doRegistration = findViewById(R.id.user_reg_regButton);
        regMobNum = findViewById(R.id.regMobileNumber);
        vehicleNumber = findViewById(R.id.regVehicleNumber);
        finishReg = findViewById(R.id.user_reg_finish);
        chooseImg = findViewById(R.id.user_reg_imgButton);
        checkNumButton = findViewById(R.id.checkNumButton);
        progressBar = findViewById(R.id.puser_reg_rogressBar);
        userImg = findViewById(R.id.user_reg_userImage);
        linearLayout = findViewById(R.id.regUserInfoLayout);
        constraintLayout = findViewById(R.id.regNumLayout);
        additionalDriverInfo = findViewById(R.id.reg_driverInfoLayout);
        rg = findViewById(R.id.reg_radioGroup);
        rbD = findViewById(R.id.radButtDriver);

        db = FirebaseFirestore.getInstance();
        ref = FirebaseStorage.getInstance().getReference("users_images");

        linesOfFirestore = new ArrayList<>();
        vehicleCapOptions = new ArrayList<>();
        vehicleCapOptions.add("7");
        vehicleCapOptions.add("4");
        ArrayAdapter vCapAdapter = ArrayAdapter.createFromResource(this, R.array.vehicleCapacity, android.R.layout.simple_spinner_dropdown_item);
        vCap.setAdapter(vCapAdapter);


        //check wether the previous activity was verification
        Bundle data = getIntent().getExtras();
        if (data != null) {
            fromVerfication = data.getString("numberFromVerf");
            mobileNumber = (fromVerfication);
            constraintLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }


        linesOfFirestore.add(0, "اختر خط النقل");

        db.collection("lines").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                            linesOfFirestore.add(q.getId());
                        }
                        AutocompleteArrayAdapter adapter = new AutocompleteArrayAdapter(getApplicationContext(), linesOfFirestore);
                        // Specify layout to be used when list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Applying the adapter to our spinner
                        spinner.setAdapter(adapter);
                    }
                });

        finishReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });
        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (rbD.isChecked()) {
                    additionalDriverInfo.setVisibility(View.VISIBLE);
                } else additionalDriverInfo.setVisibility(View.GONE);

            }
        });

        checkNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNumber = regMobNum.getText().toString();
                if (mobileNumber.length() == 10 && (mobileNumber.startsWith("056") || mobileNumber.startsWith("059"))) {
                    mobileNumber = "+97" + mobileNumber;
                    defineUser(mobileNumber);
                } else {
                    Toast.makeText(getApplicationContext(), "الرجاء ادخال رقم هاتف نقال صالح", Toast.LENGTH_LONG).show();
                }
            }
        });

        doRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner.getSelectedItemPosition() == 0) {
                    toastMessage("الرجاء اختيار خط النقل");
                } else if (rbD.isChecked() && vCap.getSelectedItemPosition() == 0) {
                    toastMessage("الرجاء اختيار سعة حمولة المركبة");
                } else if (mImageUri == null) {
                    Toast.makeText(getApplicationContext(), "الرجاء اختيار صورة لحسابك", Toast.LENGTH_LONG).show();
                } else if (name.getText().toString().equals("") || family.getText().toString().equals("") || PIN.getText().toString().equals("") || (vehicleNumber.getText().toString().equals("") && rbD.isChecked())) {
                    Toast.makeText(getApplicationContext(), "المعلومات غير مكتملة", Toast.LENGTH_LONG).show();

                } else {
                    doRegistration.setEnabled(false);
                    registerAccount();
                }

            }
        });
    }

    public void move(String mNum) {

        Intent i = new Intent(this, verification.class);
        i.putExtra("numberFromReg", mNum);
        startActivity(i);

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void registerAccount() {
        if (rbD.isChecked()) {
            driver regDriver = new driver();
            regDriver.setActive(false);
            regDriver.setName(name.getText().toString() + " " + family.getText().toString());
            regDriver.setLine(spinner.getSelectedItem().toString());
            regDriver.setCurrentPassengersNum(0);
            regDriver.setMaxPassengersNum(Integer.valueOf(vCap.getSelectedItem().toString()));
            regDriver.setMobileNum(mobileNumber);
            regDriver.setPIN(PIN.getText().toString());
            regDriver.setImgURL("");
            regDriver.setVehicleNumber(vehicleNumber.getText().toString());
            db.collection("drivers").document(mobileNumber).set(regDriver);

        } else {
            user regUser = new user();
            regUser.setName(name.getText().toString() + " " + family.getText().toString());
            regUser.setPIN(PIN.getText().toString());
            regUser.setLine(spinner.getSelectedItem().toString());
            regUser.setMobileNumber(mobileNumber);
            regUser.setCurrentLocation(null);
            regUser.setImgURL("");
            db.collection("users").document(mobileNumber).set(regUser);
        }
        uploadFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(userImg);
            Toast.makeText(this, "تم اختيار صورة", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "الرجاء اختيار صورة لحسابك", Toast.LENGTH_LONG).show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            if (rbD.isChecked()) ref = FirebaseStorage.getInstance().getReference("drivers_images");
            StorageReference fileReference = ref.child(mobileNumber + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                           /* Upload upload = new Upload("hhhhh",
                                    taskSnapshot.getDownloadUrl().toString());
                            String uploadId = db.push().getKey();*/


                            Task<Uri> newTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            newTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String u = uri.toString();
                                    db.collection(rbD.isChecked() ? "drivers" : "users").document(mobileNumber).update("imgURL", u);

                                }
                            });


                            doRegistration.setVisibility(View.GONE);
                            finishReg.setVisibility(View.VISIBLE);
                            name.setEnabled(false);
                            family.setEnabled(false);
                            PIN.setEnabled(false);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "hehe" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "لم يتم اختيار أي صورة", Toast.LENGTH_SHORT).show();
        }
    }


    public void defineUser(final String mobNumber) {
        db.collection("users").document(mobNumber).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            Toast.makeText(getApplicationContext(), "هذا الرقم مستخدم مسبقا", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getApplicationContext(), "هذا الرقم مستخدم مسبقا", Toast.LENGTH_LONG).show();
                        } else {
                            toastMessage("Welcome");

                            move(mobileNumber);

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

    public void toastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }




}
