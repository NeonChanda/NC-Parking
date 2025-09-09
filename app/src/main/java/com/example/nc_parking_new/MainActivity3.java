package com.example.nc_parking_new;



import android.content.Intent;
import android.os.Bundle;

import android.view.View;


import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity3 extends AppCompatActivity {

    // this activty is used to display the user help page and contains two buttons which take users to the manual page and the fines page respectively.
    // a large majority of the coding here has been done on the xml file similar to the landing page
    // therfore the only functionality on this page are the two buttons which both start intents to the corresponding pages.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);

    }

    public void ManualClicked(View view) {
        findViewById(R.id.UserManual); // find the button
        Intent intent = new Intent(MainActivity3.this,ManualActivity.class); // start the intent to move to another activity
        startActivity(intent);
    }

    public void FinesClicked(View view) {
        findViewById(R.id.ParkingFines); // find the button
        Intent intent = new Intent(MainActivity3.this,FinesActivity.class); // start the intent to move to another activity.
        startActivity(intent);

    }
}