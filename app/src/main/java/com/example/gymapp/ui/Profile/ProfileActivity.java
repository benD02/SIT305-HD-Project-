package com.example.gymapp.ui.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gymapp.R;
import com.example.gymapp.ui.AppData.AppData;
import com.example.gymapp.ui.ExerciseClasses.Day;
import com.example.gymapp.ui.ExerciseClasses.Exercise;
import com.example.gymapp.ui.Progress.ProgressActivity;
import com.example.gymapp.ui.Workout.WorkoutActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;


public class ProfileActivity extends AppCompatActivity {

    private User activeUser;
    private ArrayList<Day> workoutPlan;

    private LinearLayout workoutPlanContainer;


    private TextView tvProfileName, tvEmail, tvLevel, txtPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activeUser = AppData.getInstance().getActiveUser();
        workoutPlan = (ArrayList<Day>) AppData.getInstance().getWorkoutPlan();

        Log.d("loadData", activeUser.toString());
        Log.d("loadData", workoutPlan.toString());

        tvProfileName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvEmail);
        tvLevel = findViewById(R.id.tvLvl);
        workoutPlanContainer = findViewById(R.id.workoutPlanContainer);


        if (activeUser != null) {
            tvProfileName.setText(activeUser.getUsername());
            tvEmail.setText(activeUser.getEmail());
            tvLevel.setText("Level: " + activeUser.getLevel());
            displayWorkoutPlan(workoutPlan);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        navBar(bottomNavigationView);
    }

    private void displayWorkoutPlan(ArrayList<Day> workoutPlan) {
        workoutPlanContainer.removeAllViews();

        if (workoutPlan != null && !workoutPlan.isEmpty()) {
            for (Day day : workoutPlan) {
                View dayView = LayoutInflater.from(this).inflate(R.layout.day_item, workoutPlanContainer, false);
                TextView tvDayLabel = dayView.findViewById(R.id.tvDayLabel);
                LinearLayout exercisesContainer = dayView.findViewById(R.id.exercisesContainer);

                tvDayLabel.setText(day.getDayLabel());

                for (Exercise exercise : day.getExercises()) {
                    View exerciseView = LayoutInflater.from(this).inflate(R.layout.exercise_item_pp, exercisesContainer, false);
                    TextView tvExerciseName = exerciseView.findViewById(R.id.tvExerciseName);
                    TextView tvExerciseDetails = exerciseView.findViewById(R.id.tvExerciseDetails);

                    tvExerciseName.setText(exercise.getName());
                    tvExerciseDetails.setText(exercise.getReps() + " reps, " +
                            exercise.getSets() + " sets, " +
                            exercise.getWeight() + " weight, " +
                            exercise.getTime() + " time");

                    exercisesContainer.addView(exerciseView);
                }

                workoutPlanContainer.addView(dayView);
            }
        } else {
            TextView noPlanText = new TextView(this);
            noPlanText.setText("No workout plan available.");
            workoutPlanContainer.addView(noPlanText);
        }
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