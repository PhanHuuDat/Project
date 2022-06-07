package com.example.project.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.interfaces.ItemClickInterface;
import com.example.project.models.Dish;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {

    private List<Dish> dishList;
    private List<Dish> filteredDishList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    int lastPos = -1;
    private ItemClickInterface itemClickInterface;

    public DishAdapter(Context mContext, List<Dish> dishList, ItemClickInterface itemClickInterface) {
        this.dishList = dishList;
        this.filteredDishList.addAll(dishList);
        this.mContext = mContext;
        this.itemClickInterface = itemClickInterface;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.dish_item, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Dish dish = filteredDishList.get(position);

        holder.name.setText(dish.getName());
        holder.category.setText(dish.getCategory());
        holder.price.setText(String.valueOf(dish.getPrice()) + " VND");
        Picasso.get().load(dish.getFoodImg()).into(holder.avatar);
        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.itemCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickInterface.onItemClick(position);
            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        filteredDishList.clear();
        if (charText.length() == 0 || charText.equals("all")) {
            filteredDishList.addAll(dishList);
        } else {
            for (Dish d : dishList) {
                if (d.getName().toLowerCase().contains(charText)) {
                    filteredDishList.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredDishList.size();
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            // on below line we are setting animation.
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, category, price;
        public ImageView avatar;
        public CardView itemCV;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemCV = itemView.findViewById(R.id.item_cv);
            name = itemView.findViewById(R.id.txt_dishname);
            category = itemView.findViewById(R.id.txt_cateogry);
            price = itemView.findViewById(R.id.txt_price);
            avatar = itemView.findViewById(R.id.img_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, name.getText(), Toast.LENGTH_LONG).show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(mContext, "Long item clicked " + name.getText(), Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
    }
}
