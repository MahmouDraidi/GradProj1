package com.gradproj1.driver;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gradproj1.R;

public class DriversListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private SharedPreferences SP;
    EditText searchET;
    TextWatcher textWatcher;
    RecyclerView recyclerView;
    Query query;

    private driverListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_driver);
        db = FirebaseFirestore.getInstance();
        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        searchET = findViewById(R.id.searchDriverTV);
        recyclerView = findViewById(R.id.recycler_view);

        query = db.collection("drivers").whereEqualTo("line", SP.getString("line", ""));
        setUpRecyclerView(query);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                // filterDrivers(searchET.getText().toString());


            }

            @Override
            public void afterTextChanged(Editable editable) {
                query = db.collection("drivers").whereEqualTo("line", "Tulkarm - BaytLid");
                FirestoreRecyclerOptions<driver> options = new FirestoreRecyclerOptions.Builder<driver>()
                        .setQuery(query, driver.class)
                        .build();

                adapter = new driverListAdapter(options, getApplicationContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter.notifyDataSetChanged();
                //  recyclerView.swapAdapter(searchAdapter,false);

            }
        };
        searchET.addTextChangedListener(textWatcher);

    }

    private void filterDrivers(String name) {
        String searchText = searchET.getText().toString();

        query = db.collection("drivers").orderBy("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirestoreRecyclerOptions<driver> options = new FirestoreRecyclerOptions.Builder<driver>()
                .setQuery(query, driver.class)
                .build();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void setUpRecyclerView(Query query) {
        //query = db.collection("drivers").whereEqualTo("line", SP.getString("line", ""));

        //.orderBy("name").startAt("Mal").endAt("Mal"+"\uf8ff");

        FirestoreRecyclerOptions<driver> options = new FirestoreRecyclerOptions.Builder<driver>()
                .setQuery(query, driver.class)
                .build();

        adapter = new driverListAdapter(options, this);


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

    @Override
    public void onBackPressed() {
        finish();
    }
}

