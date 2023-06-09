package com.example.lightbrains.firstpages;

import static com.example.lightbrains.common.ConstantsForFireBase.mAuth;
import static com.example.lightbrains.common.ConstantsForFireBase.myDataBase;
import static com.example.lightbrains.common.ConstantsForFireBase.progressDialog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentSignUpBinding;
import com.example.lightbrains.dialogs.CustomDialogForSignUpFragment;
import com.example.lightbrains.firebase_classes.User;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding binding;

    //this variable is used to change editTexts' errorText if user wrote something wrong
    private String passwordError = "";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            //Toast.makeText(getContext(), "User already exists", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        binding.includedLayout.tvLayPassword.setError(null);
        binding.includedLayout.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //check if the password written by user doesn't contain mistakes
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                binding.includedLayout.tvLayPassword.setHelperText(getResources().getString(R.string.enter_minimum_8_char));
                binding.includedLayout.tvLayPassword.setError("");
                passwordError = getResources().getString(R.string.enter_minimum_8_char);
                if (password.contains(" ")) {
                    binding.includedLayout.tvLayPassword.setHelperText("");
                    passwordError = getResources().getString(R.string.password_cant_contain_space);
                    binding.includedLayout.tvLayPassword.setError(passwordError);
                }
                if (password.length() > ConstantsForFireBase.PASSWORD_MAX_LENGTH) {
                    binding.includedLayout.tvLayPassword.setHelperText("");
                    passwordError = getResources().getString(R.string.passwords_max_length_is) + ConstantsForFireBase.PASSWORD_MAX_LENGTH;
                    binding.includedLayout.tvLayPassword.setError(passwordError);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.imgBtnSignup.setOnClickListener(view1 -> {
            Constants.createSound(requireActivity(),R.raw.wrong);


            binding.includedLayout.tvLayName.setErrorEnabled(false);
            binding.includedLayout.tvLayMail.setErrorEnabled(false);
            binding.includedLayout.tvLayPassword.setErrorEnabled(false);
            binding.includedLayout.tvLayRepeatPassword.setErrorEnabled(false);
            String password = Objects.requireNonNull(binding.includedLayout.edtPassword.getText()).toString();

            //checking if everything is normal to create user in Firebase and send a verification email
            if (Objects.requireNonNull(binding.includedLayout.edtName.getText()).toString().equals("")) {
                binding.includedLayout.tvLayName.setError(getResources().getString(R.string.enter_name));
            } else if (Objects.requireNonNull(binding.includedLayout.edtMail.getText()).toString().equals("")) {
                binding.includedLayout.tvLayMail.setError(getResources().getString(R.string.enter_email));
            } else if (Objects.requireNonNull(binding.includedLayout.edtPassword.getText()).toString().equals("")) {
                binding.includedLayout.tvLayPassword.setHelperText("");
                binding.includedLayout.tvLayPassword.setError(getResources().getString(R.string.enter_password));
            } else if (password.equals("") || password.length() > ConstantsForFireBase.PASSWORD_MAX_LENGTH || password.length() < 8 || password.contains(" ")) {
                binding.includedLayout.tvLayPassword.setHelperText("");
                binding.includedLayout.tvLayPassword.setError(passwordError);
            } else if (!Objects.requireNonNull(binding.includedLayout.edtRepeatPassword.getText()).toString().equals(binding.includedLayout.edtPassword.getText().toString())) {
                binding.includedLayout.tvLayRepeatPassword.setError(getResources().getString(R.string.this_password_is_different));
            } else if (ConstantsForFireBase.checkConnectionIsOff(requireActivity())) {
                Constants.createToast(getContext(), R.string.you_are_offline);
            } else {
                Constants.createSound(requireActivity(),R.raw.sound_first_pages);

                String email = binding.includedLayout.edtMail.getText().toString();
                progressDialog = new ProgressDialog(getContext(), R.style.MyStyleForProgressDialog);
                ConstantsForFireBase.showProgressDialog(progressDialog, getResources().getString(R.string.registration), requireContext());

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser curUser = mAuth.getCurrentUser();
                                assert curUser != null;
                                saveUser(curUser.getUid());
                                sendEmailVerification(view, curUser);
                                progressDialog.dismiss();
                            } else {
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthWeakPasswordException e) {
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    Constants.createToast(getContext(), R.string.email_is_invalid);
                                } catch (FirebaseAuthUserCollisionException e) {
                                    Constants.createToast(getContext(), R.string.email_is_already_signed_up);
                                } catch (Exception e) {
                                    Constants.createToast(getContext(), R.string.something_went_wrong);

                                }
                                progressDialog.dismiss();
                            }
                        });


            }
            Constants.makeSoundEffect();
        });
    }

    private void sendEmailVerification(View view, FirebaseUser curUser) {
        curUser.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                CustomDialogForSignUpFragment dialog = new CustomDialogForSignUpFragment(0);
                dialog.setCancelable(false);
                dialog.show(requireActivity().getSupportFragmentManager(), Constants.DIALOG_TAG);
                Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_signInFragment);
            } else {
                Constants.createToast(getContext(), R.string.email_verification_hasnt_been_sent);
            }
        });
    }

    private void init() {
        binding.includedLayout.tvLayPassword.setCounterMaxLength(ConstantsForFireBase.PASSWORD_MAX_LENGTH);
        binding.includedLayout.tvLayRepeatPassword.setCounterMaxLength(ConstantsForFireBase.PASSWORD_MAX_LENGTH);
    }

    //save user in realtime DB with fields userName, email and ID
    private void saveUser(String Uid) {
        String id = myDataBase.getKey();
        String userName = Objects.requireNonNull(binding.includedLayout.edtName.getText()).toString();
        String email = Objects.requireNonNull(binding.includedLayout.edtMail.getText()).toString();
        User newUser = new User(id, userName, email, 0, "hello",ConstantsForFireBase.USER_TOKEN);
        myDataBase.child(Uid).setValue(newUser);
        myDataBase.child(Uid).child(ConstantsForFireBase.IS_EMAIL_VERIFIED).setValue(false);

    }

}