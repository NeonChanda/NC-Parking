package com.example.nc_parking_new;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth; // Firebase authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance(); // Initialise Firebase authentication instance

    }

   public void signIn (String email, String password) { // Sign in function

       mAuth.signInWithEmailAndPassword(email, password) // Sign in with email and password
               .addOnCompleteListener(this, task -> {
                   if (task.isSuccessful()) {

                       // Sign in success, update UI with the signed-in user's information
                       FirebaseUser user = mAuth.getCurrentUser(); // Get the current user
                       Toast.makeText(MainActivity2.this, "Authentication success.",
                       Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(MainActivity2.this,LandingPageActivity.class);
                       startActivity(intent); // Start the landing page activity

                       //user has been signed in, use an intent to move to the next activity
                   } else {

                       // If sign in fails, display a message to the user.
                       Log.w("MainActivity2", "signInWithEmail:failure",
                               task.getException());
                       Toast.makeText(MainActivity2.this, "Authentication failed.",
                               Toast.LENGTH_SHORT).show();
                   }
               });
   }

    public void signInButtonClicked(View view){
        EditText email = findViewById(R.id.editTextTextEmailAddress2); // Get the email and password from the UI
        EditText password = findViewById(R.id.editTextTextPassword2);
        if (email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) { // Check if either field is empty
            Toast.makeText(MainActivity2.this, "email cannot be empty.", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().isEmpty() && !email.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity2.this, "password cannot be empty.", Toast.LENGTH_SHORT).show();


        } else if (password.getText().toString().isEmpty() && email.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity2.this, "email and password cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else{
            String sEmail = email.getText().toString(); // Convert the email and password to strings
            String sPassword = password.getText().toString();

            signIn(sEmail, sPassword); // Call the sign in function
            Toast.makeText(MainActivity2.this, "Authentication success.", // Display a success message if the user has successfully logged in
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity2.this,LandingPageActivity.class);
            startActivity(intent); // Start the landing page activity
        }
    }
}