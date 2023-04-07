package com.example.lightbrains.part_second.attention_game;

import android.util.Log;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;

import java.util.HashMap;

public class FigureListCreator {
    private static int[] figuresGeometry = {R.drawable.img_abac_0, R.drawable.img_abac_1, R.drawable.img_abac_2, R.drawable.img_abac_3,R.drawable.img_abac_1, R.drawable.img_abac_2, R.drawable.img_abac_3,R.drawable.img_abac_3};
    private static int[] figuresFruits = {R.drawable.img_abac_0, R.drawable.img_abac_1, R.drawable.img_abac_2, R.drawable.img_abac_3,R.drawable.img_abac_1, R.drawable.img_abac_2, R.drawable.img_abac_3,R.drawable.img_abac_3};
    private static int[] figuresAnimals = {R.drawable.img_abac_0, R.drawable.img_abac_1, R.drawable.img_abac_2, R.drawable.img_abac_3, R.drawable.img_abac_0, R.drawable.img_abac_1, R.drawable.img_abac_2, R.drawable.img_abac_3};
    private static int[][] figureTypes = {figuresGeometry, figuresFruits, figuresAnimals};


    public static HashMap<Integer, Integer> createMapOfFigures(int figureType, int figureLevel, int figuresCount) {
        figureLevel = figureLevel * 3 + 2;
        Log.d("TAG", "figure level has become: " + figureLevel);
        int tempKey;
        int tempValueCount;
        int figCount = figuresCount;
        HashMap<Integer, Integer> resultMap = new HashMap<>();
        if (figureLevel <= figureTypes[figureType].length) {
            tempKey = Constants.getRandomInRange(0, figureTypes[figureType].length - 1);
            tempValueCount = Constants.getRandomInRange(1, figuresCount > figureLevel ? figuresCount - figureLevel + 1 : figuresCount - 1);
            resultMap.put(tempKey, tempValueCount);
            figuresCount -= tempValueCount;
            for (int i = 0; i < Math.min(figureLevel - 1, figCount - 1); i++) {
                while (resultMap.containsKey(tempKey)) {
                    tempKey = Constants.getRandomInRange(0, figureTypes[figureType].length - 1);
                    Log.d("TAG", "key is " + tempKey);
                }
                tempValueCount = figuresCount - i >= 1 ? Constants.getRandomInRange(1, figuresCount - i) : 0;
                if (i + 1 == Math.min(figureLevel - 1, figCount - 1)) {
                    tempValueCount = figuresCount;
                }
                figuresCount -= tempValueCount;
                resultMap.put(tempKey, tempValueCount);
            }
        }

        Log.d("TAG", String.valueOf(resultMap));

        return resultMap;
    }

}
