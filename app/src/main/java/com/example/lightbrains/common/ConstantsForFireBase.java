package com.example.lightbrains.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.lightbrains.R;

public class ConstantsForFireBase {
    public static final int PASSWORD_MAX_LENGTH= 15;
    public static String IMAGE_DB = "ImageDB";
    public static String IMAGE_DB_CHILD = "user_image";
    public static String PROFILE_IMAGE_URI = "ImageUri";
    public static String DEFAULT_IMAGE_URI = "";

    public static String USER_KEY = "Users";
    public static String USER_NAME = "UserName";
    public static String GUEST_EMAIL = "example@gmail.com";
    public static String GUEST_PASSWORD = "12345678";
    public static ProgressDialog progressDialog;

    public static void showProgressDialog(ProgressDialog progressDialog,String title,Context context){
        progressDialog.setMessage(context.getResources().getString(R.string.wait_a_little));
        progressDialog.setTitle(title);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static boolean checkConnection(Activity activity) {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");


        // Initialize listener

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        return networkInfo == null || !networkInfo.isConnectedOrConnecting();
    }

}
