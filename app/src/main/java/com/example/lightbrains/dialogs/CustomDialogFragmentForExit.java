package com.example.lightbrains.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lightbrains.R;

public class CustomDialogFragmentForExit extends DialogFragment {


    //0->flashCards
    //1->mentalCount
    private int DIALOG_POSITION_CODE;

    public CustomDialogFragmentForExit(int DIALOG_POSITION_CODE) {
        this.DIALOG_POSITION_CODE = DIALOG_POSITION_CODE;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.do_you_really_want_to_exit)).
                setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavHostFragment navHostFragment;
                        NavController navController;
                        switch (DIALOG_POSITION_CODE){
                            case 0:
                                navHostFragment =
                                        (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView_flash_cards);
                                assert navHostFragment != null;
                                navController = navHostFragment.getNavController();
                                navController.navigate(R.id.action_showFlashCardsFragment_to_fleshAnzanSettingsFragment);
                                break;
                            case 1:
                                navHostFragment =
                                        (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView_mental_counting);
                                assert navHostFragment != null;
                                navController = navHostFragment.getNavController();
                                navController.navigate(R.id.action_showMentalCountFragment_to_mentalCountingSettingsFragment);
                                break;
                        }

                        dismiss();
                    }
                }).
                setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }

}
