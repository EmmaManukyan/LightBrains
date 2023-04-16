package com.example.lightbrains.part_second.attention_game;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentAttentionGameSettingsBinding;
import com.google.android.material.slider.Slider;

public class AttentionGameSettingsFragment extends Fragment implements View.OnClickListener {

    private int complexityLevel = 0;
    private int figuresType = -1;
    private int figuresLevel = -1;

    private int figuresCount = 3;
    private int figuresGroupCount = 0;
    private float showTime = 0.3f;

    Bundle bundle;

    FragmentAttentionGameSettingsBinding binding;

    private String[] complexityArrayStrings;
    private ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAttentionGameSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();



        binding.checkboxInfo.setOnClickListener(view1 -> {
            if (!binding.checkboxInfo.isChecked()){
                binding.checkboxInfo.setChecked(true);
                binding.tvInfo.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInDown).duration(500).playOn(binding.tvInfo);
                Toast.makeText(getContext(), ""+true, Toast.LENGTH_SHORT).show();
            }else {
                binding.checkboxInfo.setChecked(false);
                binding.tvInfo.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+false, Toast.LENGTH_SHORT).show();

            }
        });
        binding.btnLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.checkBoxDefineMyslf.isChecked()){
                    if (figuresType !=-1 && figuresLevel!=-1) {
                        putBundlesWithoutComplexity();
                        Navigation.findNavController(view).navigate(R.id.action_attentionGameSettingsFragment_to_attentionGameShowFiguresFragment,bundle);

                    }else{
                        Toast.makeText(getContext(), "Select fields", Toast.LENGTH_SHORT).show();;
                    }
                }else{
                    putBundlesWithComplexity();
                    Navigation.findNavController(view).navigate(R.id.action_attentionGameSettingsFragment_to_attentionGameShowFiguresFragment,bundle);
                }
            }
        });


        binding.checkBoxDefineMyslf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    defineManually();
                } else {
                    binding.includedLayoutContainer.setVisibility(View.GONE);
                    binding.tvLayComplexity.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int temp;

        if (binding.btnPlus.equals(view)) {
            temp = Integer.parseInt(binding.autoTvCountOfFigureGroups.getText().toString());
            temp++;
            figuresGroupCount = temp;
            binding.autoTvCountOfFigureGroups.setText(Integer.toString(temp));
        } else if (binding.btnMinus.equals(view)) {
            temp = Integer.parseInt(binding.autoTvCountOfFigureGroups.getText().toString());
            if (temp - 1 > 0) {
                temp--;
                figuresGroupCount = temp;
                binding.autoTvCountOfFigureGroups.setText(Integer.toString(temp));
            }
        }
    }

    private void init() {
        bundle = new Bundle();

        binding.btnPlus.setOnClickListener(this);
        binding.btnMinus.setOnClickListener(this);
        setAdaptersToViews();
        binding.autoTvComplexity.setText(complexityArrayStrings[complexityLevel]);
        binding.includedLayout.tvTime.append(" 0.3");
        binding.includedLayout.tvFigureCount.append(" 3");

        binding.autoTvComplexity.setOnItemClickListener((adapterView, view, i, l) -> {
            complexityLevel = i;
            Toast.makeText(getContext(), ""+complexityLevel, Toast.LENGTH_SHORT).show();
        });


        binding.includedLayout.autoTvFigures.setOnItemClickListener((adapterView, view, i, l) -> {
            figuresType = i;
        });

        binding.includedLayout.autoTvFiguresLevel.setOnItemClickListener((adapterView, view, i, l) -> figuresLevel = i);

        binding.includedLayout.sliderTime.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                float time = (float) value/10;
                showTime = time;
                binding.includedLayout.tvTime.setText(getResources().getString(R.string.time) + ": " + time);
            }
        });


        binding.includedLayout.sliderFigureCount.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                figuresCount = (int) value;
                binding.includedLayout.tvFigureCount.setText(getResources().getString(R.string.number_of_following_figures)+" "+figuresCount);
            }
        });
    }

    private void defineManually() {
        binding.includedLayoutContainer.setVisibility(View.VISIBLE);
        binding.tvLayComplexity.setEnabled(false);
        String[] figureArrayStrings = getResources().getStringArray(R.array.string_figures_array);
        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, figureArrayStrings);
        binding.includedLayout.autoTvFigures.setAdapter(arrayAdapter);

        String[] figureTypeCountArrayStrings = new String[3];
        figureTypeCountArrayStrings[0] = complexityArrayStrings[0] + " (2)";
        figureTypeCountArrayStrings[1] = complexityArrayStrings[1] + " (5)";
        figureTypeCountArrayStrings[2] = complexityArrayStrings[2] + " (8)";
        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, figureTypeCountArrayStrings);
        binding.includedLayout.autoTvFiguresLevel.setAdapter(arrayAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding.checkBoxDefineMyslf.isChecked()) {
            setAdaptersToViews();
            defineManually();
        } else {
            setAdaptersToViews();
        }
    }

    private void setAdaptersToViews() {
        complexityArrayStrings = getResources().getStringArray(R.array.string_complexity_array);
        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, complexityArrayStrings);
        binding.autoTvComplexity.setAdapter(arrayAdapter);
    }

    private void putBundlesWithComplexity(){
        figuresGroupCount = Integer.parseInt(binding.autoTvCountOfFigureGroups.getText().toString());
        bundle.putInt(Constants.FIGURES_GROUP_COUNT,figuresGroupCount);
        bundle.putInt(Constants.FIGURES_COMPLEXITY_LEVEL,complexityLevel);

        bundle.putInt(Constants.FIGURES_TYPE,-1);
        bundle.putInt(Constants.FIGURES_LEVEL,-1);
        bundle.putFloat(Constants.FIGURES_SHOW_TIME,-1);
        bundle.putInt(Constants.FIGURES_COUNT,-1);
    }

    private void putBundlesWithoutComplexity(){
        figuresGroupCount = Integer.parseInt(binding.autoTvCountOfFigureGroups.getText().toString());
        bundle.putInt(Constants.FIGURES_TYPE,figuresType);
        bundle.putInt(Constants.FIGURES_LEVEL,figuresLevel);
        bundle.putFloat(Constants.FIGURES_SHOW_TIME,showTime);
        bundle.putInt(Constants.FIGURES_COUNT,figuresCount);

        bundle.putInt(Constants.FIGURES_GROUP_COUNT,figuresGroupCount);
        bundle.putInt(Constants.FIGURES_COMPLEXITY_LEVEL,-1);
    }
}