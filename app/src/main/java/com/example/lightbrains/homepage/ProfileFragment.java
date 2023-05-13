package com.example.lightbrains.homepage;

import static android.app.Activity.RESULT_OK;
import static com.example.lightbrains.common.ConstantsForFireBase.PASSWORD_MAX_LENGTH;
import static com.example.lightbrains.common.ConstantsForFireBase.mAuth;
import static com.example.lightbrains.common.ConstantsForFireBase.myDataBase;
import static com.example.lightbrains.common.ConstantsForFireBase.progressDialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.databinding.FragmentProfileBinding;
import com.example.lightbrains.dialogs.CustomDialogFragmentForExit;
import com.example.lightbrains.dialogs.CustomDialogToWritePasswordFragment;
import com.example.lightbrains.firebase_classes.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;


public class ProfileFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static FragmentProfileBinding binding;
    private String passwordError;
    private static final int REQUEST_CODE = 122;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                Log.d("TAG", "Image uri: " + data.getData());
                binding.imgProfile.setImageURI(data.getData());
                uploadImage();
            }
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewParams();
        init();

        binding.layDataContainer.setOnClickListener(new DoubleClickListener() {

            @Override
            void onDoubleClick(View v) {
                CustomDialogToWritePasswordFragment dialog = new CustomDialogToWritePasswordFragment((ProfileFragment) getParentFragment());
                dialog.show(requireActivity().getSupportFragmentManager(), Constants.DIALOG_TAG);
            }
        });



        binding.imgEditProfile.setOnClickListener(view1 -> {
            CustomDialogToWritePasswordFragment dialog = new CustomDialogToWritePasswordFragment(this);
            dialog.show(requireActivity().getSupportFragmentManager(), Constants.DIALOG_TAG);
        });

        binding.btnLogOut.setOnClickListener(view12 -> {
            showDialog(2);
        });

        binding.btnDelete.setOnClickListener(v -> {
            showDialog(5);
        });

        binding.btnConfirm.setOnClickListener(view13 -> {
            String password = Objects.requireNonNull(binding.edtPassword.getText()).toString();
            if (Objects.requireNonNull(binding.edtName.getText()).toString().equals("")) {
                binding.tvLayName.setError(getResources().getString(R.string.enter_name));
            } else if (ConstantsForFireBase.checkConnection(requireActivity())) {
                Constants.createToast(getContext(), R.string.you_are_offline);
            } else if (password.isEmpty()) {
                updateUserName(binding.edtName.getText().toString());
                disEnableViews();
            } else {
                updateUserName(binding.edtName.getText().toString());
                updateUserPassword(binding.edtPassword.getText().toString());
                disEnableViews();
            }
        });

        binding.btnCancel.setOnClickListener(view14 -> {
            setViewParams();
            disEnableViews();
        });

        binding.imgProfile.setOnClickListener(view15 -> {
            if (!ConstantsForFireBase.checkConnection(requireActivity())) {
                getImage();
            } else {
                Constants.createToast(getContext(), R.string.you_are_offline);
            }
        });
        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                binding.tvLayPassword.setHelperText(getResources().getString(R.string.enter_minimum_8_char));
                binding.tvLayPassword.setErrorEnabled(false);
                passwordError = getResources().getString(R.string.enter_minimum_8_char);
                if (password.contains(" ")) {
                    binding.tvLayPassword.setHelperText("");
                    passwordError = getResources().getString(R.string.password_cant_contain_space);
                    binding.tvLayPassword.setError(passwordError);
                }
                if (password.length() > ConstantsForFireBase.PASSWORD_MAX_LENGTH) {
                    binding.tvLayPassword.setHelperText("");
                    passwordError = getResources().getString(R.string.passwords_max_length_is) + " " + ConstantsForFireBase.PASSWORD_MAX_LENGTH;
                    binding.tvLayPassword.setError(passwordError);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updateUserName(String newName) {
        Constants.createSharedPreferences(requireActivity());
        String id = myDataBase.getKey();
        FirebaseUser curUser = mAuth.getCurrentUser();
        assert curUser != null;
        Log.d("taguhi", "saveuser:  " + Constants.sharedPreferences.getString(ConstantsForFireBase.PROFILE_IMAGE_URI, null));
        User newUser = new User(id, newName, curUser.getEmail(), Constants.sharedPreferences.getInt(Constants.SCORES,-1000), Constants.sharedPreferences.getString(ConstantsForFireBase.PROFILE_IMAGE_URI, null),true);
        if (id != null) {
            myDataBase.child(curUser.getUid()).setValue(newUser);
            String idMail = curUser.getEmail().replace(".","");
//            Log.d("fir",""+myDataBaseForMails.getDatabase());
//            myDataBase.child(ConstantsForFireBase.USERS_MAILS_KEY).child(idMail).setValue(true);
            binding.tvProfileName.setText(newUser.getUserName());
            Constants.myEditShared.putString(ConstantsForFireBase.USER_NAME, newUser.getUserName());
            Constants.myEditShared.commit();
            HomeActivity.binding.tvProfileName.setText(Constants.sharedPreferences.getString(ConstantsForFireBase.USER_NAME, null));
            Log.d("taguhi", "" + curUser.getUid() + "name " + newUser.getImageUri());
        }
    }


    @SuppressLint("UseCompatLoadingForColorStateLists")
    private void setViewParams() {
        binding.progressBarWithImage.setVisibility(View.VISIBLE);
        binding.edtName.setText(Constants.sharedPreferences.getString(ConstantsForFireBase.USER_NAME, null));
        binding.edtPassword.setText("");
        binding.edtPassword.setHintTextColor(getResources().getColorStateList(R.color.grey));
        binding.tvProfileName.setText(Constants.sharedPreferences.getString(ConstantsForFireBase.USER_NAME, null));
        Log.d("taguhi", "ui   " + Constants.sharedPreferences.getString(ConstantsForFireBase.PROFILE_IMAGE_URI, ConstantsForFireBase.DEFAULT_IMAGE_URI));
        Picasso.get().load(Constants.sharedPreferences.getString(ConstantsForFireBase.PROFILE_IMAGE_URI, null)).placeholder(R.drawable.img_profile_default).into(binding.imgProfile);
        binding.progressBarWithImage.setVisibility(View.GONE);
        HomeActivity.binding.frContainer.setVisibility(View.VISIBLE);

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
        binding.imgEditProfile.setVisibility(View.GONE);
        binding.btnConfirm.setVisibility(View.VISIBLE);
        binding.btnCancel.setVisibility(View.VISIBLE);
        binding.btnDelete.setVisibility(View.VISIBLE);
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
        binding.imgEditProfile.setVisibility(View.VISIBLE);
        binding.btnConfirm.setVisibility(View.GONE);
        binding.btnCancel.setVisibility(View.GONE);
        binding.btnDelete.setVisibility(View.GONE);
    }

    private void showDialog(int dialogCode) {
        CustomDialogFragmentForExit customDialogFragmentForExit = new CustomDialogFragmentForExit(dialogCode);
        customDialogFragmentForExit.show(requireActivity().getSupportFragmentManager(), Constants.DIALOG_TAG_EXIT);
    }

    private void updateUserPassword(String newPassword) {
        if (newPassword.equals("") || newPassword.length() > ConstantsForFireBase.PASSWORD_MAX_LENGTH || newPassword.length() < 8 || newPassword.contains(" ")) {
            binding.tvLayPassword.setHelperText("");
            binding.tvLayPassword.setError(passwordError);
        } else {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            progressDialog = new ProgressDialog(getContext(), R.style.MyStyleForProgressDialog);
            ConstantsForFireBase.showProgressDialog(progressDialog, getResources().getString(R.string.wait_a_little), getContext());
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
//                            Log.d("taguhi", "User password updated.");
                            Constants.createToast(getContext(), R.string.confirmed);
                        } else {
//                            Log.d("taguhi", "" + task.getException());

                            Constants.createToast(getContext(), R.string.something_went_wrong);
                        }
                        progressDialog.dismiss();
                    });
        }

    }

    public void reAuthenticateUser(String password) {
        Constants.closeKeyboard(requireActivity());
        progressDialog = new ProgressDialog(getContext(), R.style.MyStyleForProgressDialog);
        ConstantsForFireBase.showProgressDialog(progressDialog, getResources().getString(R.string.wait_a_little), getContext());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        AuthCredential credential = EmailAuthProvider
                .getCredential(Objects.requireNonNull(user.getEmail()), password);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        enableViews();
                    } else {
                        Constants.createToast(getContext(), R.string.password_is_wrong);
                    }
                    progressDialog.dismiss();
                });
    }

    private static Uri uploadUri;
    private StorageReference myStorageReference;

    private void getImage() {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, REQUEST_CODE);
    }

    private void uploadImage() {
        binding.btnLogOut.setEnabled(false);
        Bitmap bitmap = ((BitmapDrawable) binding.imgProfile.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArr = baos.toByteArray();
        StorageReference myRef = myStorageReference.child(ConstantsForFireBase.USER_KEY).child(ConstantsForFireBase.IMAGE_DB_CHILD);
        UploadTask up = myRef.putBytes(byteArr);
        Constants.createSharedPreferences(requireActivity());
        binding.imgProfile.setVisibility(View.INVISIBLE);
        binding.progressBarWithImage.setVisibility(View.VISIBLE);
        Task<Uri> task = up.continueWithTask(task12 -> myRef.getDownloadUrl()).addOnCompleteListener(task1 -> {
            uploadUri = task1.getResult();
            Constants.myEditShared.putString(ConstantsForFireBase.PROFILE_IMAGE_URI, String.valueOf(uploadUri));
            Constants.myEditShared.commit();
            binding.btnLogOut.setEnabled(true);
            binding.progressBarWithImage.setVisibility(View.GONE);
            binding.imgProfile.setVisibility(View.VISIBLE);
            Picasso.get().load(Constants.sharedPreferences.getString(ConstantsForFireBase.PROFILE_IMAGE_URI, null)).placeholder(R.drawable.img_profile_default).into(HomeActivity.binding.imgProfile);
            if (!ConstantsForFireBase.checkConnection(requireActivity())) {
                saveUser(true);
            }
        });
    }

    private void init() {
        passwordError = getResources().getString(R.string.enter_password);
        myStorageReference = FirebaseStorage.getInstance().getReference(ConstantsForFireBase.IMAGE_DB);
        binding.tvLayPassword.setCounterMaxLength(PASSWORD_MAX_LENGTH);

    }

    public static void saveUser(boolean isSignedIn) {
        String id = myDataBase.getKey();
        String name = Objects.requireNonNull(binding.edtName.getText()).toString();
        if (!TextUtils.isEmpty(name)) {
            FirebaseUser curUser = mAuth.getCurrentUser();
            assert curUser != null;
//            Log.d("taguhi", "saveuser:  " + Constants.sharedPreferences.getString(ConstantsForFireBase.PROFILE_IMAGE_URI, null));
            User newUser = new User(id, name, curUser.getEmail(), Constants.sharedPreferences.getInt(Constants.SCORES,-1000), Constants.sharedPreferences.getString(ConstantsForFireBase.PROFILE_IMAGE_URI, null),isSignedIn);
            if (id != null) {
                myDataBase.child(curUser.getUid()).setValue(newUser);
//                myDataBase.child(ConstantsForFireBase.USERS_MAILS_KEY).child(curUser.getEmail().replace(".","")).setValue(isSignedIn);
            }
        }
    }

    abstract static class DoubleClickListener implements View.OnClickListener {
        private Long lastClickTime = 0L;

        @Override
        public void onClick(View v) {
            long clickTime = System.currentTimeMillis();
            long DOUBLE_CLICK_TIME_DELTA = 300L;
            if (clickTime-lastClickTime< DOUBLE_CLICK_TIME_DELTA){
                onDoubleClick(v);
            }
            lastClickTime = clickTime;
        }
        abstract void onDoubleClick(View v);

    }


}
