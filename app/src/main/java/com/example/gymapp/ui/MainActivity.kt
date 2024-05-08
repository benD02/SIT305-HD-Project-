package com.example.gymapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.gymapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.gymapp.ui.Profile.ProfileActivity
import com.example.gymapp.ui.Profile.User
import com.example.gymapp.ui.Workout.WorkoutActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private var activeUser: User? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load user data
        loadUserData { loadedUser ->
            activeUser = loadedUser

        }

        val intent = Intent(this, ProfileActivity::class.java).apply {
            putExtra("activeUser", activeUser)
        }
        startActivity(intent)


    }

    private fun loadUserData(onLoaded: (User) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val loadedUser = User(
                            uid,
                            document.getString("username") ?: "",
                            document.getString("email") ?: "",
                            document.getLong("age")?.toInt() ?: -1,
                            document.getDouble("weight") ?: -1.0,
                            document.getDouble("height") ?: -1.0,
                            document.getLong("duration")?.toInt() ?: -1,
                            document.getLong("days")?.toInt() ?: -1,
                            ArrayList(document.get("goals") as List<String>? ?: listOf())
                        )
                        onLoaded(loadedUser)
                    } else {
                        Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to load user data: ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show()
        }
    }
}
