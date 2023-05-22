package com.example.lightbrains.part_second.memory_game;

import static com.example.lightbrains.part_second.attention_game.FigureListCreator.figureTypes;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentMemoryGameSettingsBinding;
import com.example.lightbrains.databinding.FragmentMemoryGameShowCardsBinding;

import java.util.ArrayList;
import java.util.Collections;

public class MemoryGameShowCardsFragment extends Fragment {
    private FragmentMemoryGameShowCardsBinding binding;

    private ArrayList<ImageView> openedImages;
    private boolean isFront = false;
    private ArrayList<Integer> resources;

    private int num = 4;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMemoryGameShowCardsBinding.inflate(inflater, container, false);
        Toast.makeText(getContext(), "Eka", Toast.LENGTH_SHORT).show();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        createTableOfImages(num);
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    private void createTableOfImages(int numOfRowsAndColumns) {
        int[] imageResources = figureTypes[Constants.getRandomInRange(0,1)];
        int margin = 8;
        int start = Constants.getRandomInRange(0, imageResources.length - 1);


        for (int i = start; i < start + (numOfRowsAndColumns * numOfRowsAndColumns) / 2; i++) {
            resources.add(imageResources[i % imageResources.length]);
            resources.add(imageResources[i % imageResources.length]);
        }
        Collections.shuffle(resources);

        binding.myGridlayout.setRowCount(numOfRowsAndColumns);
        binding.myGridlayout.setColumnCount(numOfRowsAndColumns);
        for (int row = 0; row < numOfRowsAndColumns; row++) {
            for (int col = 0; col < numOfRowsAndColumns; col++) {
                ImageView imageView = new ImageView(getContext());
                int imageIndex = row * numOfRowsAndColumns + col;
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.img_profile_default));
                imageView.setContentDescription(Constants.DEFAULT_CONTENT_DESCRIPTION);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                params.width = 0;
                params.height = 0;
                params.setMargins(margin, 0, margin, 0);
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);

                imageView.setLayoutParams(params);
                imageView.setOnClickListener(v -> {
                    if (imageView.getContentDescription().equals(Constants.DEFAULT_CONTENT_DESCRIPTION)){
                        imageView.setContentDescription(Constants.CONTENT_DESCRIPTION_CLICKED);
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
                if (isBack) {

                    openedImages.add(card);
                    card.setImageResource(resources.get(index % resources.size()));
                    card.setContentDescription(Integer.toString(resources.get(index % resources.size())));

                    Log.d("taguhi","size: "+openedImages.size());
                }else{
                    card.setImageResource(R.drawable.img_profile_default);
                    card.setContentDescription(Constants.DEFAULT_CONTENT_DESCRIPTION);
                    openedImages.clear();
                }

                ObjectAnimator flipIn = ObjectAnimator.ofFloat(card, "rotationY", -90f, 0f);
                flipIn.setDuration(Constants.FLIP_CARD_ANIM_DURATION);
                flipIn.start();

                isFront = !isFront;
                if (openedImages.size()>1){
                    Log.d("taguhi",openedImages.get(0)+" "+openedImages.get(1).getDrawable().getConstantState());
                    if (openedImages.get(0).getContentDescription().equals(openedImages.get(1).getContentDescription())){
                        Toast.makeText(getContext(), "True", Toast.LENGTH_SHORT).show();
                        flipCardToInitialState(openedImages.get(0),-1,false);
                        flipCardToInitialState(openedImages.get(1),-1,false);
//                        Toast.makeText(MatchingGameActivity.this, "Eka", Toast.LENGTH_SHORT).show();
                        requireActivity().runOnUiThread(() -> {
                            YoYo.with(Techniques.ZoomOut).duration(Constants.YOYO_ANIM_DURATION).playOn(openedImages.get(0));
                            YoYo.with(Techniques.ZoomOut).duration(Constants.YOYO_ANIM_DURATION).playOn(openedImages.get(1));
                        });
                      /*  openedImages.get(0).setVisibility(View.GONE);
                        openedImages.get(1).setVisibility(View.GONE);*/

                    }
                    else{
                        flipCardToInitialState(openedImages.get(0),-1,false);
                        flipCardToInitialState(openedImages.get(1),-1,false);
                    }
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



    private void init(){
        resources = new ArrayList<>();
        openedImages = new ArrayList<>();
// Convert the shuffled list back to an arr
    }
}