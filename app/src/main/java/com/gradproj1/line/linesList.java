package com.gradproj1.line;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.gradproj1.R;

public class linesList extends AppCompatActivity {

    private FirebaseFirestore db;
    private SharedPreferences SP;
    private RecyclerView recyclerView;
    Query query;
    private lineListAdapter linesListAdapte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_line);

        db = FirebaseFirestore.getInstance();
        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        recyclerView = findViewById(R.id.linesRecyclerview);


        query = db.collection("lines");
        setUpRecyclerView(query);

    }


    private void setUpRecyclerView(Query query) {
        //query = db.collection("drivers").whereEqualTo("line", SP.getString("line", ""));

        //.orderBy("name").startAt("Mal").endAt("Mal"+"\uf8ff");

        FirestoreRecyclerOptions<line> options = new FirestoreRecyclerOptions.Builder<line>()
                .setQuery(query, line.class)
                .build();

        linesListAdapte = new lineListAdapter(options, SP.getString("line", ""), this);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(linesListAdapte);


    }

    @Override
    protected void onStart() {
        super.onStart();
        linesListAdapte.startListening();
    }

}
