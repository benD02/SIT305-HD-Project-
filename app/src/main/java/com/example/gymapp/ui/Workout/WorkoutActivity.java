package com.example.gymapp.ui.Workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.gymapp.R;
import com.example.gymapp.databinding.ActivityCreatorBinding;
import com.example.gymapp.ui.ExerciseClasses.Day;
import com.example.gymapp.ui.Profile.ProfileActivity;
import com.example.gymapp.ui.Profile.User;
import com.example.gymapp.ui.Progress.ProgressActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {

    private User activeUser;
    private ArrayList<Day> workoutPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        activeUser = getIntent().getParcelableExtra("activeUser");
        workoutPlan = getIntent().getParcelableArrayListExtra("workoutPlan");



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_workout);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            if (id == R.id.navigation_profile) {
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user", activeUser);
                intent.putExtra("workoutPlan", new ArrayList(workoutPlan));
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            } else if (id == R.id.navigation_workout) {
                return true;
            } else if (id == R.id.navigation_progress) {
                intent = new Intent(getApplicationContext(), ProgressActivity.class);
                intent.putExtra("user", activeUser);
                intent.putExtra("workoutPlan", new ArrayList(workoutPlan));
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            }
            return false;
        });
    }
}
