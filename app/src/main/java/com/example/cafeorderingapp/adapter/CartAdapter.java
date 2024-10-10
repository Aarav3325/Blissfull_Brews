package com.example.cafeorderingapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeorderingapp.R;
import com.example.cafeorderingapp.databinding.CartItemBinding;
import com.example.cafeorderingapp.model.Cart;
import com.example.cafeorderingapp.model.FirebaseCart;
import com.example.cafeorderingapp.model.FirebaseOrder;
import com.example.cafeorderingapp.viewmodel.MyViewModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    ArrayList<FirebaseCart> cartArrayList;
    ArrayList<String> uniqueKeys;
    MyViewModel viewModel;
    String userID;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Cart");

    public CartAdapter(ArrayList<FirebaseCart> cartArrayList, MyViewModel viewModel, ArrayList<String> uniqueKeys, String userID) {
        this.cartArrayList = cartArrayList;
        this.viewModel = viewModel;
        this.uniqueKeys = uniqueKeys;
        this.userID = userID;
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
        FirebaseCart cart = cartArrayList.get(position);
        Log.i("CART", cart.getCartItemName() + cart.getCartItemCount());

        //Increase item count

        holder.cartItemBinding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cartItemCount = cart.getCartItemCount();
                cartItemCount++;
                cart.setCartItemCount(cartItemCount);
                //Convert to Map in order to update child in Firebase RTDB
                Map<String, Object> hashmap = cart.toMap();
                databaseReference.child(userID).child(uniqueKeys.get(holder.getAdapterPosition())).updateChildren(hashmap);
            }
        });

        //Decrease item count
        holder.cartItemBinding.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cartItemCount = cart.getCartItemCount();
                cartItemCount--;
                cart.setCartItemCount(cartItemCount);
                //Convert to Map in order to update child in Firebase RTDB
                Map<String, Object> hashmap = cart.toMap();
                if(cartItemCount == 0){
                    //If count is 0 then remove item
                    databaseReference.child(userID).child(uniqueKeys.get(holder.getAdapterPosition())).removeValue();
                }
                else {
                    cart.setCartItemCount(cartItemCount);
                    databaseReference.child(userID).child(uniqueKeys.get(holder.getAdapterPosition())).updateChildren(hashmap);
                }
            }
        });

//        holder.cartItemBinding.addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int cartItemCount = cart.getCartItemCount();
//                Log.i("MYTAG", "" +cartItemCount);
//                cartItemCount++;
//                cart.setCartItemCount(cartItemCount);
//                Log.i("MYTAG","" + cart.getCartItemCount());
//                viewModel.updateItemCart(cart.getCartItemCount(), cart.getCartItemName());
//                holder.cartItemBinding.count.setText(String.valueOf(cartItemCount));
//            }
//        });
//
//        holder.cartItemBinding.removeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int cartItemCount = cart.getCartItemCount();
//                cartItemCount--;
//
//                if(cartItemCount == 0){
//                    viewModel.deleteItemFromCart(cart);
//                }
//                else {
//                    cart.setCartItemCount(cartItemCount);
//                    viewModel.updateItemCart(cartItemCount, cart.getCartItemName());
//                    holder.cartItemBinding.count.setText(String.valueOf(cartItemCount));
//                }
//
//
//            }
//        });

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
