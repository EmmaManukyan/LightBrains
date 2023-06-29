package com.example.lightbrains.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.part_first_mental.flashanzan.RecyclerViewItem;

import java.util.List;

public class RecyclerAdapterFlashCards extends RecyclerView.Adapter<RecyclerAdapterFlashCards.MyViewHolder> {

    private final List<RecyclerViewItem> recyclerItemsList;
    private Context context;
    private Activity activity;

    private final OnItemClickListener listener;

    // Конструктор адаптера, принимающий список элементов, контекст, слушателя и активность
    public RecyclerAdapterFlashCards(List<RecyclerViewItem> recyclerItem, Context context, OnItemClickListener listener, Activity activity) {
        this.recyclerItemsList = recyclerItem;
        this.context = context;
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerAdapterFlashCards.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Получаем контекст родительского элемента и создаем инфлейтер
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Создаем представление элемента списка
        View recItemView = inflater.inflate(R.layout.recycler_item_layout, parent, false);
        return new MyViewHolder(recItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterFlashCards.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Resources res = context.getResources();
        // Устанавливаем текст заголовка из списка
        holder.getTvTitleName().setText(recyclerItemsList.get(position).getTitleName());
        // Устанавливаем изображение из списка
        holder.getImgLabel().setImageDrawable(res.getDrawable(recyclerItemsList.get(position).getImageResource()));
        // Устанавливаем цвет фона из списка
        holder.getConstLayInRecycler().setBackgroundColor(res.getColor(recyclerItemsList.get(position).getColorResource()));
        // Обработчик нажатия на элемент списка
        holder.view.setOnClickListener(view -> {
            // Вызываем метод слушателя при нажатии на элемент списка
            listener.onItemClick(recyclerItemsList.get(position), position);
            // Создаем звуковой эффект щелчка кнопки
            Constants.createSound(activity, R.raw.btn_click);
            Constants.makeSoundEffect();
        });
    }

    @Override
    public int getItemCount() {
        return recyclerItemsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout constLayInRecycler;
        private final ImageView imgLabel;
        private final TextView tvTitleName;
        View view;

        // Конструктор ViewHolder, инициализирующий элементы представления
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constLayInRecycler = itemView.findViewById(R.id.const_lay_recycler);
            imgLabel = itemView.findViewById(R.id.img_label_recycler_item);
            tvTitleName = itemView.findViewById(R.id.tv_title_name_recycler_item);
            view = itemView;
        }

        // Геттеры для элементов представления
        public ConstraintLayout getConstLayInRecycler() {
            return constLayInRecycler;
        }

        public ImageView getImgLabel() {
            return imgLabel;
        }

        public TextView getTvTitleName() {
            return tvTitleName;
        }
    }

    // Интерфейс слушателя нажатия на элемент списка
    public interface OnItemClickListener {
        void onItemClick(RecyclerViewItem item, int position);
    }
}
