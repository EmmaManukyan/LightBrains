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

    // Флаг, используемый в методе onBindViewHolder для отображения ответов, если пользователь нажал кнопку "Проверить ответы"
    boolean isChecking = false;


    public AttentionGameRecyclerAdapter(HashMap<Integer, Integer> showedMap, Context context, int figureType) {
        this.showedMap = showedMap;
        Set<Integer> defaultKeys = this.showedMap.keySet();
        Integer[] keys = defaultKeys.toArray(new Integer[defaultKeys.size()]);
        Arrays.sort(keys);
        this.keys = keys;
        this.context = context;
        this.figureType = figureType;
        this.answersMap = new HashMap<>();
        for (Integer key : keys) {
            answersMap.put(key, false);
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
        if (isChecking) {
            // Если выполняется проверка ответов
            holder.imgIsRight.setVisibility(View.VISIBLE);
            holder.edtAnswer.setEnabled(false);
            String s = holder.edtAnswer.getText().toString();
            if (s.isEmpty()) {
                if (s.equals("")) s = "?";
                holder.edtAnswer.setTextColor(context.getResources().getColor(R.color.is_wrong));
                holder.edtAnswer.setText(s + " ≠ " + holder.imgFigure.getContentDescription().toString().replace(Constants.WRONG, ""));
            } else if (holder.imgIsRight.getContentDescription().toString().contains(Constants.WRONG)) {
                holder.edtAnswer.setTextColor(context.getResources().getColor(R.color.is_wrong));
                holder.edtAnswer.setText(s + " ≠ " + showedMap.get(Integer.parseInt(holder.imgIsRight.getContentDescription().toString().replace(Constants.WRONG, ""))));
            }
        } else {
            // Если не выполняется проверка ответов
            holder.imgFigure.setImageResource(FigureListCreator.figureTypes[figureType][keys[position]]);
            holder.imgFigure.setContentDescription(Objects.requireNonNull(showedMap.get(keys[position])).toString());
        }

        holder.edtAnswer.addTextChangedListener(new TextWatcher() {
            // Слушатель изменений текста в поле ввода ответа
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!holder.edtAnswer.getText().toString().equals("") && !isChecking) {
                    // Если поле ввода ответа не пусто и не выполняется проверка ответов
                    if (Integer.parseInt(holder.edtAnswer.getText().toString()) == showedMap.get(keys[position])) {
                        // Если введенный ответ совпадает с правильным ответом
                        answersMap.put(keys[position], true);
                        holder.imgIsRight.setImageResource(R.drawable.right);
                        holder.imgIsRight.setContentDescription(showedMap.get(keys[position]).toString());
                    } else {
                        // Если введенный ответ не совпадает с правильным ответом
                        answersMap.put(keys[position], false);
                        holder.imgIsRight.setImageResource(R.drawable.wrong);
                        holder.imgIsRight.setContentDescription(Constants.WRONG + keys[position].toString());
                    }
                }
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
