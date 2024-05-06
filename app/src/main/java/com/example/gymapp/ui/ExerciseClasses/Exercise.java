package com.example.gymapp.ui.ExerciseClasses;

public class Exercise {
    public String name;
    public String reps;
    public String sets;
    public String weight;
    public String time;

    // Constructor
    public Exercise(String name, String reps, String sets, String weight, String time) {
        this.name = name;
        this.reps = reps;
        this.sets = sets;
        this.weight = weight;
        this.time = time;
    }
}