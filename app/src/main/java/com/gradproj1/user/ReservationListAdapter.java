package com.gradproj1.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gradproj1.R;
import com.gradproj1.Reservation;

public class ReservationListAdapter extends FirestoreRecyclerAdapter<Reservation, ReservationListAdapter.userHolder> {
    String myLine = "";
    Context context;

    public ReservationListAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options, String myLine, Context c) {
        super(options);
        this.myLine = myLine;
        context = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ReservationListAdapter.userHolder holder, int position, @NonNull Reservation model) {
        //TODO change to correct values
        holder.nameTV.setText(model.getUserName());
        // holder.reservations.setText("Active Drivers: "+model.getActiveDrivers().size());
        // holder.desc.setText(model.getMyPositionDiscripition());
        holder.mobNum.setText("عدد الحجوزات: " + model.getReservationSize());

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You pressed item in pos: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });


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

        public userHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.list_userNameTV);
            mobNum = itemView.findViewById(R.id.userMobNum);
            desc = itemView.findViewById(R.id.posDiscription);
            reservations = itemView.findViewById(R.id.resSizeTV);
            rl = itemView.findViewById(R.id.userListRelLayout);
            rv = itemView.findViewById(R.id.resListRecyclerview);
            //totalDriversNum=itemView.findViewById(R.id.lineTotalDriversNum);
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