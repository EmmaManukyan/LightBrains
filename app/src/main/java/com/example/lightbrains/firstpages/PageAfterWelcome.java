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
import android.widget.Button;
import android.widget.Switch;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;

public class PageAfterWelcome extends Fragment {

    private Button btnStart;
    private Switch btnSwitch;
    private Animation animBottom;
    private int CHECKED_LANGUAGE = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_after_welcome, container, false);
        init(view);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_pageAfterWelcome_to_signInFragment);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CHECKED_LANGUAGE = sharedPreferences.getInt(Constants.CHECKED_LANGUAGE, 0);
        if (CHECKED_LANGUAGE == 1) {
            btnSwitch.setChecked(true);
        }
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnSwitch.isChecked()) {
                    CHECKED_LANGUAGE = 1;
                    Constants.myEditShared.putInt(Constants.CHECKED_LANGUAGE, CHECKED_LANGUAGE);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                } else {
                    CHECKED_LANGUAGE = 0;
                    Constants.myEditShared.putInt(Constants.CHECKED_LANGUAGE, CHECKED_LANGUAGE);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
                Constants.myEditShared.commit();
            }
        });
    }

    private void init(View view) {
        btnStart = view.findViewById(R.id.btn_start);
        btnSwitch = view.findViewById(R.id.language_switch);
        btnStart.setAnimation(animBottom);


        Constants.createSharedPreferences(getActivity());

        animBottom = AnimationUtils.loadAnimation(getContext(), R.anim.enter_anim_from_right);
    }

}