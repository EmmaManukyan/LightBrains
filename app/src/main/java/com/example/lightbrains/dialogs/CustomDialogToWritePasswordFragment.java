package com.example.lightbrains.dialogs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lightbrains.R;
import com.example.lightbrains.databinding.FragmentCustomDialogToWritePasswordBinding;
import com.example.lightbrains.homepage.ProfileFragment;


public class CustomDialogToWritePasswordFragment extends DialogFragment {

    FragmentCustomDialogToWritePasswordBinding binding;

    ProfileFragment fragment;

    public CustomDialogToWritePasswordFragment(ProfileFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.checkPassword(binding.edtPassword.getText().toString());
                dismiss();
            }
        });
    }


}