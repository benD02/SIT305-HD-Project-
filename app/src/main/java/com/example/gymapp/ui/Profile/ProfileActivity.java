package com.example.gymapp.ui.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.gymapp.R;
import com.example.gymapp.databinding.ActivityCreatorBinding;
import com.example.gymapp.ui.Progress.ProgressActivity;
import com.example.gymapp.ui.Workout.WorkoutActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activeUser = getIntent().getParcelableExtra("activeUser");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_profile) {
                return true;
            } else if (id == R.id.navigation_workout) {
                Intent intent = new Intent(getApplicationContext(), WorkoutActivity.class);
                intent.putExtra("user", activeUser);  // Pass the activeUser to WorkoutActivity
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            } else if (id == R.id.navigation_progress) {
                Intent intent = new Intent(getApplicationContext(), ProgressActivity.class);
                intent.putExtra("user", activeUser);  // Pass the activeUser to ProgressActivity
                startActivity(intent);
               overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            }
            return false;
        });
    }
}
