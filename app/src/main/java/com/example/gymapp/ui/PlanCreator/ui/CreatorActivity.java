package com.example.gymapp.ui.PlanCreator.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gymapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreatorActivity extends AppCompatActivity {

    private LinearLayout layoutContainer;
    private DrawerLayout drawerLayout;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);  // Ensure you have this layout defined
        layoutContainer = findViewById(R.id.layout_container);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        drawerLayout = findViewById(R.id.drawer_layout);
        FloatingActionButton fab = findViewById(R.id.fab_ai);
        fab.setOnClickListener(view -> toggleDrawer());



        loadWorkoutDays();
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void loadWorkoutDays() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            db.collection("users").document(uid).collection("details")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                String daysStr = documentSnapshot.getString("days");
                                if (daysStr != null) {
                                    try {
                                        int days = Integer.parseInt(daysStr);
                                        for (int i = 1; i <= days; i++) {
                                            addDayInput(i);
                                        }
                                    } catch (NumberFormatException e) {
                                        Log.e("CreatorActivity", "Error parsing 'days' as an integer: " + e.getMessage(), e);
                                    }
                                } else {
                                    Log.e("CreatorActivity", "'days' field is null in document " + documentSnapshot.getId());
                                }
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CreatorActivity", "Error loading documents: " + e.getMessage(), e);
                    });
        } else {
            Log.e("CreatorActivity", "User is not logged in.");
        }
    }





    private void addDayInput(int day) {
        TextView dayLabel = new TextView(this);
        dayLabel.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dayLabel.setText("Day " + day);
        dayLabel.setTextSize(48);
        dayLabel.setPadding(0, 0, 0, 10); // Add bottom padding or margin to separate days
        layoutContainer.addView(dayLabel);

        // Create a container for exercises
        LinearLayout exercisesContainer = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 10); // Set margins around the exercises container
        exercisesContainer.setLayoutParams(layoutParams);
        exercisesContainer.setOrientation(LinearLayout.VERTICAL);
        layoutContainer.addView(exercisesContainer);

        // Add button to add exercises, with a limit of adding up to 10 exercises
        Button addExerciseButton = new Button(new ContextThemeWrapper(this, R.style.CustomButtonStyle));
        addExerciseButton.setText("Add Exercise");
        addExerciseButton.setOnClickListener(v -> {
            if (exercisesContainer.getChildCount() < 10 * 2) { // Each exercise adds two children (Spinner and EditText)
                addExerciseInput(exercisesContainer);
            } else {
                Toast.makeText(this, "Maximum of 10 exercises reached", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, 0, 0, 120); // Add bottom margin to the button
        addExerciseButton.setLayoutParams(buttonLayoutParams);

        layoutContainer.addView(addExerciseButton);

        // Initial exercise input
        addExerciseInput(exercisesContainer);
    }

    private void addExerciseInput(LinearLayout container) {
        // Container for a single exercise entry
        LinearLayout exerciseEntry = new LinearLayout(this);
        exerciseEntry.setOrientation(LinearLayout.VERTICAL);
        container.addView(exerciseEntry);

        // Spinner for selecting exercises
        Spinner exerciseSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.exercise_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);
        exerciseEntry.addView(exerciseSpinner);

        // Horizontal layout for exercise details
        LinearLayout detailsContainer = new LinearLayout(this);
        detailsContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        detailsContainer.setOrientation(LinearLayout.HORIZONTAL);
        exerciseEntry.addView(detailsContainer);

        // Details input fields
        addDetailInput(detailsContainer, "Reps");
        addDetailInput(detailsContainer, "Sets");
        addDetailInput(detailsContainer, "Weight");
        addDetailInput(detailsContainer, "Time");

        // Button to remove the exercise entry
        Button deleteButton = new Button(new ContextThemeWrapper(this, R.style.CustomDelButtonStyle));
        deleteButton.setText("Delete Exercise");
        deleteButton.setOnClickListener(v -> {
            // Remove the entire exercise entry from the container
            container.removeView(exerciseEntry);
        });

        // Add delete button below the details
        exerciseEntry.addView(deleteButton);
    }

    private void addDetailInput(LinearLayout container, String hint) {
        TextInputEditText editText = new TextInputEditText(new ContextThemeWrapper(this, R.style.CustomEditTextStyle));
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        editText.setHint(hint);
        container.addView(editText);
    }





}
