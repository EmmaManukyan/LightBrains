package com.example.lightbrains.common;

import static com.example.lightbrains.common.ConstantsForFireBase.mAuth;
import static com.example.lightbrains.common.ConstantsForFireBase.myDataBase;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.lightbrains.databinding.FragmentShowResultsBinding;
import com.example.lightbrains.homepage.HomeActivity;
import com.example.lightbrains.interfaces.BackPressedListener;

import java.util.Objects;


public class ShowResultsFragment extends Fragment implements BackPressedListener {
    private FragmentShowResultsBinding binding;
    private double time;
    private int rightAnswers;
    private String timeToShow;
    private int scores;

    private boolean animationIsRunning = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

// Здесь мы получаем данные, переданные из предыдущей активности
        rightAnswers = requireArguments().getInt(Constants.RIGHT_ANSWERS);
        int count = requireArguments().getInt(Constants.COUNT_FLASH_CARDS);
        time = (double) requireArguments().getLong(Constants.FIGURES_SHOW_TIME) / 60000;
        scores = requireArguments().getInt(Constants.SCORES, -1);

        time = (float) (Math.round(time * 100.0) / 100.0);
        String tempTime = String.valueOf(time);
        timeToShow = tempTime.substring(0, 4);

// Вычисление процента выполнения...
        float percent = (rightAnswers * 100) / (float) count;

        if (percent >= 50) {
            binding.btnMainPage.setText(getResources().getString(R.string.great));
            Constants.createSound(requireActivity(), R.raw.results);
            Constants.makeSoundEffect();
        } else {
            binding.btnMainPage.setText(getResources().getString(R.string.go_back_main_page));

        }

        progressBarAnimation((int) percent);
        binding.tvLayTime.setError(" ");
        binding.tvLayScores.setError(" ");

        binding.btnMainPage.setOnClickListener(view1 -> {
            Constants.createSound(requireActivity(), R.raw.btn_click);
            Constants.makeSoundEffect();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            if (Constants.sharedPreferences.getBoolean(Constants.USE_INTERNET, true) && !ConstantsForFireBase.checkConnectionIsOff(requireActivity())) {
                myDataBase.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(Constants.SCORES).setValue(Constants.sharedPreferences.getInt(Constants.SCORES, -1000));
            }
            binding.myProgressbarResult.setProgress(0);
            requireActivity().finish();
        });
        binding.edtTime.setText("" + time + " " + getResources().getString(R.string.minutes));
        binding.edtScores.setText("" + rightAnswers + " " + getResources().getString(R.string.scores).toLowerCase());
    }

    @SuppressLint("SetTextI18n")
    private void progressBarAnimation(int progress) {
        //this animation is working in thread to keep UI always clickable
        new Thread(() -> {
            animationIsRunning = true;
            scores = (scores == -1) ? rightAnswers : scores;
            Constants.createSharedPreferences(requireActivity());
            int tempScores = Constants.sharedPreferences.getInt(Constants.SCORES, -1000);
            Constants.myEditShared.putInt(Constants.SCORES, tempScores + scores);
            Constants.myEditShared.commit();
            for (int i = 0; i <= progress; i += 1) {
                if (animationIsRunning) {
                    int finalI = i;
                    requireActivity().runOnUiThread(() -> {
                        binding.myProgressbarResult.setProgress(finalI);
                        binding.tvResultPercent.setText(finalI + "%");
                    });
                    try {
                        Thread.sleep(18);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    onDestroy();
                }
            }

            requireActivity().runOnUiThread(() -> {
                binding.tvLayTime.setVisibility(View.VISIBLE);
                binding.tvLayScores.setVisibility(View.VISIBLE);
                String html = "<font color=" + getResources().getColor(R.color.color_primary_variant)
                        + ">" + timeToShow + "</font><font color="
                        + getResources().getColor(R.color.color_secondary_variant) + "> " + getResources().getString(R.string.minutes) + "</font>";
                binding.edtTime.setText(Html.fromHtml(html));
                html = "<font color=" + getResources().getColor(R.color.color_secondary_variant)
                        + ">" + scores + "</font><font color="
                        + getResources().getColor(R.color.color_primary_variant) + "> " + getResources().getString(R.string.scores).toLowerCase() + "</font>";
                binding.edtScores.setText(Html.fromHtml(html));
                YoYo.with(Techniques.ZoomIn).duration(Constants.YOYO_ANIM_DURATION).playOn(binding.tvLayTime);
                YoYo.with(Techniques.ZoomIn).duration(Constants.YOYO_ANIM_DURATION).playOn(binding.tvLayScores);
            });

            animationIsRunning = false;

        }).start();
    }


    public static BackPressedListener backpressedlistener;

    @Override
    public void onPause() {
        backpressedlistener = null;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        backpressedlistener = this;
    }

    @Override
    public void onBackPressed() {
        if (animationIsRunning){
            return;
        }
    }
}