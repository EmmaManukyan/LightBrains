package com.example.lightbrains.part_second.attention_game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentAttentionGameSettingsBinding;

import java.util.Objects;

public class AttentionGameSettingsFragment extends Fragment implements View.OnClickListener {

    private int complexityLevel = 0;
    private int figuresType = -1;
    private int figuresLevel = 0;

    private int figuresCount = 3;
    private int figuresGroupCount = 0;
    private float showTime = 0.3f;

    private Bundle bundle;

    private FragmentAttentionGameSettingsBinding binding;

    private String[] complexityArrayStrings;
    private ArrayAdapter arrayAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
            Constants.createSound(requireActivity(), R.raw.guest_sound);
            if (!binding.checkboxInfo.isChecked()) {
                binding.checkboxInfo.setChecked(true);
                binding.tvInfo.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.BounceInDown).duration(500).playOn(binding.tvInfo);
                Constants.makeSoundEffect();
                Constants.createSound(requireActivity(), R.raw.btn_click);

            } else {
                binding.checkboxInfo.setChecked(false);
                binding.tvInfo.setVisibility(View.GONE);
            }
        });
        binding.btnLetsGo.setOnClickListener(view12 -> {
            Constants.createSound(requireActivity(), R.raw.right);
            if (binding.checkBoxDefineMyslf.isChecked()) {
                if (figuresType != -1 && figuresLevel != -1) {
                    putBundlesWithoutComplexity();
                    AttentionGameValues.setStartTime(System.currentTimeMillis());
                    Navigation.findNavController(view12).navigate(R.id.action_attentionGameSettingsFragment_to_attentionGameShowFiguresFragment, bundle);
                    Constants.makeSoundEffect();

                } else {
                    Constants.createToast(getContext(), R.string.select_all_necessary_fields);
                }
            } else {
                Constants.makeSoundEffect();
                putBundlesWithComplexity();
                AttentionGameValues.setStartTime(System.currentTimeMillis());
                Constants.myEditShared.putInt(Constants.FIGURES_COMPLEXITY_LEVEL, complexityLevel);
                Constants.myEditShared.putInt(Constants.FIGURES_GROUP_COUNT, figuresGroupCount);
                Constants.myEditShared.commit();
                Navigation.findNavController(view12).navigate(R.id.action_attentionGameSettingsFragment_to_attentionGameShowFiguresFragment);
            }
        });


        binding.checkBoxDefineMyslf.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                defineManually();
            } else {
                binding.includedLayoutContainer.setVisibility(View.GONE);
                binding.tvLayComplexity.setEnabled(true);
            }
            Constants.makeSoundEffect();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        Constants.createSound(requireActivity(), R.raw.btn_click);
        int temp;

        if (binding.btnPlus.equals(view)) {
            temp = Integer.parseInt(Objects.requireNonNull(binding.autoTvCountOfFigureGroups.getText()).toString());
            temp++;
            figuresGroupCount = temp;
            binding.autoTvCountOfFigureGroups.setText(Integer.toString(temp));
        } else if (binding.btnMinus.equals(view)) {
            temp = Integer.parseInt(Objects.requireNonNull(binding.autoTvCountOfFigureGroups.getText()).toString());
            if (temp - 1 > 0) {
                temp--;
                figuresGroupCount = temp;
                binding.autoTvCountOfFigureGroups.setText(Integer.toString(temp));
            }
        }
        Constants.makeSoundEffect();
    }

    private void init() {
        AttentionGameValues.setCount(0);
        Constants.createSound(requireActivity(),R.raw.btn_click);
        bundle = new Bundle();

        binding.btnPlus.setOnClickListener(this);
        binding.btnMinus.setOnClickListener(this);
        setAdaptersToViews();
        binding.includedLayout.tvTime.append(" 0.3");
        binding.includedLayout.tvFigureCount.append(" 3");

        binding.autoTvComplexity.setOnItemClickListener((adapterView, view, i, l) -> {
            complexityLevel = i;
            Constants.makeSoundEffect();
        });


        binding.includedLayout.autoTvFigures.setOnItemClickListener((adapterView, view, i, l) -> {
            figuresType = i;
            Constants.makeSoundEffect();
        });

        binding.includedLayout.autoTvFiguresLevel.setOnItemClickListener((adapterView, view, i, l) -> {
                    figuresLevel = i;
                    Constants.makeSoundEffect();
                }
        );

        binding.includedLayout.sliderTime.addOnChangeListener((slider, value, fromUser) -> {
            float time = (float) value / 10;
            showTime = time;
            binding.includedLayout.tvTime.setText(getResources().getString(R.string.time) + ": " + time);
            Constants.makeSoundEffect();

        });


        binding.includedLayout.sliderFigureCount.addOnChangeListener((slider, value, fromUser) -> {
            figuresCount = (int) value;
            binding.includedLayout.tvFigureCount.setText(getResources().getString(R.string.number_of_following_figures) + " " + figuresCount);
            Constants.makeSoundEffect();

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
        binding.includedLayout.autoTvFiguresLevel.setText(figureTypeCountArrayStrings[0]);
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
        Constants.createSharedPreferences(getActivity());
        complexityArrayStrings = getResources().getStringArray(R.array.string_complexity_array);
        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, complexityArrayStrings);
        complexityLevel = Constants.sharedPreferences.getInt(Constants.FIGURES_COMPLEXITY_LEVEL, 0);
        figuresGroupCount = Constants.sharedPreferences.getInt(Constants.FIGURES_GROUP_COUNT, 1);
        binding.autoTvCountOfFigureGroups.setText("" + figuresGroupCount);
        binding.autoTvComplexity.setText(complexityArrayStrings[complexityLevel]);
        binding.autoTvComplexity.setAdapter(arrayAdapter);
    }

    private void putBundlesWithComplexity() {
        figuresGroupCount = Integer.parseInt(Objects.requireNonNull(binding.autoTvCountOfFigureGroups.getText()).toString());

        AttentionGameValues.setFiguresGroupCount(figuresGroupCount);
        AttentionGameValues.setComplexityLevel(complexityLevel);

        AttentionGameValues.setFiguresType(-1);
        AttentionGameValues.setFiguresLevel(-1);
        AttentionGameValues.setShowTime(-1);
        AttentionGameValues.setFiguresCount(-1);

    }

    private void putBundlesWithoutComplexity() {
        figuresGroupCount = Integer.parseInt(Objects.requireNonNull(binding.autoTvCountOfFigureGroups.getText()).toString());

        AttentionGameValues.setFiguresGroupCount(figuresGroupCount);
        AttentionGameValues.setComplexityLevel(-1);

        AttentionGameValues.setFiguresType(figuresType);
        AttentionGameValues.setFiguresLevel(figuresLevel);
        AttentionGameValues.setShowTime(showTime);
        AttentionGameValues.setFiguresCount(figuresCount);
    }
}