package com.example.lightbrains.firstpages;

import static com.example.lightbrains.common.ConstantsForFireBase.PASSWORD_MAX_LENGTH;
import static com.example.lightbrains.common.ConstantsForFireBase.mAuth;
import static com.example.lightbrains.common.ConstantsForFireBase.myDataBase;
import static com.example.lightbrains.common.ConstantsForFireBase.progressDialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentSignInBinding;
import com.example.lightbrains.firebase_classes.User;
import com.example.lightbrains.homepage.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.Executor;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private FragmentSignInBinding binding;

    //это простой флаг, чтобы убедиться, что пользователь нажал на кнопку входа в систему
// и это не функция onDataChange, вызванная из другого класса, когда что-то изменилось в Firebase
    private boolean btnIsClicked = false;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        init();
        binding.btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Установка обработчика нажатия на кнопку входа
                signInWithGoogle();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        ConstantsForFireBase.showProgressDialog(progressDialog, getResources().getString(R.string.sign_in), getContext());
        mAuth.fetchSignInMethodsForEmail(account.getEmail())
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            boolean isEmailExists = result.getSignInMethods() != null
                                    && result.getSignInMethods().size() > 0;


                            // Email exists in Firebase Authentication
                            // Proceed with sign-in
                            mAuth.signInWithCredential(credential)
                                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign-in success, update UI with the signed-in user's information
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                if (!isEmailExists) {

                                                    saveUser(user);
                                                }
                                                progressDialog.dismiss();
                                                signIn(user);

                                                // You can access the user's information using 'user' object
                                            } else {
                                                Constants.createToast(requireContext(), R.string.something_went_wrong);
                                            }
                                        }
                                    });

                        }
                    }
                });
    }


    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //SignInFragment implements View.OnClickListener, so I have method onClick here I need to check which view has been clicked
    @Override
    public void onClick(View view) {
        btnIsClicked = true;
        if (binding.btnNewUser.equals(view)) {
            Constants.createSound(requireActivity(), R.raw.sound_first_pages);
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment);
        } else if (binding.btnSignIn.equals(view)) {
            //creating sound effect...
            Constants.createSound(requireActivity(), R.raw.wrong);


            binding.tvLayMail.setErrorEnabled(false);
            binding.tvLayPassword.setErrorEnabled(false);

            //checking if everything is normal user to sign in
            if (Objects.requireNonNull(binding.edtMail.getText()).toString().equals("")) {
                binding.tvLayMail.setError(getResources().getString(R.string.enter_email));
            } else if (Objects.requireNonNull(binding.edtPassword.getText()).toString().equals("")) {
                binding.tvLayPassword.setHelperText("");
                binding.tvLayPassword.setError(getResources().getString(R.string.enter_password));
            } else if (ConstantsForFireBase.checkConnectionIsOff(requireActivity())) {
                Constants.createToast(getContext(), R.string.you_are_offline);
            } else {
                //user sign in
                Constants.createSound(requireActivity(), R.raw.sound_first_pages);
                String email = binding.edtMail.getText().toString();
                String password = binding.edtPassword.getText().toString();
                btnIsClicked = true;

                ConstantsForFireBase.showProgressDialog(progressDialog, getResources().getString(R.string.sign_in), getContext());
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final boolean[] isSignedIn = {false};

                        FirebaseUser curUser = mAuth.getCurrentUser();
                        assert curUser != null;
                        myDataBase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (btnIsClicked) {
                                    isSignedIn[0] = Boolean.TRUE.equals(snapshot.child(curUser.getUid()).child(ConstantsForFireBase.IS_SIGNED_IN).getValue(Boolean.class));
                                    //checking if user's email is verified
                                    if (curUser.isEmailVerified()) {
                                        signIn(curUser);

                                    } else {
                                        Constants.createToast(getContext(), R.string.email_not_veified);
                                    }
                                }
                                btnIsClicked = false;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        Constants.createToast(getContext(), R.string.email_or_password_is_incprrect);
                    }
                    progressDialog.dismiss();

                });

            }
        } else if (binding.tvForgotPassword.equals(view)) {
            Constants.createSound(requireActivity(), R.raw.sound_first_pages);
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_forgotFragment);
        }
        Constants.makeSoundEffect();
    }

    //Обычно я создаю метод init и вызываю его при создании представления фрагмента или активности. Здесь я устанавливаю параметры макета моего представления,
//создаю экземпляры классов
    @SuppressLint("SetTextI18n")
    private void init() {
        binding.btnSignIn.setOnClickListener(this);
        binding.btnNewUser.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);
        binding.tvLayPassword.setCounterMaxLength(PASSWORD_MAX_LENGTH);
        binding.tvGuestInfo.setText(getResources().getString(R.string.tv_guest_info) + getResources().getString(R.string.email) + ": " +
                ConstantsForFireBase.GUEST_EMAIL + "\n" + getResources().getString(R.string.password) + ": " + ConstantsForFireBase.GUEST_PASSWORD);

        progressDialog = new ProgressDialog(getContext(), R.style.MyStyleForProgressDialog);

        // Конфигурация параметров Google Sign-In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Инициализация клиента Google Sign-In
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

    }

    //function to save data to sharedPreferences and mov to next activity
    private void signIn(FirebaseUser curUser) {
        Constants.myEditShared.putBoolean(Constants.IS_LOGIN, true);
        String newToken = User.generateToken();
        Constants.myEditShared.putString(ConstantsForFireBase.USER_TOKEN, newToken);
        myDataBase.child(curUser.getUid()).child(ConstantsForFireBase.USER_TOKEN).setValue(newToken);
        Constants.myEditShared.commit();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra(ConstantsForFireBase.USER_KEY, curUser.getUid());
        startActivity(intent);
        requireActivity().finish();
    }


    private void saveUser(FirebaseUser user) {
        Toast.makeText(requireContext(), "Saving user...", Toast.LENGTH_SHORT).show();
        String id = myDataBase.getKey();
        User newUser = new User(id, "Username", user.getEmail(), 0, "hello", ConstantsForFireBase.USER_TOKEN);
        myDataBase.child(user.getUid()).setValue(newUser);
        myDataBase.child(user.getUid()).child(ConstantsForFireBase.IS_EMAIL_VERIFIED).setValue(true);

    }

}