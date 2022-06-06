package com.example.project.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private String dishID;
    private int amount;
    private long singlePrice;
    private long total;
    private String itemImg;

    public String getDishID() {
        return dishID;
    }

    public void setDishID(String dishID) {
        this.dishID = dishID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(long singlePrice) {
        this.singlePrice = singlePrice;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public static Creator<CartItem> getCREATOR() {
        return CREATOR;
    }

    public CartItem(String dishID, int amount, long singlePrice, long total, String itemImg) {
        this.dishID = dishID;
        this.amount = amount;
        this.singlePrice = singlePrice;
        this.total = total;
        this.itemImg = itemImg;
    }

    public CartItem() {
        dishID ="default";
        amount = 0;
        this.singlePrice = singlePrice;
        this.total = total;
        this.itemImg = "https://howfix.net/wp-content/uploads/2018/02/sIaRmaFSMfrw8QJIBAa8mA-article.png";
    }

    protected CartItem(Parcel in) {
        dishID = in.readString();
        amount = in.readInt();
        singlePrice = in.readLong();
        total = in.readLong();
        itemImg = in.readString();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dishID);
        dest.writeInt(amount);
        dest.writeLong(singlePrice);
        dest.writeLong(total);
        dest.writeString(itemImg);
    }
}
