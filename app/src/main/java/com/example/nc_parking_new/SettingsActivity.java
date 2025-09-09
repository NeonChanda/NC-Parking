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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    /**
     * this page is used to display some of the users settings to the user. from this page users can either change their password
     * or delete their account. the need for other settings like configuring a username were not deemed to be neccesary here as the application is
     * build for university students and uses their student number for bookings.
     */

    private FirebaseAuth mAuth; // Firebase Authentication instance
    private EditText editNewPassword;
    private Button ChangePassword, DeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        DeleteAccount = findViewById(R.id.DeleteAccount);

    }


    public void ChangePasswordClicked(View view) {
        editNewPassword = findViewById(R.id.editNewPassword); // Find UI elements
        ChangePassword = findViewById(R.id.ChangePassword);
        FirebaseUser user = mAuth.getCurrentUser(); // Get the current user
        String newPassword = editNewPassword.getText().toString().trim(); // Get the new password from the EditText, convert it to a string and then trim to remove any spaces

        if(editNewPassword.getText().toString().trim().isEmpty() && newPassword.length()<6) { // Check if the new password is empty or less than 6 characters

            editNewPassword.setError("Password cannot be empty"); // Set the error messages if the field is empty or the password is less than 6 characters
            editNewPassword.setError("Password must be at least 6 characters");

        }

        new AlertDialog.Builder(this) // Show a confirmation dialog
                .setTitle("Change Password") // Set the title
                .setMessage("Are you sure you want to change your password?") // Set the message
                .setPositiveButton("Change", (dialog, which) -> { // Set the positive button
                    user.updatePassword(newPassword) // Update the password using the new password entered by the current user.
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show(); // Show a success message
                                } else {
                                    Toast.makeText(SettingsActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show(); // Show a failure message
                                }
                            });
                })
                .setNegativeButton("Cancel", null) // Set the negative button
                .show(); // Show the dialog
    }



    public void DeleteClicked(View view){
        DeleteAccount = findViewById(R.id.DeleteAccount); // Find the delete account button
        FirebaseUser user = mAuth.getCurrentUser();
        new AlertDialog.Builder(this) // Show a confirmation dialog
                .setTitle("Delete Account") // Set the title
                .setMessage("Are you sure you want to permanently delete your account? This action cannot be undone.") // Set the message
                .setPositiveButton("Delete", (dialog, which) -> { // Set the positive button
                    user.delete() // Delete the current user's account
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                                    startActivity(intent); // Start the MainActivity once the account is deleted to allow the user to sign up again if they wish to do so.
                                } else {
                                    Toast.makeText(SettingsActivity.this, "Failed to delete account", Toast.LENGTH_SHORT).show(); // Show a failure message
                                }
                            });
                })
                .setNegativeButton("Cancel", null) // Set the negative button
                .show(); // Show the dialog
    }

    public void LogOutClicked(View view) {
        findViewById(R.id.logout); // Find the logout button
        mAuth.signOut(); // Sign the user out
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent); // Start the MainActivity once the user logs out to allow them to sign in again.
    }
}


