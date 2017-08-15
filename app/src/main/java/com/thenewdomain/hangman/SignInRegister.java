package com.thenewdomain.hangman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInRegister extends AppCompatActivity{

    private Button btnSignIn;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvRegisterHere;
    //private DatabaseHelper databaseHelper;
    //InputValidation inputValidation;

    //firebase sign in
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_register);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        etEmail = (EditText) findViewById(R.id.etEmail2);
        etPassword = (EditText) findViewById(R.id.etPassword2);
        tvRegisterHere = (TextView) findViewById(R.id.tVRegisterHere);

        //databaseHelper = new DatabaseHelper(this);
        //inputValidation = new InputValidation(this);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifyFromSQLite(); //SQLite
                signInUser(); //firebase
            }
        });

        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegisterPage = new Intent(SignInRegister.this, RegisterPage.class);
                startActivity(toRegisterPage);
            }
        });
    }

//        private void verifyFromSQLite(){
//            if(etUserName.getText().toString().trim().isEmpty() || etPassword.getText().toString().trim().isEmpty()){
//                Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if(databaseHelper.checkUser(etUserName.getText().toString().trim(),etPassword.getText().toString().trim(), true)){
//                Intent toMainMenu = new Intent(SignInRegister.this, MainMenu.class);
//
//                SharedPreferences sharedPreferences = getSharedPreferences("DataFromSignIn", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("USERNAME", etUserName.getText().toString().trim());
//                editor.commit(); //find out the difference between this and editor.apply()
//
//                //toMainMenu.putExtra("USERNAME", etUserName.getText().toString().trim());
//
//                startActivity(toMainMenu);
//            } else {
//                Toast.makeText(getApplicationContext(), "Incorrect user name or password", Toast.LENGTH_SHORT).show();
//        }
//    }
    private void signInUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)){
            //password is empty
         Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            //if validations are ok
            //registration is an internet operation, it takes times
            //show progress dialog
        progressDialog.setMessage("Signing In ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            //start main menu
                            startActivity(new Intent(SignInRegister.this, MainMenu.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to Sign In ... Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}

