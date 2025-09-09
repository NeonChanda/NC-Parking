package com.example.nc_parking_new;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MakeBookingActivity extends AppCompatActivity {
    /**
     * This is the activity that is used to make a booking.
     * there are various functionalities seen on this page. Firstly a user can create a new booking
     * finish their current booking and view their historical bookings.
     * As seen on the historical bookings page, we are once again using .whereEqualTo but in this case it is being used to check if a booking is already present
     * this ensures no duplicate bookings have been made.
     * java util locale class was used to fetch the current time when the user finishes their booking to ensure that the fine policy is followed.
     * when the button is clicked the finishtime field in the database is updated to the current time instead of pending and the status is updated to completed.
     * this way users can see their historical bookings and see the unavailable bookings on the bookingactivity by filtering the database.
     */
    private EditText editSlotNumber, editFirstName, editStartTime, editEndTime, editBookingDate; // UI elements
    private Button btnBook; // Button to book a slot
    private FirebaseFirestore db; // Firebase Firestore instance
    private FirebaseAuth mAuth; // Firebase Authentication instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_make_booking);

        editSlotNumber = findViewById(R.id.editSlotNumber); // Find UI elements
        editFirstName = findViewById(R.id.editFirstName);
        editStartTime = findViewById(R.id.editStartTime);
        editEndTime = findViewById(R.id.editEndTime);
        editBookingDate = findViewById(R.id.editBookingDate);
        btnBook = findViewById(R.id.btnBook);
        db = FirebaseFirestore.getInstance(); // Initialise Firebase instances
        mAuth = FirebaseAuth.getInstance(); // Initialise Firebase instances

    }

    public void historyClicked(View view){
        Intent intent = new Intent(MakeBookingActivity.this, HistoricalBookingsActivity.class);
        startActivity(intent); // Start the HistoricalBookingsActivity once the view historical bookings button is clicked
    }

    public void finishClicked(View view){
        String currentTime = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(new java.util.Date()); // Get current time using java SimpleDateFormat
        String FirstName = editFirstName.getText().toString().trim(); // Get the first name from the EditText
        new AlertDialog.Builder(this) // Show a confirmation dialog
                .setTitle("Finish Booking") // Set the title
                .setMessage("Are you sure you want to finish this booking?") // Set the message
                .setPositiveButton("Finish", (dialog, which) -> { // Set the positive button
                    db.collection("Bookings") // Query the Firestore collection
                            .whereEqualTo("FirstName", FirstName) // Filter by first name
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String documentId = document.getId(); // Get the document ID

                                        db.collection("Bookings").document(documentId)
                                                .update("FinishedTime", currentTime, "Status", "Completed") // Update the status and finished time
                                                .addOnSuccessListener(aVoid -> Toast.makeText(MakeBookingActivity.this, "Booking Finished!", Toast.LENGTH_SHORT).show()) // Show a success message
                                                .addOnFailureListener(e -> Toast.makeText(MakeBookingActivity.this, "Failed to finish booking!", Toast.LENGTH_SHORT).show()); // Show a failure message
                                    }
                                } else {
                                    Toast.makeText(MakeBookingActivity.this, "No active booking found for this N-Number.", Toast.LENGTH_SHORT).show(); // Show a message if no booking is found
                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(MakeBookingActivity.this, "Error searching for booking.", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null) // Set the negative button
                .show(); // Show the dialog
}

    public void bookingClicked(View view) {
        // .trim is used to remove any leading or trailing spaces from the input to ensure no duplicates are made.
        // regex was used in this case as we were dealing with many inputs from the edit texts fields resulting in a less repetitive code through the use of if and else statements.

        String SlotNumber = editSlotNumber.getText().toString().trim(); // Get the slot number from the EditText
        String FirstName = editFirstName.getText().toString().trim(); // Get the first name from the EditText
        String BookingStartTime = editStartTime.getText().toString().trim(); // Get the start time from the EditText
        String BookingEndTime = editEndTime.getText().toString().trim(); // Get the end time from the EditText
        String BookingDate = editBookingDate.getText().toString().trim(); // Get the booking date from the EditText

        Pattern slotPattern = Pattern.compile("^(?:[1-9]|[12][0-9]|30)$"); // Regular expression to validate the slot number this was done using the patterns library
        Pattern nNumberPattern = Pattern.compile("^N\\d{7}$"); // Regular expression to validate the N-Number this has been set to allow a capital letter N followed by 7 digits.
        Pattern datePattern = Pattern.compile("^\\d{1,2}(st|nd|rd|th)\\s[A-Za-z]{3,9}\\s\\d{4}$"); // Regular expression to validate the date
        Pattern timePattern = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"); // Regular expression to validate the time


        if (!slotPattern.matcher(SlotNumber).matches()) { // check if the slot number is valid using the regular expression
            editSlotNumber.setError("Slot number must be between 1-30");
            return;
        }


        if (!nNumberPattern.matcher(FirstName).matches()) { // check if the N-Number is valid using the regular expression
            editFirstName.setError("Enter a valid ID (e.g., N1048976)");
            return;
        }


        if (!datePattern.matcher(BookingDate).matches()) { // check if the date is valid using the regular expression
            editBookingDate.setError("Enter date in '20th March 2025' format");
            return;
        }


        if (!timePattern.matcher(BookingStartTime).matches()) { // check if the time is valid using the regular expression
            editStartTime.setError("Enter time in HH:mm format (e.g., 09:00)");
            return;
        }


        if (!timePattern.matcher(BookingEndTime).matches()) { // check if the time is valid using the regular expression
            editEndTime.setError("Enter time in HH:mm format (e.g., 13:00)");
            return;
        }


        if (BookingStartTime.compareTo(BookingEndTime) >= 0) { // check if the start time is before the end time
            editEndTime.setError("End time must be after start time");
            return;
        }

        new AlertDialog.Builder(this) // Show a confirmation dialog
                .setTitle("Confirm Booking") // Set the title
                .setMessage("Are you sure you want to book this slot?") // Set the message
                .setPositiveButton("Confirm", (dialog, which) -> { // Set the positive button
                    FirebaseUser currentUser = mAuth.getCurrentUser(); // Get the current user
                    db.collection("Bookings")
                            .whereEqualTo("SlotNumber", SlotNumber)       // Query the Firestore collection
                            .whereEqualTo("BookingDate", BookingDate)     // Filter by date
                            .whereEqualTo("BookingStartTime", BookingStartTime) // Filter by start time
                            .whereEqualTo("BookingEndTime", BookingEndTime)  // Filter by end time
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    QuerySnapshot snapshot = task.getResult(); // Get the query snapshot
                                    if (!snapshot.isEmpty()) {
                                        Toast.makeText(this, "This slot is already booked!", Toast.LENGTH_SHORT).show(); // Show a message if the slot is already booked
                                    } else {
                                        Map<String, Object> Booking = new HashMap<>(); // Create a new booking
                                        Booking.put("SlotNumber", SlotNumber); // set the slot number entered by the user in the input field
                                        Booking.put("FirstName", FirstName); // set the N number entered by the user in the input field, this was originally set to the users first name but was changed as many users may have the same name.
                                        Booking.put("BookingStartTime", BookingStartTime); // set the start time entered by the user in the input field
                                        Booking.put("BookingEndTime", BookingEndTime); // set the end time entered by the user in the input field
                                        Booking.put("BookingDate", BookingDate); // set the date entered by the user in the input field
                                        Booking.put("Status", "Booked"); //
                                        Booking.put("FinishedTime", "Pending");
                                        Booking.put("UserID", currentUser.getUid()); // Set the user ID this is used to filter the bookings by the current user in other parts of the app.

                                        db.collection("Bookings")
                                                .add(Booking)
                                                .addOnSuccessListener(documentReference -> { // Add the booking to the Firestore collection
                                                    Toast.makeText(this, "Slot Booked Successfully!", Toast.LENGTH_SHORT).show(); // Show a success message
                                                })
                                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to book slot!", Toast.LENGTH_SHORT).show()); // Show a failure message
                                    }
                                }
                            });
                })
                .setNegativeButton("Cancel", null) // Set the negative button
                .show(); // Show the dialog
    }
}