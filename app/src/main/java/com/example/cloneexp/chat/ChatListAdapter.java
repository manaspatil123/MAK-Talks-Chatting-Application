package com.example.cloneexp.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloneexp.message.MessageActivity;
import com.example.cloneexp.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter <ChatListAdapter.RecyclerViewHolder> {
    ArrayList<ChatObject> ChatObjectArrayList;

    public ChatListAdapter(ArrayList<ChatObject> ChatObject) {
        this.ChatObjectArrayList = ChatObject;
    }

    public ChatListAdapter() {

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat,parent,false);
        ViewGroup.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        RecyclerViewHolder recyclerViewHolder=new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        holder.mTitle.setText(ChatObjectArrayList.get(position).getChatId());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), MessageActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("chatId",ChatObjectArrayList.get(holder.getAdapterPosition()).getChatId());
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                
            }
        });
    }

    @Override
    public int getItemCount() {
        return ChatObjectArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTitle;
        public RelativeLayout layout;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.layout);
            mTitle=itemView.findViewById(R.id.title1);


        }
    }
}
