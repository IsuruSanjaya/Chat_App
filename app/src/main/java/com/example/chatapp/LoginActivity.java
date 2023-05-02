package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText etemail, etpass;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        TextView register = findViewById(R.id.etregister);
        etemail = findViewById(R.id.email);
        etpass = findViewById(R.id.pass);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Button btnLogin = findViewById(R.id.btnLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, Sign_up.class);
                startActivity(intent);

            }
        });


        btnLogin.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etemail.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter the email", Toast.LENGTH_SHORT).show();

                } else if (etpass.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter the password", Toast.LENGTH_SHORT).show();
                } else {
                    {
                        fAuth.signInWithEmailAndPassword(etemail.getText().toString(), etpass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Logged success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                }

            }
        }));

    }

}