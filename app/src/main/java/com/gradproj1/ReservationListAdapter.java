package com.gradproj1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.gradproj1.driver.driver;

public class ReservationListAdapter extends FirestoreRecyclerAdapter<Reservation, ReservationListAdapter.userHolder> {
    String myLine = "";
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String driverMobNum;

    public ReservationListAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options, String myLine, String driverMobNum, Context c) {
        super(options);
        this.myLine = myLine;
        context = c;
        this.driverMobNum = driverMobNum;

    }

    @Override
    protected void onBindViewHolder(@NonNull final ReservationListAdapter.userHolder holder, int position, @NonNull Reservation model) {
        //TODO change to correct values
        if (model.getReservationDriver().equals("none")) {
            holder.nameTV.setText(model.getUserName());
            // holder.reservations.setText("Active Drivers: "+model.getActiveDrivers().size());
            holder.desc.setText("مكان الانتظار: " + model.getPlaceDetails());
            holder.reservations.setText("عدد الحجوزات: " + model.getReservationSize());
            holder.mobNum.setText(model.getUserMobileNumber());
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(context, "You pressed item in pos: " + holder.getAdapterPosition()+" "+holder.toastMobNom(), Toast.LENGTH_SHORT).show();
                    holder.showButtons();

                }

            });
        } else {
            holder.nameTV.setText(model.getUserName());
            // holder.reservations.setText("Active Drivers: "+model.getActiveDrivers().size());
            holder.desc.setText("مكان الانتظار: " + model.getPlaceDetails());
            holder.reservations.setText("عدد الحجوزات: " + model.getReservationSize());
            holder.mobNum.setText(model.getUserMobileNumber());
            if (model.isCancelRes()) holder.showCancellationButton();
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
        LinearLayout cancelLayout;
        Button cancelRes;

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
        }

        public void cancelReservation() {
            final String userID = mobNum.getText().toString();

            db.collection("reservations").document(userID).update("reservationDriver", "none");
            db.collection("reservations").document(userID).update("driverName", "none");
            db.collection("reservations").document(userID).update("cancelRes", false);
            db.collection("reservations").document(userID).update("needDriver", true);
            updatePassengersNum(driverMobNum, Integer.valueOf(reservations.getText().toString().substring(14)));
        }

        public void addUser() {
            final int resSize = Integer.valueOf(reservations.getText().toString().substring(14));
            final String userID = mobNum.getText().toString();

            db.collection("drivers").document(driverMobNum).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            driver d = documentSnapshot.toObject(driver.class);

                            if (d.getCurrentPassengersNum() + resSize <= d.getMaxPassengersNum()) {

                                db.collection("reservations").document(mobNum.getText().toString()).update("needDriver", false);
                                db.collection("reservations").document(mobNum.getText().toString()).update("reservationDriver", d.getMobileNumber());
                                db.collection("drivers").document(driverMobNum).update("myPassengers", FieldValue.arrayUnion(userID));
                                db.collection("drivers").document(driverMobNum).update("currentPassengersNum", d.getCurrentPassengersNum() + resSize);
                                db.collection("reservations").document(mobNum.getText().toString()).update("driverName", d.getName());

                            } else
                                Toast.makeText(context, "عدد الركاب تجاوز الحد المسموح" + "\nتبقى لديك " + String.valueOf(d.getMaxPassengersNum() - d.getCurrentPassengersNum()), Toast.LENGTH_LONG).show();
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


    }

}