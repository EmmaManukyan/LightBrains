package com.example.lightbrains.firstpages;

import static com.example.lightbrains.common.Constants.languageLogs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onStart() {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int CHECKED_LANGUAGE = sh.getInt(Constants.CHECKED_LANGUAGE, 0);
        //if the user has already chosen a language, set it to the app
        setLocal(MainActivity.this,languageLogs[CHECKED_LANGUAGE]);
        super.onStart();
    }

    //setting the language of the app
    public void setLocal(Activity activity, String langCode) {
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


}