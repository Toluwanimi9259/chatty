package com.themafia.apps.chatty;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView profilePic;
    private TextInputEditText editTextSignUpEmail , editTextSignUpPassword , editTextSignUpUsername;
    private Button registerBtn;
    boolean imageControl = false;

    Uri imageUri;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        profilePic = findViewById(R.id.imageViewCircle);
        editTextSignUpEmail = findViewById(R.id.editTextSignUpEmail);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        editTextSignUpUsername = findViewById(R.id.editTextSignUpUsername);
        registerBtn =  findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        profilePic.setOnClickListener(view -> {
            imageChooser();
        });

        registerBtn.setOnClickListener(view -> {
            String email = editTextSignUpEmail.getText().toString();
            String password = editTextSignUpPassword.getText().toString();
            String username = editTextSignUpUsername.getText().toString();

            if (!email.equals("") && !password.equals("") && !username.equals("")){
                signUp(email , password , username);
            }else{
                Toast.makeText(this, "Enter A Valid Email Or Password", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void signUp(String email, String password, String username) {

        mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){


                mReference.child("Users").child(mAuth.getUid()).child("userName").setValue(username);

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
//                    Toast.makeText(this, "A Problem Has Occurred", Toast.LENGTH_SHORT).show();
                    mReference.child("Users").child(mAuth.getUid()).child("image").setValue("null");
                }

                startActivity(new Intent(SignUpActivity.this , MainActivity.class));
//                .putExtra("Username" , username)
                finish();

            }else {
                Toast.makeText(this, "A Problem Has Occurred", Toast.LENGTH_SHORT).show();
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
            Picasso.get().load(imageUri).into(profilePic);
            imageControl = true;
        }else {
            imageControl = false;
        }
    }
}