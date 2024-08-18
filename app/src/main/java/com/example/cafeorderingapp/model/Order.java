package com.example.cafeorderingapp.model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class Order {
    int orderId;
    long total;
    String timestamp;

    int status;
    // 0 - not accepted
    // 1 - accepted
    // 2 - being prepared
    // 3 - ready to serve/ ready for pick up
    // 4 - order completed

    ArrayList<FirebaseOrder> firebaseOrders;

    public Order(int orderId, long total, ArrayList<FirebaseOrder> firebaseOrders, String timestamp) {
        this.orderId = orderId;
        this.total = total;
        this.firebaseOrders = firebaseOrders;
        this.timestamp = timestamp;
        this.status = 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Order(){

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public ArrayList<FirebaseOrder> getFirebaseOrders() {
        return firebaseOrders;
    }

    public void setFirebaseOrders(ArrayList<FirebaseOrder> firebaseOrders) {
        this.firebaseOrders = firebaseOrders;
    }
}

