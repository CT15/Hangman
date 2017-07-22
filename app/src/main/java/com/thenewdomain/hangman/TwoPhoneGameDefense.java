package com.thenewdomain.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class TwoPhoneGameDefense extends AppCompatActivity {

    final private String TAG = "TwoPhoneGameDefense";
    //xml game
    private TextView tvA, tvB, tvC, tvD, tvE, tvF, tvG, tvH, tvI, tvJ, tvK, tvL, tvM, tvN, tvO, tvP,
            tvQ, tvR, tvS, tvT, tvU, tvV, tvW, tvX, tvY, tvZ;
    private HashMap<String, TextView> letters;
    private Button btnCheck, btnContinue;
    private TextView tvLife, tvSecretWord, tvCharacter1, tvCharacter2, tvPlayer1, tvPlayer2;
    private EditText etGuessLetter;

    private Intent intent;
    String roomKey;

    String gameState;
    int gameStanding;
    String blankSpace;
    String character1;
    String character2;

    //Firebase related
    DatabaseReference roomReference;
    DatabaseReference player1Reference;
    DatabaseReference player2Reference;

    ArrayList<String> messageStorage;
    ArrayList<String> letterStorage;

    //gameplay
    int lifeLine;
    String secretWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_phone_game_defense);

        init();
    }

    private void init() {
        btnCheck = (Button) findViewById(R.id.btnCheck2);
        btnContinue = (Button) findViewById(R.id.btnContinue);

        initLettersAndHashMap();

        tvLife = (TextView) findViewById(R.id.tvDefenseLife);
        tvSecretWord = (TextView) findViewById(R.id.tvSecretWord2);

        etGuessLetter = (EditText) findViewById(R.id.etGuessLetter2);
        etGuessLetter.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        tvCharacter1 = (TextView) findViewById(R.id.tvCharacter1);
        tvCharacter2 = (TextView) findViewById(R.id.tvCharacter2);
        tvPlayer1 = (TextView) findViewById(R.id.tvPlayer1);
        tvPlayer2 = (TextView) findViewById(R.id.tvPlayer2);

        intent = getIntent();
        roomKey = intent.getStringExtra("KEY");

        character1 = "";
        character2 = "";

        roomReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomKey);
        roomReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String player1ID = dataSnapshot.child("player1ID").getValue(String.class);
                String player2ID = dataSnapshot.child("player2ID").getValue(String.class);

                gameStanding = dataSnapshot.child("gameStanding").getValue(Integer.class);

                player1Reference = FirebaseDatabase.getInstance().getReference().child("users").child(player1ID);
                player1Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvPlayer1.setText(dataSnapshot.child("userName").getValue(String.class));
                        character1 = dataSnapshot.child("currentChosenCharacter").getValue(String.class);
                        Log.d(TAG, "character1: " + character1);
                        tvCharacter1.setText(character1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                player2Reference = FirebaseDatabase.getInstance().getReference().child("users").child(player2ID);
                player2Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvPlayer2.setText(dataSnapshot.child("userName").getValue(String.class));
                        character2 = dataSnapshot.child("currentChosenCharacter").getValue(String.class);
                        tvCharacter2.setText(character2);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                gameState = dataSnapshot.child("gameState").getValue(String.class);
                if(gameState.equals("player_1_guessing")){
                    secretWord = dataSnapshot.child("secretWord2").getValue(String.class);
                } else if (gameState.equals("player_2_guessing")){
                    secretWord = dataSnapshot.child("secretWord1").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageStorage = new ArrayList<String>();
        letterStorage = new ArrayList<String>();

        roomReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageStorage.clear();
                if(gameState.equals("player_1_guessing")){
                    lifeLine = dataSnapshot.child("life1").getValue(Integer.class);
                    blankSpace = dataSnapshot.child("blankSpace2").getValue(String.class);

                    for(DataSnapshot child: dataSnapshot.child("messages2").getChildren()){
                        messageStorage.add(child.getValue(String.class));
                    }

                    for(DataSnapshot child: dataSnapshot.child("chosen1").getChildren()) {
                        String tempLetter = child.getValue(String.class);
                        if(!tempLetter.equals("test")){
                            letters.get(tempLetter).setVisibility(View.INVISIBLE);
                            letterStorage.add(tempLetter);
                        }
                    }
                } else if (gameState.equals("player_2_guessing")){
                    lifeLine = dataSnapshot.child("life2").getValue(Integer.class);
                    blankSpace = dataSnapshot.child("blankSpace1").getValue(String.class);

                    for(DataSnapshot child: dataSnapshot.child("messages1").getChildren()){
                        messageStorage.add(child.getValue(String.class));
                    }

                    for(DataSnapshot child: dataSnapshot.child("chosen2").getChildren()) {
                        String tempLetter = child.getValue(String.class);
                        if(!tempLetter.equals("test")){
                            letters.get(tempLetter).setVisibility(View.INVISIBLE);
                            letterStorage.add(tempLetter);
                        }
                    }
                }

                tvSecretWord.setText(blankSpace);

                int dead = 0;
                String life = Integer.toString(lifeLine).trim();

                Log.d(TAG, "character1:: " + character1);

                // ************************* NAGASE *************************
                //"Opponent starts his defending phase with 1 less lifeline"
                if(gameState.equals("player_1_guessing")) {
                    if(character2.equals("Nagase")) {
                        dead = 1;
                        life += " - 1";
                    }
                } else if (gameState.equals("player_2_guessing")) {
                    if(character1.equals("Nagase")) {
                        dead = 1;
                        life += " - 1";
                    }
                }
                // ************************* NAGASE *************************

                tvLife.setText(life);

                if(lifeLine == dead || (noMoreBlanks(blankSpace) && !blankSpace.equals(""))){
                    etGuessLetter.setEnabled(false);
                    etGuessLetter.setHint("");
                    btnCheck.setEnabled(false);

                    //update message
                    //update TextView
                    HashMap<String, Object> temp = new HashMap<String, Object>();

                    if(lifeLine == dead){
                        tvLife.setText("YOU LOSE");
                        String answer = secretWord;
                        answer = answer.replace("", " ");
                        tvSecretWord.setText(answer);
                    } else if ((noMoreBlanks(blankSpace) && !blankSpace.equals(""))){
                        tvLife.setText("YOU WIN");
                    }
                    roomReference.updateChildren(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lifeLine == 0){
                    HashMap<String, Object> temp = new HashMap<String, Object>();
                    if(gameState.equals("player_2_guessing")){
                        //player2 lost --> player1 won
                        temp.put("gameStanding", gameStanding + 1);
                        temp.put("gameState", "player_2_setting_word");
                    } else if (gameState.equals("player_1_guessing")){
                        //player1 lost
                        temp.put("gameStanding", gameStanding - 1);
                        temp.put("gameState", "finished");
                    }
                    roomReference.updateChildren(temp);
                } else if (noMoreBlanks(blankSpace) && !blankSpace.equals("")) {
                    HashMap<String, Object> temp = new HashMap<String, Object>();
                    if (gameState.equals("player_2_guessing")) {
                        //player2 won --> player1 lost
                        temp.put("gameStanding", gameStanding - 1);
                        temp.put("gameState", "player_2_setting_word");
                    } else if (gameState.equals("player_1_guessing")) {
                        //player1 won
                        temp.put("gameStanding", gameStanding + 1);
                        temp.put("gameState", "finished");
                    }
                    roomReference.updateChildren(temp);
                } else {
                    Toast.makeText(getApplicationContext(), "You must finish the game first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCheck.setEnabled(false);
        etGuessLetter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().equals("")){
                    btnCheck.setEnabled(false);
                } else {
                    btnCheck.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chosenLetter = etGuessLetter.getText().toString().trim();
                etGuessLetter.setText("");

                HashMap<String, Object> temp = new HashMap<String, Object>();

                //need to update message for both cases
                String message = "Defending player guessed the letter " + chosenLetter + ".";

                ArrayList<Integer> positions = getPositionsOfChosenLetter(chosenLetter);
                if(positions.size() != 0){
                    //update "blankSpaces" in "rooms" database
                    String newBlankSpace = generateBlankSpaces(chosenLetter, positions);
                    if(gameState.equals("player_1_guessing")){
                        temp.put("blankSpace2", newBlankSpace);
                    } else if (gameState.equals("player_2_guessing")){
                        temp.put("blankSpace1", newBlankSpace);
                    }
                } else {
                    //lifeLine-- && update "life" in "rooms" database
                    lifeLine--;
                    message += " Life remaining: " + lifeLine;
                    if(gameState.equals("player_1_guessing")){
                        temp.put("life1", lifeLine);
                    } else if (gameState.equals("player_2_guessing")){
                        temp.put("life2", lifeLine);
                    }

                }
                messageStorage.add(message);
                letterStorage.add(chosenLetter);
                if(gameState.equals("player_1_guessing")){
                    temp.put("messages2", messageStorage);
                    temp.put("chosen1", letterStorage);
                } else if (gameState.equals("player_2_guessing")){
                    temp.put("messages1", messageStorage);
                    temp.put("chosen2", letterStorage);
                }
                roomReference.updateChildren(temp);
            }
        });
    }

    private String generateBlankSpaces(String chosenLetter, ArrayList<Integer> positions) {
        String temp = blankSpace;
        temp = temp.replace(" ", "").trim();

        String newBlankSpace = "";
        for(int i = 0; i < temp.length(); i++){
            if(positions.indexOf((Integer) i) == -1) {
                newBlankSpace += temp.substring(i, i+1);
            } else {
                newBlankSpace += chosenLetter;
            }
        }
        newBlankSpace = newBlankSpace.replace("", " ").trim();

        return newBlankSpace;
    }

    private ArrayList<Integer> getPositionsOfChosenLetter(String chosenLetter) {
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for(int i = 0; i < secretWord.length(); i++){
            if(chosenLetter.equals(secretWord.substring(i, i+1))) positions.add((Integer) i);
        }
        return positions;
    }

    private void initLettersAndHashMap() {
        letters = new HashMap<String, TextView>();

        tvA = (TextView) findViewById(R.id.tvA2);
        tvB = (TextView) findViewById(R.id.tvB2);
        tvC = (TextView) findViewById(R.id.tvC2);
        tvD = (TextView) findViewById(R.id.tvD2);
        tvE = (TextView) findViewById(R.id.tvE2);
        tvF = (TextView) findViewById(R.id.tvF2);
        tvG = (TextView) findViewById(R.id.tvG2);
        tvH = (TextView) findViewById(R.id.tvH2);
        tvI = (TextView) findViewById(R.id.tvI2);
        tvJ = (TextView) findViewById(R.id.tvJ2);
        tvK = (TextView) findViewById(R.id.tvK2);
        tvL = (TextView) findViewById(R.id.tvL2);
        tvM = (TextView) findViewById(R.id.tvM2);
        tvN = (TextView) findViewById(R.id.tvN2);
        tvO = (TextView) findViewById(R.id.tvO2);
        tvP = (TextView) findViewById(R.id.tvP2);
        tvQ = (TextView) findViewById(R.id.tvQ2);
        tvR = (TextView) findViewById(R.id.tvR2);
        tvS = (TextView) findViewById(R.id.tvS2);
        tvT = (TextView) findViewById(R.id.tvT2);
        tvU = (TextView) findViewById(R.id.tvU2);
        tvV = (TextView) findViewById(R.id.tvV2);
        tvW = (TextView) findViewById(R.id.tvW2);
        tvX = (TextView) findViewById(R.id.tvX2);
        tvY = (TextView) findViewById(R.id.tvY2);
        tvZ = (TextView) findViewById(R.id.tvZ2);

        letters.put("A", tvA);
        letters.put("B", tvB);
        letters.put("C", tvC);
        letters.put("D", tvD);
        letters.put("E", tvE);
        letters.put("F", tvF);
        letters.put("G", tvG);
        letters.put("H", tvH);
        letters.put("I", tvI);
        letters.put("J", tvJ);
        letters.put("K", tvK);
        letters.put("L", tvL);
        letters.put("M", tvM);
        letters.put("N", tvN);
        letters.put("O", tvO);
        letters.put("P", tvP);
        letters.put("Q", tvQ);
        letters.put("R", tvR);
        letters.put("S", tvS);
        letters.put("T", tvT);
        letters.put("U", tvU);
        letters.put("V", tvV);
        letters.put("W", tvW);
        letters.put("X", tvX);
        letters.put("Y", tvY);
        letters.put("Z", tvZ);
    }

    private boolean noMoreBlanks(String word) {
        for(int i = 0; i < word.length(); i++){
            //subString() function should be able to capture " "
            if(word.substring(i,i+1).equals("_")) return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}