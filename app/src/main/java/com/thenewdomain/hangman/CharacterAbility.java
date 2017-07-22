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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CharacterAbility extends AppCompatActivity implements View.OnClickListener{

    ImageView ivCharacterImg;
    TextView tvCharacterName;
    TextView tvCharacterAbility;
    Button btnConfirmCharacter;
    Button btnBackToCharacterList;

    Intent intent;
    String characterName;

    String userID;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_ability);

        ivCharacterImg = (ImageView) findViewById(R.id.ivCharacterImg);
        tvCharacterAbility = (TextView) findViewById(R.id.tvCharacterAbility);
        tvCharacterName = (TextView) findViewById(R.id.tvCharacterName1);
        btnConfirmCharacter = (Button) findViewById(R.id.btnConfirmCharacter);
        btnBackToCharacterList = (Button) findViewById(R.id.btnBackToCharacterList);

        intent = getIntent();
        characterName = intent.getStringExtra("NAME");

        tvCharacterName.setText(characterName);

        String ability = "";
        switch(characterName){
            case "Billy":
                ability = "DEFENCE\nRandomly eliminates 3 letters not found in the secret word " +
                        "during player's defending phase";
                ivCharacterImg.setImageResource(R.drawable.character_billy);
                break;
            case "Nagase":
                ability = "ATTACK\nOpponent starts his defending phase with 1 less lifeline";
                ivCharacterImg.setImageResource(R.drawable.character_nagase);
                break;
            case "Leo Kim":
                ability = "DEFENCE\nReveal the first non-repeating vowel in the secret word " +
                        "during player's defending phase, if any";
                ivCharacterImg.setImageResource(R.drawable.character_leokim);
                break;
            case "Dr. James":
                ability = "ATTACK\nOpponent starts his defending phase with 1 less lifeline for " +
                        "every 2 different vowels in the secret word";
                ivCharacterImg.setImageResource(R.drawable.character_drjames);
                break;
            case "Yasuo":
                ability = "DEFENCE\nPlayer starts his defending phase with 2 more lifelines";
                ivCharacterImg.setImageResource(R.drawable.character_yasuo);
                break;
            case "Carter":
                ability = "ATTACK\nFreezes opponent's letter board during his defending phase";
                ivCharacterImg.setImageResource(R.drawable.character_carter);
                break;
        }
        tvCharacterAbility.setText(ability);

        btnBackToCharacterList.setOnClickListener(this);
        btnConfirmCharacter.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnBackToCharacterList:
                startActivity(new Intent(CharacterAbility.this, CharacterList.class));
                break;
            case R.id.btnConfirmCharacter:
                userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

                HashMap<String, Object> temp = new HashMap<String, Object>();
                temp.put("currentChosenCharacter", characterName);
                userReference.updateChildren(temp);

                Toast.makeText(this, "Brawler chosen", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
