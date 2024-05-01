package com.example.gymapp.ui.Entry;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.gymapp.R;
import com.example.gymapp.databinding.ActivitySetupBinding;

import java.util.ArrayList;

public class SetupActivity extends AppCompatActivity {

    private ActivitySetupBinding binding;

    private ListView listViewGoals;

    private ArrayList<String> selectedGoals = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        listViewGoals = findViewById(R.id.listViewGoals);
        String[] goals = getResources().getStringArray(R.array.goals_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, goals);
        listViewGoals.setAdapter(adapter);


    }
}