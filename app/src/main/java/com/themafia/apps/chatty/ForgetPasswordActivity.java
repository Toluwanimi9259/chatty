package com.themafia.apps.chatty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputEditText editTextForgetPassword;
    private Button btnForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        editTextForgetPassword = findViewById(R.id.editTextForgetPassword);
        btnForget = findViewById(R.id.buttonForgetPAssword);

    }
}