package com.example.lightbrains.common;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.daimajia.androidanimations.library.Techniques;
import com.example.lightbrains.R;
import com.example.lightbrains.dialogs.CustomDialogFragmentForExit;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Constants {
    public static final String SPEED_FLASH_CARDS = "SpeedFlashCards";
    public static final String SPEED_MENTAL = "SpeedMental";

    public static final String DIGIT_FLASH_CARDS = "DigitFlashCards";
    public static final String DIGIT_MENTAL = "DigitMental";

    public static final String COUNT_FLASH_CARDS = "CountFlashCards";
    public static final String COUNT_OF_EXAMPLES_MENTAL = "CountOfExamplesMental";
    public static final String COUNT_OF_ROWS_MENTAL = "CountOfRowsMental";
    public static final String TOPIC_LEVEL_MENTAL = "TopicLevelMental";
    public static final String SUBTOPIC_LEVEL_MENTAL = "SubtopicLevelMental";


    public static final String SCORES = "Scores";

    public static final String IS_LOGIN = "Is log in";

    public static final String CHECKED_LANGUAGE = "CheckedL";

    public static final String DIALOG_TAG = "customDialog";
    public static final String HASHMAP_BUNDLE = "HashMap";

    public static final String FIGURES_TYPE = "FigureType";
    public static final String FIGURES_LEVEL = "FiguresLevel";
    public static final String FIGURES_SHOW_TIME = "FiguresShowTime";
    public static final String FIGURES_COUNT = "FiguresCount";
    public static final String FIGURES_GROUP_COUNT = "FiguresGroupCount";
    public static final String FIGURES_COMPLEXITY_LEVEL = "ComplexityLevel";
    public static final String DIALOG_TAG_EXIT = "exit dialog";

    public static final String FIGURES_SHOW_START_TIME = "FiguresShowStartTime";
    public static final String FIGURES_SHOW_FINISH_TIME = "FiguresShowFinishTime";



    public static SharedPreferences sharedPreferences = null;
    public static SharedPreferences.Editor myEditShared = null;


    public static int[] rightAnswersRes = new int[]{R.drawable.img_smile_eye_smile, R.drawable.img_laughing_smile, R.drawable.img_smiling_smile, R.drawable.img_heart_eyes_smile};

    public static ArrayList<Techniques> animations = new ArrayList<>(Arrays.asList(Techniques.RollIn,Techniques.Wave,Techniques.ZoomIn,
            Techniques.SlideInUp, Techniques.Pulse, Techniques.FlipInX
    ));


    public static void createSharedPreferences(Activity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences("MySharedPref", MODE_PRIVATE);
            myEditShared = sharedPreferences.edit();
        }
    }

    public static int getRandomInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max + 1 - min) + min;
    }


    public static void setEdtAnswerFocused(Context context, TextInputEditText edtAnswer) {
        edtAnswer.requestFocus();
        if (edtAnswer.hasWindowFocus()) {
            // No need to wait for the window to get focus.
            showTheKeyboardNow(context, edtAnswer);
        } else {
            edtAnswer.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
                @Override
                public void onWindowFocusChanged(boolean hasFocus) {
                    if (hasFocus) {
                        showTheKeyboardNow(context, edtAnswer);
                        edtAnswer.getViewTreeObserver().removeOnWindowFocusChangeListener(this);
                    }
                }
            });
        }
    }

    private static void showTheKeyboardNow(Context context, TextInputEditText edtAnswer) {
        if (edtAnswer.isFocused()) {
            edtAnswer.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtAnswer, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void closeKeyboard(Activity activity)
    {
        View view = activity.getCurrentFocus();

        if (view != null) {
            InputMethodManager manager
                    = (InputMethodManager)
                    activity.getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }

}
