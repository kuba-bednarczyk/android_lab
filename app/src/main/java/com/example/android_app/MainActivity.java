package com.example.android_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
//import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// importowanie bindingu
import com.example.android_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    // result launcher do odbierania i wysylania danych z innej aktywnosci
    private ActivityResultLauncher<Intent> mGradeActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        // podpięcie bindingu i inflate'era do wyswietlenia ze strings.xml
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // zarejestrowanie listenera do drugiej aktywnosci z funkcja do handlowania liczenia sredniej po powrocie
        mGradeActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleGradesActivityResult
        );

        setSupportActionBar(binding.materialToolbar); //podpięcie toolBara jako actionBar

        // przywracanie stanu bledow po obrocie ekranu
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
                if (!hasFocus) { // po utraceniu focusa z pola
                    EditText focusedTextField = (EditText) v;

                    if (focusedTextField.getText().toString().trim().isEmpty()) {
                        String errMessage = getString(R.string.err_empty_field);

                        if (v == binding.firstNameInput) {
                            errMessage = getString(R.string.err_first_name_empty);
                        } else if (v == binding.lastNameInput) {
                            errMessage = getString(R.string.err_last_name_empty);
                        }

                        focusedTextField.setError(errMessage);
                        Toast.makeText(MainActivity.this, errMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        // podpiecie listenera pod pola firstName i lastName
        binding.firstNameInput.setOnFocusChangeListener(validateText);
        binding.lastNameInput.setOnFocusChangeListener(validateText);


        // walidacja liczby ocen
        binding.gradesInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String gradesStr = binding.gradesInput.getText().toString().trim();
                    boolean err = false;

                    if (gradesStr.isEmpty()) {
                        err = true;
                    } else {
                        try { //try catch
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

        // uruchomienie aktywności GradesActivity po kliknieciu przycisku gradesBtn
        binding.gradesBtn.setOnClickListener(v -> {
            int gradesCount = Integer.parseInt(binding.gradesInput.getText().toString().trim());

            Intent intent = new Intent(MainActivity.this, GradesActivity.class);
            intent.putExtra("GRADES_COUNT", gradesCount);

            mGradeActivityLauncher.launch(intent);
        });
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

    private void handleGradesActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {

            double average = result.getData().getDoubleExtra("AVERAGE_RESULT", 0.0);

            String formattedAvg = String.format("%.2f", average);
            binding.averageTv.setText(getString(R.string.average_label, formattedAvg));
            binding.averageTv.setVisibility(View.VISIBLE);

            if (average >= 3.0) {
                binding.gradesBtn.setText(R.string.btn_super);
                binding.gradesBtn.setOnClickListener(v -> {
                    Toast.makeText(this, R.string.toast_congrats, Toast.LENGTH_LONG).show();
                    finish();
                });
            } else {
                binding.gradesBtn.setText(R.string.btn_fail);
                binding.gradesBtn.setOnClickListener(v -> {
                    Toast.makeText(this, R.string.toast_fail, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
            Toast.makeText(this, R.string.toast_no_grades, Toast.LENGTH_LONG).show();
        }
    };

}