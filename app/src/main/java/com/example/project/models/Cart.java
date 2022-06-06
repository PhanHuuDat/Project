package com.example.project.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements Parcelable {
    private String cid;
    private String uid;
    private List<CartItem> cartItems;
    private long total;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public static Creator<Cart> getCREATOR() {
        return CREATOR;
    }

    public Cart() {
        this.cid = "CartDefault";
        this.uid = "unkown";
        this.cartItems = new ArrayList<>();
        this.total = 0;
    }

    public Cart(String cid, String uid, List<CartItem> cartItems, long total) {
        this.cid = cid;
        this.uid = uid;
        this.cartItems = cartItems;
        this.total = total;
    }

    protected Cart(Parcel in) {
        cid = in.readString();
        uid = in.readString();
        cartItems = in.createTypedArrayList(CartItem.CREATOR);
        total = in.readLong();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    public void addToCart(String did, int amount, long singleP, long totalP, String foodImg) {
        boolean isFound = false;
        int i = 0;
        for (CartItem item : cartItems) {
            if (item.getDishID().toLowerCase().equals(did.toLowerCase())) {
                isFound = true;
                break;
            }
            i++;
        }

        if (!isFound) {
            this.cartItems.add(new CartItem(did, amount, singleP, totalP, foodImg));
        } else {
            int oldAmount = this.cartItems.get(i).getAmount();
            this.cartItems.get(i).setDishID(did);
            this.cartItems.get(i).setAmount(amount + oldAmount);
            this.cartItems.get(i).setTotal(this.cartItems.get(i).getAmount() * this.cartItems.get(i).getSinglePrice());
        }
        updateTotal();
    }

    public void removeFromCart(String did) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getDishID().toLowerCase().equals(did.toLowerCase())) {
                cartItems.remove(i);
                i--;
            }
        }
        updateTotal();
    }

    public void editCartItem(String did, int amount, long singleP, long totalP, String foodImg) {
        boolean isFound = false;
        int i = 0;
        for (CartItem item : cartItems) {
            if (item.getDishID().toLowerCase().equals(did.toLowerCase())) {
                isFound = true;
                break;
            }
            i++;
        }

        if (!isFound) {
            System.out.println("item not found!");
        } else {
            this.cartItems.get(i).setAmount(amount);
            this.cartItems.get(i).setTotal(amount * this.cartItems.get(i).getSinglePrice());
        }
        updateTotal();
    }

    private void updateTotal() {
        this.total = 0;
        for (CartItem c : cartItems) {
            this.total += c.getTotal();
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cid);
        dest.writeString(uid);
        dest.writeTypedList(cartItems);
        dest.writeLong(total);
    }
}
