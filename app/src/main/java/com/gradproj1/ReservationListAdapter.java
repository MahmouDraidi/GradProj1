package com.gradproj1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
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
                    holder.expand();

                }

            });
        } else {
            holder.nameTV.setText(model.getUserName());
            // holder.reservations.setText("Active Drivers: "+model.getActiveDrivers().size());
            holder.desc.setText("مكان الانتظار: " + model.getPlaceDetails());
            holder.reservations.setText("عدد الحجوزات: " + model.getReservationSize());
            holder.mobNum.setText(model.getUserMobileNumber());
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
        TextView nameTV;
        TextView mobNum;
        TextView reservations;
        TextView desc;
        RelativeLayout rl;
        RecyclerView rv;
        Button addReservation;

        public userHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.list_userNameTV);
            mobNum = itemView.findViewById(R.id.userMobNum);
            desc = itemView.findViewById(R.id.posDiscription);
            reservations = itemView.findViewById(R.id.resSizeTV);
            rl = itemView.findViewById(R.id.userListRelLayout);
            rv = itemView.findViewById(R.id.resListRecyclerview);
            addReservation = itemView.findViewById(R.id.addRes);


            addReservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addUser();
                }
            });
        }

        public void addUser() {
            db.collection("drivers").document(driverMobNum).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            driver d = documentSnapshot.toObject(driver.class);
                            // int i=Integer.valueOf(reservations.getText().toString().substring(14));
                            //Toast.makeText(context,String.valueOf(i),Toast.LENGTH_SHORT).show();
                            if (d.getMyPassengers().size() + Integer.valueOf(reservations.getText().toString().substring(14)) < 7) {
                                db.collection("reservations").document(mobNum.getText().toString()).update("needDriver", false);
                                db.collection("reservations").document(mobNum.getText().toString()).update("reservationDriver", d.getMobileNumber());

                            }
                        }
                    });
        }

        public void expand() {
            if (addReservation.getVisibility() == View.GONE)
                addReservation.setVisibility(View.VISIBLE);
            else addReservation.setVisibility(View.GONE);
        }
    }
    /*
    private Filter driverListFilter =new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        }
    };*/
}