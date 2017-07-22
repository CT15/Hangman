package com.thenewdomain.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class DecideSecretWord extends AppCompatActivity {

    //xml
    private EditText etSetSecretWord;
    private Button btnSetSecretWord;

    //intent
    Intent intent;
    String roomKey;

    String userID;

    //firebase
    DatabaseReference roomReference;

    String gameState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide_secret_word);

        etSetSecretWord = (EditText) findViewById(R.id.etSetSecretWord);
        etSetSecretWord.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        btnSetSecretWord = (Button) findViewById(R.id.btnSetSecretWord);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        intent = getIntent();
        roomKey = intent.getStringExtra("KEY");

        roomReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomKey);

        roomReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String messageKey = dataSnapshot.child("messageID").getValue(String.class);
                gameState = dataSnapshot.child("gameState").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSetSecretWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String secretWord = etSetSecretWord.getText().toString().trim();
                if (!secretWord.equals("")) {
                    if (isValidWord(secretWord)) {
                        //check if player1 or player2 --> update secretWord1 or secretWord2 accordingly
                        HashMap<String, Object> temp = new HashMap<String, Object>();
                        if (gameState.equals("player_1_setting_word")) {
                            temp.put("secretWord1", secretWord);
                            temp.put("gameState", "player_2_guessing");
                        } else if (gameState.equals("player_2_setting_word")) {
                            temp.put("secretWord2", secretWord);
                            temp.put("gameState", "player_1_guessing");
                        }

                        //new placeholder in the "rooms" database for _ _ _
                        String blankSpaces = "";
                        for (int i = 0; i < secretWord.length(); i++) blankSpaces += "_ ";
                        if(gameState.equals("player_1_setting_word")){
                            temp.put("blankSpace1", blankSpaces.trim());
                        } else if (gameState.equals("player_2_setting_word")){
                            temp.put("blankSpace2", blankSpaces.trim());
                        }


                        roomReference.updateChildren(temp);

                    } else {
                        Toast.makeText(getApplicationContext(), secretWord + " is an invalid word", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    private boolean isValidWord(String secretWord) {
        return secretWord.matches("[a-zA-Z]+");
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
