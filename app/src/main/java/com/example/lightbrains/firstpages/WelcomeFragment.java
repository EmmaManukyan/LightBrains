package com.example.lightbrains.firstpages;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.homepage.HomeActivity;


public class WelcomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView btnNext = view.findViewById(R.id.img_btn_next);
        TextView tvWelcome = view.findViewById(R.id.tv_welcome);
        ConstraintLayout layout = view.findViewById(R.id.my_constraint_welcome);
        Animation animBottom = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_animation);
        btnNext.setAnimation(animBottom);
        tvWelcome.setAnimation(animBottom);
        layout.setAnimation(animBottom);
        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);

        if (sh.getBoolean(Constants.IS_LOGIN,false)){
            new Thread(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("hhh",currentThread().getName());
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }
            }.start();
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);

                if (sh.getBoolean(Constants.IS_LOGIN,false)){
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_firstPageFragments);

                }
            }
        });
    }


}