package com.example.cafeorderingapp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CartDAO {

    @Insert
    void addItemToCart(Cart cart);

    @Delete
    void removeItemFromCart(Cart cart);

    @Query("UPDATE cart_table SET cartItemCount=:count WHERE cartItemName=:itemName")
    void updateCartItem(int count, String itemName);

    @Query("SELECT * FROM cart_table")
    LiveData<List<Cart>> getAllCartItems();
}
