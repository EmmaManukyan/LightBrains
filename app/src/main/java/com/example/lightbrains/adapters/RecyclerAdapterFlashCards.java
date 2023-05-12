package com.example.lightbrains.adapters;

import android.annotation.SuppressLint;
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
import com.example.lightbrains.part_first_mental.flashanzan.RecyclerViewItem;

import java.util.List;

public class RecyclerAdapterFlashCards extends RecyclerView.Adapter<RecyclerAdapterFlashCards.MyViewHolder> {

    private final List<RecyclerViewItem> recyclerItemsList;
    private Context context;


    private final OnItemClickListener listener;

    public RecyclerAdapterFlashCards(List<RecyclerViewItem> recyclerItem, Context context,OnItemClickListener listener) {
        this.recyclerItemsList = recyclerItem;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerAdapterFlashCards.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater =LayoutInflater.from(context);
        View recItemView =inflater.inflate(R.layout.recycler_item_layout,parent,false);
        return new MyViewHolder(recItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterFlashCards.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Resources res = context.getResources();
        holder.getTvTitleName().setText(recyclerItemsList.get(position).getTitleName());
        holder.getImgLabel().setImageDrawable(res.getDrawable(recyclerItemsList.get(position).getImageResource()));
        holder.getConstLayInRecycler().setBackgroundColor(res.getColor(recyclerItemsList.get(position).getColorResource()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(recyclerItemsList.get(position), position);
            }
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

        public ConstraintLayout getConstLayInRecycler() {
            return constLayInRecycler;
        }


        public ImageView getImgLabel() {
            return imgLabel;
        }


        public TextView getTvTitleName() {
            return tvTitleName;
        }

        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            constLayInRecycler = itemView.findViewById(R.id.const_lay_recycler);
            imgLabel = itemView.findViewById(R.id.img_label_recycler_item);
            tvTitleName = itemView.findViewById(R.id.tv_title_name_recycler_item);
            view = itemView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerViewItem item, int position);
    }
}


