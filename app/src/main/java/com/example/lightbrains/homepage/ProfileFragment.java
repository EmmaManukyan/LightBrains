package com.example.lightbrains.homepage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentProfileBinding;
import com.example.lightbrains.dialogs.CustomDialogToWritePasswordFragment;

import java.util.Objects;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewParams();


        binding.imgEditProfile.setOnClickListener(view1 -> {
            CustomDialogToWritePasswordFragment dialog = new CustomDialogToWritePasswordFragment(this);
            dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_TAG);
        });


    }

    private void setViewParams() {
        binding.edtName.setText("Emma");
        binding.edtPassword.setText("Emma");

    }


    public void checkPassword(String password) {
        if (Objects.equals(password, "Emma")) {
            enableViews();
            Toast.makeText(getContext(), "Exav", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Password is wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void enableViews() {
        binding.tvLayName.setStartIconTintList(getResources().getColorStateList(R.color.purple_500));
        binding.tvLayPassword.setStartIconTintList(getResources().getColorStateList(R.color.purple_500));
        binding.edtName.setTextColor(getResources().getColorStateList(R.color.purple_500));
        binding.edtPassword.setTextColor(getResources().getColorStateList(R.color.purple_500));
        binding.tvLayName.setEnabled(true);
        binding.edtName.setFocusedByDefault(true);
        binding.tvLayPassword.setEnabled(true);
        binding.imgEditProfile.setClickable(false);
        binding.btnConfirm.setVisibility(View.VISIBLE);

    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void disEnableViews() {
        binding.tvLayName.setStartIconTintList(getResources().getColorStateList(R.color.grey));
        binding.tvLayPassword.setStartIconTintList(getResources().getColorStateList(R.color.grey));
        binding.edtName.setTextColor(getResources().getColorStateList(R.color.grey));
        binding.edtPassword.setTextColor(getResources().getColorStateList(R.color.grey));
        binding.tvLayName.setEnabled(false);
        binding.edtName.setFocusedByDefault(false);
        binding.tvLayPassword.setEnabled(false);
        binding.imgEditProfile.setClickable(true);
        binding.btnConfirm.setVisibility(View.GONE);
    }


}
