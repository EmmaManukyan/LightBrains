package com.example.lightbrains.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.lightbrains.R;

public class CustomDialogForSignUpFragment extends DialogFragment {

    private int DIALOG_CODE;
    //0 - from sign up fragment
    //1 - from flash anzan

    public CustomDialogForSignUpFragment(int DIALOG_CODE) {
        this.DIALOG_CODE = DIALOG_CODE;
    }

    ImageButton btnOk;

    Button btnYes;
    View mainView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.dialog_layout, container, false);
        return mainView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);


        builder.setCancelable(false);
        btnOk = view.findViewById(R.id.btn_ok);
        btnYes = view.findViewById(R.id.btn_yes_back);
        btnOk.setVisibility(View.GONE);
        btnYes.setVisibility(View.GONE);


        if (DIALOG_CODE == 0) {
            view.setBackground(getResources().getDrawable(R.drawable.background_btn2_dialog));
            btnOk.setVisibility(View.VISIBLE);

        } else if (DIALOG_CODE == 1) {
            view.setBackgroundColor(getResources().getColor(R.color.green));
            btnYes.setVisibility(View.VISIBLE);
        }


        btnOk.setOnClickListener(view1 -> dismiss());


        builder.setView(view);

        return builder.create();
    }

}
