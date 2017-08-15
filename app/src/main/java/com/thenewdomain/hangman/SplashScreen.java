package com.thenewdomain.hangman;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    final private int SPLASH_TIME_OUT = 4000;
    private ImageView ivSplashImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ivSplashImg = (ImageView) findViewById(R.id.ivSplashImg);

        ((AnimationDrawable)ivSplashImg.getDrawable()).start();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                startActivity(new Intent(SplashScreen.this, SignInRegister.class));
            }
        }, SPLASH_TIME_OUT);
    }
}
