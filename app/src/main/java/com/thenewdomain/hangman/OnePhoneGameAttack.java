package com.thenewdomain.hangman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class OnePhoneGameAttack extends AppCompatActivity {

    private static final String TAG = "OnePhoneGameAttack";

    Button btnConfirm;
    EditText etWordToBeGuessed;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_phone_game_attack);

        init();

    }

    private void init() {
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnBack = (Button) findViewById(R.id.btnBack);
        etWordToBeGuessed = (EditText) findViewById(R.id.etWordToBeGuessed);

        etWordToBeGuessed.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.btnConfirm:
                        String secretWord = etWordToBeGuessed.getText().toString().trim();
                        if(isValidWord(secretWord)){
                            Intent toOnePhoneGameDefense = new Intent(OnePhoneGameAttack.this, OnePhoneGameDefense.class);
                            toOnePhoneGameDefense.putExtra("SECRET WORD", secretWord);
                            startActivity(toOnePhoneGameDefense);
                        } else if(secretWord.equals("")){

                        } else{
                            Toast.makeText(getApplicationContext(), secretWord + " is an invalid word", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.btnBack:
                        Intent toMainMenu = new Intent(OnePhoneGameAttack.this, MainMenu.class);
                        startActivity(toMainMenu);
                        break;
                }
            }
        };

        btnConfirm.setOnClickListener(listener);
        btnBack.setOnClickListener(listener);
    }

    private boolean isValidWord(String word){
        return(word.matches("[a-zA-Z]+"));
    }



    @Override
    public void onBackPressed(){
       //do nothing
    }
}
