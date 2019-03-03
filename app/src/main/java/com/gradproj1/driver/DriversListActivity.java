package com.gradproj1.driver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gradproj1.R;

import java.sql.Driver;

public class DriversListActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    private driverListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drivers_list);
        db = FirebaseFirestore.getInstance();

        setUpRecyclerView();


    }

    private void setUpRecyclerView() {
        Query query = db.collection("drivers").orderBy("active");

        FirestoreRecyclerOptions<driver> options = new FirestoreRecyclerOptions.Builder<driver>()
                .setQuery(query, driver.class)
                .build();

        adapter = new driverListAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

