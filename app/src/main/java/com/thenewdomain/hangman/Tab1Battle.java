package com.thenewdomain.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Tab1Battle extends Fragment {
    private static final String TAG = "Tab1Battle";

    private Button btnBattle1;
    private Button btnBattle2;

    //user info required for battle
    private String userName;
    private String character;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1battle, container, false);
        btnBattle1 = (Button) view.findViewById(R.id.btnBattle1);
        btnBattle2 = (Button) view.findViewById(R.id.btnBattle2);

        btnBattle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toOnePhoneGame = new Intent(getActivity(), OnePhoneGameAttack.class);
                getActivity().startActivity(toOnePhoneGame);
            }
        });

        btnBattle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRoomingActivity = new Intent(getActivity(), RoomingActivity.class);
                toRoomingActivity.putExtra("USERNAME", userName);
                toRoomingActivity.putExtra("CHARACTER", character);
                getActivity().startActivity(toRoomingActivity);
            }
        });

        return view;
    }

    public void getData(String userName, String character){
        this.userName = userName;
        this.character = character;
    }
}
