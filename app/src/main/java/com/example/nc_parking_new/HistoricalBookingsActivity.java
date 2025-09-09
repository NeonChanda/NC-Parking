package com.example.nc_parking_new;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class HistoricalBookingsActivity extends AppCompatActivity {
    /**
     * This is the historical bookings page, on this page we can view all the bookings that have been made by the user.
     * this was achieved by using the same ParkingSlot class and adapter used to display unavailable bookings to the user
     * however this time we filtered the query using the .whereEqualTo method to only fetch bookings that belong to the current user
     * using their Uid from Firebase Authentication.
     * a new listview was used to display the bookings as the same listview cannot be used twice.
     */




    // Declare UI elements
    private ListView listView;
    private FirebaseFirestore db; // Firebase Firestore instance
    private parkingAdapter adapter; // Custom adapter for displaying bookings
    private List<ParkingSlot> parkingSlotList; // List used to store bookings
    private FirebaseAuth mAuth; // Firebase Authentication instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historical_bookings);

        // Initialise Firebase instances
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        // Find the ListView UI element
        listView = findViewById(R.id.historyListView);
        parkingSlotList = new ArrayList<>();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUser.getUid();       // Get unique User ID from Firebase
        db.collection("Bookings")
                .whereEqualTo("UserID", currentUser.getUid())      // Fetch bookings that belong to the current user
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Loop through each booking document
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert Firestore document into ParkingSlot object
                            ParkingSlot slot = document.toObject(ParkingSlot.class);
                            parkingSlotList.add(slot); // Add to the list
                        }
                        // Set the adapter with the retrieved bookings
                        adapter = new parkingAdapter(HistoricalBookingsActivity.this, parkingSlotList);
                        listView.setAdapter(adapter);

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }  // if this process fails we log the error to the console.
                });


    }
}