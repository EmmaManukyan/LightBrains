package com.example.lightbrains.part_second.memory_game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentMemoryGameSettingsBinding;
import com.example.lightbrains.part_second.attention_game.FigureListCreator;

public class MemoryGameSettingsFragment extends Fragment {

    //in this fragment some codes arent finished yet, but as i worked on them i left and soon they ill be done

    private FragmentMemoryGameSettingsBinding binding;
    private ArrayAdapter arrayAdapter;
    private int rows;
    private int columns;
    private int figureType = 0;

    //this variable is for checking which radiobutton is selected an get its text resouce from array below
    //and it is int not boolean for the future plans to add more variants of complexity
    private int complexityOrder = -1;
    //this array includes the string resources time and steps
    private final int[] stringRes = new int[]{R.string.time, R.string.count_of_steps};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMemoryGameSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        binding.btnLetsGo.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.FIGURES_TYPE, figureType);
            bundle.putInt(Constants.COUNT_OF_ROWS, rows);
            bundle.putInt(Constants.COUNT_OF_COLUMNS, columns);
            bundle.putInt(Constants.COMPLEXITY_ORDER, complexityOrder);
            bundle.putInt(Constants.MAX_PARAMETER, (int) binding.includedLayout.sliderTimeOrStepCount.getValue());
            Constants.myEditShared.putInt(Constants.FIGURES_TYPE_MEMORY_GAME, figureType);
            Constants.myEditShared.putInt(Constants.COUNT_OF_COLUMNS, columns);
            Constants.myEditShared.commit();
            Constants.createSound(requireActivity(), R.raw.right);
            Constants.makeSoundEffect();
            Navigation.findNavController(v).navigate(R.id.action_memoryGameSettingsFragment_to_memoryGameShowCardsFragment, bundle);
        });


    }

    private void timeIsChecked() {
        int maxValue = rows * columns * 2;
        int minValue = rows * columns / 2;
        int curValue = (int) binding.includedLayout.sliderTimeOrStepCount.getValue();
        setSliderParams(minValue, maxValue, curValue);
        int time = (int) binding.includedLayout.sliderTimeOrStepCount.getValue();
        binding.includedLayout.tvTime.setText(requireActivity().getResources().getString(R.string.time) + ": " + time + " " + getResources().getString(R.string.seconds));
        complexityOrder = 0;
    }

    @SuppressLint("SetTextI18n")
    private void stepIsChecked() {
        int maxValue = rows * columns;
        int minValue =  (rows*columns)/2+(rows*columns)%2;
        int curValue = (int) binding.includedLayout.sliderTimeOrStepCount.getValue();
        setSliderParams(minValue, maxValue, curValue);
        int step = (int) binding.includedLayout.sliderTimeOrStepCount.getValue();
        binding.includedLayout.tvTime.setText(requireActivity().getResources().getString(R.string.count_of_columns) + ": " + step);
        complexityOrder = 1;
    }

    private void setSliderParams(int minValue, int maxValue, int curValue) {
        binding.includedLayout.sliderTimeOrStepCount.setValueTo(maxValue);
        binding.includedLayout.sliderTimeOrStepCount.setValueFrom(minValue);
        binding.includedLayout.sliderTimeOrStepCount.setStepSize(1);
        if (curValue >= minValue && curValue <= maxValue) {
            binding.includedLayout.sliderTimeOrStepCount.setValue(curValue);
        } else {
            binding.includedLayout.sliderTimeOrStepCount.setValue(minValue);
        }
    }


    @SuppressLint("SetTextI18n")
    private void init() {
        Constants.createSharedPreferences(requireActivity());
        String[] temp = getResources().getStringArray(R.array.string_figures_array);
        String[] figures = new String[temp.length + 1];
        figures[0] = getResources().getString(R.string.random);
        System.arraycopy(temp, 0, figures, 1, figures.length - 1);
        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, figures);
        Constants.createSound(requireActivity(), R.raw.btn_click);


        if (Constants.sharedPreferences.getInt(Constants.FIGURES_TYPE_MEMORY_GAME, -10) != -10) {
            figureType = Constants.sharedPreferences.getInt(Constants.FIGURES_TYPE_MEMORY_GAME, -10);
        }
        figureType = Constants.sharedPreferences.getInt(Constants.FIGURES_TYPE_MEMORY_GAME, 0);
        columns = Constants.sharedPreferences.getInt(Constants.COUNT_OF_COLUMNS, (int) binding.sliderColumnsAndRows.getValue());
        rows = Constants.sharedPreferences.getInt(Constants.COUNT_OF_COLUMNS, (int) binding.sliderColumnsAndRows.getValue());


        if (figureType != 0) {
            setSliderMaxValue(figureType);
        }
        binding.sliderColumnsAndRows.setValue(columns);
        binding.tvColumnsAndRows.setText(getResources().getString(R.string.count_of_columns) + " " + columns + " x " + columns);
        binding.autoTvFigures.setText(figures[figureType]);
        binding.autoTvFigures.setAdapter(arrayAdapter);

        binding.sliderColumnsAndRows.addOnChangeListener((slider, value, fromUser) -> {
            columns = (int) value;
            rows = (int) value;
            if (complexityOrder == 0) {
                timeIsChecked();
            } else if (complexityOrder==1){
                stepIsChecked();
            }
            binding.tvColumnsAndRows.setText(getResources().getString(R.string.count_of_columns) + ": " + columns + " x " + rows);
            Constants.makeSoundEffect();
        });


        binding.autoTvFigures.setOnItemClickListener((parent, view, position, id) -> {
            if (figureType != position) {
                figureType = position;
                if (figureType != 0) {
                    setSliderMaxValue(figureType);
                }
                Constants.makeSoundEffect();
            }

        });


        binding.checkBoxComplicate.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                complexityOrder = 0;
                timeIsChecked();
                binding.includedLayout.getRoot().setVisibility(View.VISIBLE);
                Constants.createToast(requireActivity(), R.string.we_are_still_working_on_this_part);
            } else {
                complexityOrder = -1;
                binding.includedLayout.getRoot().setVisibility(View.GONE);
            }
            Constants.makeSoundEffect();
        });

        binding.includedLayout.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_time) {
                    timeIsChecked();
                } else {
                    stepIsChecked();
                }
            }
        });

        binding.includedLayout.sliderTimeOrStepCount.addOnChangeListener((slider, value, fromUser) -> {
            int count = (int) value;
            binding.includedLayout.tvTime.setText(requireActivity().getResources().getString(stringRes[complexityOrder]) + ": "
                    + count+(complexityOrder==0?" "+requireActivity().getResources().getString(R.string.seconds):""));
            Constants.makeSoundEffect();

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.autoTvFigures.setAdapter(arrayAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void setSliderMaxValue(int figureType) {
        binding.sliderColumnsAndRows.setValue(binding.sliderColumnsAndRows.getValueFrom());
        binding.sliderColumnsAndRows.setValueTo((int) Math.sqrt(FigureListCreator.figureTypes[figureType - 1].length * 2));
        int tempColumn = (int) binding.sliderColumnsAndRows.getValue();
        binding.tvColumnsAndRows.setText(getResources().getString(R.string.count_of_columns) + ": " + tempColumn + " x " + tempColumn);
    }
}