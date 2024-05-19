package com.example.gymapp.ui.Workout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.gymapp.ui.Progress.ProgressTracker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
public class WorkoutActivity extends AppCompatActivity {

    private User activeUser;
    private ArrayList<Day> workoutPlan;
    private TextView tvDay, tvWeek;
    private ProgressTracker progressTracker;
    private Button finishButton;
    private ScrollView workoutDetails;
    private LinearLayout workoutDetailsContainer;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        activeUser = AppData.getInstance().getActiveUser();
        workoutPlan = (ArrayList<Day>) AppData.getInstance().getWorkoutPlan();
        progressTracker = AppData.getInstance().getProgressTracker();

        Log.d("loadData", progressTracker.toString());


        if (progressTracker == null) {
            progressTracker = new ProgressTracker(1, 1, activeUser.getDurationInWeeks());
            AppData.getInstance().setProgressTracker(progressTracker);
        }

        Log.d("WorkoutActivity", "Initial Progress: Day " + progressTracker.getCurrentDayNumber() + ", Week " + progressTracker.getCurrentWeekNumber());
        Log.d("loadData", activeUser.toString());
        Log.d("loadData", workoutPlan.toString());


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_workout);
        navBar(bottomNavigationView);

        setupUI();
        loadDay("Day " + progressTracker.getCurrentDayNumber());

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("WorkoutActivity", "Before Update: Day " + progressTracker.getCurrentDayNumber() + ", Week " + progressTracker.getCurrentWeekNumber());

                String nextDayLabel = "Day " + (progressTracker.getCurrentDayNumber() + 1);
                Day nextDay = findDayByLabel(nextDayLabel);

                progressTracker.updateProgress(nextDay != null);
                Log.d("WorkoutActivity", "After Update: Day " + progressTracker.getCurrentDayNumber() + ", Week " + progressTracker.getCurrentWeekNumber());

                if (progressTracker.isProgramComplete()) {
                    finishButton.setText("Workout Complete");
                    finishButton.setEnabled(false);
                } else {
                    tvWeek.setText("Week " + progressTracker.getCurrentWeekNumber());
                    loadDay("Day " + progressTracker.getCurrentDayNumber());
                }

                // Save progress tracker instance
                AppData.getInstance().setProgressTracker(progressTracker);

                // Save progress to Firestore
                saveProgressToFirestore();
            }
        });
    }

    private void setupUI() {
        tvDay = findViewById(R.id.tvDay);
        tvWeek = findViewById(R.id.tvWeek);
        workoutDetails = findViewById(R.id.workoutDetails);
        workoutDetailsContainer = findViewById(R.id.workoutDetailsContainer);
        finishButton = findViewById(R.id.finishButton);

        tvWeek.setText("Week " + progressTracker.getCurrentWeekNumber());
    }

    private void loadDay(String dayLabel) {
        Day day = findDayByLabel(dayLabel);
        if (day != null) {
            tvDay.setText(day.dayLabel);
            workoutDetailsContainer.removeAllViews(); // Clear previous exercises
            for (Exercise exercise : day.exercises) {
                View exerciseView = LayoutInflater.from(this).inflate(R.layout.exercise_item, workoutDetailsContainer, false);

                TextView tvExerciseName = exerciseView.findViewById(R.id.tvExerciseName);
                TextView tvReps = exerciseView.findViewById(R.id.tvReps);
                TextView tvSets = exerciseView.findViewById(R.id.tvSets);
                TextView tvWeight = exerciseView.findViewById(R.id.tvWeight);
                TextView tvTime = exerciseView.findViewById(R.id.tvTime);

                tvExerciseName.setText(exercise.name);
                tvReps.setText("Reps: " + exercise.reps);
                tvSets.setText("Sets: " + exercise.sets);
                tvWeight.setText("Weight: " + exercise.weight);
                tvTime.setText("Time: " + exercise.time);

                Button startExerciseButton = exerciseView.findViewById(R.id.exercise_start);
                startExerciseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showExerciseDetails(exercise);
                    }
                });

                workoutDetailsContainer.addView(exerciseView);
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

    public void navBar(BottomNavigationView bottomNavigationView) {
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
    private void showExerciseDetails(Exercise exercise) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.exercise_details_bottom_sheet, null);

        // Initialize Views
        TextView tvName = bottomSheetView.findViewById(R.id.tvExerciseDetailName);
        TextView tvReps = bottomSheetView.findViewById(R.id.tvDetailReps);
        TextView tvSets = bottomSheetView.findViewById(R.id.tvDetailSets);
        TextView tvWeight = bottomSheetView.findViewById(R.id.tvDetailWeight);
        TextView tvTime = bottomSheetView.findViewById(R.id.tvDetailTime);
        TextView tvCounter = bottomSheetView.findViewById(R.id.tvCounter);
        Button btnStartSet = bottomSheetView.findViewById(R.id.btnStartStopSet);
        Button btnPauseSet = bottomSheetView.findViewById(R.id.btnPauseSet);
        Button btnFinishExercise = bottomSheetView.findViewById(R.id.btnFinishExercise);
        TextView tvSet = bottomSheetView.findViewById(R.id.tvSet);

        // Set exercise details
        tvName.setText(exercise.name);
        tvReps.setText("Reps: " + exercise.reps);
        tvSets.setText("Sets: " + exercise.sets);
        tvWeight.setText("Weight: " + exercise.weight);
        tvTime.setText("Time: " + exercise.time);

        int totalSets = Integer.parseInt(exercise.sets);
        int[] currentSet = {1}; // Start with the first set
        tvSet.setText("Set: " + currentSet[0] + "/" + totalSets);

        // Initially hide the pause button
        btnPauseSet.setVisibility(View.GONE);

        // Timer setup
        final long[] timeLeft = {Long.parseLong(exercise.time) * 1000}; // Assuming time is in seconds
        final CountDownTimer[] timer = new CountDownTimer[1];

        btnStartSet.setOnClickListener(v -> {
            // Reset the time left to the initial full duration at the start of each set
            timeLeft[0] = Long.parseLong(exercise.time) * 1000;

            if (timer[0] != null) {
                timer[0].cancel(); // Cancel any existing timer
            }

            timer[0] = new CountDownTimer(timeLeft[0], 1000) {
                public void onTick(long millisUntilFinished) {
                    timeLeft[0] = millisUntilFinished;
                    tvCounter.setText("Time: " + millisUntilFinished / 1000 + "s");
                }

                public void onFinish() {
                    tvCounter.setText("Set Complete!");
                    btnPauseSet.setVisibility(View.GONE); // Hide pause button when timer finishes

                    currentSet[0]++;
                    if (currentSet[0] <= totalSets) {
                        tvSet.setText("Set: " + currentSet[0] + "/" + totalSets);
                        btnStartSet.setText("Start Next Set");
                    } else {
                        btnStartSet.setText("All Sets Complete");
                        btnStartSet.setEnabled(false); // Disable the start button when all sets are completed
                    }
                }
            }.start();
            btnPauseSet.setVisibility(View.VISIBLE); // Show the pause button only after starting the set
        });

        btnPauseSet.setOnClickListener(v -> {
            if ("Pause Set".equals(btnPauseSet.getText().toString())) {
                if (timer[0] != null) {
                    timer[0].cancel();
                    tvCounter.setText("Paused at: " + timeLeft[0] / 1000 + "s");
                }
                btnPauseSet.setText("Resume Set");
            } else {
                timer[0] = new CountDownTimer(timeLeft[0], 1000) {
                    public void onTick(long millisUntilFinished) {
                        timeLeft[0] = millisUntilFinished;
                        tvCounter.setText("Time: " + millisUntilFinished / 1000 + "s");
                    }

                    public void onFinish() {
                        tvCounter.setText("Set Complete!");
                        btnPauseSet.setVisibility(View.GONE);
                    }
                }.start();
                btnPauseSet.setText("Pause Set");
            }
        });

        btnFinishExercise.setOnClickListener(v -> {
            if (timer[0] != null) {
                timer[0].cancel();
            }
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void saveProgressToFirestore() {
        db.collection("users")
                .document(activeUser.getUid())
                .collection("progress")
                .document("currentProgress")
                .set(progressTracker, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Progress successfully written!"))
                .addOnFailureListener(e -> Log.w("Firestore", "Error writing document", e));
    }
}
