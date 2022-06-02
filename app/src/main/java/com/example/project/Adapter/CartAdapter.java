package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Model.Dish;
import com.example.project.R;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Dish> cartList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int totalPricePerDish;
    private Bundle bundle;
    public CartAdapter(Context mContext, List<Dish> dishList, Bundle bundle) {
        this.cartList = dishList;
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.cart_item, parent, false);
        return new CartAdapter.CartViewHolder(item).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Dish dish = cartList.get(position);
        holder.name.setText(dish.getName());
        holder.category.setText(dish.getCategory());
        holder.price.setText(String.valueOf(dish.getPrice()));
        holder.avatar.setImageResource(dish.getAvatar_id());
        holder.quantity.setText("0");
        holder.totalPrice.setText("0");
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView name, category, price, totalPrice;
        public ImageView avatar;
        public EditText quantity;
        public Button btn_delete_cart_item;
        private CartAdapter adapter;
        public CartViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_dishname);
            category = itemView.findViewById(R.id.txt_category);
            price = itemView.findViewById(R.id.txt_price);
            avatar = itemView.findViewById(R.id.img_avatar);
            quantity = itemView.findViewById(R.id.txt_quantity);
            totalPrice = itemView.findViewById(R.id.txt_total);
            btn_delete_cart_item = itemView.findViewById(R.id.btn_delete_cart_item);

            btn_delete_cart_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.cartList.remove(getAdapterPosition());
                    adapter.notifyItemRemoved(getAdapterPosition());
                    bundle.putSerializable("myPackage",(Serializable) cartList);
                }
            });

            quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    if(isNumber(quantity.getText().toString())){
                        int quan = Integer.parseInt(quantity.getText().toString());
                        int pri = Integer.parseInt(price.getText().toString());
                        totalPricePerDish = quan * pri;
                    }else{
                        totalPricePerDish = 0;
                    }
                    totalPrice.setText(String.valueOf(totalPricePerDish));
                }
            });
        }

        public CartViewHolder linkAdapter(CartAdapter adapter){
            this.adapter = adapter;
            return this;
        }
    }

    public boolean isNumber(String value){
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }


}