package com.example.cafeorderingapp.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class Cart {

    @ColumnInfo(name = "cartItemName")
    String cartItemName;

    @ColumnInfo(name = "cartItemCount")
    Integer cartItemCount;

    @ColumnInfo(name = "itemId")
    @PrimaryKey(autoGenerate = true)
    int itemId;

    @ColumnInfo(name = "cartPrice")
    int cartPrice;

    @Ignore
    public Cart() {
    }

    public Cart(String cartItemName, Integer cartItemCount, Integer cartPrice) {
        this.cartItemName = cartItemName;
        this.cartItemCount = cartItemCount;
        this.cartPrice = cartPrice;
    }

    public Integer getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(Integer cartPrice) {
        this.cartPrice = cartPrice;
    }

    public String getCartItemName() {
        return cartItemName;
    }

    public void setCartItemName(String cartItemName) {
        this.cartItemName = cartItemName;
    }

    public Integer getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(Integer cartItemCount) {
        this.cartItemCount = cartItemCount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
