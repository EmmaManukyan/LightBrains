package com.example.lightbrains.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lightbrains.R;
import com.example.lightbrains.common.Constants;
import com.example.lightbrains.common.ConstantsForFireBase;
import com.example.lightbrains.firebase_classes.User;
import com.example.lightbrains.homepage.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapterLeaderBoard extends RecyclerView.Adapter<RecyclerAdapterLeaderBoard.MyViewHolder> {
    // Контекст приложения
    private final Context context;
    // Список пользователей
    private final List<User> users;
    // Массив цветов для оформления элементов списка
    private int colors[] = new int[]{R.color.blue_leader_board, R.color.green, R.color.yellow, R.color.orange, R.color.red_light, R.color.pink};

    // Конструктор адаптера, принимающий контекст и список пользователей
    public RecyclerAdapterLeaderBoard(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerAdapterLeaderBoard.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leader_board_rec_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterLeaderBoard.MyViewHolder holder, int position) {
        // Этот шаг используется для упорядочивания пользователей в массиве в обратном порядке
        int realPosition = users.size() - 1 - position;
        // Устанавливаем имя пользователя
        holder.getTvUserName().setText(users.get(realPosition).getUserName());
        // Устанавливаем количество очков пользователя
        holder.getTvScores().setText("" + users.get(realPosition).getScores());
        // Устанавливаем цвет фона элемента списка в зависимости от позиции пользователя
        holder.layItem.setBackgroundColor(context.getResources().getColor(colors[position % colors.length]));
        // Загружаем изображение пользователя с использованием Picasso
        Picasso.get().load(users.get(realPosition).getImageUri()).placeholder(R.drawable.img_profile_default).into(holder.imgUser);
        // Проверяем, является ли текущий пользователь текущим пользователем вошедшим в систему
        if (users.get(realPosition).getEmail().equals(ConstantsForFireBase.mAuth.getCurrentUser().getEmail())) {
            // Применяем специальное оформление для текущего пользователя
            holder.layItem.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_btn4_transparent));
            holder.tvScores.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_btn1_start));
            holder.tvScores.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvScores.setElevation(8f);
            holder.layItem.setElevation(16f);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgUser;
        private final TextView tvUserName;
        private final TextView tvScores;
        private final LinearLayout layItem;

        public ImageView getImgUser() {
            return imgUser;
        }

        public TextView getTvUserName() {
            return tvUserName;
        }

        public TextView getTvScores() {
            return tvScores;
        }
        // Конструктор ViewHolder, инициализирующий элементы представления
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img_user);
            tvUserName = itemView.findViewById(R.id.tv_userName);
            tvScores = itemView.findViewById(R.id.tv_scores);
            layItem = itemView.findViewById(R.id.lay_rec_item);
        }
    }
}
