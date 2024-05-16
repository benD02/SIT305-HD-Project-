package com.example.gymapp.ui.AppData;

import com.example.gymapp.ui.ExerciseClasses.Day;
import com.example.gymapp.ui.Profile.User;
import com.example.gymapp.ui.Progress.ProgressTracker;

import java.util.ArrayList;
import java.util.List;

//This appData class is a singleton used to store the session data of both activeUser and their workout plan


//Here is some code used to set and access the global variable from any activity or fragment

//set code
//AppData.getInstance().setActiveUser(activeUser);
//AppData.getInstance().setWorkoutPlan(workoutPlan);
//
//
//get code
//User user = AppData.getInstance().getActiveUser();
//List<Day> plan = AppData.getInstance().getWorkoutPlan();
//


public class AppData {
    private static final AppData instance = new AppData();
    private User activeUser;
    private List<Day> workoutPlan = new ArrayList<>();
    private ProgressTracker progressTracker;

    private AppData() {}

    public static AppData getInstance() {
        return instance;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public List<Day> getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(List<Day> workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    public void setProgressTracker(ProgressTracker progressTracker) {
        this.progressTracker = progressTracker;
    }
}
