package com.example.cloneexp.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.cloneexp.MainActivity;
import com.example.cloneexp.R;
import com.example.cloneexp.chat.ChatActivity;
import com.example.cloneexp.user.UserList;
import com.example.cloneexp.user.UserListAdapter;
import com.example.cloneexp.utils.CountrytoPhonePrefix;
//import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Useractivity extends AppCompatActivity {
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter1;
    private RecyclerView.LayoutManager layoutManager;
    private int position;
    ArrayList<UserList> userList,contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useractivity);
//        Fresco.initialize(this);

        findViewById(R.id.logout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            Toast.makeText(getApplicationContext(), "Logging you out...", Toast.LENGTH_SHORT).show();
        });

        contactList=new ArrayList<>();
        userList=new ArrayList<>();
        getPermission();
        getContactList();
       initializeRecycler();
    }
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]
                    {
                            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 1);
        }
    }

    private String getCountryIso() {
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso() != null) {
            if (telephonyManager.getNetworkCountryIso()!="") {
                iso = telephonyManager.getNetworkCountryIso();
            }
        }
        return CountrytoPhonePrefix.getPhone(iso);
    }

    private void initializeRecycler()
    {
        recycler=findViewById(R.id.recycler3);
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(false);
        layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);
        adapter1=new UserListAdapter(userList);
        recycler.setAdapter(adapter1);
    }
    private void getContactList()
    {
        String ISOprefix=getCountryIso();
        Cursor phones=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number=phones.getString((phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            number.replace(" ","");
            number.replace("-","");
            number.replace("(","");
            number.replace(")","");
            if(!number.startsWith("+"))
            {
                number=ISOprefix+number;
            }
            UserList object=new UserList(name,number,"");
            if(contactList.size()==0)
            {
                contactList.add(object);
            }
            if(contactList.size()>0) {
                position = contactList.size();
                if (!(contactList.get(position - 1).getName().equals(number) || contactList.get(position - 1).getNumber().equals(number))) {
                    contactList.add(object);
                    getUserDetails(object);
                }
            }
        }
    }

    private void getUserDetails(UserList contact) {
        DatabaseReference mUserDB= FirebaseDatabase.getInstance().getReference().child("user");
        Query query=mUserDB.orderByChild("phone").equalTo(contact.getNumber());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String number="",name="";
                            for(DataSnapshot childSnapshot: dataSnapshot.getChildren())
                            {
                                if(childSnapshot.child("phone").getValue()!=null)
                                {
                                    number=childSnapshot.child("phone").getValue().toString();
                                }
                                if(childSnapshot.child("name").getValue()!=null)
                                {
                                    name=childSnapshot.child("name").getValue().toString();
                                }
                                    UserList newObject=new UserList(name,number,childSnapshot.getKey());
                                if(name.equals(number))
                                {
                                    for(UserList Iterator: contactList)
                                    {
                                        if(Iterator.getNumber().equals(newObject.getNumber())){
                                            newObject.setName(Iterator.getName());
                                        }
                                    }
                                }

                                      userList.add(newObject);

                                adapter1.notifyDataSetChanged();
                                return;
                            }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}