package com.example.chatapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Model.User;
import com.example.chatapp.databinding.ItemContainerUserBinding;
import com.example.chatapp.listeners.UserListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.userViewHolder> {

    private final List<User> users;
    private final UserListener userListener;


    public UserAdapter(List<User> users,UserListener userListener) {


        this.users = users;
        this.userListener=userListener;
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding=ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new userViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {

        holder.serUSerData(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class userViewHolder extends  RecyclerView.ViewHolder{

        ItemContainerUserBinding binding;
        userViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding=itemContainerUserBinding;
        };

        void serUSerData(User user){
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.imageProfile.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(view -> userListener.onUserClicked(user));

        }
    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes= Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }



}
