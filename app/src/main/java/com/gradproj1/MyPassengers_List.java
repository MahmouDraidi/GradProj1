package com.gradproj1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyPassengers_List extends AppCompatActivity {
    private FirebaseFirestore db;
    private SharedPreferences SP;
    private RecyclerView recyclerView;
    Query query;
    private ReservationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_passengers__list);

        db = FirebaseFirestore.getInstance();
        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        recyclerView = findViewById(R.id.myPassengersRV);


        query = db
                .collection("reservations")
                .whereEqualTo("line", SP.getString("line", ""))
                .whereEqualTo("reservationDriver", SP.getString("number", ""));

        setUpRecyclerView(query);
    }

    private void setUpRecyclerView(Query query) {
        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query, Reservation.class)
                .build();

        adapter = new ReservationListAdapter(options, SP.getString("line", ""), SP.getString("number", ""), this);


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
