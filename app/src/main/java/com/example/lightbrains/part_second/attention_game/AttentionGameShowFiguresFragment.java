package com.example.lightbrains.part_second.attention_game;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentAttentionGameShowFiguresBinding;

import java.util.HashMap;
import java.util.Map;

public class AttentionGameShowFiguresFragment extends Fragment {

//    Bundle bundle;

    FragmentAttentionGameShowFiguresBinding binding;

    private int complexityLevel = 0;
    private int figuresType = -1;
    private int figuresLevel = -1;

    private int figuresCount = 3;
    private int figuresGroupCount = 0;
    private int showTime = 300;

    private ThreadToShowFigures threadToShowFigures;

    private boolean runningThread = false;

    private boolean runningRows = false;

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
        if (!threadToShowFigures.isAlive()) {
            threadToShowFigures.start();
        }
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!runningThread){
                    runningThread = true;
                }else{
                    runningThread = false;
                }
            }
        });
        YoYo.with(Techniques.FadeIn).duration(showTime).playOn(binding.imgFigure);
    }

    private void init() {
        getArgs();
        threadToShowFigures = new ThreadToShowFigures(FigureListCreator.createMapOfFigures(figuresType, figuresLevel, figuresCount));

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

        Log.d("TAG", "compLevel: " + complexityLevel);
        Log.d("TAG", "figType: " + figuresType);
        Log.d("TAG", "figLevel: " + figuresLevel);
        Log.d("TAG", "figCount: " + figuresCount);

        if (complexityLevel == 0) {

        } else if (complexityLevel == 1) {

        } else if (complexityLevel == 2) {

        }




    }


    class ThreadToShowFigures extends Thread {
        HashMap<Integer, Integer> showThisMap;

        public ThreadToShowFigures(HashMap<Integer, Integer> showThisMap) {
            this.showThisMap = showThisMap;
        }

        private boolean funcOnUi(){
            for (int j = 0; j < 10; j++) {
                if (runningThread){
                    int finalI = j;
                    getActivity().runOnUiThread(() ->
                            binding.btnStart.setText(finalI + ""));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    return false;
                }
            }
            return true;
        }

        @Override
        public void run() {
            for (int i = 0; i < figuresGroupCount; i++) {
                if (runningThread){
                    showThisMap = FigureListCreator.createMapOfFigures(figuresType, figuresLevel, figuresCount);
                    if (!funcOnUi()){
                        i--;
                    }
                }else{
                    i--;
                    continue;
                }

                Log.d("TAG","Thread name: "+i);
            }
        }
    }
}