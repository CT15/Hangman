package com.thenewdomain.hangman;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class DecideSecretWord extends AppCompatActivity {

    private final String TAG = "DecideSecretWord";
    //xml
    private EditText etSetSecretWord;
    private Button btnSetSecretWord;
    private ImageView ivCharacterImg1, ivCharacterImg2;
    private TextView tvCharacter1Name, tvCharacter2Name;

    //intent
    Intent intent;
    String roomKey;

    String userID;

    //firebase
    DatabaseReference roomReference;

    DatabaseReference playerReference;
    String character1;
    String character2;

    String gameState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decide_secret_word);

        etSetSecretWord = (EditText) findViewById(R.id.etSetSecretWord);
        etSetSecretWord.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        btnSetSecretWord = (Button) findViewById(R.id.btnSetSecretWord);

        ivCharacterImg1 = (ImageView) findViewById(R.id.ivCharacterImg1);
        ivCharacterImg2 = (ImageView) findViewById(R.id.ivCharacterImg2);

        tvCharacter1Name = (TextView) findViewById(R.id.tvCharacter1Name);
        tvCharacter2Name = (TextView) findViewById(R.id.tvCharacter2Name);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        intent = getIntent();
        roomKey = intent.getStringExtra("KEY");

        playerReference = FirebaseDatabase.getInstance().getReference().child("users");

        roomReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomKey);

        roomReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String messageKey = dataSnapshot.child("messageID").getValue(String.class);
                gameState = dataSnapshot.child("gameState").getValue(String.class);

                String player1ID = dataSnapshot.child("player1ID").getValue(String.class);
                String player2ID = dataSnapshot.child("player2ID").getValue(String.class);

                playerReference.child(player1ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        character1 = dataSnapshot.child("currentChosenCharacter").getValue(String.class);
                        tvCharacter1Name.setText(character1);
                        setCharacterPic("player1", character1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                playerReference.child(player2ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        character2 = dataSnapshot.child("currentChosenCharacter").getValue(String.class);
                        tvCharacter2Name.setText(character2);
                        setCharacterPic("player2", character2);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
                        int life = 7;
                        boolean isLeoKim = false;
                        if (gameState.equals("player_1_setting_word")) {
                            temp.put("secretWord1", secretWord);
                            temp.put("gameState", "player_2_guessing");

                            if(character1.equals("Nagase")) life -= 1;
                            else if(character1.equals("Dr. James")){
                                int noOfDifferentVowels = countDifferentVowels(secretWord);

                                if(noOfDifferentVowels == 2) life -= 1;
                                else if(noOfDifferentVowels >= 4) life -= 2;
                            }

                            if(character2.equals("Yasuo")) life += 2;
                            else if (character2.equals("Leo Kim")) isLeoKim = true;

                            temp.put("life2", life);
                        } else if (gameState.equals("player_2_setting_word")) {
                            temp.put("secretWord2", secretWord);
                            temp.put("gameState", "player_1_guessing");

                            if(character1.equals("Yasuo")) life += 2;
                            else if (character1.equals("Leo Kim")) isLeoKim = true;

                            if(character2.equals("Nagase")) life -= 1;
                            else if (character2.equals("Dr. James")){
                                int noOfDifferentVowels = countDifferentVowels(secretWord);

                                if(noOfDifferentVowels == 2) life -= 1;
                                else if(noOfDifferentVowels >= 4) life -= 2;
                            }

                            temp.put("life1", life);
                        }

                        //new placeholder in the "rooms" database for _ _ _
                        String blankSpaces = "";

                        if(!isLeoKim) {
                            for (int i = 0; i < secretWord.length(); i++) blankSpaces += "_ ";
                        } else {
                            int[] vowels = new int[5];
                            //a, o, e, i, u <== in this order
                            for(int i = 0; i < secretWord.length(); i++){
                                switch(secretWord.substring(i, i+1)){
                                    case "A":
                                        vowels[0]++;
                                        break;
                                    case "O":
                                        vowels[1]++;
                                        break;
                                    case "E":
                                        vowels[2]++;
                                        break;
                                    case "I":
                                        vowels[3]++;
                                        break;
                                    case "U":
                                        vowels[4]++;
                                        break;
                                }
                            }

                            boolean firstNonRepeatingVowelIsFound = false;
                            for(int i = 0; i < secretWord.length(); i++) {
                                String character = secretWord.substring(i, i+1);
                                if(isVowel(character) && !firstNonRepeatingVowelIsFound){
                                    if(character.equals("A")){
                                        if(vowels[0] == 1){
                                            firstNonRepeatingVowelIsFound = true;
                                            blankSpaces += "A ";
                                        } else blankSpaces += "_ ";
                                    } else if (character.equals("O")) {
                                        if(vowels[1] == 1){
                                            firstNonRepeatingVowelIsFound = true;
                                            blankSpaces += "O ";
                                        } else blankSpaces += "_ ";
                                    } else if (character.equals("E")) {
                                        if(vowels[2] == 1){
                                            firstNonRepeatingVowelIsFound = true;
                                            blankSpaces += "E ";
                                        } else blankSpaces += "_ ";
                                    } else if (character.equals("I")) {
                                        if(vowels[3] == 1){
                                            firstNonRepeatingVowelIsFound = true;
                                            blankSpaces += "I ";
                                        } else blankSpaces += "_ ";
                                    } else if (character.equals("U")) {
                                        if(vowels[4] == 1){
                                            firstNonRepeatingVowelIsFound = true;
                                            blankSpaces += "U ";
                                        } else blankSpaces += "_ ";
                                    }
                                } else blankSpaces += "_ ";
                            }
                        }

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

        ivCharacterImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPopUpCharacterAbility = new Intent(DecideSecretWord.this, PopUpCharacterAbility.class);
                toPopUpCharacterAbility.putExtra("CHARACTER", character1);
                startActivity(toPopUpCharacterAbility);
            }
        });

        ivCharacterImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPopUpCharacterAbility = new Intent(DecideSecretWord.this, PopUpCharacterAbility.class);
                toPopUpCharacterAbility.putExtra("CHARACTER", character2);
                startActivity(toPopUpCharacterAbility);
            }
        });
    }

    private void setCharacterPic(String player, String character) {
        ImageView temp = ivCharacterImg1;

        if(player.equals("player1")) temp = ivCharacterImg1;
        else if (player.equals("player2")) temp = ivCharacterImg2;

        switch(character){
            case "Billy":
                temp.setImageResource(R.drawable.character_billy);
                break;
            case "Nagase":
                temp.setImageResource(R.drawable.character_nagase);
                break;
            case "Leo Kim":
                temp.setImageResource(R.drawable.character_leokim);
                break;
            case "Dr. James":
                temp.setImageResource(R.drawable.character_drjames);
                break;
            case "Yasuo":
                temp.setImageResource(R.drawable.character_yasuo);
                break;
            case "Carter":
                temp.setImageResource(R.drawable.character_carter);
                break;
        }
    }

    private int countDifferentVowels(String word) {
        int noOfDifferentVowels = 0;

        int[] vowels = new int[5];
        //a, o, e, i, u <== in this order
        for(int i = 0; i < word.length(); i++) {
            switch (word.substring(i, i + 1)) {
                case "A":
                    vowels[0]++;
                    break;
                case "O":
                    vowels[1]++;
                    break;
                case "E":
                    vowels[2]++;
                    break;
                case "I":
                    vowels[3]++;
                    break;
                case "U":
                    vowels[4]++;
                    break;
            }
        }

        for(int i = 0; i < 5; i++){
            if(vowels[i] > 0) noOfDifferentVowels++;
        }

        return noOfDifferentVowels;
    }

    private boolean isVowel(String character) {
        if(character.equals("A") || character.equals("O") || character.equals("E") ||
                character.equals("I") || character.equals("U")) return true;
        return false;
    }


    private boolean isValidWord(String secretWord) {
        return secretWord.matches("[a-zA-Z]+");
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
