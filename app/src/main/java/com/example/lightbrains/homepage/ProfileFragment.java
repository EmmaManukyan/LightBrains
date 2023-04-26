package com.example.lightbrains.homepage;

import static com.example.lightbrains.common.ConstantsForFireBase.progressDialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentProfileBinding;
import com.example.lightbrains.dialogs.CustomDialogFragmentForExit;
import com.example.lightbrains.dialogs.CustomDialogToWritePasswordFragment;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private String passwordError = "Password is empty";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
            dialog.show(requireActivity().getSupportFragmentManager(), Constants.DIALOG_TAG);
        });

        binding.btnLogOut.setOnClickListener(view12 -> {
            showDialog();
        });

        binding.btnConfirm.setOnClickListener(view13 -> {
            String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
            if (Objects.requireNonNull(binding.edtName.getText()).toString().equals("")) {
                binding.tvLayName.setError("Enter name");
            } else if (password.equals("") || password.length() > ConstantsForFireBase.PASSWORD_MAX_LENGTH || password.length() < 8 || password.contains(" ")) {
                binding.tvLayPassword.setHelperText("");
                binding.tvLayPassword.setError(passwordError);
            } else if (ConstantsForFireBase.checkConnection(requireActivity())) {
                Toast.makeText(getContext(), "There is no internet", Toast.LENGTH_SHORT).show();
            } else {
                updateUserPassword(binding.edtPassword.getText().toString());
                disEnableViews();
            }
        });

        binding.btnCancel.setOnClickListener(view14 -> {
            setViewParams();
            disEnableViews();
        });

        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                binding.tvLayPassword.setHelperText("Enter minimum 8 char");
                binding.tvLayPassword.setErrorEnabled(false);
                passwordError = "Enter minimum 8 char";
                if (password.contains(" ")) {
                    binding.tvLayPassword.setHelperText("");
                    passwordError = "Password can't contain space";
                    binding.tvLayPassword.setError(passwordError);
                }
                if (password.length() > ConstantsForFireBase.PASSWORD_MAX_LENGTH) {
                    binding.tvLayPassword.setHelperText("");
                    passwordError = "Password's max length is "+ConstantsForFireBase.PASSWORD_MAX_LENGTH;
                    binding.tvLayPassword.setError(passwordError);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void setViewParams() {
        binding.edtName.setText(Constants.sharedPreferences.getString(ConstantsForFireBase.USER_NAME, null));
        binding.edtPassword.setText("");
        binding.edtPassword.setHintTextColor(getResources().getColorStateList(R.color.grey));
        binding.tvProfileName.setText(Constants.sharedPreferences.getString(ConstantsForFireBase.USER_NAME, null));
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
        binding.btnCancel.setVisibility(View.VISIBLE);
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
        binding.btnCancel.setVisibility(View.GONE);
    }

    private void showDialog() {
        CustomDialogFragmentForExit customDialogFragmentForExit = new CustomDialogFragmentForExit(2);
        customDialogFragmentForExit.show(requireActivity().getSupportFragmentManager(), Constants.DIALOG_TAG_EXIT);
    }

    private void updateUserPassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        progressDialog = new ProgressDialog(getContext(), R.style.MyStyleForProgressDialog);
        ConstantsForFireBase.showProgressDialog(progressDialog);
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("taguhi", "User password updated.");
                        Toast.makeText(getContext(), "Confirmed!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("taguhi", "" + task.getException());
                        Toast.makeText(getContext(), "Something went wrong " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                });

    }

    public void reAuthenticateUser(String password) {
        Constants.closeKeyboard(requireActivity());
        progressDialog = new ProgressDialog(getContext(), R.style.MyStyleForProgressDialog);
        ConstantsForFireBase.showProgressDialog(progressDialog);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        AuthCredential credential = EmailAuthProvider
                .getCredential(Objects.requireNonNull(user.getEmail()), password);
        Log.d("taguhi","re Auth   "+user.getEmail());

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Log.d("taguhi", "User re-authenticated.");
                        enableViews();
                    }else {
                        Log.d("taguhi","reAuth  "+task.getException());
                        Toast.makeText(getContext(), "Password is wrong", Toast.LENGTH_SHORT).show();

                    }
                    progressDialog.dismiss();
                });
    }
}
