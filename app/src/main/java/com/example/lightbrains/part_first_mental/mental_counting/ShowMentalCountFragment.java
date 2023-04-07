package com.example.lightbrains.part_first_mental.mental_counting;

import static com.example.lightbrains.common.Constants.animations;
import static com.example.lightbrains.common.Constants.rightAnswersRes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentShowMentalCountBinding;
import com.example.lightbrains.dialogs.CustomDialogFragmentForExit;
import com.example.lightbrains.interfaces.BackPressedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class ShowMentalCountFragment extends Fragment implements BackPressedListener {

    FragmentShowMentalCountBinding binding;

    private int speed = -1;
    private int digit = -1;
    private int countOfExamples = 1;

    private int countOfRows = 2;

    private int topicLevel = 0;
    private int subtopicLevel = 0;
    private ThreadShowNumbers threadShowNumbers;

    private Level level;

    private boolean runningThread = false;
    //0-> has runned

    private boolean uiThreadIsRunning = false;

    private boolean isFirstTime = true;

    private int countNumbers;
    private Bundle bundle;

    private int scores;

    private int[] numbersArrayToShow;
    private String result;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowMentalCountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();


        binding.btnStart.setOnClickListener(v -> {
            if (!runningThread) {
                if (binding.tvAnswerLayout.getVisibility() == View.GONE || isFirstTime) {
                    if (countNumbers > 0) {
                        // unVisible();
                        binding.imgSmile.setVisibility(View.GONE);
                        binding.tvWithSmile.setVisibility(View.GONE);
                        runningThread = true;
                        isFirstTime = false;

                    } else {
                        bundle.putInt(Constants.SCORES, scores);
                        bundle.putInt(Constants.COUNT_FLASH_CARDS, countOfExamples);
                        Navigation.findNavController(view).navigate(R.id.action_showMentalCountFragment_to_showResultsFragment2, bundle);
                    }

                }
            } else {
                pauseShowing();
            }
        });

    }




    private void showResults() {
        binding.btnStart.setText(getResources().getString(R.string.finish));
    }

    private void pauseShowing(){
        binding.btnStart.setText(getResources().getString(R.string.restart));
        runningThread = false;
    }


    class ThreadShowNumbers extends Thread {
        Level levelClass;


        public ThreadShowNumbers(Level levelClass) {
            this.levelClass = levelClass;
        }

        @Override
        public void run() {

            for (final int[] i = {0}; i[0] < countOfExamples; i[0]++) {
                numbersArrayToShow = levelClass.createArrayToCount(digit, countOfRows, subtopicLevel);
                if (runningThread) {
                    try {
                        Thread.sleep(500);
                        uiThreadIsRunning = true;

                        for (int j = 0; j < countOfRows; j++) {
                            if (runningThread) {
                                int finalJ = j;
                                getActivity().runOnUiThread(() -> {
                                    binding.btnStart.setText(getResources().getString(R.string.stop));
                                    binding.tvNumber.setVisibility(View.VISIBLE);
                                    binding.tvNumber.setTextSize(150 - 15 * digit);
                                    binding.tvNumber.setText(numbersArrayToShow[finalJ] + "");
                                });
                                try {
                                    Thread.sleep(speed);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                uiThreadIsRunning = false;
                                Thread.interrupted();
                            }
                        }
                        result = String.valueOf(Arrays.stream(numbersArrayToShow).sum());
                        Log.d("TAG", result + "");


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!uiThreadIsRunning) {
                        i[0]--;
                    } else {
                        countNumbers--;
                        runningThread = false;

                    }

                } else {
                    i[0]--;
                    continue;
                }

                if (uiThreadIsRunning) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            unVisible();
                            binding.tvAnswerLayout.setVisibility(View.VISIBLE);
                            binding.edtAnswer.setText("");
                            Constants.setEdtAnswerFocused(getActivity(), binding.edtAnswer);
                            binding.btnStart.setVisibility(View.VISIBLE);
                            binding.btnStart.setEnabled(false);
                        }
                    });
                }
            }
        }
    }


    private void unVisible() {

        binding.tvNumber.setVisibility(View.GONE);
        if (!isFirstTime && !uiThreadIsRunning) {
            binding.btnStart.setText(getResources().getString(R.string.next));
        }
        if (countNumbers <= 0) {
            showResults();
        }

        runningThread = false;

    }

    private void init() {
        getArgs();
        countNumbers = countOfExamples;
        level = null;
        if (topicLevel == 0) {
            level = new Pryamoy();
        } else if (topicLevel == 1) {
            level = new Mladshi();
        }
        threadShowNumbers = new ThreadShowNumbers(level);


        binding.edtAnswer.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                if (!TextUtils.isEmpty(binding.edtAnswer.getText().toString())) {
                    if (binding.edtAnswer.getText().toString().equals(result)) {
                        answerIsRight();
                        //Toast.makeText(getContext(), "Right", Toast.LENGTH_SHORT).show();
                        binding.tvAnswerLayout.setVisibility(View.GONE);

                    } else {
                        //Toast.makeText(getContext(), "Wrong", Toast.LENGTH_SHORT).show();
                        answerIsWrong(binding.edtAnswer.getText().toString(), result);
                    }
                    binding.tvAnswerLayout.setVisibility(View.GONE);
                    binding.btnStart.setEnabled(true);
                    if (countNumbers > 0) {
                        binding.btnStart.setText(getResources().getString(R.string.next));
                    } else {
                        showResults();
                    }
                } else {
                    if (isFirstTime) {
                        runningThread = true;
                        isFirstTime = false;
                    }
                }

            }
            return false;
        });

    }

    private void answerIsRight() {
        scores++;
        binding.imgSmile.setVisibility(View.VISIBLE);
        binding.tvWithSmile.setVisibility(View.VISIBLE);
        Random random = new Random();
        int r = random.nextInt(rightAnswersRes.length);
        binding.imgSmile.setImageResource(rightAnswersRes[r]);
        binding.tvWithSmile.setText(getResources().getString(R.string.your_answer_is_right));
        binding.tvWithSmile.setTextSize(40);
        binding.tvWithSmile.setTextColor(getResources().getColor(R.color.is_right));
        r = random.nextInt(animations.size());
        YoYo.with(animations.get(r)).duration(1000).playOn(binding.imgSmile);
        YoYo.with(Techniques.FlipInY).duration(1000).playOn(binding.tvWithSmile);
    }

    private void answerIsWrong(String wrogAnsw, String result) {
        binding.tvWithSmile.setVisibility(View.VISIBLE);
        binding.imgSmile.setVisibility(View.VISIBLE);
        binding.tvWithSmile.setTextColor(getResources().getColor(R.color.is_wrong));
        binding.imgSmile.setImageResource(R.drawable.img_smile_eye_smile);

        binding.tvWithSmile.setText(getResources().getString(R.string.your_answer_is_wrong) + "\n" + wrogAnsw + " ≠ " + result+"\n\n");

        binding.tvWithSmile.setTextSize(32);
        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(binding.tvWithSmile);
        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(binding.imgSmile);
    }



    private void getArgs() {
        bundle = getArguments();
        speed = bundle.getInt(Constants.SPEED_MENTAL);
        digit = bundle.getInt(Constants.DIGIT_MENTAL);
        countOfExamples = bundle.getInt(Constants.COUNT_OF_EXAMPLES_MENTAL);
        subtopicLevel = bundle.getInt(Constants.SUBTOPIC_LEVEL_MENTAL);
        topicLevel = bundle.getInt(Constants.TOPIC_LEVEL_MENTAL);
        countOfRows = bundle.getInt(Constants.COUNT_OF_ROWS_MENTAL);

        Log.d("TAG", "speed: " + speed);
        Log.d("TAG", "count: " + countOfExamples);
        Log.d("TAG", "rowcount: " + countOfRows);
        Log.d("TAG", "digit: " + digit);
        Log.d("TAG", "level: " + topicLevel);
        Log.d("TAG", "sublevel: " + subtopicLevel);


    }

    public void onDestroy() {
        super.onDestroy();
        runningThread = false;
        if (threadShowNumbers != null) {
            threadShowNumbers.interrupt();
        }
    }

    private void showDialog() {
        pauseShowing();
        CustomDialogFragmentForExit customDialogFragmentForExit = new CustomDialogFragmentForExit(1);
        customDialogFragmentForExit.show(getActivity().getSupportFragmentManager(), "exit dialog");

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
        if (!threadShowNumbers.isAlive()) {
            threadShowNumbers.start();
        }
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }


}