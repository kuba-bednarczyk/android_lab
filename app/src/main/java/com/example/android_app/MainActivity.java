package com.example.android_app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import androidx.activity.EdgeToEdge;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_app.databinding.Activity1bBinding;
import com.google.android.material.appbar.MaterialToolbar;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Activity1bBinding binding;
    EditText firstNameInput;
    EditText lastNameInput;
    EditText gradesInput;
    Button gradesBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = Activity1bBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        firstNameInput = findViewById(R.id.firstName);
        lastNameInput = findViewById(R.id.lastName);
        gradesInput = findViewById(R.id.grades);
        gradesBtn = findViewById(R.id.gradesBtn);

    };

    private void checkTextFieldsAndShowButton() {
        boolean statusOK = true;

        if (firstNameInput.getText().toString().trim().isEmpty()) {
            statusOK = false;
        }

        if (lastNameInput.getText().toString().trim().isEmpty()) {
            statusOK = false;
        }

        String grades = gradesInput.getText().toString().trim();
        if (grades.isEmpty()) {
            statusOK = false;
        } else {
            try {
                int numOfGrades = Integer.parseInt(grades);
                if (numOfGrades < 5 || numOfGrades > 15) {
                    statusOK = false;
                }
            } catch (NumberFormatException e) {
                statusOK = false;
            }
        }

        if (statusOK == true) {
            gradesBtn.setVisibility(View.VISIBLE);
        } else {
            gradesBtn.setVisibility(View.GONE);
        }
    }


}