package com.example.nc_parking_new;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * this bookings page is used to display unavailable bookings to the user.
 * the information on this page is dynamically populated using the parkingAdapter and ParkingSlot class.
 * users will be able to see bookings that are not currently finished as well as all the meta data relating to each booking
 * the only information users cannot see is the unique user id which is fetched from firebase authentication for security reasons
 * this what done this way to allow users to see which slots are not available to book to ensure they do not try book a slot that already exists
 * they can then use this information and head to the makebookings activity to create their own booking around these slots that
 * are unavailable.
 */
public class BookingActivity extends AppCompatActivity {
    private FirebaseFirestore db; // firebase database
    private ListView listView; // list view to display bookings
    private parkingAdapter adapter; // adapter to display bookings
    private List<ParkingSlot> parkingSlotList; // list of bookings


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance(); // initialise firebase database


        listView = findViewById(R.id.exampleListView); // find the listview from the xml file


        parkingSlotList = new ArrayList<>(); //
        db.collection("Bookings")
                .whereEqualTo("FinishedTime", "Pending") // get all bookings that are not finished
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            ParkingSlot slot = document.toObject(ParkingSlot.class); // convert the document to a ParkingSlot object
                            parkingSlotList.add(slot); // add the ParkingSlot object to the list
                        }

                        adapter = new parkingAdapter(BookingActivity.this, parkingSlotList); // create the adapter
                        listView.setAdapter(adapter); // set the adapter to the listview


                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

}