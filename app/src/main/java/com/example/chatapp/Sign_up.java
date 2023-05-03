package com.example.chatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.databinding.ActivitySignUpBinding;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
public class Sign_up extends AppCompatActivity {

    EditText fullName, email, password, phone;
    Button registerBtn;
    private PreferenceManager preferenceManager;
    private ActivitySignUpBinding binding;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setListeners();

    }

    private void setListeners() {
        binding.gotoLogin.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Sign_inActivity.class)));
        binding.registerBtn.setOnClickListener(view -> {
            signUp();
        });

        binding.layoutImage.setOnClickListener(view -> {
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });




    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private  String encodedImage(Bitmap bitmap){
        int previewWidth =150;
        int previewHeight=bitmap.getHeight()* previewWidth /bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        return  Base64.encodeToString(bytes,Base64.DEFAULT);
    }


    private  final ActivityResultLauncher<Intent> pickImage=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData() !=null){
                        Uri imageUri =result.getData().getData();

                        try{
                            InputStream inputStream=getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage= encodedImage(bitmap);
                        }catch(FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private void signUp(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user=new HashMap<>();
        user.put(Constants.KEY_NAME,binding.registerName.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.registerEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.registerPassword.getText().toString());
        user.put(Constants.KEY_PHONE,binding.registerPhone.getText().toString());
        user.put(Constants.KEY_IMAGE,encodedImage);

        db.collection(Constants.KEY_COllECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME,binding.registerName.getText().toString());
                    preferenceManager.putString(Constants.KEY_PHONE,binding.registerPhone.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);

                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }).addOnFailureListener(exception ->{
                    showToast(exception.getMessage());
                });


    }

}