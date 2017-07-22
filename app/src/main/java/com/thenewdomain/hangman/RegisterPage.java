package com.thenewdomain.hangman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener{

    private EditText etEmail2;
    private EditText etPassword2;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private Button btnBackToSignIn;
    //private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    //private UserInformation user = new UserInformation();

    //others
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        etEmail2 = (EditText) findViewById(R.id.etEmail2);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnBackToSignIn = (Button) findViewById(R.id.btnBackToSignIn);

        btnRegister.setOnClickListener(this);
        btnBackToSignIn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                registerUser();
                break;
            case R.id.btnBackToSignIn:
                finish();
                startActivity(new Intent(RegisterPage.this, SignInRegister.class));
                break;
        }
    }

    private void registerUser() {
        String email = etEmail2.getText().toString().trim();
        String password = etPassword2.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(getApplicationContext(), "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassword)){
            //confirm password is empty
            Toast.makeText(getApplicationContext(), "Please Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)){
            //password is not the same as confirm password
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            //if validations are ok
            //registration is an internet operation, it takes times
            //show progress dialog
            progressDialog.setMessage("Registering User ...");
            progressDialog.show();

            //create user in Firebase console
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                //user is successfully registered and logged in
                                //Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                //start the CreateUserName activity
                                Intent toCreateUserName = new Intent(RegisterPage.this, CreateUserName.class);
                                startActivity(toCreateUserName);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to Register ... Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

// -----------------------> SQLite code onCreate
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (etUserName2.getText().toString().trim().isEmpty() ||
//                        etPassword2.getText().toString().trim().isEmpty() ||
//                        etConfirmPassword.getText().toString().trim().isEmpty()){
//                    Toast.makeText(getApplicationContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
//                } else if (etPassword2.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())){
//
//                    if(databaseHelper.checkUser(etUserName2.getText().toString().trim(), etPassword2.getText().toString().trim(), false)){
//                        //if user exists
//                        Toast.makeText(getApplicationContext(), "UserInformation name already exists", Toast.LENGTH_SHORT).show();
//                    } else {
//                        user.setPassword(etPassword2.getText().toString().trim());
//                        user.setUserName(etUserName2.getText().toString().trim());
//                        databaseHelper.addUser(user);
//                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
//
//                        SharedPreferences sharedPreferences = getSharedPreferences("DataFromSignIn", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("USERNAME", etUserName2.getText().toString().trim());
//                        editor.commit();
//
//                        Intent toMainMenu = new Intent(RegisterPage.this, MainMenu.class);
//                        startActivity(toMainMenu);
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });