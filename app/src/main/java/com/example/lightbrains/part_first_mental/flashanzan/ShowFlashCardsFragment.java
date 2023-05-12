package com.example.lightbrains.part_first_mental.flashanzan;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentShowFlashCardsBinding;
import com.example.lightbrains.dialogs.CustomDialogFragmentForExit;
import com.example.lightbrains.interfaces.BackPressedListener;
import com.example.lightbrains.part_second.attention_game.AttentionGameActivity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class ShowFlashCardsFragment extends Fragment implements BackPressedListener {

    private FragmentShowFlashCardsBinding binding;
    private ArrayList<Integer> myImageResources;
    private ArrayList<ImageView> myImages;
    private int count;
    private ShowFleshCardsThread thread;

    private int scores = 0;

    private Bundle bundle;

    private String result = "";

    private boolean running = false;
    private boolean isFirstTime = true;

    private int countNumbers;

    private long startTime = 0;
    private long endTime = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShowFlashCardsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        assert bundle != null;
        count = bundle.getInt(Constants.COUNT_FLASH_CARDS);
        int time = bundle.getInt(Constants.SPEED_FLASH_CARDS);
        int digits = bundle.getInt(Constants.DIGIT_FLASH_CARDS);

        countNumbers = count;

        init(view);


        thread = new ShowFleshCardsThread(count, time, digits);
        thread.start();
        binding.btnCheck.setOnClickListener(v -> {
            checkAnswer();
        });

        binding.btnStartFlashCards.setOnClickListener(v -> {
            if (binding.btnStartFlashCards.getText().toString().equals(getResources().getString(R.string.start))) {
                startTime = System.currentTimeMillis();
            } else if (binding.btnStartFlashCards.getText().toString().equals(getResources().getString(R.string.finish))) {
                endTime =System.currentTimeMillis();
            }
            if (!running) {
                if (binding.tvAnswerLayout.getVisibility() == View.GONE || isFirstTime) {
                    if (countNumbers > 0) {
                        running = true;
                        binding.imgSmile.setVisibility(View.GONE);
                        binding.tvWithSmile.setVisibility(View.GONE);
                        unVisible();
                        isFirstTime = false;

                    } else {
                        bundle.putInt(Constants.RIGHT_ANSWERS, scores);
                        bundle.putInt(Constants.COUNT_FLASH_CARDS, count);
                        bundle.putLong(Constants.FIGURES_SHOW_TIME,endTime-startTime);
                        Navigation.findNavController(view).navigate(R.id.action_showFlashCardsFragment_to_showResultsFragment, bundle);
                    }

                }
            } else {
                running = false;
            }
        });
    }

    private int getRandomInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max + 1 - min) + min;
    }

    public void init(View view) {
        bundle = new Bundle();


        myImageResources = new ArrayList<Integer>();
        myImageResources.add(R.drawable.img_abac_0);
        myImageResources.add(R.drawable.img_abac_1);
        myImageResources.add(R.drawable.img_abac_2);
        myImageResources.add(R.drawable.img_abac_3);
        myImageResources.add(R.drawable.img_abac_4);
        myImageResources.add(R.drawable.img_abac_5);
        myImageResources.add(R.drawable.img_abac_6);
        myImageResources.add(R.drawable.img_abac_7);
        myImageResources.add(R.drawable.img_abac_8);
        myImageResources.add(R.drawable.img_abac_9);
        myImages = new ArrayList<>();


        binding.edtAnswer.setImeOptions(EditorInfo.IME_ACTION_DONE);


        binding.edtAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    checkAnswer();

                }
                return false;
            }
        });


        myImages.add(binding.img1);
        myImages.add(binding.img2);
        myImages.add(binding.img3);
        myImages.add(binding.img4);
        myImages.add(binding.img5);
    }

    private void unVisible() {
        binding.img1.setVisibility(View.GONE);
        binding.img2.setVisibility(View.GONE);
        binding.img3.setVisibility(View.GONE);
        binding.img4.setVisibility(View.GONE);
        binding.img5.setVisibility(View.GONE);
        if (!isFirstTime) {
            binding.btnStartFlashCards.setText(getResources().getString(R.string.next));
        }
        if (countNumbers <= 0) {
            showResults();
        }
        running = false;
    }


    public void showResults() {
        binding.btnStartFlashCards.setText(getResources().getString(R.string.finish));
    }


    private void myFuncOnUI(int digits) {
        result = "";
        int random;
        if (digits == 2) {
            myImages.get(0).setVisibility(View.INVISIBLE);
            myImages.get(3).setVisibility(View.INVISIBLE);
            myImages.get(1).setVisibility(View.VISIBLE);
            myImages.get(2).setVisibility(View.VISIBLE);
            random = getRandomInRange(1, 9);
            myImages.get(1).setImageResource(myImageResources.get(random));
            result += random;
            random = (getRandomInRange(0, 9));
            myImages.get(2).setImageResource(myImageResources.get(random));
            result += random;


        } else {
            for (int i = 0; i < digits; i++) {
                ImageView tempImgView = myImages.get(i);
                myImages.get(i).setVisibility(View.VISIBLE);

                if (i == 0 && count != 1) {
                    random = getRandomInRange(1, 9);
                } else {
                    random = getRandomInRange(0, 9);
                }
                result += Integer.toString(random);
                tempImgView.setImageResource(myImageResources.get(random));
            }

        }
        if (running && result.length()>0){
            while (result.charAt(0) == '0') {
                result = result.substring(0, 0) + "" + result.substring(1);
            }
        }
        Toast.makeText(getContext(), "" + result, Toast.LENGTH_SHORT).show();

    }


    class ShowFleshCardsThread extends Thread {
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
                            binding.btnCheck.setVisibility(View.VISIBLE);
//                            YoYo.with(Techniques.ZoomIn).duration(800).playOn(binding.btnCheck);
                            binding.edtAnswer.setText("");
                            Constants.setEdtAnswerFocused(getActivity(), binding.edtAnswer);
                            unVisible();

                            binding.btnStartFlashCards.setVisibility(View.VISIBLE);
                            binding.btnStartFlashCards.setVisibility(View.GONE);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

            // Toast.makeText(getContext(), "You have finished", Toast.LENGTH_SHORT).show();
        }
    }


    private void answerIsWrong(String wrogAnsw, String result) {
        binding.tvWithSmile.setVisibility(View.VISIBLE);
        binding.tvWithSmile.setTextColor(getResources().getColor(R.color.is_wrong));
        binding.tvWithSmile.setText(getResources().getString(R.string.your_answer_is_wrong) + "\n" + wrogAnsw + " ≠ " + result);
        binding.tvWithSmile.setTextSize(32);
        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(binding.tvWithSmile);


        int d = result.length();
        if (d == 2) {
            myImages.get(1).setVisibility(View.VISIBLE);
            myImages.get(2).setVisibility(View.VISIBLE);
            myImages.get(0).setVisibility(View.INVISIBLE);
            myImages.get(3).setVisibility(View.INVISIBLE);
        } else if (d == 3) {
            myImages.get(0).setVisibility(View.INVISIBLE);
            myImages.get(4).setVisibility(View.INVISIBLE);
            myImages.get(3).setVisibility(View.VISIBLE);

            myImages.get(2).setVisibility(View.VISIBLE);
            myImages.get(1).setVisibility(View.VISIBLE);

            myImages.get(3).setImageResource(myImageResources.get(Integer.parseInt(String.valueOf(result.charAt(2)))));
            myImages.get(2).setImageResource(myImageResources.get(Integer.parseInt(String.valueOf(result.charAt(1)))));
            myImages.get(1).setImageResource(myImageResources.get(Integer.parseInt(String.valueOf(result.charAt(0)))));


        } else {
            for (int i = 0; i < d; i++) {
                myImages.get(i).setVisibility(View.VISIBLE);
            }

        }


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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (thread != null) {
            thread.interrupt();
        }
    }


    private void showDialog() {
        CustomDialogFragmentForExit customDialogFragmentForExit = new CustomDialogFragmentForExit(0);
        customDialogFragmentForExit.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_TAG_EXIT);
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
        showDialog();
    }

    private void checkAnswer(){
        if (!TextUtils.isEmpty(Objects.requireNonNull(binding.edtAnswer.getText()).toString())) {
            if (binding.edtAnswer.getText().toString().equals(result)) {
                answerIsRight();
                binding.tvAnswerLayout.setVisibility(View.GONE);
                binding.btnCheck.setVisibility(View.GONE);

            } else {
                answerIsWrong(binding.edtAnswer.getText().toString(), result);
            }
            binding.tvAnswerLayout.setVisibility(View.GONE);
            binding.btnCheck.setVisibility(View.GONE);
            binding.btnStartFlashCards.setVisibility(View.VISIBLE);
        } else {
            if (isFirstTime) {
                running = true;
                isFirstTime = false;
            }
        }
    }
}