package com.example.lightbrains.part_second.attention_game;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightbrains.R;

import java.util.HashMap;
import java.util.Set;

public class AttentionGameRecyclerAdapter extends RecyclerView.Adapter<AttentionGameRecyclerAdapter.MyViewHolder> {
    private HashMap<Integer,Integer> showedMap;
    private Context context;
    private int figureType;

    private Integer[] keys;


    public AttentionGameRecyclerAdapter(HashMap<Integer, Integer> showedMap, Context context, int figureType) {
        this.showedMap = new HashMap<>();
        for (Integer key : showedMap.keySet()) {
            if (showedMap.get(key)!=0){
                this.showedMap.put(key,showedMap.get(key));
            }
        }
        Log.d("TAG",this.showedMap.toString());


        Set<Integer> defaultKeys = this.showedMap.keySet();
        this.keys = defaultKeys.toArray(new Integer[defaultKeys.size()]);
        this.context = context;
        this.figureType = figureType;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.att_game_answers_rec_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("TAG","imgIndex =    "+this.showedMap.get(keys[position]));
        holder.imgFigure.setImageResource(FigureListCreator.figureTypes[figureType][keys[position]]);
    }

    @Override
    public int getItemCount() {
        return showedMap.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFigure;
        private EditText edtAnswer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFigure = itemView.findViewById(R.id.img_att_game_figure);
            edtAnswer = itemView.findViewById(R.id.edt_att_game_answer);
        }

        public ImageView getImgFigure() {
            return imgFigure;
        }

        public EditText getEdtAnswer() {
            return edtAnswer;
        }
    }
}
