package com.example.lightbrains.firstpages;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.databinding.FragmentSignInBinding;
import com.example.lightbrains.homepage.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private FragmentSignInBinding binding;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
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
            if (Objects.requireNonNull(binding.edtMail.getText()).toString().equals("")) {
                binding.tvLayMail.setError("Enter mail");
            } else if (Objects.requireNonNull(binding.edtPassword.getText()).toString().equals("")) {
                binding.tvLayPassword.setHelperText("");
                binding.tvLayPassword.setError("Enter password");
            } else {
                String email = binding.edtMail.getText().toString();
                String password = binding.edtPassword.getText().toString();

                progressDialog.setMessage("Please wait...");
                progressDialog.setTitle("Registration");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() ){
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user!=null;
                            if (user.isEmailVerified()){
                                Constants.myEditShared.putBoolean(Constants.IS_LOGIN, true);
                                Constants.myEditShared.commit();
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                requireActivity().finish();
                            }else{
                                Toast.makeText(getContext(), "Email is not verified", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Toast.makeText(getContext(), "Email or password is wrong", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();

                    }
                });

            }
        } else if (binding.tvForgotPassword.equals(view)) {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_forgotFragment);
        }
    }

    private void init() {
        binding.btnSignIn.setOnClickListener(this);
        binding.btnNewUser.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getContext());

    }
}