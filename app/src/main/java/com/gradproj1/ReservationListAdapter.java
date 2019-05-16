package com.gradproj1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.gradproj1.driver.driver;
import com.gradproj1.user.userMap;

import java.sql.Driver;

public class ReservationListAdapter extends FirestoreRecyclerAdapter<Reservation, ReservationListAdapter.userHolder> {
    String myLine = "";
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String driverMobNum;
    String activityName;

    public ReservationListAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options, String myLine, String driverMobNum, Context c, String activityName) {
        super(options);
        this.myLine = myLine;
        this.context = c;
        this.driverMobNum = driverMobNum;
        this.activityName = activityName;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ReservationListAdapter.userHolder holder, int position, @NonNull Reservation model) {
        //TODO change to correct values

        switch (activityName) {
            case "all":

                // holder.reservations.setText("Active Drivers: "+model.getActiveDrivers().size());
                if (model.isNeedDriver()) {

                    holder.rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Toast.makeText(context, "You pressed item in pos: " + holder.getAdapterPosition()+" "+holder.toastMobNom(), Toast.LENGTH_SHORT).show();
                            holder.showButtons();

                        }
                    });
                }
                holder.nameTV.setText(model.getUserName());
                holder.desc.setText("مكان الانتظار: " + model.getPlaceDetails());
                holder.reservations.setText("عدد الحجوزات: " + model.getReservationSize());
                holder.mobNum.setText(model.isNeedDriver() ? model.getUserMobileNumber() : "السائق: " + model.getDriverName());


                break;
            case "myPassengers":
                holder.nameTV.setText(model.getUserName());
                // holder.reservations.setText("Active Drivers: "+model.getActiveDrivers().size());
                holder.desc.setText("مكان الانتظار: " + model.getPlaceDetails());
                holder.reservations.setText("عدد الحجوزات: " + model.getReservationSize());
                holder.mobNum.setText(model.getUserMobileNumber());
                if (model.isCancelRes()) holder.showCancellationButton();
                holder.rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.showReservationOption();
                    }
                });
                break;
        }




    }

    @NonNull
    @Override
    public ReservationListAdapter.userHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_reservation,
                parent, false);
        return new ReservationListAdapter.userHolder(v);
    }

    class userHolder extends RecyclerView.ViewHolder {
        LinearLayout buttonsGroup;
        TextView nameTV;
        TextView mobNum;
        TextView reservations;
        TextView desc;
        RelativeLayout rl;
        RecyclerView rv;
        Button addReservation;
        LinearLayout cancelLayout, resOptions;
        Button cancelRes;
        ImageView account, delete, chat;

        public userHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.list_userNameTV);
            mobNum = itemView.findViewById(R.id.userMobNum);
            desc = itemView.findViewById(R.id.posDiscription);
            reservations = itemView.findViewById(R.id.resSizeTV);
            rl = itemView.findViewById(R.id.userListRelLayout);
            rv = itemView.findViewById(R.id.resListRecyclerview);
            addReservation = itemView.findViewById(R.id.addRes);
            buttonsGroup = itemView.findViewById(R.id.buttonsGroup);
            cancelLayout = itemView.findViewById(R.id.cancelResLayout);
            cancelRes = itemView.findViewById(R.id.cancelResButton);
            account = itemView.findViewById(R.id.row_res_account);
            delete = itemView.findViewById(R.id.row_res_delete);
            chat = itemView.findViewById(R.id.row_res_chat);
            resOptions = itemView.findViewById(R.id.resOptions);


            addReservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addUser();
                }
            });
            cancelRes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelReservation();
                }
            });

            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO add to log history

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("هل بالفعل تريد إنزال الراكب؟");
                    builder.setPositiveButton("إنزال الراكب", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.collection("reservations").document(mobNum.getText().toString()).delete();
                            db.collection("drivers").document(driverMobNum).update("myPassengers", FieldValue.arrayRemove(mobNum.getText().toString()));
                            updatePassengersNum(driverMobNum, Integer.parseInt(reservations.getText().toString().substring(14)));

                        }
                    })
                            .setNegativeButton("إلغاء", null);
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
            account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void cancelReservation() {
            final String userID = mobNum.getText().toString();

            db.collection("reservations").document(userID).update("reservationDriver", "none");
            db.collection("reservations").document(userID).update("driverName", "none");
            db.collection("reservations").document(userID).update("cancelRes", false);
            db.collection("reservations").document(userID).update("needDriver", true);
            db.collection("reservations").document(userID).update("driverName", "لا يوجد");
            updatePassengersNum(driverMobNum, Integer.valueOf(reservations.getText().toString().substring(14)));
        }

        public void addUser() {
            final int resSize = Integer.valueOf(reservations.getText().toString().substring(14));
            final String userID = mobNum.getText().toString();

            db.collection("drivers").document(driverMobNum).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                driver d = documentSnapshot.toObject(driver.class);

                                if (d.getCurrentPassengersNum() + resSize <= d.getMaxPassengersNum()) {

                                    db.collection("reservations").document(mobNum.getText().toString()).update("needDriver", false);
                                    db.collection("reservations").document(mobNum.getText().toString()).update("reservationDriver", d.getMobileNumber());
                                    db.collection("drivers").document(driverMobNum).update("myPassengers", FieldValue.arrayUnion(userID));
                                    db.collection("drivers").document(driverMobNum).update("currentPassengersNum", d.getCurrentPassengersNum() + resSize);
                                    db.collection("reservations").document(mobNum.getText().toString()).update("driverName", d.getName());

                                } else
                                    Toast.makeText(context, "عدد الركاب تجاوز الحد المسموح" + "\nتبقى لديك " + String.valueOf(d.getMaxPassengersNum() - d.getCurrentPassengersNum()), Toast.LENGTH_LONG).show();
                            } else Toast.makeText(context, driverMobNum, Toast.LENGTH_LONG).show();
                        }
                    });
        }

        public void showButtons() {
            if (buttonsGroup.getVisibility() == View.GONE)
                buttonsGroup.setVisibility(View.VISIBLE);
            else buttonsGroup.setVisibility(View.GONE);
        }

        public void showCancellationButton() {
            cancelLayout.setVisibility(View.VISIBLE);
        }

        public void updatePassengersNum(final String resDriver, final int cancelledResSize) {
            db.collection("drivers").document(resDriver).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                int passengersNum = documentSnapshot.toObject(driver.class).getCurrentPassengersNum();
                                db.collection("drivers").document(resDriver).update("currentPassengersNum", passengersNum - cancelledResSize);

                            }
                        }
                    });
        }


        public void showReservationOption() {
            if (resOptions.getVisibility() == View.GONE)
                resOptions.setVisibility(View.VISIBLE);
            else resOptions.setVisibility(View.GONE);
        }
    }

}