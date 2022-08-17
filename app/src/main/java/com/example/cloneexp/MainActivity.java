package com.example.cloneexp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cloneexp.user.Useractivity;
import com.example.cloneexp.utils.CountrytoPhonePrefix;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText phoneNumber;
    private Spinner mcountrycode;
    private ArrayList<String> codes;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OTPactivity.user= FirebaseAuth.getInstance().getCurrentUser();
        if(OTPactivity.user!=null)
        {
            startActivity(new Intent(this, Useractivity.class));
        }
        setContentView(R.layout.activity_main);
        mcountrycode=findViewById(R.id.countrycode);
        codes=new ArrayList<>();
        getcodes();
        ArrayAdapter adapter=new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,codes);
        mcountrycode.setAdapter(adapter);
        mcountrycode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                   @Override
                                                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                       code=codes.get(position);

                                                   }

                                                   @Override
                                                   public void onNothingSelected(AdapterView<?> parent) {

                                                   }
                                               });

                findViewById(R.id.proceedButton).setOnClickListener(v -> {

                            phoneNumber = findViewById(R.id.phoneNumber);
                            String phoneFinal = phoneNumber.getText().toString();
                            phoneFinal = code + phoneFinal;
                            if (phoneNumber.getText().toString().length() == 10) {
                                Intent intent = new Intent(MainActivity.this, OTPactivity.class);
                                intent.putExtra("number", phoneFinal);
                                startActivity(intent);
                            } else if(phoneNumber.getText().toString()==""){
                                Toast.makeText(MainActivity.this, "Number Invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

    }
    private void getcodes()
    {
        codes.add( "+91");
        codes.add( "+93");
        codes.add("+355");
        codes.add( "+213");
        codes.add( "+376");
        codes.add( "+244");
        codes.add( "+1-268");
        codes.add( "+54");
        codes.add( "+374");
        codes.add("+61");
        codes.add("+43");
        codes.add("+994");
        codes.add("+1-242");
        codes.add("+973");
        codes.add("+880");
        codes.add("+1-246");
        codes.add("+375");
        codes.add( "+32");
        codes.add( "+501");
        codes.add("+229");
        codes.add("+975");
        codes.add( "+591");
        codes.add( "+387");
        codes.add( "+267");
        codes.add( "+55");
        codes.add( "+673");
        codes.add( "+359");
        codes.add( "+226");
        codes.add( "+257");
        codes.add( "+855");
        codes.add( "+237");
        codes.add( "+1");
        codes.add( "+238");
        codes.add( "+236");
        codes.add( "+235");
        codes.add( "+56");
        codes.add( "+86");
        codes.add( "+57");
        codes.add( "+269");
        codes.add( "+243");
        codes.add( "+242");
        codes.add( "+506");
        codes.add( "+225");
        codes.add( "+385");
        codes.add("+53");
        codes.add( "+357");
        codes.add( "+420");
        codes.add("+45");
        codes.add("+253");
        codes.add( "+1-767");
        codes.add( "+1-809and1-829");
        codes.add( "+593");
        codes.add( "+20");
        codes.add("+503");
        codes.add("+240");
        codes.add( "+291");
        codes.add( "+372");
        codes.add( "+251");
        codes.add( "+679");
        codes.add( "+358");
        codes.add( "+33");
        codes.add("+241");
        codes.add("+220");
        codes.add( "+995");
        codes.add("+49");
        codes.add( "+233");
        codes.add("+30");
        codes.add("+1-473");
        codes.add("+502");
        codes.add("+224");
        codes.add("+245");
        codes.add( "+592");
        codes.add("+509");
        codes.add("+504");
        codes.add("+36");
        codes.add("+354");

        codes.add("+62");
        codes.add( "+98");
        codes.add( "+964");
        codes.add("+353");
        codes.add( "+972");
        codes.add("+39");
        codes.add( "+1-876");
        codes.add( "+81");
        codes.add( "+962");
        codes.add( "+7");
        codes.add( "+254");
        codes.add("+686");
        codes.add( "+850");
        codes.add( "+82");
        codes.add( "+965");
        codes.add("+996");
        codes.add( "+856");
        codes.add( "+371");
        codes.add( "+373");
        codes.add( "+377");
        codes.add( "+976");
        codes.add( "+382");
        codes.add("+212");



        codes.add( "+381");
        codes.add( "+970");
        codes.add( "+212");
    }

}