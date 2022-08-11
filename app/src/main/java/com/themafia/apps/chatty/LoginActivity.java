package com.themafia.apps.chatty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail , editTextPassword;
    private Button btnSignUp , btnSignIn;
    private TextView textForgotPassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnSignIn = findViewById(R.id.buttonSignIn);
        btnSignUp = findViewById(R.id.buttonSignUp);
        textForgotPassword = findViewById(R.id.textViewForgotPassword);

        mAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (!email.equals("") && !password.equals("")){
                signIn(email , password);
            }else{
                Toast.makeText(this, "Enter A Valid Email Or Password", Toast.LENGTH_SHORT).show();

            }


        });

        btnSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this , SignUpActivity.class);
            startActivity(intent);
        });

        textForgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this , ForgetPasswordActivity.class);
            startActivity(intent);
        });
    }

    public void signIn(String email , String password){
        mAuth.signInWithEmailAndPassword(email  , password).addOnCompleteListener((Task<AuthResult> task) -> {
            if (task.isSuccessful()){

                Toast.makeText(this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                startActivity(intent);

            }else {
                Toast.makeText(this, R.string.login_failed, 1).show();

            }
        });
    }


}