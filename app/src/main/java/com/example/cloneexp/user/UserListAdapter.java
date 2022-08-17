package com.example.cloneexp.user;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloneexp.R;
import com.example.cloneexp.chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.RecyclerViewHolder> {
    ArrayList<UserList> userListArrayList;
    public UserListAdapter(ArrayList<UserList> userlist) {
        this.userListArrayList = userlist;
    }

    public UserListAdapter() {

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.phone,parent,false);
        ViewGroup.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            holder.personName.setText(userListArrayList.get(position).getName());
            holder.number.setText(userListArrayList.get(position).getNumber());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();
                          FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
                          FirebaseDatabase.getInstance().getReference().child("user").child(userListArrayList.get(position).getUid()).child("chat").child(key).setValue(true);
                    v.getContext().startActivity(new Intent(v.getContext(), ChatActivity.class));

                }
            }
            );
    }

    @Override
    public int getItemCount() {
        return userListArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        public RelativeLayout layout;
        public TextView personName,number;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.layout);
            personName=itemView.findViewById(R.id.name);
            number=itemView.findViewById(R.id.number);

        }
    }

}
