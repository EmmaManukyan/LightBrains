package com.example.lightbrains.firstpages;

import static com.example.lightbrains.common.ConstantsForFireBase.PASSWORD_MAX_LENGTH;
import static com.example.lightbrains.common.ConstantsForFireBase.progressDialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentSignInBinding;
import com.example.lightbrains.homepage.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private FragmentSignInBinding binding;
    private FirebaseAuth mAuth;
    private boolean guestOpened = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        init();
        binding.imgGuest.setOnClickListener(v -> {
            binding.tvGuestInfo.setVisibility(!guestOpened? View.VISIBLE:View.GONE);
            YoYo.with(!guestOpened? Techniques.SlideInRight:Techniques.SlideOutRight).duration(500).playOn(binding.tvGuestInfo);
            guestOpened = !guestOpened;
        });
        return binding.getRoot();
    }


    @Override
    public void onClick(View view) {
        if (binding.btnNewUser.equals(view)) {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment);
        } else if (binding.btnSignIn.equals(view)) {
            binding.tvLayMail.setErrorEnabled(false);
            binding.tvLayPassword.setErrorEnabled(false);
            if (Objects.requireNonNull(binding.edtMail.getText()).toString().equals("")) {
                binding.tvLayMail.setError(getResources().getString(R.string.enter_email));
            } else if (Objects.requireNonNull(binding.edtPassword.getText()).toString().equals("")) {
                binding.tvLayPassword.setHelperText("");
                binding.tvLayPassword.setError(getResources().getString(R.string.enter_password));
            } else if (ConstantsForFireBase.checkConnection(requireActivity())) {
                Constants.createToast(getContext(), R.string.you_are_offline);
            } else {
                String email = binding.edtMail.getText().toString();
                String password = binding.edtPassword.getText().toString();
                progressDialog = new ProgressDialog(getContext(), R.style.MyStyleForProgressDialog);
                ConstantsForFireBase.showProgressDialog(progressDialog, getResources().getString(R.string.sign_in), getContext());

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        if (user.isEmailVerified() || Objects.equals(user.getEmail(), ConstantsForFireBase.GUEST_EMAIL)) {
                            Constants.myEditShared.putBoolean(Constants.IS_LOGIN, true);
                            Constants.myEditShared.commit();
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.putExtra(ConstantsForFireBase.USER_KEY, user.getUid());
//                            Log.d("taguhi","user id   "+user.getUid());
                            startActivity(intent);
                            requireActivity().finish();
                        }  else {
                            Constants.createToast(getContext(), R.string.email_not_veified);
                        }

                    } else {
                        Constants.createToast(getContext(), R.string.email_or_password_is_incprrect);
                    }
                    progressDialog.dismiss();

                });

            }
        } else if (binding.tvForgotPassword.equals(view)) {
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_forgotFragment);
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        binding.btnSignIn.setOnClickListener(this);
        binding.btnNewUser.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        binding.tvLayPassword.setCounterMaxLength(PASSWORD_MAX_LENGTH);
        binding.tvGuestInfo.setText(getResources().getString(R.string.tv_guest_info)+getResources().getString(R.string.email)+": "+
                ConstantsForFireBase.GUEST_EMAIL+"\n"+getResources().getString(R.string.password)+": "+ConstantsForFireBase.GUEST_PASSWORD);
        mAuth = FirebaseAuth.getInstance();
    }

}