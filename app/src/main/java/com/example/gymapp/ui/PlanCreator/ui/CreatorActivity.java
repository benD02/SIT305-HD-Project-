package com.example.gymapp.ui.PlanCreator.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gymapp.R;
import com.example.gymapp.ui.ExerciseClasses.Day;
import com.example.gymapp.ui.ExerciseClasses.Exercise;
import com.example.gymapp.ui.Profile.User;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreatorActivity extends AppCompatActivity {

    private LinearLayout layoutContainer;
    private DrawerLayout drawerLayout;
    private TextView responseText;
    private Button createPlanButton;

    private User activeUser;

    private List<Day> workoutDays = new ArrayList<>();


    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String daysStr;
    String age;
    String duration;
    List<String> goals;
    String height;
    String weight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);  // Ensure you have this layout defined
        layoutContainer = findViewById(R.id.layout_container);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        createPlanButton = findViewById(R.id.create_btn);
        createPlanButton.setOnClickListener(v -> saveRoutineToFirestore());
        activeUser = getIntent().getParcelableExtra("user");


        responseText  = findViewById(R.id.tv_response);
        drawerLayout = findViewById(R.id.drawer_layout);
        FloatingActionButton fab = findViewById(R.id.fab_ai);
        fab.setOnClickListener(view -> toggleDrawer());

        loadWorkoutDays();
        generativeAi(daysStr, age, duration, goals, height, weight );
    }

    private void saveRoutineToFirestore() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (workoutDays.isEmpty()) {
            Toast.makeText(this, "No workout days to save.", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference workoutPlans = db.collection("users")
                .document(user.getUid())
                .collection("workoutPlans");

        for (Day day : workoutDays) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("Day", day.dayLabel);
            List<Map<String, Object>> exercisesData = new ArrayList<>();

            LinearLayout exercisesContainer = (LinearLayout) layoutContainer.findViewWithTag(day);
            for (int i = 0; i < exercisesContainer.getChildCount(); i++) {
                View view = exercisesContainer.getChildAt(i);
                if (view instanceof LinearLayout) {
                    LinearLayout entry = (LinearLayout) view;
                    Spinner exerciseSpinner = (Spinner) entry.getChildAt(0);
                    TextInputEditText[] inputs = (TextInputEditText[]) exerciseSpinner.getTag();

                    Map<String, Object> exerciseData = new HashMap<>();
                    exerciseData.put("Exercise Name", exerciseSpinner.getSelectedItem().toString());
                    exerciseData.put("Reps", inputs[0].getText().toString());
                    exerciseData.put("Sets", inputs[1].getText().toString());
                    exerciseData.put("Weight", inputs[2].getText().toString());
                    exerciseData.put("Time", inputs[3].getText().toString());
                    exercisesData.add(exerciseData);
                }
            }

            dayData.put("Exercises", exercisesData);
            workoutPlans.add(dayData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(CreatorActivity.this, "Workout plan for " + day.dayLabel + " saved successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CreatorActivity.this, "Failed to save workout plan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void loadWorkoutDays() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            db.collection("users").document(uid).collection("details")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                 daysStr = documentSnapshot.getString("days");
                                 age = documentSnapshot.getString("age");
                                 duration = documentSnapshot.getString("duration");
                                 goals = (List<String>) documentSnapshot.get("goals");
                                 height = documentSnapshot.getString("height");
                                 weight = documentSnapshot.getString("weight");

                                Log.d("CreatorActivity", "Age: " + age);
                                Log.d("CreatorActivity", "Duration: " + duration);
                                Log.d("CreatorActivity", "Height: " + height);
                                Log.d("CreatorActivity", "Weight: " + weight);
                                Log.d("CreatorActivity", "Goals: " + goals);
                                if (daysStr != null) {
                                    try {
                                        int days = Integer.parseInt(daysStr);
                                        for (int i = 1; i <= days; i++) {
                                            addDayInput(i);
                                        }
                                    } catch (NumberFormatException e) {
                                        Log.e("CreatorActivity", "Error parsing 'days' as an integer: " + e.getMessage(), e);
                                    }
                                } else {
                                    Log.e("CreatorActivity", "'days' field is null in document " + documentSnapshot.getId());
                                }

                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("CreatorActivity", "Error loading documents: " + e.getMessage(), e);
                    });

        } else {
            Log.e("CreatorActivity", "User is not logged in.");
        }


    }

    private void generativeAi(String daysStr, String age, String duration, List goals, String height, String weight ){
        GenerativeModel gm = new  GenerativeModel(/* modelName */ "gemini-pro",/* apiKey */  "AIzaSyDJxlEt0SvOaoocUIcorL-sDxaFjUXNU60");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder()
                .addText( "Make a single week repeatable Gym routine accounting for the following variables:" + duration +  "weeks, covering the following goals:" + goals + "frequency: "
                        + daysStr + "times/week, " + "age: "+ age + "body weight:" + weight + "kg, height:" + height +  " cm,")
                .build();

        Log.d("CreatorActivity", content.toString());


        ExecutorService executor  = Executors.newSingleThreadExecutor();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Log.d("CreatorActivity", response.toString());
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                runOnUiThread(() -> {
                    responseText.setText(resultText);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), "Error generating content", Toast.LENGTH_SHORT).show();
                });
                t.printStackTrace();
            }
        }, executor);




    }



    // Updated addDayInput method
    private void addDayInput(int day) {
        // Create a new Day instance
        Day currentDay = new Day("Day " + day);
        workoutDays.add(currentDay);

        TextView dayLabel = new TextView(this);
        dayLabel.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dayLabel.setText(currentDay.dayLabel);
        dayLabel.setTextSize(48);
        dayLabel.setPadding(0, 0, 0, 10); // Add bottom padding/margin to separate days
        layoutContainer.addView(dayLabel);

        // Create a container for exercises
        LinearLayout exercisesContainer = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 10, 0, 10); // Add margins around the exercises container
        exercisesContainer.setLayoutParams(layoutParams);
        exercisesContainer.setOrientation(LinearLayout.VERTICAL);
        exercisesContainer.setTag(currentDay); // Associate the day object with this layout
        layoutContainer.addView(exercisesContainer);

        // Add button to add exercises, with a limit of adding up to 10 exercises
        Button addExerciseButton = new Button(new ContextThemeWrapper(this, R.style.CustomButtonStyle));
        addExerciseButton.setText("Add Exercise");
        addExerciseButton.setOnClickListener(v -> {
            if (exercisesContainer.getChildCount() < 10 * 2) { // Each exercise adds two children (Spinner and EditText)
                addExerciseInput(exercisesContainer);
            } else {
                Toast.makeText(this, "Maximum of 10 exercises reached", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, 0, 0, 120); // Add bottom margin to the button
        addExerciseButton.setLayoutParams(buttonLayoutParams);
        layoutContainer.addView(addExerciseButton);

        // Initial exercise input
        addExerciseInput(exercisesContainer);
    }

    // Updated addExerciseInput method
    private void addExerciseInput(LinearLayout container) {
        Day currentDay = (Day) container.getTag();

        LinearLayout exerciseEntry = new LinearLayout(this);
        exerciseEntry.setOrientation(LinearLayout.VERTICAL);
        container.addView(exerciseEntry);

        Spinner exerciseSpinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.exercise_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);
        exerciseEntry.addView(exerciseSpinner);

        LinearLayout detailsContainer = new LinearLayout(this);
        detailsContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        detailsContainer.setOrientation(LinearLayout.HORIZONTAL);
        exerciseEntry.addView(detailsContainer);

        // Adding detail inputs and associating them with the spinner via tag to fetch them later
        exerciseSpinner.setTag(new TextInputEditText[]{
                addDetailInput(detailsContainer, "Reps"),
                addDetailInput(detailsContainer, "Sets"),
                addDetailInput(detailsContainer, "Weight"),
                addDetailInput(detailsContainer, "Time")
        });
    }


    private TextInputEditText addDetailInput(LinearLayout container, String hint) {
        TextInputEditText editText = new TextInputEditText(new ContextThemeWrapper(this, R.style.CustomEditTextStyle));
        editText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        editText.setHint(hint);
        container.addView(editText);
        return editText; // Return the created input field
    }





}
