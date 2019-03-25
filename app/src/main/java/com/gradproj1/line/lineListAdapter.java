package com.gradproj1.line;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gradproj1.R;

public class lineListAdapter extends FirestoreRecyclerAdapter<line, lineListAdapter.ListHolder> {
    String myLine = "";
    Context context;

    public lineListAdapter(@NonNull FirestoreRecyclerOptions<line> options, String myLine, Context c) {
        super(options);
        this.myLine = myLine;
        context = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull final com.gradproj1.line.lineListAdapter.ListHolder holder, int position, @NonNull line model) {
        //TODO change to correct values
        holder.nameTV.setText(model.getName());
        //holder.totalDriversNum.setText("All"+model.getActiveDrivers().size());
        holder.active.setText("Active Drivers: " + model.getActiveDrivers().size());
        holder.price.setText("Price: " + model.getPrice());
        if (model.getName().equals(myLine)) {

            holder.nameTV.setTextColor(Color.parseColor("#FF9800"));

        }
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You pressed item in pos: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @NonNull
    @Override
    public com.gradproj1.line.lineListAdapter.ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout_line,
                parent, false);
        return new com.gradproj1.line.lineListAdapter.ListHolder(v);
    }

    class ListHolder extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView active;
        TextView price;
        RelativeLayout rl;
        RecyclerView rv;

        public ListHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.lineName);
            active = itemView.findViewById(R.id.lineActiveDrivers);
            price = itemView.findViewById(R.id.price);
            rl = itemView.findViewById(R.id.lineListRelLayout);
            rv = itemView.findViewById(R.id.linesRecyclerview);

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