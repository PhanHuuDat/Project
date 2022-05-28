package com.example.project;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class DishViewHolder extends RecyclerView.ViewHolder {

    ImageView avatar;
    TextView dishName;
    TextView dishCategory;
    TextView dishPrice;


    public DishViewHolder(@NonNull View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.img_avatar);
        dishName = itemView.findViewById(R.id.txt_dishname);
        dishCategory = itemView.findViewById(R.id.txt_category);
        dishPrice = itemView.findViewById(R.id.txt_price);
    }
}
