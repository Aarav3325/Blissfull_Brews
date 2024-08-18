package com.example.cafeorderingapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeorderingapp.databinding.CartItemBinding;
import com.example.cafeorderingapp.room.Cart;
import com.example.cafeorderingapp.room.MyViewModel;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    ArrayList<Cart> cartArrayList;
    MyViewModel viewModel;

    public CartAdapter(ArrayList<Cart> cartArrayList) {
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding;
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cart_item, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartArrayList.get(position);
        Log.i("CART", cart.getCartItemName() + cart.getCartItemCount());
        holder.cartItemBinding.setCart(cart);
    }

    @Override
    public int getItemCount() {
        if(cartArrayList != null){
        return cartArrayList.size();
    }
    else {
        return 0;
    }
    }

//    public void setCartArrayList(ArrayList<Cart> cartArrayList) {
//        this.cartArrayList = cartArrayList;
//
//        notifyDataSetChanged();
//    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding cartItemBinding;

        public CartViewHolder(CartItemBinding cartItemBinding) {
            super(cartItemBinding.getRoot());
            this.cartItemBinding = cartItemBinding;
        }
    }
}
