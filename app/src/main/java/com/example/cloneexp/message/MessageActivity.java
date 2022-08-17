package com.example.cloneexp.message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cloneexp.R;
import com.example.cloneexp.chat.ChatListAdapter;
import com.example.cloneexp.message.MessageListAdapter;
import com.example.cloneexp.message.MessageObject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    ArrayList<MessageObject> chats;
    private RecyclerView recycler,mediarecycler;
    private RecyclerView.Adapter adapter,mediaAdapter;
    private RecyclerView.LayoutManager layoutManager,mediaLayoutManager;
    private EditText message;
    String chatId;
    DatabaseReference inboxDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Button send=findViewById(R.id.send);
        Button media=findViewById(R.id.media);
        chats=new ArrayList<>();
        chatId= getIntent().getExtras().getString("chatId");
        inboxDB=FirebaseDatabase.getInstance().getReference().child("chat").child(chatId);
        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        initializeMedia();
        initializeRecyclerView();
        getMessages();
    }
    ArrayList<String> mediaList=new ArrayList<>();
    int totalImagesAdded=0;
    private void sendMessage()
    {

        message=findViewById(R.id.message);
        if(!message.getText().toString().isEmpty())
        {
            String messageId=inboxDB.push().getKey();
            DatabaseReference messageDB= inboxDB.push();
            Map newMessageMap= new HashMap<>();
            newMessageMap.put("text",message.getText().toString());
            newMessageMap.put("creator", FirebaseAuth.getInstance().getUid());
            if(!mediaUriList.isEmpty())
            {
                for(String mediaUri:mediaUriList) {
                    String mediaId = messageDB.child("media").push().getKey();
                    mediaList.add(mediaId);
                    final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("chat").child(chatId).child(messageId).child(mediaId);
                    UploadTask uploadTask=filePath.putFile(Uri.parse(mediaUri));
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    newMessageMap.put("/media/" + mediaList.get(totalImagesAdded) + "/", uri.toString());
                                    totalImagesAdded++;
                                    if (totalImagesAdded == mediaUriList.size()) {
                                        updateDatabaseWithNewMessage(messageDB, newMessageMap);
                                    }
                                }
                            });
                        }
                    });
                }
            }
            else
            {
                if(!message.getText().toString().isEmpty())
                    updateDatabaseWithNewMessage(messageDB,newMessageMap);

            }
        }


    }
    private void updateDatabaseWithNewMessage(DatabaseReference newMessageDB, Map newMessageMap)
    {
        newMessageDB.updateChildren(newMessageMap);
        message.setText("");
        mediaUriList.clear();
        mediaList.clear();
        mediaAdapter.notifyDataSetChanged();
    }

    private void getMessages() {
        inboxDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    String message="",creator="";
                    ArrayList<String> mediaUrlList=new ArrayList<>();
                    if(snapshot.child("text").getValue()!=null)
                    {
                        message=snapshot.child("text").getValue().toString();
                    }
                    if(snapshot.child("creator").getValue()!=null)
                    {
                        creator=snapshot.child("creator").getValue().toString();
                    }
                    if(snapshot.child("media").getChildrenCount()>0) {
                        for(DataSnapshot mediaSnapshot:snapshot.child("media").getChildren())
                        {
                            mediaUrlList.add(mediaSnapshot.getValue().toString());
                        }
                    }
                        MessageObject messageObject = new MessageObject(snapshot.getKey(), message, creator, mediaUrlList);
                        chats.add(messageObject);
                        layoutManager.scrollToPosition(chats.size() - 1);
                        adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initializeRecyclerView()
    {
        recycler=findViewById(R.id.messageActivity);
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);
        adapter=new MessageListAdapter(chats);
        recycler.setAdapter(adapter);
    }
    int PICK_IMAGE_INTENT=1;
    ArrayList<String> mediaUriList=new ArrayList<>();
    private void initializeMedia()
    {
        mediarecycler=findViewById(R.id.mediaList);
        mediarecycler.setNestedScrollingEnabled(false);
        mediarecycler.setHasFixedSize(false);
        mediaLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        mediarecycler.setLayoutManager(mediaLayoutManager);
         mediaAdapter=new MediaAdapter(getApplicationContext(),mediaUriList);
        mediarecycler.setAdapter(mediaAdapter);
    }
    private void openGallery()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            startActivityForResult(Intent.createChooser(intent,"Select pictures..."),PICK_IMAGE_INTENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==PICK_IMAGE_INTENT)
            {
                if(data.getClipData()==null)
                {
                    mediaUriList.add(data.getData().toString());
                }
                else
                {
                    for(int i=0;i<data.getClipData().getItemCount();i++)
                    {
                        mediaUriList.add(data.getClipData().getItemAt(i).getUri().toString());
                    }
                }
                mediaAdapter.notifyDataSetChanged();
            }
        }
    }
}