package com.example.lightbrains.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConstantsForFireBase {
    public static final int PASSWORD_MAX_LENGTH= 15;
    public static String USER_KEY = "Users";
    public static String USER_NAME = "UserName";
    public static ProgressDialog progressDialog;

    public static void showProgressDialog(ProgressDialog progressDialog){
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Registration");
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
