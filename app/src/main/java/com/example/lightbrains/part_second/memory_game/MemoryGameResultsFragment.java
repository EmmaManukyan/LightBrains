package com.example.lightbrains.part_second.memory_game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentMemoryGameResultsBinding;

public class MemoryGameResultsFragment extends Fragment {
    private FragmentMemoryGameResultsBinding binding;
    private double time;
    private String timeToShow;
    private int scores;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMemoryGameResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("hello","mt1a");
        time = (double) requireArguments().getLong(Constants.FIGURES_SHOW_TIME) / 60000;
        scores = 12;
        binding.tvLayTime.setVisibility(View.VISIBLE);
        binding.tvLayCountOfSteps.setVisibility(View.VISIBLE);
        binding.tvLayTime.setError(" ");
        binding.tvLayCountOfSteps.setError(" ");
        Constants.createSound(requireActivity(),R.raw.results);
        Constants.makeSoundEffect();
    }

}