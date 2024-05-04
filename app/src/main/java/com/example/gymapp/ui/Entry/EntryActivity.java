package com.example.gymapp.ui.Entry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.google.firebase.FirebaseApp;
import com.example.gymapp.ui.MainActivity;
import com.example.gymapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class EntryActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        FirebaseApp.initializeApp(this); // Initialize Firebase

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.username_input);
        passwordEditText = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_btn);
        registerTextView = findViewById(R.id.register_textBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntryActivity.this, RegisterActivity .class));
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(EntryActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EntryActivity.this, com.example.gymapp.ui.MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(EntryActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_LONG).show();
        }
    }
}
