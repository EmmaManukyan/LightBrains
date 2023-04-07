package com.example.lightbrains.part_first_mental.mental_counting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lightbrains.R;
import com.example.lightbrains.part_first_mental.flashanzan.ShowFlashCardsFragment;

public class MentalCountingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_counting);
    }

    @Override
    public void onBackPressed() {
        if (ShowMentalCountFragment.backpressedlistener!=null){
            ShowMentalCountFragment.backpressedlistener.onBackPressed();
        }else{
            super.onBackPressed();
        }
    }
}