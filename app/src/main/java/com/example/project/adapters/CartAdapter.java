package com.example.project.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.interfaces.ItemClickInterface;
import com.example.project.interfaces.ItemLongClickInterface;
import com.example.project.models.CartItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<CartItem> cartItems;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    int lastPos = -1;
    private ItemClickInterface itemClickInterface;
    private ItemLongClickInterface itemLongClickInterface;

    public CartAdapter(Context mContext, List<CartItem> cartItems, ItemClickInterface itemClickInterface, ItemLongClickInterface itemLongClickInterface) {
        this.cartItems = cartItems;
        this.mContext = mContext;
        this.itemClickInterface = itemClickInterface;
        this.itemLongClickInterface = itemLongClickInterface;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.cart_item, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CartItem cartItem = cartItems.get(position);
        holder.tvName.setText(cartItem.getDishID() + " x " + cartItem.getAmount());
        holder.tvSinglePrice.setText(cartItem.getSinglePrice() + " VND");
        holder.tvSumPrice.setText(cartItem.getTotal() + " VND");
        Picasso.get().load(cartItem.getItemImg()).into(holder.avatar);
        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.itemCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickInterface.onItemClick(position);
            }
        });

        holder.itemCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemLongClickInterface.onItemLongClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
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
        public TextView tvName, tvSinglePrice, tvSumPrice;
        public ImageView avatar;
        public CardView itemCV;
        public CheckBox ckSelect;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemCV = itemView.findViewById(R.id.item_cv);
            tvName = itemView.findViewById(R.id.tv_dish_name);
            tvSinglePrice = itemView.findViewById(R.id.tv_signle_price);
            tvSumPrice = itemView.findViewById(R.id.tv_sum_price);
            avatar = itemView.findViewById(R.id.img_avatar);
            ckSelect = itemView.findViewById(R.id.ck_select);

        }
    }
}
