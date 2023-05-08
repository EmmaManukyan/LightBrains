package com.example.lightbrains.part_first_mental.mental_counting;

import static com.example.lightbrains.common.Constants.sharedPreferences;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentMentalCountingSettingsBinding;

public class MentalCountingSettingsFragment extends Fragment implements View.OnClickListener {


    FragmentMentalCountingSettingsBinding binding;

    private ArrayAdapter arrayAdapter;

    private String[] speedArrayStrings;
    private String[] digitsArrayStrings;
    private String[] topicArrayStrings;

    private AutoCompleteTextView[] spinners;
    private String[][] spinnerItems;

    private String[] subTopicStrings;


    private int speed = -1;
    private int digit = -1;
    private int countOfRows = 2;

    private int countOfExamples = 5;

    private int topicLevel = 0;
    private int subtopicLevel = 0;

    private int speedPosition = -1;
    private int digitPosition = -1;
    private int topicPosition = 0;

    private int[] spinPositions;
    private final String[] constantsArr = {Constants.SPEED_MENTAL, Constants.DIGIT_MENTAL, Constants.TOPIC_LEVEL_MENTAL, Constants.COUNT_OF_EXAMPLES_MENTAL};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMentalCountingSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @SuppressLint("SetTextI18n")
    private void initDopDownMenus() {

        Constants.createSharedPreferences(getActivity());

        for (int i = 0; i < spinners.length; i++) {
            if (sharedPreferences.getInt(Constants.SPEED_MENTAL, -1) != -1) {
                spinners[i].setText(spinnerItems[i][sharedPreferences.getInt(constantsArr[i], -1)]);
                spinPositions[i] = sharedPreferences.getInt(constantsArr[i], -1);
            }
            arrayAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, spinnerItems[i]);
            spinners[i].setAdapter(arrayAdapter);

        }


        if (sharedPreferences.getInt(Constants.SPEED_MENTAL, -1) != -1) {
            speedPosition = spinPositions[0];
            digitPosition = spinPositions[1];
            topicPosition = spinPositions[2];
            String s = speedArrayStrings[speedPosition];
            if (s.contains(" ")) {
                s = s.substring(0, s.indexOf(" "));
            }
            speed = (int) (Float.parseFloat(s) * 1000);
            topicLevel = topicPosition;
            digit = Integer.parseInt(digitsArrayStrings[digitPosition]);
            binding.autoTvCount.setText(Integer.toString(sharedPreferences.getInt(Constants.COUNT_OF_ROWS_MENTAL, 2)));
            countOfRows = Integer.parseInt(binding.autoTvCount.getText().toString());
            countOfExamples = sharedPreferences.getInt(Constants.COUNT_OF_EXAMPLES_MENTAL,5);
            Log.d("TAG",countOfExamples+"");
            binding.sliderExampleCount.setValue(countOfExamples);
            binding.tvNumberExampleCount.setText(getResources().getString(R.string.quantity)+" "+ countOfExamples);
            if (topicLevel!=0){
                binding.tvLaySubTopic.setVisibility(View.VISIBLE);
            }

        }else{
            binding.tvNumberExampleCount.setText(getResources().getString(R.string.quantity)+" "+ 5);

        }

        setSubTopicsSpinnerItem(topicLevel);


    }

    private void init(View v) {


        binding.btnStart.setOnClickListener(this);
        binding.btnMinus.setOnClickListener(this);
        binding.btnPlus.setOnClickListener(this);


        speedArrayStrings = getResources().getStringArray(R.array.string_list_speeds);
        digitsArrayStrings = getResources().getStringArray(R.array.string_list_digits);
        topicArrayStrings = getResources().getStringArray(R.array.string_list_mental_topics);


        spinnerItems = new String[][]{speedArrayStrings, digitsArrayStrings, topicArrayStrings};

        spinners = new AutoCompleteTextView[]{binding.autoTvSpeed, binding.autoTvDigits, binding.autoTvTopic};

        spinPositions = new int[]{speedPosition, digitPosition, topicPosition};


        binding.autoTvSpeed.setOnItemClickListener((parent, view, position, id) -> {
            String s = speedArrayStrings[position];
            if (s.contains(" ")) {
                s = s.substring(0, s.indexOf(" "));
                System.out.println(s);
            }
            speed = (int) (Float.parseFloat(s) * 1000);
            speedPosition = position;
        });

        binding.autoTvDigits.setOnItemClickListener((parent, view, position, id) -> {
            digit = Integer.parseInt(digitsArrayStrings[position]);
            digitPosition = position;
        });

        binding.autoTvTopic.setOnItemClickListener((parent, view, position, id) -> {
            topicLevel = position;
            topicPosition = position;
            setSubTopicsSpinnerItem(topicLevel);
            if (topicLevel==0){
                binding.tvLaySubTopic.setVisibility(View.GONE);
            }else{
                binding.tvLaySubTopic.setVisibility(View.VISIBLE);
            }
        });

        binding.autoTvSubTopic.setOnItemClickListener((adapterView, view, i, l) -> {
            subtopicLevel = i;
        });

        binding.sliderExampleCount.addOnChangeListener((slider, value, fromUser) -> {
            countOfExamples = (int) value;
            binding.tvNumberExampleCount.setText(getResources().getString(R.string.quantity)+" "+ countOfExamples);
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        int temp;

        if (binding.btnStart.equals(view)) {
            if (topicLevel>1){
                Log.d("taguhi",""+subtopicLevel);
                Navigation.findNavController(view).navigate(R.id.action_mentalCountingSettingsFragment_to_mentalCountingPartWorkingPageFragment);
            }
            else if (speed != -1 && digit != -1 && countOfRows != -1) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.SPEED_MENTAL, speed);
                bundle.putInt(Constants.DIGIT_MENTAL, digit);
                bundle.putInt(Constants.COUNT_OF_ROWS_MENTAL, countOfRows);
                bundle.putInt(Constants.TOPIC_LEVEL_MENTAL, topicLevel);
                bundle.putInt(Constants.SUBTOPIC_LEVEL_MENTAL, subtopicLevel);
                bundle.putInt(Constants.COUNT_OF_EXAMPLES_MENTAL, countOfExamples);

                Constants.myEditShared.putInt(Constants.SPEED_MENTAL, speedPosition);
                Constants.myEditShared.putInt(Constants.DIGIT_MENTAL, digitPosition);
                Constants.myEditShared.putInt(Constants.COUNT_OF_ROWS_MENTAL, countOfRows);
                Constants.myEditShared.putInt(Constants.TOPIC_LEVEL_MENTAL, topicLevel);
                Constants.myEditShared.putInt(Constants.COUNT_OF_EXAMPLES_MENTAL, countOfExamples);
                Constants.myEditShared.commit();
                Navigation.findNavController(view).navigate(R.id.action_mentalCountingSettingsFragment_to_showMentalCountFragment, bundle);
            }else{
                Constants.createToast(getContext(), R.string.select_all_necessary_fields);
            }
        } else if (binding.btnPlus.equals(view)) {
            temp = Integer.parseInt(binding.autoTvCount.getText().toString());
            if (temp <= 50) {
                temp++;
                countOfRows = temp;
                binding.autoTvCount.setText(Integer.toString(temp));
            }
        } else if (binding.btnMinus.equals(view)) {
            temp = Integer.parseInt(binding.autoTvCount.getText().toString());
            if (temp - 1 > 1) {
                temp--;
                countOfRows = temp;
                binding.autoTvCount.setText(Integer.toString(temp));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initDopDownMenus();
    }

    private void setSubTopicsSpinnerItem(int topicLevel) {
        switch (topicLevel) {
            case 0:
                subTopicStrings = getResources().getStringArray(R.array.string_list_mental_subtopics_direct_count);
                break;

            case 1:
                subTopicStrings = getResources().getStringArray(R.array.string_list_mental_subtopics_junior_comrades);
                break;

            case 2:
                subTopicStrings = getResources().getStringArray(R.array.string_list_mental_subtopics_senior_comrades);
                break;
            default:
                subTopicStrings = getResources().getStringArray(R.array.string_list_mental_subtopics_mix_formulas);
                break;
        }
        arrayAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, subTopicStrings);
        binding.autoTvSubTopic.setText(subTopicStrings[0]);
        binding.autoTvSubTopic.setAdapter(arrayAdapter);

    }
}