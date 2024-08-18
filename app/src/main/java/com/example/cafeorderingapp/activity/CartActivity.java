package com.example.cafeorderingapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeorderingapp.R;
import com.example.cafeorderingapp.adapter.CartAdapter;
import com.example.cafeorderingapp.model.Cart;
import com.example.cafeorderingapp.model.FirebaseCart;
import com.example.cafeorderingapp.room.CartDatabase;
import com.example.cafeorderingapp.model.FirebaseOrder;
import com.example.cafeorderingapp.viewmodel.MyViewModel;
import com.example.cafeorderingapp.model.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CartActivity extends AppCompatActivity {

    ArrayList<FirebaseCart> cartArrayList;
    CartAdapter cartAdapter;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String firebaseUser = firebaseAuth.getCurrentUser().getUid();

    CartDatabase cartDatabase;
    MyViewModel viewModel;
    TextView tvTotal;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, reference;
    TextView tvItemTotal, tvGstTotal, tvPlatformFee, tvGrandTotal;
    EditText etName, etPhone;
    ArrayList<String> uniqueKeys;
    ProgressDialog progressDialog;


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


        progressDialog = new ProgressDialog(CartActivity.this);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Cart").child(firebaseUser);
        reference = firebaseDatabase.getReference("Orders").child(firebaseUser);

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

        //String uniqueKey = databaseReference.push().getKey();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    FirebaseCart firebaseCart = dataSnapshot.getValue(FirebaseCart.class);
                    cartArrayList.add(firebaseCart);
                }

                if(cartArrayList.isEmpty()){
                    findViewById(R.id.main).setVisibility(View.GONE);
                    findViewById(R.id.emptyCart).setVisibility(View.VISIBLE);
                }
                cartAdapter.notifyDataSetChanged();

                long total = 0;
                for (FirebaseCart cart : cartArrayList) {
                    int price = cart.getCartItemPrice();
                    int count = cart.getCartItemCount();
                    total = total + ((long) count * price);
                    Log.i("TOTAL", "" + cart.getCartItemName() + cart.getCartItemPrice());
                }

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

                                ArrayList<FirebaseOrder> orderArrayList = new ArrayList<>();



                long finalTotal1 = total;
                final String uniqueKey = reference.push().getKey();
                checkout.setOnClickListener(new View.OnClickListener() {
                    final Random randomOrderId = new Random(34);
                    int orderId = generateOrderId();
                    @Override
                    public void onClick(View view) {
                        progressDialog = new ProgressDialog(CartActivity.this);
                        progressDialog.setMessage("Processing Order");
                        progressDialog.show();
                        for (FirebaseCart cart : cartArrayList){
                             FirebaseOrder firebaseOrder = new FirebaseOrder(cart.getCartItemName(), cart.getCartItemPrice(), cart.getCartItemCount());
                             orderArrayList.add(firebaseOrder);

                        }

                        Order order = new Order(orderId, finalTotal1, orderArrayList, new Date().toString());


                        reference.child(uniqueKey).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();

                                Intent intent = new Intent(CartActivity.this, OrderTrackingActivity.class);
                                intent.putExtra("name", etName.getText().toString());
                                intent.putExtra("phone", etPhone.getText().toString());
                                intent.putExtra("uniqueKey", uniqueKey);
                                intent.putExtra("itemTotal", itemTotal);
                                intent.putExtra("taxes", totalGST);
                                intent.putExtra("platformFee", platformFee);
                                startActivity(intent);

                                databaseReference.removeValue();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });



                        cartArrayList.clear();
                        cartAdapter.notifyDataSetChanged();

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uniqueKeys = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                uniqueKeys.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String uniqueKey = dataSnapshot.getKey();
                    uniqueKeys.add(uniqueKey);
                    Log.i("UNIQUEKEY", ""+dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        viewModel.getAllCartItems().observe(this, new Observer<List<Cart>>() {
//
//            @Override
//            public void onChanged(List<Cart> carts) {
//                //   cartArrayList = (ArrayList<Cart>) carts;
//                cartArrayList.clear();
//
//                for (Cart c : carts) {
//                    cartArrayList.add(c);
//                    //Log.i("CART", "" + cartArrayList.get(i).getCartItemName() + cartArrayList.get(i).getCartItemCount());
//
//                }
//
//                if(carts.isEmpty()){
//                    findViewById(R.id.main).setVisibility(View.GONE);
//                    findViewById(R.id.emptyCart).setVisibility(View.VISIBLE);
//                }
//                //tvTotal.setText(total);
//                cartAdapter.notifyDataSetChanged();
//
//                long total = 0;
//                for (Cart cart : cartArrayList) {
//                    int price = cart.getCartPrice();
//                    int count = cart.getCartItemCount();
//                    total = total + ((long) count * price);
//                    Log.i("TOTAL", "" + cart.getCartItemName() + cart.getCartPrice());
//                }
//
//                //tvTotal.setText("Total : " + total);
//
//
//                double cgst = (double) (total * 5) /100;
//                double sgst = (double) (total * 5) /100;
//                double totalGST = cgst + sgst;
//                int platformFee = 1;
//                double itemTotal = total - (totalGST + platformFee);
//
//                double grandTotal = itemTotal + totalGST + platformFee;
//
//                tvItemTotal.setText("₹" + itemTotal);
//                tvGstTotal.setText("₹" + totalGST);
//                tvPlatformFee.setText("₹" + platformFee);
//                tvGrandTotal.setText("₹" + grandTotal);
//
//                ArrayList<FirebaseOrder> orderArrayList = new ArrayList<>();
//
//
//
//                long finalTotal1 = total;
//                reference = databaseReference;
//                final String uniqueKey = reference.push().getKey();
//                checkout.setOnClickListener(new View.OnClickListener() {
//                    final Random randomOrderId = new Random(34);
//                    int orderId = generateOrderId();
//                    @Override
//                    public void onClick(View view) {
//                        progressDialog = new ProgressDialog(CartActivity.this);
//                        progressDialog.setMessage("Processing Order");
//                        progressDialog.show();
//                        for (Cart cart : cartArrayList){
//                             FirebaseOrder firebaseOrder = new FirebaseOrder(cart.getCartItemName(), cart.getCartPrice(), cart.getCartItemCount());
//                             orderArrayList.add(firebaseOrder);
//
//                        }
//
//                        Order order = new Order(orderId, finalTotal1, orderArrayList, new Date().toString());
//
//
//                        reference.child(uniqueKey).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                progressDialog.dismiss();
//                                Intent intent = new Intent(CartActivity.this, OrderTrackingActivity.class);
//                                intent.putExtra("name", etName.getText().toString());
//                                intent.putExtra("phone", etPhone.getText().toString());
//                                intent.putExtra("uniqueKey", uniqueKey);
//                                intent.putExtra("itemTotal", itemTotal);
//                                intent.putExtra("taxes", totalGST);
//                                intent.putExtra("platformFee", platformFee);
//                                startActivity(intent);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                progressDialog.dismiss();
//                            }
//                        });
//
//
//
//                        cartArrayList.clear();
//                        cartAdapter.notifyDataSetChanged();
//
//                    }
//                });
//
//
//
//            }
//        });


        cartAdapter = new CartAdapter(cartArrayList, viewModel, uniqueKeys, firebaseUser);
        recyclerView.setAdapter(cartAdapter);


    }


    private int generateOrderId(){
        Random r = new Random(System.currentTimeMillis());
        return 1000000000 + r.nextInt(2000000000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CartActivity.this, MainActivity.class);
        intent.putExtra("uniqueKeyList", uniqueKeys);
        startActivity(intent);
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