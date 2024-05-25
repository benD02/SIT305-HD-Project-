package com.example.gymapp.ui.ExerciseClasses;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class Exercise implements Parcelable{
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

    protected Exercise(Parcel in) {
        name = in.readString();
        reps = in.readString();
        sets = in.readString();
        weight = in.readString();
        time = in.readString();
    }

    public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel in) {
            return new Exercise(in);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(reps);
        dest.writeString(sets);
        dest.writeString(weight);
        dest.writeString(time);
    }
    //added tostring function for debugging purposes
    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", reps='" + reps + '\'' +
                ", sets='" + sets + '\'' +
                ", weight='" + weight + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String  getName() {
        return name;
    }


    public String getReps() {
        return reps;
    }

    public String getSets() {
        return sets;
    }

    public String getWeight() {
        return weight;
    }

    public String getTime() {
        return time;
    }

}