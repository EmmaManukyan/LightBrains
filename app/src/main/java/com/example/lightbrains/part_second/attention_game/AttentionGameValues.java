package com.example.lightbrains.part_second.attention_game;

public class AttentionGameValues {
    private static int complexityLevel = -1;
    private static int figuresType = -1;
    private static int figuresLevel = -1;
    private static int figuresCount = 3;
    private static int figuresGroupCount = 0;
    private static float showTime = 0.3f;

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
