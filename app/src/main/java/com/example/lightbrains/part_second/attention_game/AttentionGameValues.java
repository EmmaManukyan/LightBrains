package com.example.lightbrains.part_second.attention_game;

public class AttentionGameValues {
    // Этот класс создан для хранения и управления значениями игры внимания.
    // Здесь определены различные статические поля, которые хранят уровень сложности, тип и уровень фигур, количество фигур и другие параметры игры.
    // Класс также содержит методы для получения и установки значений этих параметров.
    private static int complexityLevel = -1;
    private static int figuresType = -1;
    private static int figuresLevel = -1;
    private static int figuresCount = 3;
    private static int figuresGroupCount = 0;
    private static float showTime = 0.3f;

    private static int scores = 0;
    private static int rightAnswers = 0;

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        AttentionGameValues.count = count;
    }

    private static int count = 0;

    public static int getScores() {
        return scores;
    }

    public static void setScores(int scores) {
        AttentionGameValues.scores = scores;
    }

    public static int getRightAnswers() {
        return rightAnswers;
    }

    public static void setRightAnswers(int rightAnswers) {
        AttentionGameValues.rightAnswers = rightAnswers;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        AttentionGameValues.startTime = startTime;
    }

    private static long startTime;


    public static int getComplexityLevel() {
        return complexityLevel;
    }

    public static void setComplexityLevel(int complexityLevel) {
        AttentionGameValues.complexityLevel = complexityLevel;
    }

    public static int getFiguresType() {
        return figuresType;
    }

    public static void setFiguresType(int figuresType) {
        AttentionGameValues.figuresType = figuresType;
    }

    public static int getFiguresLevel() {
        return figuresLevel;
    }

    public static void setFiguresLevel(int figuresLevel) {
        AttentionGameValues.figuresLevel = figuresLevel;
    }

    public static int getFiguresCount() {
        return figuresCount;
    }

    public static void setFiguresCount(int figuresCount) {
        AttentionGameValues.figuresCount = figuresCount;
    }

    public static int getFiguresGroupCount() {
        return figuresGroupCount;
    }

    public static void setFiguresGroupCount(int figuresGroupCount) {
        AttentionGameValues.figuresGroupCount = figuresGroupCount;
    }

    public static float getShowTime() {
        return showTime;
    }

    public static void setShowTime(float showTime) {
        AttentionGameValues.showTime = showTime;
    }
}
