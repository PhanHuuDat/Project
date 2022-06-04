package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomeSpinerCategories extends BaseAdapter {

    Context context;
    int icon_categories[];
    String[] food_categories;
    LayoutInflater inflater;

    public CustomeSpinerCategories(Context context, String[] food_categories,int[] icon_categories) {
        this.context = context;
        this.icon_categories = icon_categories;
        this.food_categories = food_categories;
        inflater=(LayoutInflater.from(context.getApplicationContext()));
    }
    @Override
    public int getCount() {
        return icon_categories.length;
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

        view = inflater.inflate(R.layout.spiner_item_categories, null);
        ImageView icon = (ImageView) view.findViewById(R.id.img_categories);
        TextView names = (TextView) view.findViewById(R.id.tv_categories);
        icon.setImageResource(icon_categories[position]);
        names.setText(food_categories[position]);
        return view;
    }
}
