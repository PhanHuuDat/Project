package com.example.project.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.example.project.models.Order;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<Order> orders;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    int lastPos = -1;
    private ItemClickInterface itemClickInterface;

    public OrderAdapter(Context mContext, List<Order> orders, ItemClickInterface itemClick) {
        this.orders = orders;
        this.mContext = mContext;
        this.itemClickInterface = itemClick;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = mLayoutInflater.inflate(R.layout.order_item, parent, false);
        return new OrderAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Order order = orders.get(position);

        holder.tvOID.setText("ID: " + order.getOid());
        holder.tvOTime.setText("Order: " + order.getOrderTime());
        holder.tvRTime.setText("Receive: " + order.getReceiveTime());
        holder.tvFinalPrice.setText("Total: " + order.getPrice() + " VND");
        holder.imgStatus.setVisibility(View.VISIBLE);
        switch (order.getStatus()) {
            case "pending":
                Picasso.get().load(R.drawable.pending).placeholder(R.drawable.pending).into(holder.imgStatus);
                break;
            case "delivering":
                Picasso.get().load(R.drawable.awaiting).placeholder(R.drawable.awaiting).into(holder.imgStatus);
                break;
            case "received":
                Picasso.get().load(R.drawable.received).placeholder(R.drawable.received).into(holder.imgStatus);
                break;
            case "cancelled":
                Picasso.get().load(R.drawable.cancelled).placeholder(R.drawable.cancelled).into(holder.imgStatus);
                break;
            default:
                Picasso.get().load(R.drawable.unknown).placeholder(R.drawable.unknown).into(holder.imgStatus);
                break;
        }

        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.itemCV.setOnClickListener(v -> itemClickInterface.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
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
        public TextView tvOID, tvOTime, tvRTime, tvFinalPrice;
        public ImageView imgStatus;
        public CardView itemCV;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemCV = itemView.findViewById(R.id.item_cv);
            tvOID = itemView.findViewById(R.id.tv_oid);
            tvOTime = itemView.findViewById(R.id.tv_order_time);
            tvRTime = itemView.findViewById(R.id.tv_rev_time);
            tvFinalPrice = itemView.findViewById(R.id.tv_final_price);
            imgStatus = itemView.findViewById(R.id.im_status);

        }
    }
}