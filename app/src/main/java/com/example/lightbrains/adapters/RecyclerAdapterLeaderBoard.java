package com.example.lightbrains.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private final Context context;
    private final List<User> users;

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterLeaderBoard.MyViewHolder holder, int position) {
        int realPosition = users.size()-1-position;
        holder.getTvUserName().setText(users.get(realPosition).getUserName());
        holder.getTvScores().setText("" + users.get(realPosition).getScores());
        Log.d("fir", users.get(realPosition).getImageUri());
        Picasso.get().load(users.get(realPosition).getImageUri()).placeholder(R.drawable.img_profile_default).into(holder.imgUser);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgUser;
        private final TextView tvUserName;
        private final TextView tvScores;

        public ImageView getImgUser() {
            return imgUser;
        }

        public TextView getTvUserName() {
            return tvUserName;
        }

        public TextView getTvScores() {
            return tvScores;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.img_user);
            tvUserName = itemView.findViewById(R.id.tv_userName);
            tvScores = itemView.findViewById(R.id.tv_scores);

        }
    }
}