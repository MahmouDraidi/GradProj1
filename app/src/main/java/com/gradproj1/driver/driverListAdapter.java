package com.gradproj1.driver;

import android.content.Context;
import android.graphics.Color;
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


public class driverListAdapter extends FirestoreRecyclerAdapter<driver, driverListAdapter.driverHolder> {
    Context context;

    public driverListAdapter(@NonNull FirestoreRecyclerOptions<driver> options, Context c) {
        super(options);
        context = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull final driverHolder holder, int position, @NonNull driver model) {
        //TODO change to correct values
        holder.nameTV.setText(model.getName());
        holder.directionTV.setText(model.getLine());
        holder.passengersNum.setText("عدد الركاب: " + model.getPIN());
        if (model.isActive()) {
            holder.nameTV.setTextColor(Color.parseColor("#FF9800"));
        } else holder.nameTV.setTextColor(Color.GRAY);

        holder.driverRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You pressed item in pos: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });


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
        RelativeLayout driverRL;

        public driverHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.driverNameTV);
            directionTV = itemView.findViewById(R.id.driverDirectionTV);
            passengersNum = itemView.findViewById(R.id.numOfPassengers);
            driverRL = itemView.findViewById(R.id.driverListRelLayout);
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