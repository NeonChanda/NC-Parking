package com.example.nc_parking_new;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    /**
     * this is the main activity for the app. it handles the sign up and log in functionality.
     * permissions for notifications have been handled in the onCreate method to ensure that they are accepted
     * on launch as they are crucial for the functionality of the app.
     */

    /**
     * Some code in this class has been taken from: https://www.digitalocean.com/community/tutorials/android-location-api-tracking-gps
     */
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTracking locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        mAuth = FirebaseAuth.getInstance();





        FirebaseMessaging.getInstance().subscribeToTopic("all");
        getFirebaseCloudMessagingToken();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT >= 33) {
                if
                (ContextCompat.checkSelfPermission(MainActivity.this,
                        android.Manifest.permission.POST_NOTIFICATIONS) !=
                        PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new
                            String[]{android.Manifest.permission.POST_NOTIFICATIONS},101);
                }
            }

            NotificationChannel channel = new
                    NotificationChannel("firebase", "Firebase channel",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager =
                    getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


    }

    private void getFirebaseCloudMessagingToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        Log.d("FCM Token", "Token: " + token);
                        // Save the token for future use (e.g., sending messages from the server)
                    } else {
                        Log.e("FCM Token", "Failed to get token");
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            //User is signed in use an intent to move to another activity
        }
    }

    public void signup(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("MainActivity",
                                "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(MainActivity.this,
                                "Authentication success. Use an intent to move to a new activity",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,LandingPageActivity.class);
                        startActivity(intent);

                        //user has been signed in, use an intent to move to the next activity
                    } else {

                        // If sign in fails, display a message to the user.
                                Log.w("MainActivity",
                                        "createUserWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void signupButtonClicked(View view){
        EditText email = findViewById(R.id.editTextTextEmailAddress); // Find the UI elements
        EditText password = findViewById(R.id.editTextTextPassword);
        if (email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) { // Check if the fields are empty
            Toast.makeText(MainActivity.this, "email cannot be empty.", Toast.LENGTH_SHORT).show(); // If so, display an error message
        } else if (password.getText().toString().isEmpty() && !email.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "password cannot be empty.", Toast.LENGTH_SHORT).show();


        } else if (password.getText().toString().isEmpty() && email.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "email and password cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else{
            String sEmail = email.getText().toString(); // Get the text from the EditText fields
            String sPassword = password.getText().toString();

            signup(sEmail, sPassword); // Call the signup method with the entered values
            Intent intent = new Intent(MainActivity.this,LandingPageActivity.class); // Create an intent to move to the next activity
            startActivity(intent);
        }
    }

    public void alreadyRegClicked(View view) {
        Intent intent = new Intent(MainActivity.this,MainActivity2.class);
        startActivity(intent); // Start the MainActivity2 once the already registered button is clicked
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) { // Helper method to find the permissions that have not been accepted
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    (dialog, which) -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) { // Helper method to show a dialog with a message and two buttons that allow them to accept the permissions or cancel them.
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message) // Set the message
                .setPositiveButton("OK", okListener)  // Set the positive button
                .setNegativeButton("Cancel", null)   // Set the negative button
                .create() // Create the dialog
                .show(); // Show the dialog
    }

    @Override
    protected void onDestroy() { // This method is called when the activity is destroyed
        super.onDestroy(); // Call the superclass method first
        locationTrack.stopListener(); // Stop the location tracking service
    }

}