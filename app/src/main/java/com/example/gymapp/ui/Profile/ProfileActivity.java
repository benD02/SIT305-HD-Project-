package com.example.gymapp.ui.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.gymapp.R;
import com.example.gymapp.databinding.ActivityCreatorBinding;
import com.example.gymapp.ui.AppData.AppData;
import com.example.gymapp.ui.ExerciseClasses.Day;
import com.example.gymapp.ui.Progress.ProgressActivity;
import com.example.gymapp.ui.Workout.WorkoutActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private User activeUser;
    private ArrayList<Day> workoutPlan;

    private TextView tvProfileName, tvEmail, tvLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        activeUser = AppData.getInstance().getActiveUser();
        workoutPlan = (ArrayList<Day>) AppData.getInstance().getWorkoutPlan();

        Log.d("loadData", activeUser.toString());
        Log.d("loadData", workoutPlan.toString());

        // Retrieve the global active user and workout plan
        activeUser = AppData.getInstance().getActiveUser();
        workoutPlan = (ArrayList<Day>) AppData.getInstance().getWorkoutPlan();

        // Set the profile details
         tvProfileName = findViewById(R.id.tvProfileName);
         tvEmail = findViewById(R.id.tvEmail);
         tvLevel = findViewById(R.id.tvLvl);

        // Set texts from active user
        if (activeUser != null) {
            tvProfileName.setText(activeUser.getUsername()); // Assuming getName() method exists
            tvEmail.setText(activeUser.getEmail());     // Assuming getEmail() method exists
            tvLevel.setText("Level: " + activeUser.getLevel()); // Assuming getLevel() method exists
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        navBar( bottomNavigationView);

    }

    public void navBar(BottomNavigationView bottomNavigationView){
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_profile) {
                return true;
            } else if (id == R.id.navigation_workout) {
                Intent intent = new Intent(getApplicationContext(), WorkoutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            } else if (id == R.id.navigation_progress) {
                Intent intent = new Intent(getApplicationContext(), ProgressActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            }
            return false;
        });

    }
}
