package com.example.invoiceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    TextInputEditText email, password;
    FirebaseAuth mAuth;
    Button btnRegister;
    ProgressBar progressBar;
    TextView clickToLogin;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(Register.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progressBar);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);
        clickToLogin = findViewById(R.id.clickToLogin);

        mAuth = FirebaseAuth.getInstance();

        clickToLogin.setOnClickListener(v -> {
            Intent i = new Intent(Register.this, Login.class);
            startActivity(i);
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            btnRegister.setActivated(false);
            String emailText, passwordText;
            emailText = String.valueOf(email.getText());
            passwordText = String.valueOf(password.getText());

            if (emailText.isBlank()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (passwordText.isBlank()) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(ProgressBar.GONE);
                        btnRegister.setActivated(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Register.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        });
    }
}