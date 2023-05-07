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
import com.example.lightbrains.common.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class AttentionGameRecyclerAdapter extends RecyclerView.Adapter<AttentionGameRecyclerAdapter.MyViewHolder> {
    private final HashMap<Integer, Integer> showedMap;

    private final HashMap<Integer, Boolean> answersMap;

    public HashMap<Integer, Boolean> getAnswersMap() {
        return answersMap;
    }

    private final Context context;
    private final int figureType;

    private final Integer[] keys;


    boolean isChecking = false;


    public AttentionGameRecyclerAdapter(HashMap<Integer, Integer> showedMap, Context context, int figureType) {
        this.showedMap = showedMap;
        Log.d("TAG", "getted " + this.showedMap.toString());
        Set<Integer> defaultKeys = this.showedMap.keySet();
        Integer[] keys = defaultKeys.toArray(new Integer[defaultKeys.size()]);
        Arrays.sort(keys);
        this.keys = keys;
        this.context = context;
        this.figureType = figureType;
        this.answersMap = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            answersMap.put(keys[i],false);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.att_game_answers_rec_item, parent, false);
        return new MyViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d("TAG", "imgIndex =    " + this.showedMap.get(keys[position]));
        // holder1 = holder;

        if (isChecking) {
            holder.imgIsRight.setVisibility(View.VISIBLE);
            holder.edtAnswer.setEnabled(false);
            String s = holder.edtAnswer.getText().toString();
            if (s.isEmpty()) {
                if (s.equals("")) s = "?";
                Log.d("tagavor", answersMap.toString());
                holder.edtAnswer.setTextColor(context.getResources().getColor(R.color.is_wrong));
                holder.edtAnswer.setText(s + "!=" + holder.imgFigure.getContentDescription().toString().replace(Constants.WRONG, ""));
            } else if (holder.imgIsRight.getContentDescription().toString().contains(Constants.WRONG)) {
                holder.edtAnswer.setTextColor(context.getResources().getColor(R.color.is_wrong));
                holder.edtAnswer.setText(s + " ≠ " + showedMap.get(Integer.parseInt(holder.imgIsRight.getContentDescription().toString().replace(Constants.WRONG, ""))));

            }
        } else {
            holder.imgFigure.setImageResource(FigureListCreator.figureTypes[figureType][keys[position]]);
            holder.imgFigure.setContentDescription(Objects.requireNonNull(showedMap.get(keys[position])).toString());
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
                if (!holder.edtAnswer.getText().toString().equals("") && !isChecking) {
                    if (Integer.parseInt(holder.edtAnswer.getText().toString()) == showedMap.get(keys[position])) {
                        answersMap.put(keys[position], true);
                        holder.imgIsRight.setImageResource(R.drawable.right);
                        holder.imgIsRight.setContentDescription(showedMap.get(keys[position]).toString());
                    } else {
                        answersMap.put(keys[position], false);
                        holder.imgIsRight.setImageResource(R.drawable.wrong);
                        holder.imgIsRight.setContentDescription(Constants.WRONG + keys[position].toString());

                    }
                }
                Log.d("TAG", holder.edtAnswer.getText() + "  " + "position: " + answersMap.get(keys[position]));
            }
        });
    }


    @Override
    public int getItemCount() {
        return showedMap.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgFigure;
        private final ImageView imgIsRight;
        private final EditText edtAnswer;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFigure = itemView.findViewById(R.id.img_att_game_figure);
            imgIsRight = itemView.findViewById(R.id.img_is_right_or_wrong);
            edtAnswer = itemView.findViewById(R.id.edt_att_game_answer);
        }
    }

}
