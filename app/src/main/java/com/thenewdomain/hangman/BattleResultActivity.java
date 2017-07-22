package com.thenewdomain.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class BattleResultActivity extends AppCompatActivity {

    //points
    private final int DRAW = 30;
    private final int WIN = 50;
    private final int LOSE = 10;

    //data from intent
    private Intent intent;
    private String roomKey;

    private int point;

    //firebase
    private DatabaseReference roomReference;
    private DatabaseReference userReference;
    ValueEventListener roomListener;

    private String userID;
    private String player1ID;
    private String player2ID;

    //xml
    private TextView tvUserName, tvCharacter, tvBattleStatus, tvPoint;
    private Button btnBackToMainMenuAfterBattle;
    private ImageView ivCharacterImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_result);

        tvUserName = (TextView) findViewById(R.id.tvUserNameFinal);
        tvCharacter = (TextView) findViewById(R.id.tvCharacterFinal);
        tvBattleStatus = (TextView) findViewById(R.id.tvBattleStatus);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        btnBackToMainMenuAfterBattle = (Button) findViewById(R.id.btnBackToMainMenuAfterBattle);
        ivCharacterImg = (ImageView) findViewById(R.id.ivCharacterImg2);

        intent = getIntent();
        roomKey = intent.getStringExtra("KEY");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        roomReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomKey);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvUserName.setText(dataSnapshot.child("userName").getValue(String.class));
                String currentChosenCharacter = dataSnapshot.child("currentChosenCharacter").getValue(String.class);
                tvCharacter.setText(currentChosenCharacter);

                switch (currentChosenCharacter){
                    case "Billy":
                        ivCharacterImg.setImageResource(R.drawable.character_billy);
                        break;
                    case "Nagase":
                        ivCharacterImg.setImageResource(R.drawable.character_nagase);
                        break;
                    case "Leo Kim":
                        ivCharacterImg.setImageResource(R.drawable.character_leokim);
                        break;
                    case "Dr. James":
                        ivCharacterImg.setImageResource(R.drawable.character_drjames);
                        break;
                    case "Yasuo":
                        ivCharacterImg.setImageResource(R.drawable.character_yasuo);
                        break;
                    case "Carter":
                        ivCharacterImg.setImageResource(R.drawable.character_carter);
                        break;
                }

                point = dataSnapshot.child("rankPoints").getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        roomListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int gameStanding = dataSnapshot.child("gameStanding").getValue(Integer.class);
                player1ID = dataSnapshot.child("player1ID").getValue(String.class);
                player2ID = dataSnapshot.child("player2ID").getValue(String.class);

                //if gameStanding is +1 --> player1 wins
                String tempText = "+";
                if(gameStanding == 0){
                    tvBattleStatus.setText("DRAW");
                    tempText += DRAW;
                    point += DRAW;
                } else if (gameStanding == -2){
                    if(userID.equals(player1ID)){
                        tvBattleStatus.setText("DEFEAT");
                        tempText += LOSE;
                        point += LOSE;
                    } else if (userID.equals(player2ID)){
                        tvBattleStatus.setText("VICTORY");
                        tempText += WIN;
                        point += WIN;
                    }
                } else if (gameStanding == 2){
                    if(userID.equals(player1ID)){
                        tvBattleStatus.setText("VICTORY");
                        tempText += WIN;
                        point += WIN;
                    } else if (userID.equals(player2ID)){
                        tvBattleStatus.setText("DEFEAT");
                        tempText += LOSE;
                        point += LOSE;
                    }
                }
                tvPoint.setText(tempText);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        roomReference.addListenerForSingleValueEvent(roomListener);

        btnBackToMainMenuAfterBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update point
                HashMap<String, Object> temp = new HashMap<String, Object>();
                temp.put("rankPoints", point);
                userReference.updateChildren(temp);
                temp.clear();
                if(userID.equals(player1ID)){
                    temp.put("player1IsOut", true);
                } else if (userID.equals(player2ID)){
                    temp.put("player2IsOut", true);
                }
                roomReference.removeEventListener(roomListener);
                roomReference.updateChildren(temp);

                Intent toMainMenu = new Intent(BattleResultActivity.this, MainMenu.class);
                startActivity(toMainMenu);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
