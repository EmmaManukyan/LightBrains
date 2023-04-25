package com.example.lightbrains.common;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.lightbrains.R;

public class ConstantsForFireBase {
    public static String USER_KEY = "Users";
    public static String USER_NAME = "UserName";
    public static ProgressDialog progressDialog;

    public static void showProgressDialog(ProgressDialog progressDialog){
        progressDialog.setMessage("Please wait...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
}
