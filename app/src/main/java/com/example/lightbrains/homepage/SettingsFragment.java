package com.example.lightbrains.homepage;

import static com.example.lightbrains.common.Constants.sharedPreferences;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private ArrayAdapter adapter;
    private String[] languageArrayStrings;
    private int CHECKED_LANGUAGE = 0;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeActivity.binding.frContainer.setVisibility(View.VISIBLE);
        init();
        binding.autoTvLanguages.setOnItemClickListener((adapterView, view1, position, l) -> {
            if (CHECKED_LANGUAGE != position) {
                CHECKED_LANGUAGE = position;
                Constants.myEditShared.putInt(Constants.CHECKED_LANGUAGE, CHECKED_LANGUAGE);
                requireActivity().finish();
                startActivity(requireActivity().getIntent());
                Constants.myEditShared.commit();
            }
        });

        binding.internetSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Constants.myEditShared.putBoolean(Constants.USE_INTERNET, isChecked);
            Constants.myEditShared.commit();
        });
    }

    private void init() {
        languageArrayStrings = getResources().getStringArray(R.array.string_languages_array);
        CHECKED_LANGUAGE = sharedPreferences.getInt(Constants.CHECKED_LANGUAGE, 0);
        setLanguages();
        Constants.createSharedPreferences(requireActivity());
        binding.internetSwitch.setChecked(sharedPreferences.getBoolean(Constants.USE_INTERNET,true));
    }

    private void setLanguages() {
        adapter = new ArrayAdapter(getActivity(), R.layout.dropdown_item, languageArrayStrings);
        binding.autoTvLanguages.setText(languageArrayStrings[CHECKED_LANGUAGE]);
        binding.autoTvLanguages.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setLanguages();
    }
}