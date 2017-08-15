package com.thenewdomain.hangman;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by CalvinTantio on 7/6/2017.
 */

public class Tab0User extends Fragment {
    private static final String TAG = "Tab0User";

    private TextView tvCharacterName;
    private TextView tvUserName;
    private TextView tvRankPoints;
    private Button btnSignOut;
    private ImageView ivCharacterImage;

    //firebase
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;
    private UserInformation userInfo;
    private String userID;

    //for data transfer among tabs
    SendInfo sendInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab0user, container, false);

        tvCharacterName = (TextView) view.findViewById(R.id.tvCharacterName);
        tvRankPoints = (TextView) view.findViewById(R.id.tvRankPoints);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        btnSignOut = (Button) view.findViewById(R.id.btnSignOut);
        ivCharacterImage = (ImageView) view.findViewById(R.id.ivCharacterImage);

        firebaseAuth = FirebaseAuth.getInstance();

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent toSignInRegister = new Intent(getActivity(), SignInRegister.class);
                startActivity(toSignInRegister);
            }
        });

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        databaseReference = firebaseDatabase.getReference().child("users").child(userID);
        userInfo = new UserInformation();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading User Information ...");
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get data from database
                String userName = dataSnapshot.child("userName").getValue(String.class);
                String currentChosenCharacter = dataSnapshot.child("currentChosenCharacter").getValue(String.class);
                int rankPoints = dataSnapshot.child("rankPoints").getValue(Integer.class);
                int noOfCharactersUnlocked = dataSnapshot.child("noOfCharactersUnlocked").getValue(Integer.class);

                //set user info
                tvCharacterName.setText(currentChosenCharacter);
                tvUserName.setText(userName);
                tvRankPoints.setText(Integer.toString(rankPoints));

                switch (currentChosenCharacter){
                    case "Billy":
                        ivCharacterImage.setImageResource(R.drawable.character_billy);
                        progressDialog.dismiss();
                        break;
                    case "Nagase":
                        ivCharacterImage.setImageResource(R.drawable.character_nagase);
                        progressDialog.dismiss();
                        break;
                    case "Leo Kim":
                        ivCharacterImage.setImageResource(R.drawable.character_leokim);
                        progressDialog.dismiss();
                        break;
                    case "Dr. James":
                        ivCharacterImage.setImageResource(R.drawable.character_drjames);
                        progressDialog.dismiss();
                        break;
                    case "Yasuo":
                        ivCharacterImage.setImageResource(R.drawable.character_yasuo);
                        progressDialog.dismiss();
                        break;
                    case "Carter":
                        ivCharacterImage.setImageResource(R.drawable.character_carter);
                        progressDialog.dismiss();
                        break;
                }

                //send data from Tab0User to MainActivity
                sendInfo.sendData(userName, currentChosenCharacter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("DataFromMainMenu", Context.MODE_PRIVATE);
//        tvUserName.setText(sharedPreferences.getString("USERNAME", ""));
//        tvRankPoints.setText(Integer.toString(sharedPreferences.getInt("RANKPOINTS",0)));
//        tvCharacterName.setText(sharedPreferences.getString("CURRENTCHOSENCHARACTER",""));

        return view;
    }

    interface SendInfo {
        public void sendData(String userName, String character);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            sendInfo = (SendInfo) context;
        } catch(ClassCastException e){
            throw new ClassCastException("You need to implement sendData method");
        }
    }
}
