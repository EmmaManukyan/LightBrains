package com.example.lightbrains.part_first_mental.mental_counting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lightbrains.R;
import com.example.lightbrains.databinding.FragmentMentalCountingPartWorkingPageBinding;

public class MentalCountingPartWorkingPageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mental_counting_part_working_page, container, false);
    }
}