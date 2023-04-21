package com.example.lightbrains.part_second.attention_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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

    private HashMap<Integer,Boolean> answersMap;
    private Context context;
    private int figureType;

    private Integer[] keys;

   // private  MyViewHolder holder1;

    boolean isChecking =false;


    public AttentionGameRecyclerAdapter(HashMap<Integer, Integer> showedMap, Context context, int figureType) {
        this.showedMap = showedMap;
        Log.d("TAG","getted "+this.showedMap.toString());
        Set<Integer> defaultKeys = this.showedMap.keySet();
        this.keys = defaultKeys.toArray(new Integer[defaultKeys.size()]);
        this.context = context;
        this.figureType = figureType;
        this.answersMap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.att_game_answers_rec_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d("TAG","imgIndex =    "+this.showedMap.get(keys[position]));
        holder.imgFigure.setImageResource(FigureListCreator.figureTypes[figureType][keys[position]]);
       // holder1 = holder;

        if (isChecking){
            holder.imgIsRight.setVisibility(View.VISIBLE);
            holder.edtAnswer.setEnabled(false);
        }
        holder.edtAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!holder.edtAnswer.getText().toString().equals("")){
                    if (Integer.parseInt(holder.edtAnswer.getText().toString())==showedMap.get(keys[position])){
                        answersMap.put(keys[position], true);
                        holder.imgIsRight.setImageResource(R.drawable.img_heart_eyes_smile);
                    }else{
                        answersMap.put(keys[position], false);
                        holder.imgIsRight.setImageResource(R.drawable.img_sad_smile);


                    }
                }
                Log.d("TAG",holder.edtAnswer.getText()+"  "+"position: "+answersMap.get(keys[position]));
            }
        });
    }


    @Override
    public int getItemCount() {
        return showedMap.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFigure;
        private ImageView imgIsRight;
        private EditText edtAnswer;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFigure = itemView.findViewById(R.id.img_att_game_figure);
            imgIsRight = itemView.findViewById(R.id.img_is_right_or_wrong);
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
