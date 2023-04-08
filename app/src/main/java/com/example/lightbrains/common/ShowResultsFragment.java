package com.example.lightbrains.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.databinding.FragmentShowResultsBinding;
import com.example.lightbrains.homepage.HomeActivity;
import com.example.lightbrains.homepage.HomeFragment;
import com.example.lightbrains.interfaces.BackPressedListener;


public class ShowResultsFragment extends Fragment {
    private FragmentShowResultsBinding binding;
    private float time;
    private int scores;

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
        scores = getArguments().getInt(Constants.SCORES);
        int count = getArguments().getInt(Constants.COUNT_FLASH_CARDS);
        time = (float) getArguments().getLong(Constants.FIGURES_SHOW_TIME)/60000;
        time = (float) (Math.round(time * 100.0) / 100.0);
        Log.d("TAG",time+"");

        float percent = (scores*100)/(float)count;

        progressBarAnimation((int) percent);
        binding.tvLayTime.setError(" ");
        binding.tvLayScores.setError(" ");
        binding.tvLayTime.setError(" ");

        binding.btnMainPage.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        });
        binding.edtTime.setText(""+time +" "+getResources().getString(R.string.minutes));
        binding.edtScores.setText(""+scores +" "+getResources().getString(R.string.scores));
    }

    private void progressBarAnimation(int progress) {
        new Thread() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                for (int i = 0; i <= progress; i += 1) {
                    int finalI = i;
                    getActivity().runOnUiThread(() -> {
                        binding.myProgressbarResult.setProgress(finalI);
                        binding.tvResultPercent.setText(finalI + "%");
                    });
                    try {
                        Thread.sleep(18);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.tvLayTime.setVisibility(View.VISIBLE);
                        binding.tvLayScores.setVisibility(View.VISIBLE);
                        binding.edtTime.setText(""+time+" "+getResources().getString(R.string.minutes));
                        binding.edtScores.setText(""+scores+" "+getResources().getString(R.string.scores));
                        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(binding.tvLayTime);
                        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(binding.tvLayScores);
                    }
                });

            }
        }.start();
    }
}