package com.thenewdomain.hangman;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/*need to create firebase message together when the room is created ...
and give condition perhaps when putting into the arraylist that if "" then no need to put in
 */

public class TwoPhoneGameAttack extends AppCompatActivity {

    final String TAG = "TwoPhoneGameAttack";

    //xml
    TextView tvCharacter1, tvCharacter2, tvPlayer1, tvPlayer2;
    ListView lv;
    ArrayList<String> messageStorage;
    ArrayAdapter<String> adapter;

    //intent
    Intent intent;
    String roomKey;

    //firebase
    DatabaseReference roomReference;
    DatabaseReference player1Reference;
    DatabaseReference player2Reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_phone_game_attack);

        tvCharacter1 = (TextView) findViewById(R.id.tvCharacter1Attack);
        tvCharacter2 = (TextView) findViewById(R.id.tvCharacter2Attack);
        tvPlayer1 = (TextView) findViewById(R.id.tvPlayer1Attack);
        tvPlayer2 = (TextView) findViewById(R.id.tvPlayer2Attack);

        lv = (ListView) findViewById(R.id.lvAttack);
        messageStorage = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, messageStorage);
        lv.setAdapter(adapter);

        intent = getIntent();
        roomKey = intent.getStringExtra("KEY");

        roomReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomKey);
        roomReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String player1ID = dataSnapshot.child("player1ID").getValue(String.class);
                String player2ID = dataSnapshot.child("player2ID").getValue(String.class);

                player1Reference = FirebaseDatabase.getInstance().getReference().child("users").child(player1ID);
                player1Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tvPlayer1.setText(dataSnapshot.child("userName").getValue(String.class));
                        tvCharacter1.setText(dataSnapshot.child("currentChosenCharacter").getValue(String.class));
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
                        tvCharacter2.setText(dataSnapshot.child("currentChosenCharacter").getValue(String.class));
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

        roomReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String gameState = dataSnapshot.child("gameState").getValue(String.class);
                messageStorage.clear();
                if(gameState.equals("player_1_guessing")){
                    for (DataSnapshot child : dataSnapshot.child("messages2").getChildren()) {
                        messageStorage.add(child.getValue(String.class));
                    }
                } else if (gameState.equals("player_2_guessing")) {
                    for (DataSnapshot child : dataSnapshot.child("messages1").getChildren()) {
                        messageStorage.add(child.getValue(String.class));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
