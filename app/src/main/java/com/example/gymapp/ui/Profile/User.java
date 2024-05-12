package com.example.gymapp.ui.Profile;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class User implements Parcelable {
    private String uid;
    private String username;
    private String email;

    private String level;
    private int age;
    private double weight;
    private double height;
    private int daysPerWeek;
    private int durationInWeeks;
    private ArrayList<String> goals;

    public User() {
        // Default constructor
    }

    public User(String uid, String username, String email, String level, int age, double weight, double height, int daysPerWeek,
                int durationInWeeks, ArrayList<String> goals) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.level = level;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.daysPerWeek = daysPerWeek;
        this.durationInWeeks = durationInWeeks;
        this.goals = goals;
    }

    protected User(Parcel in) {
        uid = in.readString();
        username = in.readString();
        email = in.readString();
        level = in.readString();
        age = in.readInt();
        weight = in.readDouble();
        height = in.readDouble();
        daysPerWeek = in.readInt();
        durationInWeeks = in.readInt();
        goals = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Getters and setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public String getLevel() {
        return level;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(int daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public int getDurationInWeeks() {
        return durationInWeeks;
    }

    public void setDurationInWeeks(int durationInWeeks) {
        this.durationInWeeks = durationInWeeks;
    }

    public ArrayList<String> getGoals() {
        return goals;
    }

    public void setGoals(ArrayList<String> goals) {
        this.goals = goals;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(level);
        dest.writeInt(age);
        dest.writeDouble(weight);
        dest.writeDouble(height);
        dest.writeInt(daysPerWeek);
        dest.writeInt(durationInWeeks);
        dest.writeStringList(goals);
    }



    //added tostring function for debugging purposes
    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", level='" + level + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", duration=" + daysPerWeek +
                ", days=" + durationInWeeks +
                ", goals=" + goals +
                '}';
    }

}
