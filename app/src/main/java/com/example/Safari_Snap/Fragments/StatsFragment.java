package com.example.Safari_Snap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.Safari_Snap.R;


public class StatsFragment extends Fragment {

    int show_total;

    Button button;
    TextView correct, incorrect, total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        button = view.findViewById(R.id.reset_btn);
        correct = view.findViewById(R.id.correct_id);
        incorrect = view.findViewById(R.id.incorrect_id);
        total = view.findViewById(R.id.total_id);


        display(ResultFragment.correct, ResultFragment.incorrect);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResultFragment.correct = 0;
                ResultFragment.incorrect = 0;

                display(ResultFragment.correct, ResultFragment.incorrect);
            }
        });


        return view;
    }

    //this function updates the text view when called
    private void display(int show_correct, int show_incorrect) {
        show_total = show_correct + show_incorrect;

        correct.setText("" + show_correct);
        incorrect.setText("" + show_incorrect);
        total.setText("" + show_total);
    }

}