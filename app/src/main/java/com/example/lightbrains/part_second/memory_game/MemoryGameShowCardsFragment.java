package com.example.lightbrains.part_second.memory_game;

import static com.example.lightbrains.common.Constants.animations;
import static com.example.lightbrains.common.Constants.rightAnswersRes;
import static com.example.lightbrains.part_second.attention_game.FigureListCreator.figureTypes;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentMemoryGameShowCardsBinding;
import com.example.lightbrains.dialogs.CustomDialogFragmentForExit;
import com.example.lightbrains.interfaces.BackPressedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MemoryGameShowCardsFragment extends Fragment implements BackPressedListener {
    private FragmentMemoryGameShowCardsBinding binding;

    private ArrayList<ImageView> openedImages;
    private boolean isFront = false;
    private ArrayList<Integer> resources;
    private int isInFlipAnimFunc = 0;

    private int rows;
    private int columns;
    private int figureType;

    private int countOfPairs;

    private long startTime;
    private int countOfSteps = 0;
    private boolean animationRunning = false;

    private int complexityOrder;
    private int maxParam;

    private boolean timerThreadRunning = false;
    private final Object lock = new Object();

    private boolean gameIsOver = false;

    private ThreadShowTimerAnimation threadShowTimerAnimation;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMemoryGameShowCardsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        if (complexityOrder == 0 && !threadShowTimerAnimation.isAlive()) {
            threadShowTimerAnimation.start();
        }
        createTableOfImages(rows, columns);
        startTime = System.currentTimeMillis();

        binding.btnStartAgain.setOnClickListener(v -> {
            Constants.createSound(requireActivity(), R.raw.right);
            if (countOfPairs != 0) {
                resources.clear();
                openedImages.clear();
                isFront = false;
                isInFlipAnimFunc = 0;
                binding.myGridlayout.removeAllViews();
                createTableOfImages(rows, columns);
                startTime = System.currentTimeMillis();
                if (complexityOrder != -1) {
                    binding.progressbarTime.setVisibility(View.VISIBLE);
                    binding.imgTime.setVisibility(View.VISIBLE);
                    binding.myGridlayout.setVisibility(View.VISIBLE);
                    binding.includedLayout.getRoot().setVisibility(View.GONE);
                    binding.progressbarTime.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_500)));

                    if (complexityOrder == 0) {
                        binding.progressbarTime.setProgress(maxParam * 1000);
                        if (timerThreadRunning) {
                            timerThreadRunning = false;
                            threadShowTimerAnimation.setI(maxParam * 1000);
                        } else {
                            threadShowTimerAnimation = new ThreadShowTimerAnimation(maxParam * 1000);
                            threadShowTimerAnimation.start();
                        }
                    } else {
                        binding.progressbarTime.setProgress(maxParam);
                    }
                    binding.btnStartAgain.setText(getResources().getString(R.string.restart));
                }
            } else {
                if (binding.btnStartAgain.getText().equals(getResources().getString(R.string.finish)) && isInFlipAnimFunc != 2) {
                    long endTime = System.currentTimeMillis();
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constants.FIGURES_SHOW_TIME, endTime - startTime);
                    bundle.putInt(Constants.COUNT_OF_STEPS, countOfSteps);
                    bundle.putInt(Constants.SCORES, giveScores(rows, columns, countOfSteps));
                    Navigation.findNavController(v).navigate(R.id.action_memoryGameShowCardsFragment_to_memoryGameResultsFragment, bundle);
                }
            }
        });
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void createTableOfImages(int numOfRows, int numOfColumns) {
        countOfPairs = (int) (numOfColumns * numOfRows / 2);

        int[] imageResources;
        if (figureType == -1) {
            figureType = Constants.getRandomInRange(0, 2);
        }
        imageResources = figureTypes[figureType];
        int margin = 8;
        int start = Constants.getRandomInRange(0, imageResources.length - 1);


        for (int i = start; i < start + (numOfRows * numOfColumns) / 2; i++) {
            resources.add(imageResources[i % imageResources.length]);
            resources.add(imageResources[i % imageResources.length]);
        }
        if (numOfRows * numOfColumns % 2 == 1) {
            resources.add(R.drawable.baseline_scores_24);
            countOfPairs++;
        }
        Collections.shuffle(resources);

        binding.myGridlayout.setRowCount(numOfRows);
        binding.myGridlayout.setColumnCount(numOfColumns);

        for (int row = 0; row < numOfRows; row++) {
            for (int col = 0; col < numOfColumns; col++) {
                ImageView imageView = new ImageView(getContext());
                int imageIndex = row * numOfColumns + col;
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.img_profile_default));
                imageView.setContentDescription(Constants.DEFAULT_CONTENT_DESCRIPTION);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.setMargins(margin, margin / 2, margin, margin / 2);
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

                imageView.setLayoutParams(params);
                imageView.setOnClickListener(v -> {
                    timerThreadRunning = true;
                    if (imageView.getContentDescription().equals(Constants.DEFAULT_CONTENT_DESCRIPTION) && isInFlipAnimFunc != 2) {
                        imageView.setContentDescription(Constants.CONTENT_DESCRIPTION_CLICKED);
                        isInFlipAnimFunc = ++isInFlipAnimFunc % 3;
                        flipCardToInitialState(imageView, imageIndex, true);
                    }
                });
                binding.myGridlayout.addView(imageView);
            }
        }
    }


    //этот метод используется для анимации переворота карточки
    public void flipCardToInitialState(final View view, int index, boolean isBack) {
        final ImageView card = (ImageView) view;

        ObjectAnimator flipOut = ObjectAnimator.ofFloat(card, "rotationY", 0f, 90f);
        flipOut.setDuration(Constants.FLIP_CARD_ANIM_DURATION);

        flipOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                card.setRotationY(-90f);
                if (figureType == 0 && isBack) {
                    card.setPadding(32, 32, 32, 32);
                } else if (isBack) {
                    int padding = (int) (1.0 / rows * 32);
                    card.setPadding(padding, padding, padding, padding);
                } else {
                    card.setPadding(0, 0, 0, 0);

                }
                if (isBack) {
                    card.setContentDescription(Integer.toString(resources.get(index % resources.size())));
                    openedImages.add(card);
                    card.setImageResource(resources.get(index));

                } else {
                    card.setImageResource(R.drawable.img_profile_default);
                    card.setContentDescription(Constants.DEFAULT_CONTENT_DESCRIPTION);
                    openedImages.clear();
                }

                ObjectAnimator flipIn = ObjectAnimator.ofFloat(card, "rotationY", -90f, 0f);
                flipIn.setDuration(Constants.FLIP_CARD_ANIM_DURATION);
                flipIn.start();

                isFront = !isFront;
                if (openedImages.size() > 1) {
                    if (openedImages.get(0).getContentDescription().equals(openedImages.get(1).getContentDescription())) {
                        showRightAnimation(getResources().getString(R.string.your_answer_is_right));
                        countOfPairs--;

                        flipCardToInitialState(openedImages.get(0), -1, false);
                        flipCardToInitialState(openedImages.get(1), -1, false);
                        requireActivity().runOnUiThread(() -> {
                            YoYo.with(Techniques.ZoomOut).duration(Constants.YOYO_ANIM_DURATION).playOn(openedImages.get(0));
                            YoYo.with(Techniques.ZoomOut).duration(Constants.YOYO_ANIM_DURATION).playOn(openedImages.get(1));
                            openedImages.get(0).setClickable(false);
                            openedImages.get(1).setClickable(false);
                        });

                    } else {
                        flipCardToInitialState(openedImages.get(0), -1, false);
                        flipCardToInitialState(openedImages.get(1), -1, false);
                    }
                    countOfSteps++;
                    setProgressbarProgress();
                    isInFlipAnimFunc = 0;

                } else if (openedImages.size() == 1 && card.getContentDescription().equals(Integer.toString(R.drawable.baseline_scores_24)) && isInFlipAnimFunc != 2) {
                    showRightAnimation(getResources().getString(R.string.bonus));
                    requireActivity().runOnUiThread(() -> {
                        flipCardToInitialState(openedImages.get(0), -1, false);
                        YoYo.with(Techniques.ZoomOut).duration(Constants.YOYO_ANIM_DURATION).playOn(openedImages.get(0));
                        openedImages.clear();
                        isFront = false;
                        isInFlipAnimFunc = 0;
                    });
                    countOfPairs--;

                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

        });

        flipOut.start();
    }

    private void init() {
        resources = new ArrayList<>();
        openedImages = new ArrayList<>();
        Bundle bundle = getArguments();
        assert bundle != null;
        rows = bundle.getInt(Constants.COUNT_OF_ROWS);
        columns = bundle.getInt(Constants.COUNT_OF_COLUMNS);
        figureType = bundle.getInt(Constants.FIGURES_TYPE) - 1;
        complexityOrder = bundle.getInt(Constants.COMPLEXITY_ORDER);
        if (complexityOrder != -1) {
            maxParam = bundle.getInt(Constants.MAX_PARAMETER);
            if (complexityOrder == 0) {
                threadShowTimerAnimation = new ThreadShowTimerAnimation(maxParam * 1000);
                binding.progressbarTime.setMax(maxParam * 1000);
                binding.progressbarTime.setProgress(maxParam * 1000);
            } else {
                binding.progressbarTime.setMax(maxParam);
                binding.progressbarTime.setProgress(maxParam);
            }
        } else {
            binding.progressbarTime.setVisibility(View.GONE);
            binding.imgTime.setVisibility(View.GONE);
        }
    }

    private void showRightAnimation(String message) {
        new Thread(() -> {

            synchronized (lock) {
                animationRunning = true;

                requireActivity().runOnUiThread(() -> {
                    binding.btnStartAgain.setEnabled(false);
                    binding.btnStartAgain.setClickable(false);
                    if (countOfPairs == 0) {
                        gameIsOver();
                    }
                    Constants.makeSoundEffect();
                    binding.tvRight.setVisibility(View.VISIBLE);
                    binding.tvRight.setText(message);
                    YoYo.with(Techniques.BounceInUp).duration(800).playOn(binding.tvRight);

                });
                try {
                    Thread.sleep(1000);
                    requireActivity().runOnUiThread(() -> {
                        animationRunning = false;
                        if (countOfPairs != 0) {
                            YoYo.with(Techniques.ZoomOut).duration(300).playOn(binding.tvRight);
                        } else {
                            binding.tvRight.setVisibility(View.GONE);
                        }
                        binding.btnStartAgain.setEnabled(true);
                        binding.btnStartAgain.setClickable(true);

                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

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
        showDialog();
    }

    private void showDialog() {
        CustomDialogFragmentForExit customDialogFragmentForExit = new CustomDialogFragmentForExit(6);
        customDialogFragmentForExit.show(requireActivity().getSupportFragmentManager(), Constants.DIALOG_TAG_EXIT);
    }

    private int giveScores(int countOfRows, int countOfColumns, int countOfSteps) {
        int maxScores = (int) (countOfColumns * countOfRows * 1.5);
        int scores = Math.max(maxScores - countOfSteps, (countOfRows + countOfColumns) / 4);
        return scores;
    }


    class ThreadShowTimerAnimation extends Thread {
        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        private int time;
        private int i;


        public void setI(int i) {
            this.i = i;
        }

        public ThreadShowTimerAnimation(int time) {
            this.time = time;
            i = time;

        }

        @Override
        public void run() {
            while (i > 0) {
                if (timerThreadRunning) {
                    try {
                        Thread.sleep(time / 50);
                        int finalI = i;
                        requireActivity().runOnUiThread(() -> {
                            if (finalI < time / 5) {
                                binding.progressbarTime.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                                YoYo.with(Techniques.Bounce).duration(500).playOn(binding.imgTime);
                            }
                            binding.progressbarTime.setProgress(finalI);
                        });
                    } catch (InterruptedException e) {
                        Log.d("TAG", e.getMessage());
                    }
                    i -= 50;
                }
            }

            if (countOfPairs != 0) {
                requireActivity().runOnUiThread(() -> {
                    binding.myGridlayout.setVisibility(View.GONE);
                    gameIsOver(getResources().getString(R.string.time_is_over));
                });
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerThreadRunning = false;
        if (threadShowTimerAnimation != null) {
            threadShowTimerAnimation.interrupt();
        }
    }

    private void gameIsOver() {
        gameIsOver = true;
        isInFlipAnimFunc = 0;
        binding.btnStartAgain.setText(getResources().getString(R.string.finish));
        binding.includedLayout.imgSmile.setVisibility(View.VISIBLE);
        binding.includedLayout.getRoot().setVisibility(View.VISIBLE);
        binding.includedLayout.tvWithSmile.setText(getResources().getString(R.string.you_have_finished));
        binding.includedLayout.tvWithSmile.setVisibility(View.VISIBLE);
        binding.includedLayout.tvWithSmile.setTextColor(getResources().getColor(R.color.color_primary));
        Random random = new Random();
        int r = random.nextInt(rightAnswersRes.length);
        binding.includedLayout.imgSmile.setImageResource(rightAnswersRes[r]);
        r = random.nextInt(animations.size());
        //animation of smiles
        YoYo.with(animations.get(r)).duration(1000).playOn(binding.includedLayout.imgSmile);
        YoYo.with(Techniques.FlipInY).duration(1000).playOn(binding.includedLayout.tvWithSmile);

        binding.progressbarTime.setVisibility(View.GONE);
        binding.imgTime.setVisibility(View.GONE);
        Constants.createSound(requireActivity(), R.raw.right);
        Constants.makeSoundEffect();

        timerThreadRunning = false;

    }


    private void gameIsOver(String message) {
        gameIsOver = true;
        isInFlipAnimFunc = 0;
        binding.btnStartAgain.setText(getResources().getString(R.string.try_again));
        binding.myGridlayout.setVisibility(View.GONE);
        binding.includedLayout.getRoot().setVisibility(View.VISIBLE);
        binding.includedLayout.imgSmile.setVisibility(View.VISIBLE);
        binding.includedLayout.tvWithSmile.setText(message);
        binding.includedLayout.tvWithSmile.setVisibility(View.VISIBLE);
        binding.includedLayout.tvWithSmile.setTextColor(getResources().getColor(R.color.color_primary));
        int r = new Random().nextInt(animations.size());
        binding.includedLayout.imgSmile.setImageResource(R.drawable.star_sad_1);

        //animation of smiles
        YoYo.with(animations.get(r)).duration(1000).playOn(binding.includedLayout.imgSmile);
        YoYo.with(Techniques.FlipInY).duration(1000).playOn(binding.includedLayout.tvWithSmile);

        binding.progressbarTime.setVisibility(View.GONE);
        binding.imgTime.setVisibility(View.GONE);
        Constants.createSound(requireActivity(), R.raw.wrong);
        Constants.makeSoundEffect();

        if (threadShowTimerAnimation != null) {
            timerThreadRunning = false;
            threadShowTimerAnimation.interrupt();
        }
    }

    private void setProgressbarProgress() {
        if (binding.progressbarTime.getProgress() - 1 > binding.progressbarTime.getMin()) {
            binding.progressbarTime.setProgress(binding.progressbarTime.getProgress() - 1);

        } else {
            if (countOfPairs != 0) {
                binding.progressbarTime.setProgress(binding.progressbarTime.getProgress() - 1);
                gameIsOver(getResources().getString(R.string.time_is_over));
            } else {
                gameIsOver();
            }
        }
    }

}