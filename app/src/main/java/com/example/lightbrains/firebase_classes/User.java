package com.example.lightbrains.firebase_classes;

import java.util.UUID;

public class User {
    public String getIdKey() {
        return idKey;
    }

    private String idKey;
    private String userName;
    private String email;
    private int scores;

    private boolean isSignedIn;

    public String getImageUri() {
        return imageUri;
    }

    private String imageUri;

    private boolean emailIsVerified = true;

    private String userToken;

    User(){}

    public User(String idKey, String userName, String email, int scores,String imageUri,String userToken) {
        this.idKey = idKey;
        this.userName = userName;
        this.email = email;
        this.scores = scores;
        this.imageUri = imageUri;
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public int getScores() {
        return scores;
    }


    public boolean isEmailIsVerified() {
        return emailIsVerified;
    }

    public void setEmailIsVerified(boolean emailIsVerified) {
        this.emailIsVerified = emailIsVerified;
    }


    public boolean isSignedIn() {
        return isSignedIn;
    }

    public void setSignedIn(boolean signedIn) {
        isSignedIn = signedIn;
    }

    public static String generateToken(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
