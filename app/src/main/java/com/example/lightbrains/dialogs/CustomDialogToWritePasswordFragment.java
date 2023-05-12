package com.example.lightbrains.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentCustomDialogToWritePasswordBinding;
import com.example.lightbrains.homepage.ProfileFragment;

import java.util.Objects;


public class CustomDialogToWritePasswordFragment extends DialogFragment {

    private FragmentCustomDialogToWritePasswordBinding binding;

    ProfileFragment fragment;

    public CustomDialogToWritePasswordFragment(ProfileFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCustomDialogToWritePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnOk.setOnClickListener(view1 -> {
            if (!Objects.requireNonNull(binding.edtPassword.getText()).toString().isEmpty()){
                fragment.reAuthenticateUser(Objects.requireNonNull(binding.edtPassword.getText()).toString());
            }else{
                Toast.makeText(getContext(), getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
            }
            dismiss();

        });
    }



}