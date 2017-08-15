package com.thenewdomain.hangman;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class WaitingRoom extends AppCompatActivity {

    private SensorManager sm;

    private float acelVal; //CURRENT ACCELERATION VALUE AND GRAVITY
    private float acelLast; //LAST ACCELERATION VALUE AND GRAVITY
    private float shake; //ACCELERATION VALUE differ FROM GRAVITY

    ImageView ivExecutioner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        ivExecutioner = (ImageView) findViewById(R.id.ivExecutioner);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) x*x + y*y + z*z);

            float delta = acelVal - acelLast;
            shake = shake*0.9f + delta;

            if(shake > 10) {
                ivExecutioner.setImageResource(android.R.color.transparent);
                ivExecutioner.setImageResource(R.drawable.executioner_dizzy);
                AnimationDrawable animation = (AnimationDrawable) ivExecutioner.getDrawable();
                animation.start();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
