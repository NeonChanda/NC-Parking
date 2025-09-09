package com.example.nc_parking_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class LandingPageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);

    }


    // all of the functions below handle the clicks of the cards on the landing page, when each card is clicked
    // it will take the user to the corresponding activity using an intent.

    public void MapsClicked(View view) {
        findViewById(R.id.mapscard);
        Intent intent = new Intent(LandingPageActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void CreateBookingClicked(View view){
        findViewById(R.id.makeBookingCard);
        Intent intent = new Intent(LandingPageActivity.this, MakeBookingActivity.class);
        startActivity(intent);
    }

    public void CurrentBookingsClicked(View view){
        findViewById(R.id.availableSlotscard);
        Intent intent = new Intent(LandingPageActivity.this, BookingActivity.class);
        startActivity(intent);
    }

    public void idUploadClicked(View view) {
        findViewById(R.id.IDCard);
        Intent intent= new Intent(LandingPageActivity.this, CameraActivity.class);
        startActivity(intent);
    }

    public void helpClicked(View view){
        findViewById(R.id.helpcard);
        Intent intent = new Intent(LandingPageActivity.this, MainActivity3.class);
        startActivity(intent);
    }

    public void SettingsClicked(View view) {
        findViewById(R.id.fabSettings);
        Intent intent = new Intent(LandingPageActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}