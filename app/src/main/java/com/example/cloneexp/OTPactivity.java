package com.example.cloneexp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cloneexp.user.Useractivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.PhoneAuthProvider.*;

public class OTPactivity extends AppCompatActivity {
    EditText OTPnumber;
    String phone;
    FirebaseAuth mAuth;
    String autoCode,code;
    static
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_o_t_pactivity);
      FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        phone=getIntent().getStringExtra("number");
        signInVerification();
        OTPnumber=findViewById(R.id.OTPnumber);
        findViewById(R.id.signin).setOnClickListener(v -> {
            if(OTPnumber.getText().toString().isEmpty())
                signInVerification();
            else
                verifySignIn();
        });

    }
    private void verifySignIn()
    {
        PhoneAuthCredential credential= getCredential(code,OTPnumber.getText().toString());
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(OTPactivity.this, "Logging you in...", Toast.LENGTH_SHORT).show();
                   final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null)
                    {
                       final DatabaseReference mUserDB=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                       mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if(!snapshot.exists())
                               {
                                   Map<String, Object> manualInput= new HashMap<>();
                                   manualInput.put("phone",user.getPhoneNumber());
                                   manualInput.put("name",user.getPhoneNumber());
                                   mUserDB.updateChildren(manualInput);
                               }
                               userIsLoggedIn();
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });
                    }

                }
                else
                {
                    Toast.makeText(OTPactivity.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void signInVerification()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 phone ,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                OTPactivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
 }
    OnVerificationStateChangedCallbacks  mCallbacks=new OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            autoCode=phoneAuthCredential.getSmsCode();
            if(autoCode!=null)
            {
                verifySignIn();
            }
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPactivity.this, "Sorry, please try later", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            code=s;
        }
    };
    private void userIsLoggedIn()
    {
        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            startActivity(new Intent(OTPactivity.this, Useractivity.class));
        }
    }
}