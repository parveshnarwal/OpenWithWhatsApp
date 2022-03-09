package com.openwithwhatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    AppCompatEditText cc, pn;
    AppCompatButton btn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cc = findViewById(R.id.etCC);
        pn = findViewById(R.id.etPN);
        btn = findViewById(R.id.btn);
        //Set focus
        pn.requestFocus();

        //Open keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        String c_code = sharedPreferences.getString("COUNTRY_CODE", "");

        if(!c_code.isEmpty())
            cc.setText(c_code);

        btn.setOnClickListener(view -> OpenInWhatsApp());

    }

    private void OpenInWhatsApp(){
        String code  = Objects.requireNonNull(cc.getText()).toString().trim();
        String phoneNum = Objects.requireNonNull(pn.getText()).toString().trim();
        if(code.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter country code", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phoneNum.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "https://api.whatsapp.com/send?phone="+ code + phoneNum;

        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("COUNTRY_CODE", code);
        myEdit.apply();

        try{
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        catch (ActivityNotFoundException anfe){
            Toast.makeText(getApplicationContext(), "No supported app found in your phone.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Sorry something went wrong.", Toast.LENGTH_SHORT).show();
        }


    }
}