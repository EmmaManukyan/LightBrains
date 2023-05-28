package com.example.lightbrains.part_second.memory_game;

import static com.example.lightbrains.common.ConstantsForFireBase.mAuth;
import static com.example.lightbrains.common.ConstantsForFireBase.myDataBase;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentMemoryGameResultsBinding;
import com.example.lightbrains.homepage.HomeActivity;

import java.util.Objects;

public class MemoryGameResultsFragment extends Fragment {
    private FragmentMemoryGameResultsBinding binding;
    private double time;
    private String timeToShow;
    private int scores;
    private int countOfSteps = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMemoryGameResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("hello","mt1a");

        Bundle bundle = getArguments();
        assert bundle != null;
        time = (double) bundle.getLong(Constants.FIGURES_SHOW_TIME) / 60000;
        countOfSteps = bundle.getInt(Constants.COUNT_OF_STEPS);
        scores = bundle.getInt(Constants.SCORES);

        time = (float) (Math.round(time * 100.0) / 100.0);
        String tempTime = String.valueOf(time);
        timeToShow = tempTime.substring(0, 4);

//        Toast.makeText(getContext(), ""+time, Toast.LENGTH_SHORT).show();
        Constants.createSound(requireActivity(),R.raw.results);
        Constants.makeSoundEffect();
        setViews();

        binding.btnMainPage.setOnClickListener(view1 -> {
            Constants.createSound(requireActivity(), R.raw.btn_click);
            Constants.makeSoundEffect();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            if (Constants.sharedPreferences.getBoolean(Constants.USE_INTERNET, true) && !ConstantsForFireBase.checkConnectionIsOff(requireActivity())) {
                myDataBase.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child(Constants.SCORES).setValue(Constants.sharedPreferences.getInt(Constants.SCORES, -1000));
            }
            requireActivity().finish();
        });
    }

    private void setViews(){
        binding.tvLayTime.setVisibility(View.VISIBLE);
        binding.tvLayCountOfSteps.setVisibility(View.VISIBLE);
        binding.tvLayTime.setError(" ");
        binding.tvLayCountOfSteps.setError(" ");
        binding.edtCountOfSteps.setText(""+countOfSteps);
        binding.edtTime.setText(timeToShow);
        binding.tvScores.setText("+"+scores);
        Constants.createSharedPreferences(requireActivity());
        int tempScores = Constants.sharedPreferences.getInt(Constants.SCORES, -1000);
        Constants.myEditShared.putInt(Constants.SCORES, tempScores + scores);
        Constants.myEditShared.commit();
    }

}