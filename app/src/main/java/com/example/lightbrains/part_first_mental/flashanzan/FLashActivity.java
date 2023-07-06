package com.example.lightbrains.part_first_mental.flashanzan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lightbrains.R;
import com.example.lightbrains.common.ShowResultsFragment;

public class FLashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
    }

    @Override
    public void onBackPressed() {
        if (ShowFlashCardsFragment.backpressedlistener != null) {
            ShowFlashCardsFragment.backpressedlistener.onBackPressed();
        } else if (ShowResultsFragment.backpressedlistener != null) {
            ShowResultsFragment.backpressedlistener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}