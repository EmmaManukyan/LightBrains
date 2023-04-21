package com.example.lightbrains.part_second.attention_game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentAttentionGameShowFiguresBinding;
import com.example.lightbrains.dialogs.CustomDialogFragmentForExit;
import com.example.lightbrains.interfaces.BackPressedListener;

import java.util.HashMap;
import java.util.Set;

public class AttentionGameShowFiguresFragment extends Fragment implements BackPressedListener {

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

    private HashMap<Integer, Integer> showedMap;


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
        binding.btnStart.setOnClickListener(view1 -> {

            if (!binding.btnStart.getText().equals(getResources().getString(R.string.finish))) {
                if (!runningThread) {
                    runningThread = true;
                    binding.btnStart.setText(getResources().getString(R.string.stop));
                } else {
                    runningThread = false;
                    binding.btnStart.setText(getResources().getString(R.string.restart));
                }
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HASHMAP_BUNDLE, showedMap);
                bundle.putInt(Constants.FIGURES_TYPE, figuresType);
                Log.d("TAG", "firs showedMap " + showedMap.toString());
                Navigation.findNavController(getView()).navigate(R.id.action_attentionGameShowFiguresFragment_to_attentionGameWriteAnswersFragment, bundle);
            }
        });
    }

    private void init() {
        getArgs();
        threadToShowFigures = new ThreadToShowFigures(FigureListCreator.createMapOfFigures(figuresType, figuresLevel, figuresCount));

    }

    private void getArgs() {
        Bundle bundle = getArguments();
        assert bundle != null;
        complexityLevel = bundle.getInt(Constants.FIGURES_COMPLEXITY_LEVEL);
        figuresType = bundle.getInt(Constants.FIGURES_TYPE);
        figuresLevel = bundle.getInt(Constants.FIGURES_LEVEL);
        figuresCount = bundle.getInt(Constants.FIGURES_COUNT);
        figuresGroupCount = bundle.getInt(Constants.FIGURES_GROUP_COUNT);
        showTime = (int) (bundle.getFloat(Constants.FIGURES_SHOW_TIME) * 1000);
        Toast.makeText(getContext(), "" + showTime, Toast.LENGTH_SHORT).show();


        if (complexityLevel == 0) {
            figuresType = Constants.getRandomInRange(0, FigureListCreator.figureTypes.length - 1);
            figuresLevel = 3;
            showTime = 900;
            figuresCount = 7;
        } else if (complexityLevel == 1) {
            figuresType = Constants.getRandomInRange(0, FigureListCreator.figureTypes.length - 1);
            figuresLevel = 5;
            showTime = 800;
            figuresCount = 10;

        } else if (complexityLevel == 2) {
            figuresType = Constants.getRandomInRange(0, FigureListCreator.figureTypes.length - 1);
            figuresLevel = 10;
            showTime = 700;
            figuresCount = 14;
        }
        Log.d("TAG", "compLevel: " + complexityLevel);
        Log.d("TAG", "figType: " + figuresType);
        Log.d("TAG", "figLevel: " + figuresLevel);
        Log.d("TAG", "figCount: " + figuresCount);
        Log.d("TAG", "time: " + showTime);


    }


    class ThreadToShowFigures extends Thread {
        HashMap<Integer, Integer> showThisMap;

        public ThreadToShowFigures(HashMap<Integer, Integer> showThisMap) {
            this.showThisMap = showThisMap;
        }


        private int getIndexOfArrToShow(HashMap<Integer, Integer> m) {
            Set<Integer> defaultKeys = m.keySet();
            Integer[] keys = defaultKeys.toArray(new Integer[defaultKeys.size()]);

            int figIndex = 0;
            int key;
            figIndex = Constants.getRandomInRange(0, keys.length - 1);
            while (m.get(keys[figIndex]) < 1) {
                figIndex = Constants.getRandomInRange(0, keys.length - 1);
            }
            key = keys[figIndex];
            return key;
        }

        private boolean funcOnUi(HashMap<Integer, Integer> showThisMap) {
            for (int j = 0; j < figuresCount; j++) {
                if (runningThread) {
                    int finalI = j;
                    getActivity().runOnUiThread(() -> {
                        int key = getIndexOfArrToShow(showThisMap);
                        int temp = showThisMap.get(key) - 1;
                        showThisMap.put(key, temp);
                        Log.d("TAG", showThisMap.toString());
                        binding.imgFigure.setImageResource(FigureListCreator.figureTypes[figuresType][key]);
                        YoYo.with(Techniques.FadeIn).duration(showTime).playOn(binding.imgFigure);
                        if (finalI == FigureListCreator.figureTypes[1].length) {
                            binding.btnStart.setText(getResources().getString(R.string.finish));
                        }

                    });


                    try {
                        Thread.sleep(showTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void run() {
            while (true) {
                if (runningThread) {
                    showThisMap = FigureListCreator.createMapOfFigures(figuresType, figuresLevel, figuresCount);
                    showedMap = (HashMap<Integer, Integer>) showThisMap.clone();
                    //Log.d("TAG", "showed map============================" + showedMap.toString());

                    if (funcOnUi(showThisMap)) {
                        runningThread = false;
                        getActivity().runOnUiThread(() -> binding.btnStart.setText(getResources().getString(R.string.finish)));
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        runningThread = false;
        if (threadToShowFigures != null) {
            threadToShowFigures.interrupt();
        }
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
        runningThread = false;
        binding.btnStart.setText(getResources().getString(R.string.restart));
        showDialog();
    }

    private void showDialog() {
        CustomDialogFragmentForExit customDialogFragmentForExit = new CustomDialogFragmentForExit(3);
        customDialogFragmentForExit.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_TAG_EXIT);
    }
}