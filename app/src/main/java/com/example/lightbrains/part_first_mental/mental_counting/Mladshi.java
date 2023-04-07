package com.example.lightbrains.part_first_mental.mental_counting;

public class Mladshi implements Level {
    private static int[] levels = {4, -4, 3, -3, 2, -2, 1, -1};


    public int[] createArrayToCount(int digits, int count, int level) {
        int[] tempArr = startArrayOneDigit(count, level);
        for (int i = 0; i < digits - 1; i++) {
            tempArr = joinArrays(tempArr, level);
        }
        return tempArr;
    }

    public int[] startArrayOneDigit(int size, int level) {
        int[] resultArr;
        if (level == 8) {
            resultArr = getAllMladshi(size);
        } else {
            resultArr = getSpecificTopic(size, level);
        }
        return resultArr;
    }

    private int[] getAllMladshi(int size) {
        int[] arr = new int[size];
        int i = 0;

        int min = -9;
        int max = 9;
        int result = 0;
        int b = 0;
        int a;
        while (i < size) {
            b = Level.getRandomInRange(min, max);
            a = getRandom(result, Level.getRandomInRange(0, levels.length - 1));
            if (a != 0) {
                b = a;
            }
            if (b == 0 || (i == 0 && b < 0) || (result + b < 0 || result + b > 9)) {
                continue;
            }
            result += b;
            arr[i] = b;
            i++;
        }
        return arr;
    }


    private int[] getAllMladshiForDigits(int[] startArray) {
        int[] arr = new int[startArray.length];
        int i = 0;

        int min = 0;
        int max = 9;
        int result = 0;
        int b = 0;
        int a;
        while (i < startArray.length) {
            b = Level.getRandomInRange(min, max);
            a = getRandom(result, Level.getRandomInRange(0, levels.length - 1));


            if (a != 0 && Math.abs(a) == a) {
                b = a;
            } else {
                if (Level.checkifIsNotPryamoy(result, b)) {
                    continue;
                }
            }
            if (startArray[i] < 0) {
                b *= -1;
            }
            if (b == 0 || (i == 0 && b < 0) || (result + b < 0 || result + b > 9)) {
                continue;
            }


            result += b;
            arr[i] = b;
            i++;
        }
        return arr;
    }

    private int[] getSpecificTopic(int size, int level) {
        int[] arr = new int[size];
        int i = 0;

        int min = getMin(level);
        int max = getMax(level);
        int result = 0;
        int b = 0;
        int a;
        // System.out.println("min "+min+" max "+max);
        while (i < size) {
            b = Level.getRandomInRange(min, max);
            //System.out.println("b is "+b);
            if (i % 3 != 1) {
                a = getRandom(result, Level.getRandomInRange(0, level));
                //System.out.println(i+"Patahakan "+a);
            } else {
                a = getRandom(result, level);
                // System.out.println(i+"Konkret "+a);

            }
            if (a != 0) {
                b = a;
                // System.out.println(i+"b is"+b);
            } else {
                if (Level.checkifIsNotPryamoy(result, b)) {
                    continue;
                }
            }
            // System.out.println("B is " + b);
            if (b == 0 || (i == 0 && b < 0) || (result + b < 0 || result + b > 9)) {
                // System.out.println("esia mghavor");
                continue;
            }
            min = -9;
            max = 9;
            result += b;
            arr[i] = b;
            i++;
        }
        return arr;
    }


    private int[] getSpecificTopicForSecondDigits(int[] startArray, int level) {
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

            a = getRandom(result, Level.getRandomInRange(0, level));
            if (a != 0 && Math.abs(a) == a) {
                b = a;
                if (startArray[i] < 0) {
                    b *= -1;
                }
            } else {
                if (Level.checkifIsNotPryamoy(result, b)) {
                    continue;
                }
            }

            //System.out.println("b is "+b);

            if ((i == 0 && b < 0) || (result + b < 0 || result + b > 9)) {
                continue;
            }
            result += b;
            arr[i] = b;
            i++;
        }
        return arr;
    }



    private static int getRandom(int result, int level) {

        int returnReult = 0;
        if (levels[level] > 0) {
            if (result >= (5 - levels[level]) && result < 5) {
                returnReult = levels[level];
            }
        } else {
            // System.out.println("stegh em, result = "+result+"level = "+levels[level]);
            if (result >= 5 && result <= 4 - levels[level]) {
                returnReult = levels[level];
            }
        }
        return returnReult;
    }

    private static int getMax(int level) {
        int max = 0;
        if (levels[level] > 0) {
            max = 4;
        } else {
            max = 4 - levels[level];
        }
        return max;
    }

    private static int getMin(int level) {
        int min = 0;
        if (levels[level] > 0) {
            min = 5 - levels[level];
        } else {
            min = 5;
        }
        return min;
    }

    public int[] joinArrays(int[] arr1, int level) {
        int[] myArr = new int[arr1.length];
        int[] arr2;
        if (level == 8) {
            arr2 = getAllMladshiForDigits(arr1);
        } else {
            arr2 = getSpecificTopicForSecondDigits(arr1, level);
        }

        for (int i = 0; i < myArr.length; i++) {

            String tempS = "";
            tempS = Integer.toString(arr1[i]);
            tempS += Math.abs(arr2[i]);
            myArr[i] = Integer.parseInt(tempS);

        }
        return myArr;
    }


}
