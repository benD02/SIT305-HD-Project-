package com.example.gymapp.ui.Progress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
    private ProgressBar progressBar;
    private LinearLayout progressBarContainer;
    private TextView progressTextView;
    private ScrollView scrollView;

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
        progressBar = findViewById(R.id.progress_pb);
        progressTextView = findViewById(R.id.progress_tv);
        scrollView = findViewById(R.id.scrollView);

        updateProgress();

        updateProgressBar();
    }
    private void updateProgressBar() {
        progressBarContainer.removeAllViews();

        int totalDays = activeUser.getDurationInWeeks() * activeUser.getDaysPerWeek();
        int completedDays = (progressTracker.getCurrentWeekNumber() - 1) * activeUser.getDaysPerWeek() + progressTracker.getCurrentDayNumber();
        for (int i = 1; i <= totalDays; i++) {
            // Inflate the dot layout
            View dotLayout = LayoutInflater.from(this).inflate(R.layout.dot_layout, progressBarContainer, false);
            ImageView dot = dotLayout.findViewById(R.id.dotImage);
            TextView dayLabel = dotLayout.findViewById(R.id.dayLabel);

            if (i <= completedDays) {
                dot.setImageResource(R.drawable.dot_complete);
            } else {
                dot.setImageResource(R.drawable.dot_incomplete);
            }
            dayLabel.setText("Day " + i);

            progressBarContainer.addView(dotLayout);

            // Inflate the line layout
            if (i < totalDays) {
                View lineLayout = LayoutInflater.from(this).inflate(R.layout.line_layout, progressBarContainer, false);
                progressBarContainer.addView(lineLayout);
            }

            // Auto-scroll to the current day
            if (i == completedDays) {
                dotLayout.post(() -> {
                    int scrollToY = dotLayout.getTop();
                    scrollView.smoothScrollTo(0, scrollToY);
                });
            }
        }
    }

    private void updateProgress() {
        int totalDays = activeUser.getDurationInWeeks() * activeUser.getDaysPerWeek();
        int completedDays = (progressTracker.getCurrentWeekNumber() - 1) * activeUser.getDaysPerWeek() + progressTracker.getCurrentDayNumber();
        int progress = (int) ((completedDays / (float) totalDays) * 100);

        progressBar.setProgress(progress);
        progressTextView.setText("Your plan is " + progress + "% completed");
    }


}