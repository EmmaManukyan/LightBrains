package com.example.lightbrains.part_first_mental.flashanzan;


import static com.example.lightbrains.common.Constants.sharedPreferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentFlashCardsSettingsBinding;

public class FleshCardSettingsFragment extends Fragment implements View.OnClickListener {

    FragmentFlashCardsSettingsBinding binding;

    private ArrayAdapter arrayAdapter;

    private String[] speedArrayStrings;
    private String[] digitsArrayStrings;

    private AutoCompleteTextView[] spinners;
    private String[][] spinnerItems;


    private int speed = -1;
    private int digit = -1;
    private int count = 1;

    private int speedPosition = -1;
    private int digitPosition = -1;

    private int[] spinPositions;
    private final String[] constantsArr = {Constants.SPEED_FLASH_CARDS, Constants.DIGIT_FLASH_CARDS, Constants.COUNT_FLASH_CARDS};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFlashCardsSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void initDopDownMenus() {

        Constants.createSharedPreferences(getActivity());

        for (int i = 0; i < spinners.length; i++) {
            if (sharedPreferences.getInt(Constants.SPEED_FLASH_CARDS, -1) != -1) {
                spinners[i].setText(spinnerItems[i][sharedPreferences.getInt(constantsArr[i], -1)]);
                spinPositions[i] = sharedPreferences.getInt(constantsArr[i], -1);
            }
            arrayAdapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, spinnerItems[i]);
            spinners[i].setAdapter(arrayAdapter);

        }


        if (sharedPreferences.getInt(Constants.SPEED_FLASH_CARDS, -1) != -1) {
            speedPosition = spinPositions[0];
            digitPosition = spinPositions[1];
            digit = Integer.parseInt(digitsArrayStrings[digitPosition]);
            String s = speedArrayStrings[speedPosition];
            if (s.contains(" ")) {
                s = s.substring(0, s.indexOf(" "));
            }
            speed = (int) (Float.parseFloat(s) * 1000);
            binding.autoTvCount.setText(Integer.toString(sharedPreferences.getInt(Constants.COUNT_FLASH_CARDS, 1)));
            count = Integer.parseInt(binding.autoTvCount.getText().toString());
        }


    }

    private void init(View v) {


        binding.btnStart.setOnClickListener(this);
        binding.btnMinus.setOnClickListener(this);
        binding.btnPlus.setOnClickListener(this);


        speedArrayStrings = getResources().getStringArray(R.array.string_list_speeds);
        digitsArrayStrings = getResources().getStringArray(R.array.string_list_digits);


        spinnerItems = new String[][]{speedArrayStrings, digitsArrayStrings};

        spinners = new AutoCompleteTextView[]{binding.autoTvSpeed, binding.autoTvDigits};

        spinPositions = new int[]{speedPosition, digitPosition};


        binding.autoTvSpeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = speedArrayStrings[position];
                if (s.contains(" ")) {
                    s = s.substring(0, s.indexOf(" "));
                    System.out.println(s);
                }
                speed = (int) (Float.parseFloat(s) * 1000);
                speedPosition = position;
            }
        });

        binding.autoTvDigits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                digit = Integer.parseInt(digitsArrayStrings[position]);
                digitPosition = position;
            }
        });

    }

    @Override
    public void onClick(View view) {
        int temp;

        if (binding.btnStart.equals(view)) {
            if (speed != -1 && digit != -1 && count != -1) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.SPEED_FLASH_CARDS, speed);
                bundle.putInt(Constants.DIGIT_FLASH_CARDS, digit);
                bundle.putInt(Constants.COUNT_FLASH_CARDS, count);
                Constants.myEditShared.putInt(Constants.SPEED_FLASH_CARDS, speedPosition);
                Constants.myEditShared.putInt(Constants.DIGIT_FLASH_CARDS, digitPosition);
                Constants.myEditShared.putInt(Constants.COUNT_FLASH_CARDS, count);
                Constants.myEditShared.commit();
                Navigation.findNavController(view).navigate(R.id.action_fleshAnzanSettingsFragment_to_showFlashCardsFragment, bundle);
            }
        } else if (binding.btnPlus.equals(view)) {
            temp = Integer.parseInt(binding.autoTvCount.getText().toString());
            temp++;
            count = temp;
            binding.autoTvCount.setText(Integer.toString(temp));
        } else if (binding.btnMinus.equals(view)) {
            temp = Integer.parseInt(binding.autoTvCount.getText().toString());
            if (temp - 1 > 0) {
                temp--;
                count = temp;
                binding.autoTvCount.setText(Integer.toString(temp));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initDopDownMenus();
    }
}

