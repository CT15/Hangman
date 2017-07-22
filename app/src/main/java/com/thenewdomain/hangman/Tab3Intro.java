package com.thenewdomain.hangman;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Tab3Intro extends Fragment {
    private static final String TAG = "Tab3Intro";

    private Button btnIntro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3intro, container, false);
        btnIntro = (Button) view.findViewById(R.id.btnIntro);

        btnIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"FILLED WITH SCREENSHOTS!",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
