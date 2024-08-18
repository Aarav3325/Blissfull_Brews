package com.example.cafeorderingapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cafeorderingapp.model.ItemModel;
import com.example.cafeorderingapp.model.Cart;
import com.example.cafeorderingapp.room.Repository;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    Repository repository;
    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public MutableLiveData<List<ItemModel>> getAllItems(){
        return repository.itemModelMutableLiveData;
    }

    public void addItemToCart(Cart cart){
        repository.addItem(cart);
    }

    public void deleteItemFromCart(Cart cart){
        repository.deleteItem(cart);
    }

    public void updateItemCart(int count, String itemName){
        repository.updateItem(count, itemName);
    }

    public LiveData<List<Cart>> getAllCartItems(){
        return repository.getCartItems();
    }
}
