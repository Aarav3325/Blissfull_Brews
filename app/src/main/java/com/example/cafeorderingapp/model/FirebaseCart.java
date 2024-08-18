package com.example.cafeorderingapp.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class FirebaseCart {
    String cartItemName;
    Integer cartItemPrice;
    Integer cartItemCount;
    String cartItemId;
    String key;

    public FirebaseCart(String cartItemName, Integer cartItemPrice, Integer cartItemCount, String cartItemId) {
        this.cartItemName = cartItemName;
        this.cartItemPrice = cartItemPrice;
        this.cartItemCount = cartItemCount;
        this.cartItemId = cartItemId;
        this.key = null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FirebaseCart() {
        this.key = null;
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cartItemName", cartItemName);
        result.put("cartItemCount", cartItemCount);
        result.put("cartItemPrice", cartItemPrice);
        result.put("cartItemId", cartItemId);

        return result;
    }

    public String getCartItemName() {
        return cartItemName;
    }

    public void setCartItemName(String cartItemName) {
        this.cartItemName = cartItemName;
    }

    public Integer getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(Integer cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }

    public Integer getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(Integer cartItemCount) {
        this.cartItemCount = cartItemCount;
    }
}
