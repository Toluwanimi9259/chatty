package com.themafia.apps.chatty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText updateUsername;
    private Button updateBtn;
    private CircleImageView updateProfilePic;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseUser mUser;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;

    boolean imageControl = false;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        updateUsername = findViewById(R.id.textUpdateUsername);
        updateProfilePic = findViewById(R.id.imageViewUpdateCircle);
        updateBtn = findViewById(R.id.buttonUpdate);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        getUserInfo();

        updateProfilePic.setOnClickListener(e -> {
            imageChooser();
        });

        updateBtn.setOnClickListener(v -> {
            updateUserInfo();
        });

    }

    public void updateUserInfo(){

        String userName = updateUsername.getText().toString();
        mReference.child("Users").child(mUser.getUid()).child("userName").setValue(userName);


        if (imageControl){

            UUID randomID = UUID.randomUUID();
            String imageName = "images/"+randomID+".jpg";
            mStorageReference.child(imageName).putFile(imageUri)
                    .addOnSuccessListener((UploadTask.TaskSnapshot taskSnapshot) -> {
                        StorageReference myStoredImage = mStorage.getReference(imageName);
                        myStoredImage.getDownloadUrl().addOnSuccessListener((Uri uri) -> {
                            mReference.child("Users").child(mAuth.getUid()).child("image").setValue(uri.toString())
                                    .addOnSuccessListener((Void unused) -> {
                                        Toast.makeText(this, "Write To Database Successful", Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener((Exception e) -> {
                                        Toast.makeText(this, "UnSuccessful" + e, Toast.LENGTH_SHORT).show();

                                    });
                        });
                    });

        }else{
            mReference.child("Users").child(mAuth.getUid()).child("image").setValue("null");
        }

        startActivity(new Intent(ProfileActivity.this , MainActivity.class).putExtra("Username" , userName));
        finish();

    }

    public void getUserInfo(){
        mReference.child("Users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String userName = snapshot.child("userName").getValue().toString();
                String imageUrl = snapshot.child("image").getValue().toString();
                updateUsername.setText(userName);
                if (imageUrl.equals("null")){
                    updateProfilePic.setImageResource(R.drawable.account);

                }else{
                    Picasso.get().load(imageUrl).into(updateProfilePic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void imageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent ,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(updateProfilePic);
            imageControl = true;
        }else {
            imageControl = false;
        }
    }
}