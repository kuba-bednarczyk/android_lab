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

    // konstruktor adaptera
    public GradesAdapter(Context context, List<Grade> gradeList) {
        mInflater = LayoutInflater.from(context);
        this.mGradeList = gradeList;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GradeViewHolder(GradeRowBinding.inflate(mInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Grade currentGrade = mGradeList.get(position);

        holder.binding.subjectNameTextView.setText(currentGrade.getName());

        double g = currentGrade.getGrade();
        if (g == 2.0) holder.binding.gradesRadioGroup.check(R.id.radioButton2);
        else if (g == 3.0) holder.binding.gradesRadioGroup.check(R.id.radioButton3);
        else if (g == 3.5) holder.binding.gradesRadioGroup.check(R.id.radioButton35);
        else if (g == 4.0) holder.binding.gradesRadioGroup.check(R.id.radioButton4);
        else if (g == 4.5) holder.binding.gradesRadioGroup.check(R.id.radioButton45);
        else if (g == 5.0) holder.binding.gradesRadioGroup.check(R.id.radioButton5);
    }

    @Override
    public int getItemCount() {
        return mGradeList.size();
    }

    public class GradeViewHolder extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {
        public GradeRowBinding binding;

        public GradeViewHolder(GradeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.gradesRadioGroup.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Grade grade = mGradeList.get(position);

                // Zapisujemy nową ocenę wybraną przez użytkownika [cite: 131]
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