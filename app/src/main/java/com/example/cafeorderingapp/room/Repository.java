package com.example.cafeorderingapp.room;

import android.app.Application;
import android.os.Looper;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cafeorderingapp.model.ItemModel;
import com.example.cafeorderingapp.model.Cart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
    ArrayList<ItemModel> cartArrayList = new ArrayList<>();

    private final CartDAO cartDAO;

   public MutableLiveData<List<ItemModel>> itemModelMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<ItemModel>> cartMutableLiveData = new MutableLiveData<>();
    ExecutorService executorService;
    Handler handler;

    public Repository(Application application){
        CartDatabase cartDatabase = CartDatabase.getDbInstance(application);
        this.cartDAO = cartDatabase.getCartDAO();

        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

//    public MutableLiveData<List<ItemModel>> getAllItems(){
//        itemModelMutableLiveData.setValue(itemModelArrayList);
//        return itemModelMutableLiveData;
//    }


    public void addItem(Cart cart){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.addItemToCart(cart);
            }
        });
    }

    public void deleteItem(Cart cart){
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                cartDAO.removeItemFromCart(cart);
            }
        });
    }

    public void updateItem(int count, String itemName){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updateCartItem(count, itemName);
            }
        });
    }

    public LiveData<List<Cart>> getCartItems(){
        return cartDAO.getAllCartItems();
    }
}
