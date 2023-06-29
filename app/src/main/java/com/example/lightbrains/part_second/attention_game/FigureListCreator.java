package com.example.lightbrains.part_second.attention_game;

import android.util.Log;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;

import java.util.HashMap;

public class FigureListCreator {
    private static int[] figuresGeometry = {R.drawable.att_game_geom_fig_6angle, R.drawable.att_game_geom_fig_5angle, R.drawable.att_game_geom_fig_rectangle, R.drawable.att_game_geom_fig_square, R.drawable.att_game_geom_fig_triangle, R.drawable.att_game_geom_fig_circle, R.drawable.att_game_geom_fig_rhombus, R.drawable.att_game_geom_fig_heart};
        private static int[] figuresFruits = {R.drawable.att_game_abricot, R.drawable.att_game_ananas, R.drawable.att_game_apple, R.drawable.att_game_banana, R.drawable.att_game_broccoly, R.drawable.att_game_carrot, R.drawable.att_game_cherry, R.drawable.att_game_cucumber
            , R.drawable.att_game_pumpkin, R.drawable.att_game_cucuruze, R.drawable.att_game_grapes
            ,R.drawable.att_game_lemon, R.drawable.att_game_strawberry, R.drawable.att_game_pear
            , R.drawable.att_game_pepper, R.drawable.att_game_pomodor, R.drawable.att_game_watermelon
    };
    private static int[] figuresAnimals = {R.drawable.att_game_animals_bear, R.drawable.att_game_animals_cat, R.drawable.att_game_animals_chicken, R.drawable.att_game_animals_cock, R.drawable.att_game_animals_cow, R.drawable.att_game_animals_crab, R.drawable.att_game_animals_deer, R.drawable.att_game_animals_dog
    ,R.drawable.att_game_animals_fish, R.drawable.att_game_animals_hedgehog, R.drawable.att_game_animals_hen, R.drawable.att_game_animals_jellyfish, R.drawable.att_game_animals_lamb, R.drawable.att_game_animals_monkey,
            R.drawable.att_game_animals_mouse, R.drawable.att_game_animals_parrot, R.drawable.att_game_animals_pig, R.drawable.att_game_animals_pinguin, R.drawable.att_game_animals_turtle, R.drawable.att_game_animals_white_beer, R.drawable.att_game_animals_sea_lion};
    public static int[][] figureTypes = {figuresGeometry, figuresFruits, figuresAnimals};


    public static HashMap<Integer, Integer> createMapOfFigures(int figureType, int figureLevel, int figuresCount) {
        if (figureLevel<3){
            figureLevel = figureLevel * 3 + 2;
        }else if (figureType==0 && figureLevel>8){
            figureLevel = 8;
        }

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
                }
                tempValueCount = figuresCount - i >= 1 ? Constants.getRandomInRange(1, figuresCount - i) : 0;
                if (i + 1 == Math.min(figureLevel - 1, figCount - 1)) {
                    tempValueCount = figuresCount;
                }
                figuresCount -= tempValueCount;
                resultMap.put(tempKey, tempValueCount);
            }
        }


        return resultMap;
    }

}
