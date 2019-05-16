package com.gradproj1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gradproj1.R;
import com.gradproj1.Reservation;
import com.gradproj1.ReservationListAdapter;

public class ReservationsList extends AppCompatActivity {
    private FirebaseFirestore db;
    private SharedPreferences SP;
    private RecyclerView recyclerView;
    Query query;
    private ReservationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_reservation);

        db = FirebaseFirestore.getInstance();
        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        recyclerView = findViewById(R.id.resListRecyclerview);


        query = db
                .collection("reservations")
                .whereEqualTo("line", SP.getString("line", ""));
        setUpRecyclerView(query);


    }

    private void setUpRecyclerView(Query query) {
        //query = db.collection("drivers").whereEqualTo("line", SP.getString("line", ""));

        //.orderBy("name").startAt("Mal").endAt("Mal"+"\uf8ff");

        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query, Reservation.class)
                .build();

        adapter = new ReservationListAdapter(options, SP.getString("line", ""), SP.getString("number", ""), this, "all");


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
