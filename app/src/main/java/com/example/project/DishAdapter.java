package com.example.project;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.MyViewHolder> {

    private List<Dish> dishList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public DishAdapter(Context mContext, List<Dish> dishList) {
        this.dishList = dishList;
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.dish_item, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, category, price;
        public ImageView avatar;

        public MyViewHolder(View itemView) {
            super(itemView);
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
