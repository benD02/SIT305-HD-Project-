package com.example.gymapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gymapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.gymapp.ui.Profile.ProfileActivity
import com.example.gymapp.ui.Workout.WorkoutActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_menu)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_menu -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                R.id.bottom_nav_menu -> {
                    startActivity(Intent(this, WorkoutActivity::class.java))
                }
            }
            true
        }

        // Optionally, set the default selected item if needed
        if (savedInstanceState == null) {
            navView.selectedItemId = R.id.bottom_nav_menu
        }
    }
}
