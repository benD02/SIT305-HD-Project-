package com.example.gymapp.ui.Progress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gymapp.R;
import com.example.gymapp.ui.AppData.AppData;
import com.example.gymapp.ui.ExerciseClasses.Day;
import com.example.gymapp.ui.Profile.ProfileActivity;
import com.example.gymapp.ui.Profile.User;
import com.example.gymapp.ui.Workout.WorkoutActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;



public class ProgressActivity extends AppCompatActivity {
    private User activeUser;
    private ArrayList<Day> workoutPlan;
    private ProgressTracker progressTracker;
    private LinearLayout progressBarContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        activeUser = AppData.getInstance().getActiveUser();
        workoutPlan = (ArrayList<Day>) AppData.getInstance().getWorkoutPlan();
        progressTracker = AppData.getInstance().getProgressTracker();

        Log.d("loadData", activeUser.toString());
        Log.d("loadData", workoutPlan.toString());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_progress);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            if (id == R.id.navigation_profile) {
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            } else if (id == R.id.navigation_workout) {
                intent = new Intent(getApplicationContext(), WorkoutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            } else if (id == R.id.navigation_progress) {
                return true;
            }
            return false;
        });

        progressBarContainer = findViewById(R.id.progressBarContainer);
        updateProgressBar();
    }
    private void updateProgressBar() {
        progressBarContainer.removeAllViews();

        int totalDays = activeUser.getDurationInWeeks() * activeUser.getDaysPerWeek();
        int completedDays = (progressTracker.getCurrentWeekNumber() - 1) * activeUser.getDaysPerWeek() + progressTracker.getCurrentDayNumber();

        for (int i = 1; i <= totalDays; i++) {
            // Create a FrameLayout to stack the day label on top of the dot
            FrameLayout dotContainer = new FrameLayout(this);
            LinearLayout.LayoutParams dotContainerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            dotContainerParams.setMargins(0, 0, 0, 0);
            dotContainer.setLayoutParams(dotContainerParams);

            // Add the dot
            ImageView dot = new ImageView(this);
            if (i <= completedDays) {
                dot.setImageResource(R.drawable.dot_complete);
            } else {
                dot.setImageResource(R.drawable.dot_incomplete);
            }
            FrameLayout.LayoutParams dotParams = new FrameLayout.LayoutParams(
                    200, // Width of the dot
                    200  // Height of the dot
            );
            dot.setLayoutParams(dotParams);
            dotContainer.addView(dot);

            // Add the day label
            TextView dayLabel = new TextView(this);
            dayLabel.setTextSize(15);
            dayLabel.setText("day:" + i);
            dayLabel.setGravity(Gravity.CENTER);
            FrameLayout.LayoutParams labelParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            dayLabel.setLayoutParams(labelParams);
            dotContainer.addView(dayLabel);

            progressBarContainer.addView(dotContainer);

            // Add the line
            if (i < totalDays) {
                View line = new View(this);
                LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                        10, // Width of the line
                        100 // Height of the line
                );
                lineParams.setMargins(0, 0, 0, 0);
                line.setLayoutParams(lineParams);
                line.setBackgroundColor(getResources().getColor(R.color.line_color)); // Set the color of the line
                progressBarContainer.addView(line);
            }
        }
    }
}