package com.thenewdomain.hangman;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class IntermediateActivity extends AppCompatActivity {

    final String TAG = "IntermediateActivity";

    Intent intent;
    String roomKey;

    String userID;

    DatabaseReference roomReference;

    String gameState;
    String player1ID;
    String player2ID;

    Boolean player1IsOut;
    Boolean player2IsOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate);

        intent = getIntent();
        roomKey = intent.getStringExtra("KEY");

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        roomReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomKey);

        roomReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameState = dataSnapshot.child("gameState").getValue(String.class);
                Log.d(TAG, "gameState: " + gameState);
                player1ID = dataSnapshot.child("player1ID").getValue(String.class);
                player2ID = dataSnapshot.child("player2ID").getValue(String.class);
                player1IsOut = dataSnapshot.child("player1IsOut").getValue(Boolean.class);
                player2IsOut = dataSnapshot.child("player2IsOut").getValue(Boolean.class);

                if(!player1IsOut || !player2IsOut) {
                    if (gameState != null) {
                        if (!gameState.equals("finished") && !gameState.equals("end_game")) {
                            if (userID.equals(player1ID)) {
                                if (gameState.equals("player_1_setting_word")) {
                                    Intent toDecideSecretWord = new Intent(IntermediateActivity.this, DecideSecretWord.class);
                                    toDecideSecretWord.putExtra("KEY", roomKey);
                                    startActivity(toDecideSecretWord);
                                } else if (gameState.equals("player_2_guessing")) {
                                    Intent toTwoPhoneGameAttack = new Intent(IntermediateActivity.this, TwoPhoneGameAttack.class);
                                    toTwoPhoneGameAttack.putExtra("KEY", roomKey);
                                    startActivity(toTwoPhoneGameAttack);
                                } else if (gameState.equals("player_2_setting_word")) {
                                    Intent toWaitingRoom = new Intent(IntermediateActivity.this, WaitingRoom.class);
                                    toWaitingRoom.putExtra("KEY", roomKey);
                                    startActivity(toWaitingRoom);
                                } else if (gameState.equals("player_1_guessing")) {
                                    Intent toTwoPhoneGameDefense = new Intent(IntermediateActivity.this, TwoPhoneGameDefense.class);
                                    toTwoPhoneGameDefense.putExtra("KEY", roomKey);
                                    startActivity(toTwoPhoneGameDefense);
                                }
                            } else if (userID.equals(player2ID)) {
                                if (gameState.equals("player_1_setting_word")) {
                                    Intent toWaitingRoom = new Intent(IntermediateActivity.this, WaitingRoom.class);
                                    toWaitingRoom.putExtra("KEY", roomKey);
                                    startActivity(toWaitingRoom);
                                } else if (gameState.equals("player_2_guessing")) {
                                    Intent toTwoPhoneGameDefense = new Intent(IntermediateActivity.this, TwoPhoneGameDefense.class);
                                    toTwoPhoneGameDefense.putExtra("KEY", roomKey);
                                    startActivity(toTwoPhoneGameDefense);
                                } else if (gameState.equals("player_2_setting_word")) {
                                    Intent toDecideSecretWord = new Intent(IntermediateActivity.this, DecideSecretWord.class);
                                    toDecideSecretWord.putExtra("KEY", roomKey);
                                    startActivity(toDecideSecretWord);
                                } else if (gameState.equals("player_1_guessing")) {
                                    Intent toTwoPhoneGameAttack = new Intent(IntermediateActivity.this, TwoPhoneGameAttack.class);
                                    toTwoPhoneGameAttack.putExtra("KEY", roomKey);
                                    startActivity(toTwoPhoneGameAttack);
                                }
                            }
                        } else if (gameState.equals("finished")) {
                            Intent toBattleResultActivity = new Intent(IntermediateActivity.this, BattleResultActivity.class);
                            toBattleResultActivity.putExtra("KEY", roomKey);
                            startActivity(toBattleResultActivity);
                        }
                    }
                } else {
                    HashMap<String, Object> temp = new HashMap<String, Object>();
                    temp.put("removeRoom", true);
                    roomReference.updateChildren(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
