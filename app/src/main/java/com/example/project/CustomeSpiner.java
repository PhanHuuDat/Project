package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CustomeSpiner extends BaseAdapter {

    Context context;
    int food[];
    LayoutInflater inflater;

    public CustomeSpiner(Context context, int[] food) {
        this.context = context;
        this.food = food;
        inflater = (LayoutInflater.from(context.getApplicationContext()));
    }

    @Override

    public int getCount() {
        return food.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.spiner_item_food, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        icon.setImageResource(food[position]);
        return view;

    }
}
