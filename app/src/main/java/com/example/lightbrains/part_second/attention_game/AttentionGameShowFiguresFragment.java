package com.example.lightbrains.part_second.attention_game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentAttentionGameShowFiguresBinding;

public class AttentionGameShowFiguresFragment extends Fragment {

//    Bundle bundle;

    FragmentAttentionGameShowFiguresBinding binding;

    private int complexityLevel = 0;
    private int figuresType = -1;
    private int figuresLevel = -1;

    private int figuresCount = 0;
    private int figuresGroupCount = 0;
    private int showTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAttentionGameShowFiguresBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        YoYo.with(Techniques.FadeIn).duration(showTime).playOn(binding.imgFigure);
//binding.imgFigure.setVisibility(View.GONE);
    }

    private void init() {
        getArgs();
    }

    private void getArgs() {
        Bundle bundle = getArguments();
        complexityLevel = bundle.getInt(Constants.FIGURES_COMPLEXITY_LEVEL);
        figuresType = bundle.getInt(Constants.FIGURES_TYPE);
        figuresLevel = bundle.getInt(Constants.FIGURES_LEVEL);
        figuresCount = bundle.getInt(Constants.FIGURES_COUNT);
        figuresGroupCount = bundle.getInt(Constants.FIGURES_GROUP_COUNT);
        showTime = (int) (bundle.getFloat(Constants.FIGURES_SHOW_TIME) * 1000);
        Toast.makeText(getContext(), "" + showTime, Toast.LENGTH_SHORT).show();

        if (complexityLevel == 0) {

        } else if (complexityLevel == 1) {

        } else if (complexityLevel == 2) {

        }
    }


  /*  class ShowFigures extends Thread {
        int count;
        int time;
        int digits;

        ShowFleshCardsThread(int count, int time, int digits) {
            this.count = count;
            this.time = time;
            this.digits = digits;
        }

        @Override
        public void run() {
            for (int i = 0; i < count; i++) {
                if (running) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.btnStartFlashCards.setVisibility(View.GONE);
                            myFuncOnUI(digits);
                            countNumbers--;
                        }
                    });
                } else {
                    i--;
                    continue;
                }
                try {
                    Thread.sleep(time);
                    running = false;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.tvAnswerLayout.setVisibility(View.VISIBLE);
                            binding.edtAnswer.setText("");
                            setEdtAnswerFocused(binding.edtAnswer);
                            unVisible();

                            binding.btnStartFlashCards.setVisibility(View.VISIBLE);
                            binding.btnStartFlashCards.setEnabled(false);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

            // Toast.makeText(getContext(), "You have finished", Toast.LENGTH_SHORT).show();
        }
    }*/
}