package com.thenewdomain.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateUserName extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "Create User Name";

    //xml related attributes
    EditText etUserName;
    Button btnConfirmUserName;

    //others
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_name);

        init();
    }

    private void init() {
        //xml related attributes
        etUserName = (EditText) findViewById(R.id.etUserName);
        etUserName.setFilters(new InputFilter[] {new InputFilter.LengthFilter(7)});

        btnConfirmUserName = (Button) findViewById((R.id.btnConfirmUserName));

        btnConfirmUserName.setOnClickListener(this);

        //others
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirmUserName:
                if (!TextUtils.isEmpty(etUserName.getText().toString().trim())) {
                    saveUser();
                    finish();
                    startActivity(new Intent(CreateUserName.this, MainMenu.class));
                }
                break;
        }
    }

    private void saveUser() {
        String userName = etUserName.getText().toString().trim();
        UserInformation userInformation = new UserInformation(userName);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("users").child(user.getUid()).setValue(userInformation);
    }
}