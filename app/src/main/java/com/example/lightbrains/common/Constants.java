package com.example.lightbrains.common;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

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


    public static final String SCORES = "scores";

    public static final String IS_LOGIN = "Is log in";

    public static final String CHECKED_LANGUAGE = "CheckedL";

    public static final String DIALOG_TAG = "customDialog";
    public static final String HASHMAP_BUNDLE = "HashMap";

    public static final String FIGURES_TYPE_MEMORY_GAME = "FigureTypeMemoryGame";
    public static final String FIGURES_TYPE = "FigureType";
    public static final String FIGURES_SHOW_TIME = "FiguresShowTime";
    public static final String FIGURES_GROUP_COUNT = "FiguresGroupCount";
    public static final String COUNT_OF_ROWS = "CountOfRows";
    public static final String COUNT_OF_COLUMNS = "CountOfColumns";
    public static final String FIGURES_COMPLEXITY_LEVEL = "ComplexityLevel";
    public static final String DIALOG_TAG_EXIT = "exit dialog";


    public static final String RIGHT_ANSWERS = "RightAnswers";
    public static final String RIGHT = "right";
    public static final String WRONG = "wrong";
    public static final String USE_INTERNET = "useInternet";
    public static final String SOUND_EFFECTS = "soundEffects";


    public static final String DEFAULT_CONTENT_DESCRIPTION = "default";
    public static final String CONTENT_DESCRIPTION_CLICKED = "clicked";
    public static final int FLIP_CARD_ANIM_DURATION = 300;
    public static final int YOYO_ANIM_DURATION = 1000;

    public static final String[] languageLogs = {"ru","hy","en"};




    public static SharedPreferences sharedPreferences = null;
    public static SharedPreferences.Editor myEditShared = null;

    private static MediaPlayer mediaPlayer = null;

    public static void createSound(Activity activity,int resId){
        mediaPlayer = MediaPlayer.create(activity,resId);
    }


    public static int[] rightAnswersRes = new int[]{R.drawable.img_smile_eye_smile, R.drawable.img_laughing_smile, R.drawable.img_smiling_smile, R.drawable.img_heart_eyes_smile};

    public static ArrayList<Techniques> animations = new ArrayList<>(Arrays.asList(Techniques.RollIn, Techniques.Wave, Techniques.ZoomIn,
            Techniques.SlideInUp, Techniques.Pulse, Techniques.FlipInX
    ));


    public static void createSharedPreferences(Activity activity) {
        if (sharedPreferences == null) {
            sharedPreferences = activity.getSharedPreferences("MySharedPref", MODE_PRIVATE);
            myEditShared = sharedPreferences.edit();
        }
    }

    public static void makeSoundEffect(){
        if (sharedPreferences.getBoolean(SOUND_EFFECTS,true)){
            mediaPlayer.start();
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

    public static void closeKeyboard(Activity activity) {
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

    public static void createToast(Context context,int stringId){
        Toast.makeText(context, context.getResources().getString(stringId), Toast.LENGTH_SHORT).show();
    }

}
