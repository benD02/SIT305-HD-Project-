package com.example.gymapp.ui.Entry;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.example.gymapp.R;
import com.example.gymapp.ui.Profile.User;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;
    private EditText confirmEditText;
    private Button signupButton;
    private TextView loginTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;  // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        usernameEditText = findViewById(R.id.signup_username_input);
        emailEditText = findViewById(R.id.signup_email_input);
        passwordEditText = findViewById(R.id.signup_password_input);
        confirmEditText = findViewById(R.id.signup_password_confirm_input);
        signupButton = findViewById(R.id.signup_btn);
        loginTextView = findViewById(R.id.loginButton);

        signupButton.setOnClickListener(v -> registerUser());
        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, EntryActivity.class));
            finish();
        });
    }

    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Email and password fields cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User registration success
                        FirebaseUser user = mAuth.getCurrentUser();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("username", username);
                        userData.put("email", email);

                        //We need to save the UID locally so it can be properly referneced
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        // Add a new document with the user's UID
                        db.collection("users").document(user.getUid())
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    //Stored the user instance inside  of the on success listener so it doesnt make the instnace if the  firestore is save is unsuccessful
                                    User activeUser = new User(firebaseUser.getUid(), username, email, -1, -1, -1, -1, -1, new ArrayList<>());
                                    Toast.makeText(RegisterActivity.this, "Registration successful and data stored.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                                    setupIntent.putExtra("user", activeUser);
                                    startActivity(setupIntent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegisterActivity.this, "Error saving user data: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                });
                    } else {
                        // Registration failed
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
