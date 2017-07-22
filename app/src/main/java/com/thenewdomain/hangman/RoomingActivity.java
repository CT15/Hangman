package com.thenewdomain.hangman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RoomingActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "RoomingActivity";

    //from xml file
    private EditText etRoomName;
    private Button btnCreateRoom, btnJoinRoom, btnBack;

    private String userID;

    //Firebase rooming
    DatabaseReference roomReference;
    DatabaseReference currentRoomRef;
    HashMap<Room, String> allRooms;
    ValueEventListener roomListener;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooming);

        init();
    }

    private void init() {
        //from xml file
        etRoomName = (EditText) findViewById(R.id.etRoomName);
        btnCreateRoom = (Button) findViewById(R.id.btnCreateRoom);
        btnJoinRoom = (Button) findViewById(R.id.btnJoinRoom);
        btnBack = (Button) findViewById(R.id.btnBackToMainMenuFromRooming);

        //initial state of the button
        btnJoinRoom.setEnabled(false);
        btnCreateRoom.setEnabled(false);

        etRoomName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().equals("")){
                    btnJoinRoom.setEnabled(false);
                    btnCreateRoom.setEnabled(false);
                } else {
                    btnJoinRoom.setEnabled(true);
                    btnCreateRoom.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //initialise buttons
        btnCreateRoom.setOnClickListener(this);
        btnJoinRoom.setOnClickListener(this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(currentRoomRef != null) currentRoomRef.removeValue();
                Intent toMainMenu = new Intent(RoomingActivity.this, MainMenu.class);
                startActivity(toMainMenu);
            }
        });

        //initialise database reference
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        roomReference = FirebaseDatabase.getInstance().getReference().child("rooms");

        allRooms = new HashMap<Room, String>(); //for rooms iteration

        roomListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    room = child.getValue(Room.class);
                    allRooms.put(room, child.getKey());

                    if(currentRoomRef != null) {
                        if (currentRoomRef.getKey().equals(child.getKey())) {
                            String player1ID = child.child("player1ID").getValue(String.class);
                            String player2ID = child.child("player2ID").getValue(String.class);

                            if (!player1ID.equals("") && !player2ID.equals("")) {
                                Intent toIntermediateActivity = new Intent(RoomingActivity.this, IntermediateActivity.class);
                                toIntermediateActivity.putExtra("KEY", child.getKey());
                                startActivity(toIntermediateActivity);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        roomReference.addValueEventListener(roomListener);
    }

    @Override
    public void onClick(View v) {
        String roomName = etRoomName.getText().toString().trim();
        Room room = roomExists(roomName);

        switch(v.getId()){
            case R.id.btnCreateRoom:
                if(room == null) {
                    //creating new room in the firebase "rooms" database
                    Room newRoom = new Room(roomName);
                    String key = roomReference.push().getKey();
                    currentRoomRef = roomReference.child(key);
                    currentRoomRef.setValue(newRoom);

                    HashMap<String, Object> temp = new HashMap<String, Object>();

                    //update player1ID in the "rooms" database
                    temp.put("player1ID", userID);

                    //front end
                    Toast.makeText(getApplicationContext(),
                            "Room is successfully created. Waiting for another player to join ...",
                            Toast.LENGTH_SHORT).show();

                    etRoomName.setEnabled(false);
                    btnCreateRoom.setEnabled(false);
                    btnJoinRoom.setEnabled(false);

                    //update Firebase database
                    currentRoomRef.updateChildren(temp);
                } else {
                    Toast.makeText(getApplicationContext(),"Room already exists", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnJoinRoom:
                if(room != null && !roomIsFull(room)) {
                    currentRoomRef = roomReference.child(allRooms.get(room));

                    //update player2ID in the "rooms" database
                    HashMap<String, Object> temp = new HashMap<String, Object>();
                    temp.put("player2ID", userID);
                    currentRoomRef.updateChildren(temp);
                } else {
                    Toast.makeText(getApplicationContext(),"Room does not exist", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean roomIsFull(Room room) {
        String player2ID = room.getPlayer2ID();

        if(player2ID.equals("")) return false;

        return true;
    }

    private Room roomExists(String roomName) {
        for(Room room: allRooms.keySet()){
            if(roomName.equals(room.getRoomName())) return room;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
