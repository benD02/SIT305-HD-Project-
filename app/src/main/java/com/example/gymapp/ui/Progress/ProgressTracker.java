package com.example.gymapp.ui.Progress;

import android.util.Log;

import com.example.gymapp.ui.AppData.AppData;
import com.example.gymapp.ui.Profile.User;

public class ProgressTracker {
    private int currentDayNumber;
    private int currentWeekNumber;
    private int durationInWeeks;


    public ProgressTracker(int currentDayNumber, int currentWeekNumber, int durationInWeeks) {
        this.currentDayNumber = currentDayNumber;
        this.currentWeekNumber = currentWeekNumber;
        this.durationInWeeks =  durationInWeeks;
    }

    public int getCurrentDayNumber() {
        return currentDayNumber;
    }

    public int getCurrentWeekNumber() {
        return currentWeekNumber;
    }

    public void incrementDay() {
        currentDayNumber++;
    }

    public void resetDay() {
        currentDayNumber = 1;
    }

    public void incrementWeek() {
        currentWeekNumber++;
    }

    public boolean isProgramComplete() {
        return currentWeekNumber > durationInWeeks;
    }

    public void updateProgress(boolean dayExists) {
        if (dayExists) {
            incrementDay();
        } else {
            resetDay();
            incrementWeek();
        }
        Log.d("ProgressTracker", "Updated Progress: Day " + currentDayNumber + ", Week " + currentWeekNumber);
    }


}
