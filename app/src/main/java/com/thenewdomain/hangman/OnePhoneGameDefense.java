package com.thenewdomain.hangman;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class OnePhoneGameDefense extends AppCompatActivity{

    private TextView tvA, tvB, tvC, tvD, tvE, tvF, tvG, tvH, tvI, tvJ, tvK, tvL, tvM, tvN, tvO, tvP,
                        tvQ, tvR, tvS, tvT, tvU, tvV, tvW, tvX, tvY, tvZ;
    private HashMap<String, TextView> letters = new HashMap<String, TextView>();
    private Button btnGiveUp, btnCheck;
    private TextView tvLife;
    private TextView tvSecretWord;
    private EditText etGuessLetter;

    private String secretWord;
    private Intent intent;
    private int life;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_phone_game_defense);

        // --------------- INITIATION -----------------------
        life = 7;
        intent = getIntent();
        secretWord = intent.getStringExtra("SECRET WORD");

        init();
        setButton();

        tvLife.setText(Integer.toString(life));

        String temp = "";
        for(int i = 0; i < secretWord.length(); i++) temp += "_ ";
        tvSecretWord.setText(temp.trim());

        // ------------------ END -----------------------

    }

    private void setButton() {
        btnGiveUp = (Button) findViewById(R.id.btnGiveUp);
        btnCheck = (Button) findViewById(R.id.btnCheck);

        btnGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLife.setText("YOU LOSE");
                btnCheck.setEnabled(false);
                etGuessLetter.setEnabled(false);
                etGuessLetter.setHint("");
                changeGiveUpToBack();
                tvSecretWord.setText(secretWord.replace("", " ").trim());
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etGuessLetter.getText().toString().trim().equals("")) {
                    String chosenLetter = etGuessLetter.getText().toString().trim();

                    letters.get(chosenLetter).setVisibility(View.INVISIBLE);
                    etGuessLetter.setText("");

                    if (checkLetter(chosenLetter, secretWord)) {
                        ArrayList<Integer> positions = getPositions(chosenLetter, secretWord);
                        String guessWord = setStringForGuessWord(chosenLetter, positions, tvSecretWord.getText().toString().trim());
                        tvSecretWord.setText(guessWord.replace("", " ").trim());

                        if(noMoreBlanks(tvSecretWord.getText().toString().trim())){
                            tvLife.setText("YOU WIN");
                            btnCheck.setEnabled(false);
                            etGuessLetter.setEnabled(false);
                            etGuessLetter.setHint("");
                            changeGiveUpToBack();
                        }
                    } else {
                        life -= 1;
                        if(life > 0) tvLife.setText(Integer.toString(life));
                        else {
                            tvLife.setText("YOU LOSE");
                            btnCheck.setEnabled(false);
                            etGuessLetter.setEnabled(false);
                            etGuessLetter.setHint("");
                            changeGiveUpToBack();
                            tvSecretWord.setText(secretWord.replace("", " ").trim());
                        }
                    }
                }
            }
        });

    }

    private boolean noMoreBlanks(String word){
        for(int i = 0; i < word.length(); i++){
            if(word.substring(i,i+1).equals("_")) return false;
        }
        return true;
    }

    private String setStringForGuessWord(String chosenLetter, ArrayList<Integer> positions, String secretWord){
        secretWord = secretWord.replace(" ", "").trim();
        String temp = "";

        for(int i = 0; i < secretWord.length(); i++){
            if(positions.contains((Integer) i)) temp += chosenLetter;
            else if(!secretWord.substring(i, i+1).equals("_")) temp += secretWord.substring(i, i+1);
            else temp += "_";
        }

        return temp;
    }

    private ArrayList<Integer> getPositions(String chosenLetter, String secretWord){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        char charChosen = chosenLetter.charAt(0);
        char[] checkWord = secretWord.toCharArray();

        for(int i = 0; i < checkWord.length; i++){
            if(checkWord[i] == charChosen) temp.add((Integer)i);
        }
        return temp;
    }

    private void changeGiveUpToBack(){
        btnGiveUp.setText("Back");
        btnGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toOnePhoneGameAttack = new Intent(OnePhoneGameDefense.this, OnePhoneGameAttack.class);
                startActivity(toOnePhoneGameAttack);
            }
        });
    }
    private boolean checkLetter (String chosenLetter, String secretWord){
        for(int i = 0; i < secretWord.length(); i++){
            if(chosenLetter.equals(secretWord.substring(i,i+1))) return true;
        }
        return false;
    }


    private void init() {
        tvA = (TextView) findViewById(R.id.tvA);
        tvB = (TextView) findViewById(R.id.tvB);
        tvC = (TextView) findViewById(R.id.tvC);
        tvD = (TextView) findViewById(R.id.tvD);
        tvE = (TextView) findViewById(R.id.tvE);
        tvF = (TextView) findViewById(R.id.tvF);
        tvG = (TextView) findViewById(R.id.tvG);
        tvH = (TextView) findViewById(R.id.tvH);
        tvI = (TextView) findViewById(R.id.tvI);
        tvJ = (TextView) findViewById(R.id.tvJ);
        tvK = (TextView) findViewById(R.id.tvK);
        tvL = (TextView) findViewById(R.id.tvL);
        tvM = (TextView) findViewById(R.id.tvM);
        tvN = (TextView) findViewById(R.id.tvN);
        tvO = (TextView) findViewById(R.id.tvO);
        tvP = (TextView) findViewById(R.id.tvP);
        tvQ = (TextView) findViewById(R.id.tvQ);
        tvR = (TextView) findViewById(R.id.tvR);
        tvS = (TextView) findViewById(R.id.tvS);
        tvT = (TextView) findViewById(R.id.tvT);
        tvU = (TextView) findViewById(R.id.tvU);
        tvV = (TextView) findViewById(R.id.tvV);
        tvW = (TextView) findViewById(R.id.tvW);
        tvX = (TextView) findViewById(R.id.tvX);
        tvY = (TextView) findViewById(R.id.tvY);
        tvZ = (TextView) findViewById(R.id.tvZ);

        letters.put("A",tvA);
        letters.put("B",tvB);
        letters.put("C",tvC);
        letters.put("D",tvD);
        letters.put("E",tvE);
        letters.put("F",tvF);
        letters.put("G",tvG);
        letters.put("H",tvH);
        letters.put("I",tvI);
        letters.put("J",tvJ);
        letters.put("K",tvK);
        letters.put("L",tvL);
        letters.put("M",tvM);
        letters.put("N",tvN);
        letters.put("O",tvO);
        letters.put("P",tvP);
        letters.put("Q",tvQ);
        letters.put("R",tvR);
        letters.put("S",tvS);
        letters.put("T",tvT);
        letters.put("U",tvU);
        letters.put("V",tvV);
        letters.put("W",tvW);
        letters.put("X",tvX);
        letters.put("Y",tvY);
        letters.put("Z",tvZ);

        tvLife = (TextView) findViewById(R.id.tvLife);
        tvSecretWord = (TextView) findViewById(R.id.tvSecretWord);
        etGuessLetter = (EditText) findViewById(R.id.etGuessLetter);
        etGuessLetter.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(1)});
}

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
