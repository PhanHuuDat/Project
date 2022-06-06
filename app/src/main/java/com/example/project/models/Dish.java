package com.example.project.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Dish implements Parcelable {

    private String id;
    private String name;
    private String category;
    private String price;
    private String foodImg;

    public Dish(String id, String name, String category, String price, String foodImg) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.foodImg = foodImg;
    }

    public Dish() {
        this.id = "unkown";
        this.name = "unkown";
        this.category = "unkown";
        this.price = "unkown";
        this.foodImg = "unkown";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    public static Creator<Dish> getCREATOR() {
        return CREATOR;
    }

    protected Dish(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
        price = in.readString();
        foodImg = in.readString();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(price);
        dest.writeString(foodImg);
    }
}
