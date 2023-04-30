package com.example.lightbrains.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.firstpages.MainActivity;
import com.example.lightbrains.homepage.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class CustomDialogFragmentForExit extends DialogFragment {


    //0->flashCards
    //1->mentalCount
    //2->appLogout
    //3->att_game
    //4->att_game_write_answers
    private final int DIALOG_POSITION_CODE;

    public CustomDialogFragmentForExit(int DIALOG_POSITION_CODE) {
        this.DIALOG_POSITION_CODE = DIALOG_POSITION_CODE;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Constants.createSharedPreferences(getActivity());


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.do_you_really_want_to_exit)).
                setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {
                    NavHostFragment navHostFragment;
                    NavController navController;
                    switch (DIALOG_POSITION_CODE){
                        case 0:
                            navHostFragment =
                                    (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView_flash_cards);
                            assert navHostFragment != null;
                            navController = navHostFragment.getNavController();
                            navController.navigate(R.id.action_showFlashCardsFragment_to_fleshAnzanSettingsFragment);
                            break;
                        case 1:
                            navHostFragment =
                                    (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView_mental_counting);
                            assert navHostFragment != null;
                            navController = navHostFragment.getNavController();
                            navController.navigate(R.id.action_showMentalCountFragment_to_mentalCountingSettingsFragment);
                            break;
                        case 2:
                            ProfileFragment.saveUser();
                            FirebaseAuth.getInstance().signOut();
                            Constants.myEditShared.clear();
                            Constants.myEditShared.commit();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                            break;

                        case 3:
                            navHostFragment =
                                    (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView_att_game);
                            assert navHostFragment != null;
                            navController = navHostFragment.getNavController();
                            navController.navigate(R.id.action_attentionGameShowFiguresFragment_to_attentionGameSettingsFragment);
                            break;
                        case 4:
                            navHostFragment =
                                    (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView_att_game);
                            assert navHostFragment != null;
                            navController = navHostFragment.getNavController();
                            navController.navigate(R.id.action_attentionGameWriteAnswersFragment_to_attentionGameSettingsFragment);
                            break;
                    }

                    dismiss();
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
