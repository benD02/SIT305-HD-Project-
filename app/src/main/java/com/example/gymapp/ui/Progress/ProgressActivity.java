package com.example.gymapp.ui.Progress;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.gymapp.R;
import com.example.gymapp.ui.Profile.ProfileActivity;
import com.example.gymapp.ui.Profile.User;
import com.example.gymapp.ui.Workout.WorkoutActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProgressActivity extends AppCompatActivity {
    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        activeUser = getIntent().getParcelableExtra("activeUser");


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_progress);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent;
            if (id == R.id.navigation_profile) {
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user", activeUser);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            } else if (id == R.id.navigation_workout) {
                intent = new Intent(getApplicationContext(), WorkoutActivity.class);
                intent.putExtra("user", activeUser);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_enter, R.anim.fade_exit);
                finish();
                return true;
            } else if (id == R.id.navigation_progress) {
                return true;
            }
            return false;
        });
    }
}
