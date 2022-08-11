package com.themafia.apps.chatty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputEditText editTextForgetPassword;
    private Button btnForget;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        editTextForgetPassword = findViewById(R.id.editTextForgetPassword);
        btnForget = findViewById(R.id.buttonForgetPAssword);

        mAuth = FirebaseAuth.getInstance();

        btnForget.setOnClickListener(e -> {
            String email = editTextForgetPassword.getText().toString();

            if (email.isEmpty()){

                Toast.makeText(this, R.string.Invalid_Email, Toast.LENGTH_SHORT).show();

            }else{
                passwordReset(email);
            }
        });

    }

    public void passwordReset(String email){
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
            Toast.makeText(this, "Please Check Your Email", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "There Was A Problem" + e, Toast.LENGTH_SHORT).show();
        });
    }
}