package com.example.lightbrains.common;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

public class Constants {
     public static final String SPEED_FLASH_CARDS = "SpeedFlashCards";
     public static final String SPEED_MENTAL = "SpeedMental";

     public static final String DIGIT_FLASH_CARDS = "DigitFlashCards";
     public static final String DIGIT_MENTAL = "DigitMental";

     public static final String COUNT_FLASH_CARDS = "CountFlashCards";
     public static final String COUNT_MENTAL = "CountMental";
     public static final String TOPIC_LEVEL_MENTAL = "TopicLevelMental";


     public static final String SCORES = "Scores";

     public static final String IS_LOGIN = "Is log in";

     public static final String CHECKED_LANGUAGE = "CheckedL";

     public static final String DIALOG_TAG = "customDialog";

     public static final String FIGURES_TYPE = "FigureType";
     public static final String FIGURES_LEVEL = "FiguresLevel";
     public static final String FIGURES_SHOW_TIME = "FiguresShowTime";
     public static final String FIGURES_COUNT = "FiguresCount";
     public static final String FIGURES_GROUP_COUNT = "FiguresGroupCount";
     public static final String FIGURES_COMPLEXITY_LEVEL = "ComplexityLevel";


     public static SharedPreferences sharedPreferences = null;
     public static SharedPreferences.Editor myEditShared = null;

     public static void createSharedPreferences(Activity activity){
          if (sharedPreferences==null){
               sharedPreferences = activity.getSharedPreferences("MySharedPref", MODE_PRIVATE);
               myEditShared = sharedPreferences.edit();
          }
     }

}
