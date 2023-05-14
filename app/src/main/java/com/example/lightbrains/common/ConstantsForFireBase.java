package com.example.lightbrains.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.lightbrains.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConstantsForFireBase {
    public static final int PASSWORD_MAX_LENGTH= 15;
    public static String IMAGE_DB = "ImageDB";
    public static String IMAGE_DB_CHILD = "user_image";
    public static String PROFILE_IMAGE_URI = "ImageUri";
    public static String DEFAULT_IMAGE_URI = "";

    public static String USER_KEY = "Users";
    public static String USERS_MAILS_KEY = "UserMails";
    public static String USER_NAME = "userName";
    public static String IS_SIGNED_IN = "signedIn";
    public static String USER_TOKEN = "userToken";
    public static String IS_EMAIL_VERIFIED = "emailIsVerified";
    public static String GUEST_EMAIL = "example@gmail.com";
    public static String GUEST_PASSWORD = "12345678";
    public static ProgressDialog progressDialog;

    public static DatabaseReference myDataBase;
    public static FirebaseAuth mAuth;

    public static void createFireBaseInstances(){
        myDataBase = FirebaseDatabase.getInstance().getReference(ConstantsForFireBase.USER_KEY);
        Log.d("fir","exav");
        mAuth = FirebaseAuth.getInstance();
    }


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
