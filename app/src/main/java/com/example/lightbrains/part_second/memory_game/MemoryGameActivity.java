package com.example.lightbrains.part_second.memory_game;

import static com.example.lightbrains.part_second.attention_game.FigureListCreator.figureTypes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.ActivityMemoryGameBinding;
import com.example.lightbrains.part_second.attention_game.AttentionGameShowFiguresFragment;
import com.example.lightbrains.part_second.attention_game.AttentionGameWriteAnswersFragment;

import java.util.ArrayList;
import java.util.Collections;

public class MemoryGameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);
    }
    @Override
    public void onBackPressed() {
        if (MemoryGameShowCardsFragment.backpressedlistener != null) {
            MemoryGameShowCardsFragment.backpressedlistener.onBackPressed();
        } else if (MemoryGameShowCardsFragment.backpressedlistener != null) {
            MemoryGameShowCardsFragment.backpressedlistener.onBackPressed();

        } else {
            super.onBackPressed();
        }
    }
}