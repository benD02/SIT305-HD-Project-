package com.example.gymapp.ui.Entry;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetupActivity extends AppCompatActivity {

    private EditText editAge, editWeight, editHeight, editDuration, editDays;
    private ListView listViewGoals;
    private Button confirmButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        editAge = findViewById(R.id.edit_age);
        editWeight = findViewById(R.id.edit_weight);
        editHeight = findViewById(R.id.edit_height);
        editDuration = findViewById(R.id.edit_duration);
        editDays = findViewById(R.id.edit_days);
        listViewGoals = findViewById(R.id.listViewGoals);
        confirmButton = findViewById(R.id.confirm_button);

        // Setup ListView
        String[] goals = getResources().getStringArray(R.array.goals_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, goals);
        listViewGoals.setAdapter(adapter);

        // Confirm button click listener
        confirmButton.setOnClickListener(v -> saveUserDetails());
    }

    private void saveUserDetails() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            String ageStr = editAge.getText().toString().trim();
            String weightStr = editWeight.getText().toString().trim();
            String heightStr = editHeight.getText().toString().trim();
            String durationStr = editDuration.getText().toString().trim();
            String daysStr = editDays.getText().toString().trim();

            int age = Integer.parseInt(ageStr.isEmpty() ? "-1" : ageStr);
            double weight = Double.parseDouble(weightStr.isEmpty() ? "-1" : weightStr);
            double height = Double.parseDouble(heightStr.isEmpty() ? "-1" : heightStr);
            int duration = Integer.parseInt(durationStr.isEmpty() ? "-1" : durationStr);
            int days = Integer.parseInt(daysStr.isEmpty() ? "-1" : daysStr);

            if (days < 1 || days > 7) {
                Toast.makeText(this, "Days per week should be between 1 and 7", Toast.LENGTH_LONG).show();
                return;
            }
            if (age < 10 || age > 100) {
                Toast.makeText(this, "Age should be between 10 and 100 years", Toast.LENGTH_LONG).show();
                return;
            }
            if (weight < 20 || weight > 300) {
                Toast.makeText(this, "Weight should be between 20kg and 300kg", Toast.LENGTH_LONG).show();
                return;
            }
            if (height < 100 || height > 250) {
                Toast.makeText(this, "Height should be between 100cm and 250cm", Toast.LENGTH_LONG).show();
                return;
            }
            if (duration < 1 || duration > 52) {
                Toast.makeText(this, "Duration should be between 1 week and 52 weeks", Toast.LENGTH_LONG).show();
                return;
            }


            Map<String, Object> userMap = new HashMap<>();
            userMap.put("age", editAge.getText().toString().trim());
            userMap.put("weight", editWeight.getText().toString().trim());
            userMap.put("height", editHeight.getText().toString().trim());
            userMap.put("duration", editDuration.getText().toString().trim());
            userMap.put("days", editDays.getText().toString().trim());
            userMap.put("goals", getSelectedGoals());

            db.collection("users").document(user.getUid()).collection("details")
                    .add(userMap)
                    .addOnSuccessListener(documentReference -> Toast.makeText(SetupActivity.this, "Details Saved Successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(SetupActivity.this, "Failed to Save Details: " + e.getMessage(), Toast.LENGTH_LONG).show());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<String> getSelectedGoals() {
        ArrayList<String> selectedGoals = new ArrayList<>();
        for (int i = 0; i < listViewGoals.getCount(); i++) {
            if (listViewGoals.isItemChecked(i)) {
                selectedGoals.add(listViewGoals.getItemAtPosition(i).toString());
            }
        }
        return selectedGoals;
    }
}
