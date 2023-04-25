package com.example.lightbrains.firebase_classes;

public class User {
    private String idKey;
    private String userName;
    private String email;
    private int scores;
    User(){}

    public User(String idKey, String userName, String email, int scores) {
        this.idKey = idKey;
        this.userName = userName;
        this.email = email;
        this.scores = scores;
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
