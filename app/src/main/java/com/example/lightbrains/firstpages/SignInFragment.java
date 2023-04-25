package com.example.lightbrains.firstpages;

import static com.example.lightbrains.common.ConstantsForFireBase.progressDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentSignInBinding;
import com.example.lightbrains.firebase_classes.ConnectionReceiver;
import com.example.lightbrains.homepage.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignInFragment extends Fragment implements View.OnClickListener,ConnectionReceiver.ReceiverListener {

    private FragmentSignInBinding binding;
    private FirebaseAuth mAuth;



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
            binding.tvLayMail.setErrorEnabled(false);
            binding.tvLayPassword.setErrorEnabled(false);
            if (Objects.requireNonNull(binding.edtMail.getText()).toString().equals("")) {
                binding.tvLayMail.setError("Enter mail");
            } else if (Objects.requireNonNull(binding.edtPassword.getText()).toString().equals("")) {
                binding.tvLayPassword.setHelperText("");
                binding.tvLayPassword.setError("Enter password");
            }else if(!checkConnection()){
                Toast.makeText(getContext(), "There is no internet", Toast.LENGTH_SHORT).show();
            } else {
                String email = binding.edtMail.getText().toString();
                String password = binding.edtPassword.getText().toString();
                progressDialog = new ProgressDialog(getContext(),R.style.MyStyleForProgressDialog);
                ConstantsForFireBase.showProgressDialog(progressDialog);

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
                                intent.putExtra(ConstantsForFireBase.USER_KEY,user.getUid());
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
    }

    private boolean checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");


        // Initialize listener
        ConnectionReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
        Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
    }

}