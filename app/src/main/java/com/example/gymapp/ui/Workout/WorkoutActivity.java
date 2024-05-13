package com.example.gymapp.ui.Workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.gymapp.R;
import com.example.gymapp.databinding.ActivityCreatorBinding;
import com.example.gymapp.ui.AppData.AppData;
import com.example.gymapp.ui.ExerciseClasses.Day;
import com.example.gymapp.ui.ExerciseClasses.Exercise;
import com.example.gymapp.ui.Profile.ProfileActivity;
import com.example.gymapp.ui.Profile.User;
import com.example.gymapp.ui.Progress.ProgressActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {

    private User activeUser;
    private ArrayList<Day> workoutPlan;
    private TextView tvDay;

    private int currentDayNumber  = 1; // Tracks the current workout day, starts at Day 1
    private Button finishButton;
    private ScrollView workoutDetails;
    private LinearLayout workoutDetailsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        activeUser = AppData.getInstance().getActiveUser();
        workoutPlan = (ArrayList<Day>) AppData.getInstance().getWorkoutPlan();

        Log.d("loadData", activeUser.toString());
        Log.d("loadData", workoutPlan.toString());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_workout);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        navBar( bottomNavigationView);

        setupUI();
        loadDay("Day " + currentDayNumber);




        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDayNumber++;
                String nextDayLabel = "Day " + currentDayNumber;
                Day nextDay = findDayByLabel(nextDayLabel);
                if (nextDay != null) {
                    loadDay(nextDayLabel);
                } else {
                    finishButton.setText("Workout Complete");
                    finishButton.setEnabled(false);
                }
            }
        });


    }

    private void setupUI() {
        tvDay = findViewById(R.id.tvDay);
        workoutDetails = findViewById(R.id.workoutDetails);
        workoutDetailsContainer = findViewById(R.id.workoutDetailsContainer);
        finishButton = findViewById(R.id.finishButton);

    }

    private void loadDay(String dayLabel) {
        Day day = findDayByLabel(dayLabel);
        if (day != null) {
            tvDay.setText(day.dayLabel);

            workoutDetailsContainer.removeAllViews(); // Clear previous exercises
            for (Exercise exercise : day.exercises) {
                TextView tvExercise = new TextView(this);
                tvExercise.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                tvExercise.setTextSize(20);
                tvExercise.setTextColor(getResources().getColor(R.color.white));
                String exerciseDetails = String.format("%s - Reps: %s, Sets: %s, Weight: %s, Time: %s",
                        exercise.name, exercise.reps, exercise.sets, exercise.weight, exercise.time);
                tvExercise.setText(exerciseDetails);
                workoutDetailsContainer.addView(tvExercise);
            }
        } else {
            tvDay.setText("Day not found: " + dayLabel);
        }
    }

    private Day findDayByLabel(String label) {
        for (Day day : workoutPlan) {
            if (day.dayLabel.equals(label)) {
                return day;
            }
        }
        return null; // Return null if no matching day is found
    }



    public void navBar(BottomNavigationView bottomNavigationView){
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_profile) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            } else if (id == R.id.navigation_workout) {
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
