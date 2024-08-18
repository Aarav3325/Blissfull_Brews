package com.example.cafeorderingapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeorderingapp.databinding.CartItemBinding;
import com.example.cafeorderingapp.room.Cart;
import com.example.cafeorderingapp.room.CartDatabase;
import com.example.cafeorderingapp.room.MyViewModel;
import com.example.cafeorderingapp.room.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CartActivity extends AppCompatActivity {

    ArrayList<Cart> cartArrayList;
    CartAdapter cartAdapter;

    CartDatabase cartDatabase;
    MyViewModel viewModel;
    TextView tvTotal;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, reference;
    TextView tvItemTotal, tvGstTotal, tvPlatformFee, tvGrandTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        for(ItemModel i : MyAdapter.cartList){
//
//            Log.i("MYTAG", i.getItemName() + i.getCount());
//
//        }

        //tvTotal = findViewById(R.id.totalAmt);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");

        tvItemTotal = findViewById(R.id.itemTotal);
        tvGstTotal = findViewById(R.id.gstTotal);
        tvPlatformFee = findViewById(R.id.platformFee);
        tvGrandTotal = findViewById(R.id.grandTotal);

        cartArrayList = new ArrayList<>();
        cartDatabase = CartDatabase.getDbInstance(this);

        RecyclerView recyclerView = findViewById(R.id.cartRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        viewModel = new ViewModelProvider(this).get(MyViewModel.class);


        Button checkout = findViewById(R.id.checkoutBtn);



        viewModel.getAllCartItems().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                //   cartArrayList = (ArrayList<Cart>) carts;
                cartArrayList.clear();

                int i = 0;
                for (Cart c : carts) {
                    cartArrayList.add(c);
                    Log.i("CART", "" + cartArrayList.get(i).getCartItemName() + cartArrayList.get(i).getCartItemCount());
                    i++;
                }
                //tvTotal.setText(total);
                cartAdapter.notifyDataSetChanged();

                long total = 0;
                for (Cart cart : cartArrayList) {
                    int price = cart.getCartPrice();
                    int count = cart.getCartItemCount();
                    total = total + ((long) count * price);
                    Log.i("TOTAL", "" + cart.getCartItemName() + cart.getCartPrice());
                }

                //tvTotal.setText("Total : " + total);


                double cgst = (double) (total * 5) /100;
                double sgst = (double) (total * 5) /100;
                double totalGST = cgst + sgst;
                int platformFee = 1;
                double itemTotal = total - (totalGST + platformFee);

                double grandTotal = itemTotal + totalGST + platformFee;

                tvItemTotal.setText("₹" + itemTotal);
                tvGstTotal.setText("₹" + totalGST);
                tvPlatformFee.setText("₹" + platformFee);
                tvGrandTotal.setText("₹" + grandTotal);

                ArrayList<Order> orderArrayList = new ArrayList<>();

                long finalTotal1 = total;
                reference = databaseReference;
                final String uniqueKey = reference.push().getKey();
                checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Random randomOrderId = new Random(34);
                        int orderId = randomOrderId.nextInt();
                        for (Cart cart : cartArrayList){
                            Order order = new Order(cart.getCartItemName(), cart.getCartPrice(), cart.getCartItemCount(), orderId, finalTotal1);
                            orderArrayList.add(order);

                            assert uniqueKey != null;
                        }


                        reference.child(uniqueKey).setValue(orderArrayList);
                        reference.child(uniqueKey).child("cartTotal").setValue(finalTotal1);

                        cartArrayList.clear();
                        cartAdapter.notifyDataSetChanged();


                    }
                });



            }
        });


        cartAdapter = new CartAdapter(cartArrayList);
        recyclerView.setAdapter(cartAdapter);


    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
//
//        cartArrayList.clear();
//
//        cartAdapter.notifyDataSetChanged();
//
//
//        Toast.makeText(this, "Pause", Toast.LENGTH_LONG).show();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
//
//
//        viewModel.getAllCartItems().observe(this, new Observer<List<Cart>>() {
//            @Override
//            public void onChanged(List<Cart> carts) {
//                //   cartArrayList = (ArrayList<Cart>) carts;
//                int i = 0;
//                for(Cart c : carts){
//                        cartArrayList.add(c);
//
////                    Log.i("CART", ""+cartArrayList.get(0).getCartItemName() + cartArrayList.get(0).getCartItemCount());
////                    i++;
//                }
//                cartAdapter.notifyDataSetChanged();
//            }
//        });

}