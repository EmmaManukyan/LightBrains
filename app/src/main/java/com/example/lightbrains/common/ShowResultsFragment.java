package com.example.lightbrains.common;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lightbrains.databinding.FragmentShowResultsBinding;


public class ShowResultsFragment extends Fragment {
    FragmentShowResultsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int percent = (getArguments().getInt(Constants.SCORES) * 100) / (getArguments().getInt(Constants.COUNT_FLASH_CARDS));

        progressBarAnimation(percent);
        binding.tvLayTime.setError(" ");
        binding.tvLayScores.setError(" ");
        binding.tvLayTime.setError(" ");
    }

    private void progressBarAnimation(int progress) {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i <= progress; i += 2) {
                    int finalI = i;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.myProgressbarResult.setProgress(finalI);
                            binding.tvResultPercent.setText(finalI + "%");
                        }
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }.start();
    }
}