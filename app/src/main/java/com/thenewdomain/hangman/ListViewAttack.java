package com.thenewdomain.hangman;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewAttack extends Fragment {

//    final String TAG = "ListViewAttack";
//    private ArrayList<String> messageStorage;
//    private ArrayAdapter<String> adapter;
//    private String previousMessage;
//    private ListView lvBattleInfo;
//
//    //firebase storing message
//    DatabaseReference messageDatabase;
//    String messageDatabaseKey;
//
//    public ListViewAttack() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_list_view_attack, container, false);
//
//        messageStorage = new ArrayList<String>();
//
//        previousMessage = "";
//
//        adapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_expandable_list_item_1,
//                messageStorage);
//
//        lvBattleInfo = (ListView) view.findViewById(R.id.lvBattleInfo);
//        lvBattleInfo.setAdapter(adapter);
//
//        messageDatabase = FirebaseDatabase.getInstance().getReference().child("messages");
//        messageDatabaseKey = messageDatabase.push().getKey();
//        messageDatabase.child(messageDatabaseKey).setValue("Battle Start!");
//
//        messageDatabase.child(messageDatabaseKey).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot child: dataSnapshot.getChildren()){
//                    messageStorage.add(child.getValue(String.class));
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        return view;
//    }
//
//    public void updateListView(String message){
//        if(!previousMessage.equals(message)){
//            previousMessage = message;
//
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            for(int i = 0; i < messageStorage.size(); i++){
//                map.put("message" + i, messageStorage.get(i));
//            }
//
//            messageDatabase.updateChildren(map);
//        }
//    }
}
