package com.example.android_app;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

// importowanie bindingu
import com.example.android_app.databinding.Activity1bBinding;


public class MainActivity extends AppCompatActivity {

    private Activity1bBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = Activity1bBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.materialToolbar);

        // przywracanie błędów po obrocie ekranu - setError trzeba przywrócić ręcznie
        if (savedInstanceState != null ) {
            if (savedInstanceState.containsKey("ERR_FIRST")) {
                binding.firstNameInput.setError(savedInstanceState.getString("ERR_FIRST"));
            }

            if (savedInstanceState.containsKey("ERR_LAST")) {
                binding.lastNameInput.setError(savedInstanceState.getString("ERR_LAST"));
            }

            if (savedInstanceState.containsKey("ERR_GRADES")) {
                binding.gradesInput.setError(savedInstanceState.getString("ERR_GRADES"));
            }
        }

        // walidacja imienia i nazwiska
        View.OnFocusChangeListener validateText = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    EditText focusedTextField = (EditText) v;

                    if (focusedTextField.getText().toString().trim().isEmpty()) {
                        String errMessage = getString(R.string.err_empty_field);

                        if (focusedTextField.getId() == R.id.firstNameInput) {
                            errMessage = getString(R.string.err_first_name_empty);
                        } else if (focusedTextField.getId() == R.id.lastNameInput) {
                            errMessage = getString(R.string.err_last_name_empty);
                        }

                        focusedTextField.setError(errMessage);
                        Toast.makeText(MainActivity.this, errMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        binding.firstNameInput.setOnFocusChangeListener(validateText);
        binding.lastNameInput.setOnFocusChangeListener(validateText);


        // walidacja liczby ocen - pokazywanie bledow
        binding.gradesInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String gradesStr = binding.gradesInput.getText().toString().trim();
                    boolean err = false;

                    if (gradesStr.isEmpty()) {
                        err = true;
                    } else {
                        try {
                            int gradesInt = Integer.parseInt(gradesStr);
                            if (gradesInt < 5 || gradesInt > 15) {
                                err = true;
                            }
                        } catch (Exception e) {
                            err = true;
                        }
                    }

                    if (err) {
                        String errMsg = getString(R.string.err_grades_range);
                        binding.gradesInput.setError(errMsg);
                        Toast.makeText(MainActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //Text Watcher do chowania i odkrywania przycisku
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkTextFieldsAndShowButton();
            }
        };

        binding.firstNameInput.addTextChangedListener(watcher);
        binding.lastNameInput.addTextChangedListener(watcher);
        binding.gradesInput.addTextChangedListener(watcher);

        // funkcja pomocznicza dla przycisku
        checkTextFieldsAndShowButton();
    }


    // zapisywanie bledow przed obrotem ekranu
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // zapisujemy tresc komunikatu o bledzie
        if (binding.firstNameInput.getError() != null) {
            outState.putString("ERR_FIRST", binding.firstNameInput.getError().toString());
        }

        if (binding.lastNameInput.getError() != null) {
            outState.putString("ERR_LAST", binding.lastNameInput.getError().toString());
        }

        if (binding.gradesInput.getError() != null) {
            outState.putString("ERR_GRADES", binding.gradesInput.getError().toString());
        }
    }


    private void checkTextFieldsAndShowButton() {
        boolean statusOK = true;

        if (binding.firstNameInput.getText().toString().trim().isEmpty()) {
            statusOK = false;
        }

        if (binding.lastNameInput.getText().toString().trim().isEmpty()) {
            statusOK = false;
        }

        String grades = binding.gradesInput.getText().toString().trim();
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

        if (statusOK) {
            binding.gradesBtn.setVisibility(View.VISIBLE);
        } else {
            binding.gradesBtn.setVisibility(View.GONE);
        }
    }

}