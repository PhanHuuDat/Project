package com.example.project.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Model.Dish;
import com.example.project.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Dish> cartList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public CartAdapter(Context mContext, List<Dish> dishList) {
        this.cartList = dishList;
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.cart_item, parent, false);
        return new CartAdapter.CartViewHolder(item);
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

        public CartViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt_dishname);
            category = itemView.findViewById(R.id.txt_category);
            price = itemView.findViewById(R.id.txt_price);
            avatar = itemView.findViewById(R.id.img_avatar);
            quantity = itemView.findViewById(R.id.txt_quantity);
            totalPrice = itemView.findViewById(R.id.txt_total);


            quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int quan = Integer.parseInt(quantity.getText().toString());
                    int pri = Integer.parseInt(price.getText().toString());
                    int totalPricePerDish = quan * pri;
                    totalPrice.setText(String.valueOf(totalPricePerDish));
                }
            });


        }
    }
}
