package com.example.project.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Order implements Parcelable {
    private String oid;
    private String uid;
    private long price;
    private String status;
    private String orderTime;
    private String receiveTime;
    private List<CartItem> orderItems;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public List<CartItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CartItem> orderItems) {
        this.orderItems = orderItems;
    }

    public static Creator<Order> getCREATOR() {
        return CREATOR;
    }

    public Order(String oid, String uid, long price, String status, List<CartItem> orderItems) {
        this.oid = oid;
        this.uid = uid;
        this.price = price;
        this.status = status;
        this.orderTime = getCurrentDateTime();
        this.receiveTime = getReceiveTime();
    }

    public Order() {
        this.oid = "-1";
        this.uid = "unkown";
        this.price = 0;
        this.orderItems = new ArrayList<>();
        this.status = "unsubmit";
        this.orderTime = getCurrentDateTime();
        this.receiveTime = getReceiveTime();
    }

    protected Order(Parcel in) {
        oid = in.readString();
        uid = in.readString();
        price = in.readLong();
        status = in.readString();
        orderTime = in.readString();
        receiveTime = in.readString();
        orderItems = in.createTypedArrayList(CartItem.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getCurrentDateTime() {
        StringBuffer stringBuffer = new StringBuffer();
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.format(now, stringBuffer, new FieldPosition(0));
        return stringBuffer.toString();
    }


    public String getReceiveTime() {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Date orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(this.getOrderTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderDate);
            calendar.add(Calendar.MINUTE, 1);
            orderDate = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            simpleDateFormat.format(orderDate, stringBuffer, new FieldPosition(0));
            return stringBuffer.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "2077-06-06 00:00";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oid);
        dest.writeString(uid);
        dest.writeLong(price);
        dest.writeString(status);
        dest.writeString(orderTime);
        dest.writeString(receiveTime);
        dest.writeTypedList(orderItems);
    }
}
