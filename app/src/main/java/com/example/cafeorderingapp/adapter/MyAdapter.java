package com.example.cafeorderingapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cafeorderingapp.R;
import com.example.cafeorderingapp.databinding.ItemViewBinding;
import com.example.cafeorderingapp.model.FirebaseCart;
import com.example.cafeorderingapp.model.ItemModel;
import com.example.cafeorderingapp.model.MenuItem;
import com.example.cafeorderingapp.viewmodel.MyViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<ItemModel> itemArrayList;
    MyViewModel viewModel;
    ArrayList<String> temp = new ArrayList<>();
    Context context;
    //Cart cart;
    //static ArrayList<ItemModel> cartList = new ArrayList<>();
    ArrayList<MenuItem> menuItems;
    boolean empty;
    int count = 0;
    ArrayList<FirebaseCart> firebaseCartArrayList;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DatabaseReference reference;
    ArrayList<FirebaseCart> cartArrayList;
    String uniqueKey;
    ArrayList<String> uniqueKeyList;
    String userID;
    public MyAdapter(ArrayList<MenuItem> menuItems, MyViewModel viewModel, Context context) {
        this.menuItems = menuItems;
        this.viewModel = viewModel;
        this.context = context;
        //this.empty = empty;
    }

    public MyAdapter(ArrayList<MenuItem> menuItems, ArrayList<FirebaseCart> firebaseCartArrayList, MyViewModel viewModel, Context context, ArrayList<String> uniqueKeyList, String userID) {
        this.menuItems = menuItems;
        this.firebaseCartArrayList = firebaseCartArrayList;
        this.viewModel = viewModel;
        this.context = context;
        this.uniqueKeyList = uniqueKeyList;
        this.userID = userID;
        this.databaseReference = firebaseDatabase.getReference("Cart").child(userID);
        //this.empty = empty;
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
        MenuItem menuItem = menuItems.get(position);
        Log.i("ItemId", menuItem.getItemId() + " ");

        holder.binding.setMenu(menuItem);

        //Load item images using Glide Dependency
        Glide.with(context).load(menuItem.getImageUrl()).into(holder.binding.image);

        //
        FirebaseCart firebaseCart1 = new FirebaseCart(menuItem.getItemName(), menuItem.getItemPrice(), menuItem.getCount(), menuItem.getItemId());
        Log.i("MYTAG", "" + firebaseCartArrayList.contains(firebaseCart1));


        //Check if cart is empty or not
        if (!firebaseCartArrayList.isEmpty()) {

            //If cart is not empty then update item counts from firebaseCartList
            for (FirebaseCart firebaseCart : firebaseCartArrayList) {
                //Check if item is present in art or not
                if (menuItem.getItemId().equals(firebaseCart.getCartItemId())) {
                    Log.i("MYTAG", "" + menuItem.getItemId() + " " + firebaseCart.getCartItemCount());
                    holder.binding.add.setText("" + firebaseCart.getCartItemCount());
                    holder.binding.addItem.setVisibility(View.VISIBLE);
                    holder.binding.removeItem.setVisibility(View.VISIBLE);

                    // Add item count
                    holder.binding.addItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int itemCount = firebaseCart.getCartItemCount();
                            itemCount++;
                            menuItem.setCount(itemCount);
                            firebaseCart.setCartItemCount(itemCount);
                            Map<String, Object> hashMap = firebaseCart.toMap();
                            databaseReference.child(firebaseCart.getKey()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    holder.binding.add.setText("" + firebaseCart.getCartItemCount());
                                }
                            });
                        }
                    });

                    // Remove item count
                    holder.binding.removeItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int itemCount = firebaseCart.getCartItemCount();
                            itemCount--;
                            menuItem.setCount(itemCount);
                            firebaseCart.setCartItemCount(itemCount);

                            Map<String, Object> hashMap = firebaseCart.toMap();
                            if (itemCount == 0) {
                                holder.binding.add.setText("ADD" + " +");
                                holder.binding.addItem.setVisibility(View.GONE);
                                holder.binding.removeItem.setVisibility(View.GONE);
                                databaseReference.child(firebaseCart.getKey()).removeValue();
                            } else {
                                databaseReference.child(firebaseCart.getKey()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        holder.binding.add.setText("" + firebaseCart.getCartItemCount());
                                    }
                                });
                            }
                        }
                    });

                } else {
                        // For items that are not present in cart

                        // Add item to cart
                        holder.binding.add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                FirebaseCart firebaseCart1 = new FirebaseCart(menuItem.getItemName(), menuItem.getItemPrice(), menuItem.getCount(), menuItem.getItemId());
                                int user = firebaseCartArrayList.size();

                                int itemCount = firebaseCart1.getCartItemCount();
                                itemCount++;
                                uniqueKey = databaseReference.push().getKey();
                                firebaseCart1.setKey(uniqueKey);
                                if (itemCount == 1) {
                                    menuItem.setCount(1);
                                    firebaseCart1.setCartItemCount(1);
                                    databaseReference.child(uniqueKey).setValue(firebaseCart1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.addItem.setVisibility(View.VISIBLE);
                                            holder.binding.removeItem.setVisibility(View.VISIBLE);
                                            holder.binding.add.setText("" + firebaseCart1.getCartItemCount());
                                        }
                                    });
                                }
                            }
                        });


                }
            }



        } else {

            // When cart is empty
            holder.binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseCart firebaseCart1 = new FirebaseCart(menuItem.getItemName(), menuItem.getItemPrice(), menuItem.getCount(), menuItem.getItemId());

                    int itemCount = firebaseCart1.getCartItemCount();
                    itemCount++;
                    uniqueKey = databaseReference.push().getKey();
                    firebaseCart1.setKey(uniqueKey);
                    if (itemCount == 1) {
                        menuItem.setCount(1);
                        firebaseCart1.setCartItemCount(1);
                        databaseReference.child(uniqueKey).setValue(firebaseCart1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                holder.binding.addItem.setVisibility(View.VISIBLE);
                                holder.binding.removeItem.setVisibility(View.VISIBLE);
                                holder.binding.add.setText("" + firebaseCart1.getCartItemCount());
                            }
                        });
                    }
                }
            });
        }


//        if (!firebaseCartArrayList.isEmpty()) {
//
//            for (FirebaseCart firebaseCart : firebaseCartArrayList) {
//                if (menuItem.getItemId().equals(firebaseCart.getCartItemId())) {
//                    holder.binding.add.setText("" + firebaseCart.getCartItemCount());
//                    holder.binding.addItem.setVisibility(View.VISIBLE);
//                    holder.binding.removeItem.setVisibility(View.VISIBLE);
//
//                    holder.binding.addItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            int itemCount = firebaseCart.getCartItemCount();
//                            itemCount++;
//                            menuItem.setCount(itemCount);
//                            firebaseCart.setCartItemCount(itemCount);
//                            Map<String, Object> hashMap = firebaseCart.toMap();
//                            databaseReference.child(firebaseCart.getKey()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//
//                                    holder.binding.add.setText("" + firebaseCart.getCartItemCount());
//                                }
//                            });
//                        }
//                    });
//
//                    holder.binding.removeItem.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            int itemCount = firebaseCart.getCartItemCount();
//                            itemCount--;
//                            menuItem.setCount(itemCount);
//                            firebaseCart.setCartItemCount(itemCount);
//
//                            Map<String, Object> hashMap = firebaseCart.toMap();
//                            if (itemCount == 0) {
//                                holder.binding.add.setText("ADD" + " +");
//                                holder.binding.addItem.setVisibility(View.GONE);
//                                holder.binding.removeItem.setVisibility(View.GONE);
//                                databaseReference.child(firebaseCart.getKey()).removeValue();
//                            } else {
//                                databaseReference.child(firebaseCart.getKey()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        holder.binding.add.setText("" + firebaseCart.getCartItemCount());
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
//            }
//                else{
//                    holder.binding.addItem.setVisibility(View.GONE);
//                    holder.binding.removeItem.setVisibility(View.GONE);
//                    holder.binding.add.setText("ADD +");
//                    holder.binding.add.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            FirebaseCart firebaseCart1 = new FirebaseCart(menuItem.getItemName(), menuItem.getItemPrice(), menuItem.getCount(), menuItem.getItemId());
//
//                            int itemCount = firebaseCart1.getCartItemCount();
//                            itemCount++;
//                            uniqueKey = databaseReference.push().getKey();
//                            firebaseCart1.setKey(uniqueKey);
//                            if (itemCount == 1) {
//                                menuItem.setCount(1);
//                                firebaseCart1.setCartItemCount(1);
//                                databaseReference.child(uniqueKey).setValue(firebaseCart1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        holder.binding.addItem.setVisibility(View.VISIBLE);
//                                        holder.binding.removeItem.setVisibility(View.VISIBLE);
//                                        holder.binding.add.setText("" + firebaseCart1.getCartItemCount());
//                                    }
//                                });
//                            }
//                        }
//                    });
//                }
//            }

//        else {
//            Log.i("MYTAG", "EMPTY CHECKED");
//            holder.binding.add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    FirebaseCart firebaseCart = new FirebaseCart(menuItem.getItemName(), menuItem.getItemPrice(), menuItem.getCount(), menuItem.getItemId());
//
//                    int itemCount = firebaseCart.getCartItemCount();
//                    itemCount++;
//                    uniqueKey = databaseReference.push().getKey();
//                    firebaseCart.setKey(uniqueKey);
//                    if (itemCount == 1) {
//                        menuItem.setCount(1);
//                        firebaseCart.setCartItemCount(1);
//                        databaseReference.child(uniqueKey).setValue(firebaseCart).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                holder.binding.addItem.setVisibility(View.VISIBLE);
//                                holder.binding.removeItem.setVisibility(View.VISIBLE);
//                                holder.binding.add.setText("" + firebaseCart.getCartItemCount());
//                            }
//                        });
//                    }
//                }
//            });
//
//        }


    }


    @Override
    public int getItemCount() {
        return menuItems.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ItemViewBinding binding;

        public MyViewHolder(@NonNull ItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
