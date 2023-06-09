package com.example.lightbrains.firstpages;

import static com.example.lightbrains.common.ConstantsForFireBase.mAuth;
import static com.example.lightbrains.common.ConstantsForFireBase.progressDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentForgotBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotFragment extends Fragment {
    //I used binding instead of using findViewById. It's more convenient and helps not to confuse views' ids
    private FragmentForgotBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentForgotBinding.inflate(inflater, container, false);
        progressDialog = new ProgressDialog(getContext(), R.style.MyStyleForProgressDialog);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnReset.setOnClickListener(v -> {
            if (!binding.edtMail.getText().toString().isEmpty()) {
                Constants.createSound(requireActivity(),R.raw.sound_first_pages);
                beginRecovery(binding.edtMail.getText().toString(), view);
            } else {
                Constants.createSound(requireActivity(),R.raw.wrong);
                Constants.createToast(getContext(), R.string.enter_email);
            }
            Constants.makeSoundEffect();
        });
    }

    //method to send the user message for reseting password
    private void beginRecovery(String email, View view) {
        ConstantsForFireBase.showProgressDialog(progressDialog, getResources().getString(R.string.sending_email),getContext());
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Constants.createToast(getContext(), R.string.check_email_forgot);
                //returning to SignInFragment
                Navigation.findNavController(view).navigate(R.id.action_forgotFragment_to_signInFragment);
            } else {
//                Toast.makeText(getContext(), "Error Occurred " + task.getException(), Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Constants.createToast(getContext(), R.string.something_went_wrong);
        });
    }
}