package com.example.cloneexp.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;

import com.example.cloneexp.R;
import com.example.cloneexp.user.UserListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    ArrayList<ChatObject> chats;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        chats=new ArrayList<>();
        getPermission();

        initializeRecycler();
        getChats();

    }
    private void getChats()
    {
        DatabaseReference mChatDB= FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");
        mChatDB.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot childsnapshot: snapshot.getChildren())
                    {
                        ChatObject mchat=new ChatObject(childsnapshot.getKey());
                        boolean exists=false;
                        for(ChatObject chatIterator:chats)
                        {
                            if(chatIterator.getChatId().equals(mchat.getChatId()))
                            {exists=true;}
                        }
                        if(exists)
                        {continue;}
                        chats.add(mchat);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initializeRecycler()
    {
        recycler=findViewById(R.id.recycler2);
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);
        adapter=new ChatListAdapter(chats);
        recycler.setAdapter(adapter);
    }
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]
                    {
                            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 1);
        }
    }

}
