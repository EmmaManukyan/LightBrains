package com.example.lightbrains.part_first_mental.mental_counting;

import java.util.Random;

public class Pryamoy implements Level {

    @Override
    public int[] startArrayOneDigit(int size, int level) {
        Random random = new Random();
        int[] arr = new int[size];
        int i = 0;
        int min = -9;
        int max = 9;
        int result = 0;
        int tempResult = 0;
        int b = 0;
        int tempB = 0;
        while (i < size) {
            b = random.nextInt(max + 1 - min) + min;
            if (b == 0 || (i == 0 && b < 0) || (result + b < 0 || result + b > 9)) {
                continue;
            }
            if (Level.checkifIsNotPryamoy(result, b)) {
                continue;
            }
            result += b;
            arr[i] = b;
            i++;
        }


        return arr;
    }


    private int[] getDigits(int[] startArray) {
        int[] arr = new int[startArray.length];
        int i = 0;

        int min = 0;
        int max = 9;
        int result = 0;
        int b = 0;
        int a;
        while (i < startArray.length) {
            b = Level.getRandomInRange(min, max);
            if (startArray[i] < 0) {
                b *= -1;
            }
            if ((i == 0 && b < 0) || (result + b < 0 || result + b > 9)) {
                continue;
            }
            if (Level.checkifIsNotPryamoy(result, b)) {
                continue;
            }
            result += b;
            arr[i] = b;
            i++;
        }

        return arr;
    }


    @Override
    public int[] joinArrays(int[] arr1, int level) {
        int[] myArr = new int[arr1.length];
        int[] arr2 = getDigits(arr1);

        for (int i = 0; i < myArr.length; i++) {

            String tempS = "";
            tempS = Integer.toString(arr1[i]);
            tempS += Math.abs(arr2[i]);
            myArr[i] = Integer.parseInt(tempS);

        }
        return myArr;
    }

    @Override
    public int[] createArrayToCount(int digits, int count, int level) {
        int[] tempArr = startArrayOneDigit(count, level);
        for (int i = 0; i < digits - 1; i++) {
            tempArr = joinArrays(tempArr, level);
        }
        return tempArr;    }
}
