package com.example.gymapp.ui.ExerciseClasses;

import java.util.ArrayList;
import java.util.List;

public class Day {
    public String dayLabel;
    public List<Exercise> exercises;

    // Constructor
    public Day(String dayLabel) {
        this.dayLabel = dayLabel;
        this.exercises = new ArrayList<>();
    }

    // Add an exercise to the list
    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }
}
