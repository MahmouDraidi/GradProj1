package com.gradproj1.line;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.gradproj1.R;
import com.gradproj1.user.userMap;

public class lineListAdapter extends FirestoreRecyclerAdapter<line, lineListAdapter.ListHolder> {
    String myLine = "";
    Context context;
    SharedPreferences SP;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public lineListAdapter(@NonNull FirestoreRecyclerOptions<line> options, String myLine, Context c) {
        super(options);
        this.myLine = myLine;
        context = c;
        SP = c.getSharedPreferences("mobile_number", context.MODE_PRIVATE);
    }

    @Override
    protected void onBindViewHolder(@NonNull final com.gradproj1.line.lineListAdapter.ListHolder holder, int position, @NonNull line model) {
        //TODO change to correct values
        holder.nameTV.setText(model.getName());
        //holder.totalDriversNum.setText("All"+model.getActiveDrivers().size());
        holder.active.setText("Active Drivers: " + model.getActiveDrivers().size());
        holder.price.setText("Price: " + model.getPrice());
        if (model.getName().equals(SP.getString("line", ""))) {

            holder.nameTV.setTextColor(Color.parseColor("#FF9800"));

        }

        if (!model.getName().equals(SP.getString("line", ""))) {
            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Toast.makeText(context, "You pressed item in pos: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("هل بالفعل تريد تغيير الخط إلى:\n " + holder.nameTV.getText().toString());
                    builder.setPositiveButton("تغيير", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.collection("users").document(SP.getString("number", "")).update("line", holder.nameTV.getText().toString());
                            SP.edit().putString("line", holder.nameTV.getText().toString()).apply();
                            context.startActivity(new Intent(context, userMap.class));


                        }
                    })
                            .setNegativeButton("إلغاء", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }


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
