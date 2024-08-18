package com.example.cafeorderingapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeorderingapp.databinding.ItemViewBinding;
import com.example.cafeorderingapp.room.Cart;
import com.example.cafeorderingapp.room.MyViewModel;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<ItemModel> itemArrayList;
    MyViewModel viewModel;
    Context context;
    Cart cart;
    //static ArrayList<ItemModel> cartList = new ArrayList<>();
    static ArrayList<Cart> cartArrayList = new ArrayList<>();

    int count = 0;

    public MyAdapter(ArrayList<ItemModel> itemArrayList, MyViewModel viewModel, Context context) {
        this.itemArrayList = itemArrayList;
        this.viewModel = viewModel;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewBinding binding;
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_view, parent, false);

        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemModel itemModel = itemArrayList.get(position);
        holder.binding.setItem(itemModel);

        holder.binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart = new Cart(itemModel.getItemName(),itemModel.getCount(), itemModel.getPrice());
                Log.i("MYTAG", cart.getCartItemName() + " " + cart.getCartItemCount());

                int c = cart.getCartItemCount();
                c++;
                if(c==1){
                    itemModel.setCount(1);
                    cart.setCartItemCount(1);
                    viewModel.addItemToCart(cart);
                    //cartArrayList.add(cart);

                    Log.i("COUNTCART", "" + cart.getCartItemCount());
                }
                else {

                    itemModel.setCount(c);
                    cart.setCartItemCount(c);
                    viewModel.updateItemCart(c, cart.getCartItemName());
                    //cartArrayList.get(position).setCartItemCount(c);
                    //Log.i("COUNTCART", "" + cartArrayList.get(position).getCartItemCount());
                }
//                else {
//                    //Log.i("COUNT", "" + c);
//
//                }*/


                Log.i("ITEM", "" + cart.getCartItemCount());


                holder.binding.add.setText("ADD " + c + " +");

            }
        });
    }

//    public void setCartArrayList(ArrayList<Cart> cartArrayList) {
//        this.cartArrayList = cartArrayList;
//
//        notifyDataSetChanged();
//    }


    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ItemViewBinding binding;

        public MyViewHolder(@NonNull ItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
