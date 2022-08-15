package com.themafia.apps.chatty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    MessageAdapter adapter;
    List<MessageModelClass> list;
    

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

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        userName = getIntent().getStringExtra("Username");
        otherName = getIntent().getStringExtra("OtherName");

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();

        mOtherName.setText(otherName);

        getMessage();
        adapter = new MessageAdapter(list,userName);
        rvChat.setAdapter(adapter);
        
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

    public void sendMessage(String message) {
       final String key = mReference.child("Messages").child(userName).child(otherName).push().getKey();
        MessageModelClass messageMap = new MessageModelClass(message , userName);
        mReference.child("Messages").child(userName).child(otherName).child(key).setValue(messageMap)
                .addOnCompleteListener((Task<Void> task) -> {
            if (task.isSuccessful()){
                mReference.child("Messages").child(otherName).child(userName).child(key).setValue(messageMap);
            }
        });
    }

    public void getMessage()
    {
        mReference.child("Messages").child(userName).child(otherName)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        MessageModelClass modelClass = snapshot.getValue(MessageModelClass.class);
                        list.add(modelClass);
//                        Log.d("MAIN" , "List " + list);
                        adapter.notifyDataSetChanged();
                        rvChat.scrollToPosition(list.size()-1);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

//        adapter = new MessageAdapter(list,userName);
//        rvChat.setAdapter(adapter);
      }
}