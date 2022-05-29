package com.example.project.Model;

import android.widget.ImageView;

public class Dish {
    private String name, category;
    private double price;
    private int avatar_id;

    public Dish(String name, String category, double price, int avatar_id) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.avatar_id = avatar_id;
    }
    public Dish(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvatar_id() {
        return avatar_id;
    }

    public void setAvatar_id(int avatar_id) {
        this.avatar_id = avatar_id;
    }
}
