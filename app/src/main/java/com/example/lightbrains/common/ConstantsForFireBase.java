package com.example.lightbrains.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConstantsForFireBase {
    public static final int PASSWORD_MAX_LENGTH= 15;
    public static String IMAGE_DB = "ImageDB";
    public static String IMAGE_DB_CHILD = "user_image";
    public static String PROFILE_IMAGE_URI = "ImageUri";
    public static String DEFAULT_IMAGE_URI = "https://firebasestorage.googleapis.com/v0/b/lightbrains-5d955.appspot.com/o/ImageDB%2FUsers%2Fuser_image?alt=media&token=59633fd0-68c5-471b-8c17-2f8d82893790";

    public static String USER_KEY = "Users";
    public static String USER_NAME = "UserName";
    public static ProgressDialog progressDialog;

    public static void showProgressDialog(ProgressDialog progressDialog,String title){
        progressDialog.setMessage("Please wait...");
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
