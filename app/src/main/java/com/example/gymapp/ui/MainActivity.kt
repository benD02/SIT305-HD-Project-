package com.example.gymapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymapp.R
import com.example.gymapp.ui.ExerciseClasses.Day
import com.example.gymapp.ui.ExerciseClasses.Exercise
import com.example.gymapp.ui.Profile.ProfileActivity
import com.example.gymapp.ui.Profile.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private var activeUser: User? = null
    private var workoutPlan: MutableList<Day> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            loadUserData(uid) { loadedUser ->
                activeUser = loadedUser
                // Load workout data after user data is loaded
                loadWorkoutData(uid) {
                    // Proceed to ProfileActivity or wherever necessary after loading all data
                    val intent = Intent(this, ProfileActivity::class.java).apply {
                        putExtra("activeUser", activeUser)
                        if (workoutPlan.isNotEmpty()) {
                            putExtra("workoutPlan", ArrayList(workoutPlan))
                        }
                    }
                    startActivity(intent)
                    finish()  // If you're moving to the new activity and don't want to return to this one
                }
            }
        } else {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserData(uid: String, onLoaded: (User) -> Unit) {
        val userDocRef = FirebaseFirestore.getInstance().collection("users").document(uid)

        userDocRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val username = document.getString("username") ?: "N/A"
                val email = document.getString("email") ?: "N/A"

                // Fetch additional details from the 'details' sub-collection
                userDocRef.collection("details").get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            // Assuming details are in the first document of the collection for simplicity
                            val detailsDoc = querySnapshot.documents[0]
                            val level = detailsDoc.getString("level") ?: "Beginner"
                            val age = detailsDoc.getString("age")?.toIntOrNull() ?: -1
                            val weight = detailsDoc.getString("weight")?.toDoubleOrNull() ?: -1.0
                            val height = detailsDoc.getString("height")?.toDoubleOrNull() ?: -1.0
                            val duration = detailsDoc.getString("duration")?.toIntOrNull() ?: -1
                            val days = detailsDoc.getString("days")?.toIntOrNull() ?: -1
                            val goals = ArrayList<String>(detailsDoc.get("goals") as? List<String> ?: listOf())

                            val loadedUser = User(
                                uid,
                                username,
                                email,
                                level,
                                age,
                                weight,
                                height,
                                duration,
                                days,
                                goals
                            )
                            onLoaded(loadedUser)
                        } else {
                            Toast.makeText(this, "Additional user details not found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("loadUserData", "Failed to load additional user details", e)
                        Toast.makeText(this, "Failed to load additional user details: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener { e ->
                Log.e("loadUserData", "Failed to load user data", e)
                Toast.makeText(this, "Failed to load user data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    private fun loadWorkoutData(uid: String, onLoaded: () -> Unit) {
        FirebaseFirestore.getInstance().collection("users").document(uid).collection("workoutPlans")
            .get()
            .addOnSuccessListener { querySnapshot ->
                workoutPlan.clear()
                querySnapshot.documents.forEach { document ->
                    val dayLabel = document.getString("Day") ?: "Unknown Day"
                    val exercisesList = document.get("Exercises") as List<Map<String, Any>>?
                    val day = Day(dayLabel)
                    exercisesList?.forEach { exerciseMap ->
                        val exercise = Exercise(
                            exerciseMap["Exercise Name"].toString(),
                            exerciseMap["Reps"].toString(),
                            exerciseMap["Sets"].toString(),
                            exerciseMap["Weight"].toString(),
                            exerciseMap["Time"].toString()
                        )
                        day.addExercise(exercise)
                    }
                    workoutPlan.add(day)
                }
                onLoaded()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load workout data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
