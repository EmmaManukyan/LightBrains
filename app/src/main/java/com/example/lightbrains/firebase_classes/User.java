package com.example.lightbrains.firebase_classes;

public class User {
    public String getIdKey() {
        return idKey;
    }

    private String idKey;
    private String userName;
    private String email;
    private int scores;

    public String getImageUri() {
        return imageUri;
    }

    private String imageUri;

    User(){}

    public User(String idKey, String userName, String email, int scores,String imageUri) {
        this.idKey = idKey;
        this.userName = userName;
        this.email = email;
        this.scores = scores;
        this.imageUri = imageUri;
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
}
