package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.firestore.FieldValue;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import android.util.Base64;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager=new PreferenceManager(getApplicationContext());
        loadUSerDetails();
        getToken();
        setListeners();
    }

    private void loadUSerDetails(){
        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
        byte[] bytes= Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);

    }

    private void setListeners(){
        binding.signout.setOnClickListener(view -> signOut());
        binding.fabNewChat.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),UserActivity.class)));
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }


    private  void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference documentReference=
                db.collection(Constants.KEY_COllECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
                .addOnFailureListener(e -> showToast("unbale to update token"));
    }

    @NonNull
    private  void  signOut(){
    showToast("Signing out");
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    DocumentReference documentReference=db.collection(Constants.KEY_COllECTION_USERS).document(
            preferenceManager.getString(Constants.KEY_USER_ID)
    );
        HashMap<String,Object> updates=new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN,FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(
                unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), Sign_up.class));
                    finish();
                }).addOnFailureListener(e -> showToast("unbale to signout"));

    }
}