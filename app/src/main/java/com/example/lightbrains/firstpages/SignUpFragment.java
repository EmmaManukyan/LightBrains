package com.example.lightbrains.firstpages;

import static com.example.lightbrains.common.ConstantsForFireBase.USER_KEY;
import static com.example.lightbrains.common.ConstantsForFireBase.progressDialog;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentSignUpBinding;
import com.example.lightbrains.dialogs.CustomDialogForSignUpFragment;
import com.example.lightbrains.firebase_classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class SignUpFragment extends Fragment{
    private FragmentSignUpBinding binding;
    private String passwordError = "";
    private FirebaseAuth mAuth;

    private DatabaseReference myDataBase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(getContext(), "User already exists", Toast.LENGTH_SHORT).show();
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
                if (password.length() > ConstantsForFireBase.PASSWORD_MAX_LENGTH) {
                    binding.includedLayout.tvLayPassword.setHelperText("");
                    passwordError = "Password's max length is "+ConstantsForFireBase.PASSWORD_MAX_LENGTH;
                    binding.includedLayout.tvLayPassword.setError(passwordError);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.imgBtnSignup.setOnClickListener(view1 -> {
            binding.includedLayout.tvLayName.setErrorEnabled(false);
            binding.includedLayout.tvLayMail.setErrorEnabled(false);
            binding.includedLayout.tvLayPassword.setErrorEnabled(false);
            String password = Objects.requireNonNull(binding.includedLayout.edtPassword.getText()).toString();
            if (Objects.requireNonNull(binding.includedLayout.edtName.getText()).toString().equals("")) {
                binding.includedLayout.tvLayName.setError("Enter name");
            } else if (Objects.requireNonNull(binding.includedLayout.edtMail.getText()).toString().equals("")) {
                binding.includedLayout.tvLayMail.setError("Enter mail");
            } else if (password.equals("") || password.length() > ConstantsForFireBase.PASSWORD_MAX_LENGTH || password.length() < 8 || password.contains(" ")) {
                binding.includedLayout.tvLayPassword.setHelperText("");
                binding.includedLayout.tvLayPassword.setError(passwordError);
            } else if(ConstantsForFireBase.checkConnection(requireActivity())){
                Toast.makeText(getContext(), "There is no internet", Toast.LENGTH_SHORT).show();
            } else {
                String email = binding.includedLayout.edtMail.getText().toString();
                progressDialog = new ProgressDialog(getContext(), R.style.MyStyleForProgressDialog);
                ConstantsForFireBase.showProgressDialog(progressDialog,"Registration");

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser curUser = mAuth.getCurrentUser();
                                assert curUser != null;
                                saveUser(curUser.getUid());
                                sendEmailVerification(view,curUser);
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });


            }
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
                Toast.makeText(getContext(), "Email verification hasn't been send", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        myDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        binding.includedLayout.tvLayPassword.setCounterMaxLength(ConstantsForFireBase.PASSWORD_MAX_LENGTH);
    }

    private void saveUser(String Uid) {
        String id = myDataBase.getKey();
        String userName = Objects.requireNonNull(binding.includedLayout.edtName.getText()).toString();
        String email = Objects.requireNonNull(binding.includedLayout.edtMail.getText()).toString();
        User newUser = new User(id,userName,email,0,"hello");
        myDataBase.child(Uid).setValue(newUser);

    }

}