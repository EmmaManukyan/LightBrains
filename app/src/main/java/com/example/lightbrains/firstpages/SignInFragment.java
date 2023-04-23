package com.example.lightbrains.firstpages;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentSignInBinding;
import com.example.lightbrains.homepage.HomeActivity;

public class SignInFragment extends Fragment implements View.OnClickListener {

    FragmentSignInBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
    }



    @Override
    public void onClick(View view) {
        if (binding.btnNewUser.equals(view)) {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment);
        } else if (binding.btnSignIn.equals(view)) {
            binding.tvLayMail.setError("");
            binding.tvLayPassword.setError("");
             if (binding.edtMail.getText().toString().equals("")) {
                binding.tvLayMail.setError("Enter mail");
            } else if (binding.edtPassword.getText().toString().equals("")) {
                binding.tvLayPassword.setHelperText("");
                binding.tvLayPassword.setError("Enter password");
            }
            else{
                 Constants.myEditShared.putBoolean(Constants.IS_LOGIN, true);
                 Constants.myEditShared.commit();
                 Intent intent = new Intent(getActivity(), HomeActivity.class);
                 startActivity(intent);
                 requireActivity().finish();
             }
        } else if (binding.tvForgotPassword.equals(view)) {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_forgotFragment);
        }
    }

    private void init(){
        binding.btnSignIn.setOnClickListener(this);
        binding.btnNewUser.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
    }
}