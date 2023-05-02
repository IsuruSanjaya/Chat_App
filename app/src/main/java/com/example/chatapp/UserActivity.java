package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.chatapp.Model.User;
import com.example.chatapp.adapters.UserAdapter;
import com.example.chatapp.databinding.ActivityUserBinding;
import com.example.chatapp.utilities.Constants;
import com.example.chatapp.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setListener();
        getUsers();


    }


    private void showError(){
        binding.textErrorMessage.setText(String.format("%s", "No user avaialable"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }
    private  void loading(Boolean isLoading){
        if(isLoading){
            binding.progrssBar.setVisibility(View.VISIBLE);
        }else{
            binding.progrssBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setListener(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());
    }


    private  void getUsers(){
        loading(true);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COllECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId=preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null){
                        List<User> users=new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot :task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                continue;
                            }

                            User user=new User();
                            user.name=queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email=queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.phone=queryDocumentSnapshot.getString(Constants.KEY_PHONE);
                            user.image=queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token=queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            users.add(user);

                        }

                        if(users.size()>0){
                            UserAdapter usersAdapter=new UserAdapter(users);
                            binding.userRecycleView.setAdapter(usersAdapter);
                            binding.userRecycleView.setVisibility(View.VISIBLE);
                        }else{
                            showError();

                        }
                    }else{
                        showError();
                    }
                });
    }



//    private void loading(Boolean isLoading){
//        if(isLoading){
//            binding.progressBar.setV
//        }
//    }
}