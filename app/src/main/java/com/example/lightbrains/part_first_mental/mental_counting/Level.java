package com.example.lightbrains.part_first_mental.mental_counting;

import java.util.Random;

interface Level {

    public int[] startArrayOneDigit(int size, int level);
    public int[] joinArrays(int[] arr1, int level);

    public int[] createArrayToCount(int digits, int count, int level);

    static boolean checkifIsNotPryamoy(int result, int b) {
        int tempResult = result >= 5 ? result - 5 : result;
        int tempB = getBottomPartOfNumber(b);


        //pryamoy
        if (tempResult + tempB >= 5 || tempResult + tempB < 0) {
            //System.out.println("  -tranq");
            return true;
        }
        return false;
    }

    static int getBottomPartOfNumber(int n) {
        if (n > 0) {
            return n >= 5 ? n - 5 : n;
        }
        if (n == 0) {
            return 0;
        } else {
            return n <= -5 ? n + 5 : n;
        }
    }

    static int getRandomInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max + 1 - min) + min;
    }


}
