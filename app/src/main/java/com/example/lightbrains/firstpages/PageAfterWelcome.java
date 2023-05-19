package com.example.lightbrains.firstpages;

import static com.example.lightbrains.common.Constants.sharedPreferences;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentPageAfterWelcomeBinding;

public class PageAfterWelcome extends Fragment {

    private Animation animBottom;
    private FragmentPageAfterWelcomeBinding binding;
    private int CHECKED_LANGUAGE = 0;
    private String[] languageArrayStrings;
    private ArrayAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPageAfterWelcomeBinding.inflate(inflater, container, false);
        init();

        binding.btnStart.setOnClickListener(view -> {
            Constants.createSound(requireActivity(),R.raw.sound_first_pages);
            Constants.mediaPlayer.start();
            Navigation.findNavController(view).navigate(R.id.action_pageAfterWelcome_to_signInFragment);
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        CHECKED_LANGUAGE = sharedPreferences.getInt(Constants.CHECKED_LANGUAGE, 0);


        if (CHECKED_LANGUAGE == 1) {
            binding.autoTvLanguages.setText(languageArrayStrings[CHECKED_LANGUAGE]);
        }

        binding.autoTvLanguages.setText(languageArrayStrings[CHECKED_LANGUAGE]);


        binding.autoTvLanguages.setOnItemClickListener((adapterView, view1, position, l) -> {

            Constants.createSound(requireActivity(),R.raw.spinner_sound);
            Constants.mediaPlayer.start();

            CHECKED_LANGUAGE = position;
            Constants.myEditShared.putInt(Constants.CHECKED_LANGUAGE, CHECKED_LANGUAGE);
            getActivity().finish();
            startActivity(getActivity().getIntent());
            Constants.myEditShared.commit();
        });
    }

    private void init() {
        binding.btnStart.setAnimation(animBottom);
        Constants.createSharedPreferences(getActivity());
        animBottom = AnimationUtils.loadAnimation(getContext(), R.anim.enter_anim_from_right);
        languageArrayStrings = getResources().getStringArray(R.array.string_languages_array);
        setLanguages();
    }

    @Override
    public void onResume() {
        super.onResume();
        setLanguages();
    }

    private void setLanguages() {
        adapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, languageArrayStrings);
        binding.autoTvLanguages.setAdapter(adapter);
    }
}