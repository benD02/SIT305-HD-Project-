package com.example.gymapp.ui.ExerciseClasses;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Day implements Parcelable {
    public String dayLabel;
    public List<Exercise> exercises;

    // Constructor
    public Day(String dayLabel) {
        this.dayLabel = dayLabel;
        this.exercises = new ArrayList<>();
    }

    protected Day(Parcel in) {
        dayLabel = in.readString();
        exercises = in.createTypedArrayList(Exercise.CREATOR);
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    // Add an exercise to the list
    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(dayLabel);
        dest.writeTypedList(exercises);
    }
    //added tostring function for debugging purposes
    @Override
    public String toString() {
        return "Day{" +
                "dayLabel='" + dayLabel + '\'' +
                ", exercises=" + exercises +
                '}';
    }

}
