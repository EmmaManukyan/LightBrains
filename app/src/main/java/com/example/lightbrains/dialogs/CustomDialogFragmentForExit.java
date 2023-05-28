package com.example.lightbrains.dialogs;

import static com.example.lightbrains.common.ConstantsForFireBase.myDataBase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.firstpages.MainActivity;
import com.example.lightbrains.homepage.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CustomDialogFragmentForExit extends DialogFragment {


    //0->flashCards
    //1->mentalCount
    //2->appLogout
    //3->att_game
    //4->att_game_write_answers
    //5->delete accaunt
    //6->memory_game
    private final int DIALOG_POSITION_CODE;

    public CustomDialogFragmentForExit(int DIALOG_POSITION_CODE) {
        this.DIALOG_POSITION_CODE = DIALOG_POSITION_CODE;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Constants.createSharedPreferences(getActivity());
        Constants.createSound(requireActivity(),R.raw.wrong);
        Constants.makeSoundEffect();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (DIALOG_POSITION_CODE == 5) {
            builder.setTitle(getResources().getString(R.string.do_you_really_want_to_delete_your_account));
        } else {
            builder.setTitle(getResources().getString(R.string.do_you_really_want_to_exit));
        }
        builder.setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {
                    NavHostFragment navHostFragment;
                    NavController navController;
                    switch (DIALOG_POSITION_CODE) {
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
                            Log.d("dilijan","mta");
                            ProfileFragment.saveUser(false);
                            Constants.myEditShared.clear();
                            Log.d("dilijan","cleared");
                            Constants.myEditShared.commit();
                            FirebaseAuth.getInstance().signOut();
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
                        case 5:
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            assert user != null;
                            myDataBase.child(user.getUid()).removeValue();
//                            myDataBase.child(ConstantsForFireBase.USERS_MAILS_KEY).child(user.getEmail().replace(".","")).removeValue();
                            Toast.makeText(getContext(), getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                            Constants.myEditShared.clear();
                            Constants.myEditShared.commit();
                            user.delete()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "User account deleted.");
                                            FirebaseAuth.getInstance().signOut();

                                        } else {
                                            Toast.makeText(getContext(), "chexav", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            FirebaseAuth.getInstance().signOut();
                            Intent intent1 = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent1);
                            requireActivity().finish();
                            break;
                        case 6:
                            navHostFragment =
                                    (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fr_container_memory_game);
                            assert navHostFragment != null;
                            navController = navHostFragment.getNavController();
                            navController.navigate(R.id.action_memoryGameShowCardsFragment_to_memoryGameSettingsFragment);
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
