package com.gradproj1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.Reference;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent ;
    ImageView sendButton ;
    EditText messageBox ;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendButton = findViewById(R.id.sendButton);
        messageBox = findViewById(R.id.messageBox);
    }

    void sendMessage (String sender , String receiver,String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> messageToSend = new HashMap<>();
        messageToSend.put("sender",sender);
        messageToSend.put("receiver",receiver);
        messageToSend.put("message",message);
    }

    public void send (View view) {
        String message = messageBox.getText().toString();
        sendMessage("+970595435114","+970599444444",message);
    }
}
