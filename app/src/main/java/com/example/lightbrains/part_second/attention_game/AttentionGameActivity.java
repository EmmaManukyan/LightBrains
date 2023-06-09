package com.example.lightbrains.part_second.attention_game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.ShowResultsFragment;
import com.example.lightbrains.databinding.ActivityAttentionGameBinding;
import com.example.lightbrains.part_first_mental.flashanzan.ShowFlashCardsFragment;

public class AttentionGameActivity extends AppCompatActivity {
    public static ActivityAttentionGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttentionGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AttentionGameValues.setScores(0);
        AttentionGameValues.setRightAnswers(0);
    }


    @Override
    public void onBackPressed() {
        // При нажатии на кнопку "Назад" проверяем, есть ли у текущего фрагмента слушатель для обработки нажатия
        // Если есть, вызываем метод onBackPressed() у соответствующего фрагмента
        if (AttentionGameShowFiguresFragment.backpressedlistener != null) {
            AttentionGameShowFiguresFragment.backpressedlistener.onBackPressed();
        } else if (AttentionGameWriteAnswersFragment.backpressedlistener != null) {
            AttentionGameWriteAnswersFragment.backpressedlistener.onBackPressed();
        } else if (ShowResultsFragment.backpressedlistener != null) {
            ShowResultsFragment.backpressedlistener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }


}