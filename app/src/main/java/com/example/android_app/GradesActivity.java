package com.example.android_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android_app.databinding.ActivityGradesBinding;
import com.example.android_app.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class GradesActivity extends AppCompatActivity {

    private ActivityGradesBinding binding;
    private int mGradesCount = 5; //domyslna liczba ocen
    List<Grade> mGradeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // podpiecie bindingu do inflate'era (do wyswietlenia ze strings.xml nazw przedmiotow i ocen)
        binding = ActivityGradesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // odebranie danych z poprzedniego widoku
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mGradesCount = extras.getInt("GRADES_COUNT", 5);
        }

        // utworzenie listy z przedmiotami i ocenami
        mGradeList = new ArrayList<>();
        String[] subjects = getResources().getStringArray(R.array.subjects);
        // przywracanie stanu po obrocie ekranu
        if (savedInstanceState != null && savedInstanceState.containsKey("SAVED_GRADES")) {
            double[] savedGrades = savedInstanceState.getDoubleArray("SAVED_GRADES");

            for (int i = 0; i < mGradesCount; i++) {
                mGradeList.add(new Grade(subjects[i], savedGrades[i]));
            }
        } else {
            // domyslne wartosci przy pierwszym uruchomieniu ekranu
            for (int i=0; i < mGradesCount; i++) {
                mGradeList.add(new Grade(subjects[i], 2.0));
            }
        }

        // podpiecie adaptera
        binding.gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GradesAdapter adapter = new GradesAdapter(this, mGradeList);
        binding.gradesRecyclerView.setAdapter(adapter);

        //zbindowanie strzalki powortu
        binding.topAppBar.setNavigationOnClickListener(v -> {
            setResult(GradesActivity.RESULT_CANCELED);
            finish();
        });

        // obliczenia sredniej i przekazanie danych z powrotem do glownego ekranu;
        binding.calculateAverageButton.setOnClickListener(v -> {
            double sum = 0;
            for (Grade g : mGradeList) {
                sum += g.getGrade();
            }

            double average = sum/mGradeList.size();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("AVERAGE_RESULT", average);
            setResult(GradesActivity.RESULT_OK, resultIntent);
            finish();
        });

    }

    // zachowanie aktywnosci przed odwroceniem ekranu
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mGradeList != null) {
            double[] gradesArr = new double[mGradeList.size()];

            for (int i = 0; i < mGradeList.size(); i++) {
                gradesArr[i] = mGradeList.get(i).getGrade();
            }

            outState.putDoubleArray("SAVED_GRADES", gradesArr);
        }
    }
}