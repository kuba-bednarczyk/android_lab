package com.example.android_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_app.databinding.GradeRowBinding;

import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradeViewHolder> {

    private List<Grade> mGradeList;
    private LayoutInflater mInflater;

    // Konstruktor do przekazania adapterowi listy ocen
    public GradesAdapter(Context context, List<Grade> gradeList) {
        mInflater = LayoutInflater.from(context);
        this.mGradeList = gradeList;
    }

    // onCreateViewHolder - tworzy nowy pusty wiersz listy ocen, uruchamia się tyle razy ile moze byc widocznych wierszy na ekanie
    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradeViewHolder(GradeRowBinding.inflate(mInflater, parent, false));
    }


    // onBindViewHolder - wypelnianie danymi holdera
    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        //wyciagniecie przedmiotu z danej pozycji
        Grade currentGrade = mGradeList.get(position);

        // ustawienie nazwy przedmiotu
        holder.binding.subjectNameTextView.setText(currentGrade.getName());

        // ustawienie kliknietego przycisku z oceną
        double g = currentGrade.getGrade();
        if (g == 2.0) holder.binding.gradesRadioGroup.check(R.id.radioButton2);
        else if (g == 3.0) holder.binding.gradesRadioGroup.check(R.id.radioButton3);
        else if (g == 3.5) holder.binding.gradesRadioGroup.check(R.id.radioButton35);
        else if (g == 4.0) holder.binding.gradesRadioGroup.check(R.id.radioButton4);
        else if (g == 4.5) holder.binding.gradesRadioGroup.check(R.id.radioButton45);
        else if (g == 5.0) holder.binding.gradesRadioGroup.check(R.id.radioButton5);
    }

    // getItemCount - zwraca liczbe wierszy listy ocen do wygenerowania
    @Override
    public int getItemCount() {
        return mGradeList.size();
    }

    // pojedynczy wiersz na liscie
    public class GradeViewHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {
        public GradeRowBinding binding;

        public GradeViewHolder(GradeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.gradesRadioGroup.setOnCheckedChangeListener(this);
        }

        // wlasciwa zmiana zaznaczonej oceny
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int position = getBindingAdapterPosition();
            // sprawdzamy w ktorym wierszu uzytykownik kliknal
            if (position != RecyclerView.NO_POSITION) {
                Grade grade = mGradeList.get(position);

                // Zapisujemy nową ocenę wybraną przez użytkownika
                if (checkedId == R.id.radioButton2) grade.setGrade(2.0);
                else if (checkedId == R.id.radioButton3) grade.setGrade(3.0);
                else if (checkedId == R.id.radioButton35) grade.setGrade(3.5);
                else if (checkedId == R.id.radioButton4) grade.setGrade(4.0);
                else if (checkedId == R.id.radioButton45) grade.setGrade(4.5);
                else if (checkedId == R.id.radioButton5) grade.setGrade(5.0);
            }
        }
    }
}