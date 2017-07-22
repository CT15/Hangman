package com.thenewdomain.hangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainMenu extends AppCompatActivity implements Tab0User.SendInfo{

    private static final String TAG = "Main Activity";

    //Firebase database stuff
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;


    //private SectionsPageAdapter mySectionsPageAdapter;
    //private ViewPager myViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Log.d(TAG, "onCreate: Starting");

        SectionsPageAdapter mySectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        ViewPager myViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(myViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(myViewPager);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(user != null){
                    //user is signed in
                    //don't really wanna use it
                } else {
                    //user is signed out
                    //don't really wanna user it
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onBackPressed(){

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab0User(), "User");
        adapter.addFragment(new Tab1Battle(), "Battle");
        adapter.addFragment(new Tab2Character(), "Brawler");
        adapter.addFragment(new Tab3Intro(), "Guide");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void sendData(String userName, String character) {
        //to send info about userName and character from Tab0User to Tab1Battle
        Log.d(TAG, "userName " + userName);
        String tag = "android:switcher:" + R.id.container + ":" + 1;
        Tab1Battle tab1 = (Tab1Battle) getSupportFragmentManager().findFragmentByTag(tag);
        tab1.getData(userName, character);
    }
}
