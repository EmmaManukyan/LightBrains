package com.example.lightbrains.part_second.memory_game;

import static com.example.lightbrains.part_second.attention_game.FigureListCreator.figureTypes;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMemoryGameShowCardsBinding.inflate(inflater, container, false);
//        Toast.makeText(getContext(), "Eka", Toast.LENGTH_SHORT).show();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
//        createTableOfImages(2,3);
//        Toast.makeText(getContext(), rows + " " + columns, Toast.LENGTH_SHORT).show();
        createTableOfImages(rows, columns);
        startTime = System.currentTimeMillis();

        binding.btnStartAgain.setOnClickListener(v -> {
            if (countOfPairs!=0){
                resources.clear();
                openedImages.clear();
                isFront = false;
                isInFlipAnimFunc = 0;
                binding.myGridlayout.removeAllViews();
                createTableOfImages(rows, columns);
                startTime = System.currentTimeMillis();
            }else{
                if (binding.btnStartAgain.getText().equals(getResources().getString(R.string.finish))){
                    long endTime = System.currentTimeMillis();
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constants.FIGURES_SHOW_TIME,endTime-startTime);
                    Navigation.findNavController(v).navigate(R.id.action_memoryGameShowCardsFragment_to_memoryGameResultsFragment,bundle);
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
            Log.d("taguhi", "" + i);
        }
        if (numOfRows * numOfColumns % 2 == 1) {
            resources.add(R.drawable.baseline_scores_24);
            countOfPairs++;
            Toast.makeText(getContext(), ""+countOfPairs, Toast.LENGTH_SHORT).show();
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
                    if (imageView.getContentDescription().equals(Constants.DEFAULT_CONTENT_DESCRIPTION) && isInFlipAnimFunc != 2) {
                        imageView.setContentDescription(Constants.CONTENT_DESCRIPTION_CLICKED);
                        isInFlipAnimFunc = ++isInFlipAnimFunc % 3;
                        flipCardToInitialState(imageView, imageIndex, true);
                    }
//                    Toast.makeText(MatchingGameActivity.this, "" + imageIndex, Toast.LENGTH_SHORT).show();
                });
                binding.myGridlayout.addView(imageView);
            }
        }
    }


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
                if (figureType==0 && isBack){
//                    Toast.makeText(getContext(), "mta", Toast.LENGTH_SHORT).show();
                    card.setPadding(32,32,32,32);
                }else{
                    card.setPadding(0,0,0,0);

                }
                if (isBack) {
                    card.setContentDescription(Integer.toString(resources.get(index % resources.size())));
                    openedImages.add(card);
                    card.setImageResource(resources.get(index));

                    Log.d("taguhi", "size: " + openedImages.size());
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
                    Log.d("taguhi", openedImages.get(0) + " " + openedImages.get(1).getDrawable().getConstantState());
                    if (openedImages.get(0).getContentDescription().equals(openedImages.get(1).getContentDescription())) {
//                        Toast.makeText(getContext(), "True", Toast.LENGTH_SHORT).show();
                        showRightAnimation(getResources().getString(R.string.your_answer_is_right));
                        countOfPairs--;

//                        Toast.makeText(getContext(), ""+countOfPairs, Toast.LENGTH_SHORT).show();
                        flipCardToInitialState(openedImages.get(0), -1, false);
                        flipCardToInitialState(openedImages.get(1), -1, false);
//                        Toast.makeText(MatchingGameActivity.this, "Eka", Toast.LENGTH_SHORT).show();
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
                    isInFlipAnimFunc = 0;

                } else if (openedImages.size() == 1 && card.getContentDescription().equals(Integer.toString(R.drawable.baseline_scores_24))&&isInFlipAnimFunc != 2) {
                    showRightAnimation(getResources().getString(R.string.bonus));
                    countOfPairs--;
                    requireActivity().runOnUiThread(() -> {
                        flipCardToInitialState(openedImages.get(0), -1, false);
                        YoYo.with(Techniques.ZoomOut).duration(Constants.YOYO_ANIM_DURATION).playOn(openedImages.get(0));
                        openedImages.clear();
                        isFront = false;
                        isInFlipAnimFunc = 0;
                    });
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
        rows = bundle.getInt(Constants.COUNT_OF_ROWS);
        columns = bundle.getInt(Constants.COUNT_OF_COLUMNS);
        figureType = bundle.getInt(Constants.FIGURES_TYPE) - 1;
// Convert the shuffled list back to an arr
    }


    private void showRightAnimation(String message) {

        new Thread(() -> {
            requireActivity().runOnUiThread(() -> {
                binding.btnStartAgain.setEnabled(false);
                binding.btnStartAgain.setClickable(false);
                if (countOfPairs == 0) {
//                    Toast.makeText(getContext(), "End", Toast.LENGTH_SHORT).show();
                    binding.btnStartAgain.setText(getResources().getString(R.string.finish));
//                    Navigation.findNavController(mainView).navigate(R.id.action_memoryGameShowCardsFragment_to_memoryGameResultsFragment);
                }
                Constants.makeSoundEffect();
                binding.tvRight.setVisibility(View.VISIBLE);
                binding.tvRight.setText(message);
                YoYo.with(Techniques.BounceInUp).duration(800).playOn(binding.tvRight);
                //  Toast.makeText(getContext(), "Excellent", Toast.LENGTH_SHORT).show();

            });
            try {
                Thread.sleep(1000);
                requireActivity().runOnUiThread(() -> {
                    YoYo.with(Techniques.ZoomOut).duration(200).playOn(binding.tvRight);
                    binding.btnStartAgain.setEnabled(true);
                    binding.btnStartAgain.setClickable(true);

                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
}