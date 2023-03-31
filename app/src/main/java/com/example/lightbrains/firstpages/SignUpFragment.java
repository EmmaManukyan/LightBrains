package com.example.lightbrains.firstpages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentSignUpBinding;
import com.example.lightbrains.dialogs.CustomDialogForSignUpFragment;


public class SignUpFragment extends Fragment {
    FragmentSignUpBinding binding;
    private String passwordError = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.includedLayout.tvLayPassword.setError(null);
        binding.includedLayout.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                binding.includedLayout.tvLayPassword.setHelperText("Enter minimum 8 char");
                binding.includedLayout.tvLayPassword.setError("");
                passwordError = "Enter minimum 8 char";
                if (password.contains(" ")) {
                    binding.includedLayout.tvLayPassword.setHelperText("");
                    passwordError = "Password can't contain space";
                    binding.includedLayout.tvLayPassword.setError(passwordError);
                }
                if (password.length()>10){
                    binding.includedLayout.tvLayPassword.setHelperText("");
                    passwordError = "Password's max length is 10";
                    binding.includedLayout.tvLayPassword.setError(passwordError);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.imgBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showCustomDialog();
                binding.includedLayout.tvLayName.setError("");
                binding.includedLayout.tvLayMail.setError("");
                binding.includedLayout.tvLayPassword.setError("");
                String password = binding.includedLayout.edtPassword.getText().toString();
                if (binding.includedLayout.edtName.getText().toString().equals("")) {
                    binding.includedLayout.tvLayName.setError("Enter name");
                } else if (binding.includedLayout.edtMail.getText().toString().equals("")) {
                    binding.includedLayout.tvLayMail.setError("Enter mail");
                } else if (password.equals("") || password.length()>10 || password.length()<8 || password.contains(" ")) {
                    binding.includedLayout.tvLayPassword.setHelperText("");
                    binding.includedLayout.tvLayPassword.setError(passwordError);
                }
                else {
                    CustomDialogForSignUpFragment dialog = new CustomDialogForSignUpFragment(0);
                    dialog.setCancelable(false);
                    dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_TAG);
                    Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_signInFragment);

                }
            }
        });
    }
}