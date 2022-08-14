package com.themafia.apps.chatty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private ImageView backBtn;
    private EditText chatMessage;
    private FloatingActionButton sendMessageBtn;
    private TextView mOtherName;

    String userName , otherName;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_chat);

        rvChat = findViewById(R.id.rVChat);
        backBtn = findViewById(R.id.backButton);
        chatMessage = findViewById(R.id.editTextChatMessage);
        sendMessageBtn = findViewById(R.id.floatingActionButton);
        mOtherName = findViewById(R.id.textViewChat);

        userName = getIntent().getStringExtra("Username");
        otherName = getIntent().getStringExtra("OtherName");

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        mOtherName.setText(otherName);
        
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(ChatActivity.this , MainActivity.class));
        });
        
        sendMessageBtn.setOnClickListener(v -> {
            String message = chatMessage.getText().toString();
            
            if (!message.equals("")){
                sendMessage(message);
                chatMessage.setText("");
            }
        });


    }

    private void sendMessage(String message) {
        String key = mReference.child("Messages").child(userName).child(otherName).push().getKey();
        HashMap<String , Object> messageMap = new HashMap<>();
        messageMap.put("Message" , message);
        messageMap.put("From" , userName);

        mReference.child("Messages").child(userName).child(otherName).child(key).setValue(messageMap)
                .addOnCompleteListener((Task<Void> task) -> {
            if (task.isSuccessful()){
                mReference.child("Messages").child(otherName).child(userName).child(key).setValue(messageMap);
            }
        });
    }
}