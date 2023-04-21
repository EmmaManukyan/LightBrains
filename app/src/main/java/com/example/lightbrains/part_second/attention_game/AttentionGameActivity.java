package com.example.lightbrains.part_second.attention_game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lightbrains.R;
import com.example.lightbrains.part_first_mental.flashanzan.ShowFlashCardsFragment;

public class AttentionGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_game);
    }

    @Override
    public void onBackPressed() {
        if (AttentionGameShowFiguresFragment.backpressedlistener!=null){
            AttentionGameShowFiguresFragment.backpressedlistener.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }
}