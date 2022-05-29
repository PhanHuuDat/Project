package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Model.Dish;
import com.example.project.R;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private List<Dish> dishList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<Dish> selectList;

    public DishAdapter(Context mContext, List<Dish> dishList) {
        this.dishList = dishList;
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.dish_item, parent, false);
        return new DishViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.name.setText(dish.getName());
        holder.category.setText(dish.getCategory());
        holder.price.setText(String.valueOf(dish.getPrice()));
        holder.avatar.setImageResource(dish.getAvatar_id());
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }


    public class DishViewHolder extends RecyclerView.ViewHolder {
        public TextView name, category, price;
        public ImageView avatar;

        public DishViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_dishname);
            category = itemView.findViewById(R.id.txt_category);
            price = itemView.findViewById(R.id.txt_price);
            avatar = itemView.findViewById(R.id.img_avatar);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, name.getText() + "Đã thêm vào giỏ hàng", Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}
