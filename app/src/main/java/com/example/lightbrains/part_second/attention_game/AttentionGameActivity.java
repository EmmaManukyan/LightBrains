package com.example.lightbrains.part_second.attention_game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.databinding.ActivityAttentionGameBinding;
import com.example.lightbrains.part_first_mental.flashanzan.ShowFlashCardsFragment;

public class AttentionGameActivity extends AppCompatActivity {
    ActivityAttentionGameBinding binding;

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
        if (AttentionGameShowFiguresFragment.backpressedlistener!=null){
            AttentionGameShowFiguresFragment.backpressedlistener.onBackPressed();
        } else if (AttentionGameWriteAnswersFragment.backpressedlistener!=null) {
            AttentionGameWriteAnswersFragment.backpressedlistener.onBackPressed();

        } else{
            super.onBackPressed();
        }
    }
    public void showRightAnimation(){
        new Thread(() -> {
            runOnUiThread(() -> {
                binding.imgAnim.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.ZoomIn).duration(1000).playOn(binding.imgAnim);

            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            runOnUiThread(() -> {
                YoYo.with(Techniques.ZoomOut).duration(1000).playOn(binding.imgAnim);
            });

        });
    }
}