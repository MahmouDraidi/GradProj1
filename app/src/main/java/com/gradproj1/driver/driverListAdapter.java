package com.gradproj1.driver;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gradproj1.R;


public class driverListAdapter extends FirestoreRecyclerAdapter<driver, driverListAdapter.driverHolder> {


    public driverListAdapter(@NonNull FirestoreRecyclerOptions<driver> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull driverHolder holder, int position, @NonNull driver model) {
        //TODO change to correct values
        holder.nameTV.setText(model.getName());
        holder.directionTV.setText(model.getLine());
        holder.passengersNum.setText(model.getPIN());
        if (model.isActive()) {
            holder.nameTV.setTextColor(Color.parseColor("#FF9800"));
        } else holder.nameTV.setTextColor(Color.GRAY);


    }

    @NonNull
    @Override
    public driverHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_drivers,
                parent, false);
        return new driverHolder(v);
    }

    class driverHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView directionTV;
        TextView passengersNum;

        public driverHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.driverNameTV);
            directionTV = itemView.findViewById(R.id.driverDirectionTV);
            passengersNum = itemView.findViewById(R.id.numOfPassengers);
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