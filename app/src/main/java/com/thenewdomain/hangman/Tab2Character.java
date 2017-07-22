package com.thenewdomain.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Tab2Character extends Fragment {
    private static final String TAG = "Tab1Character";

    private Button btnCharacter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2character, container, false);
        btnCharacter = (Button) view.findViewById(R.id.btnCharacter);

        btnCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCharacterList = new Intent(getActivity(), CharacterList.class);
                getActivity().startActivity(toCharacterList);
            }
        });
        return view;
    }
}
