package com.example.cafeorderingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cafeorderingapp.R;
import com.example.cafeorderingapp.adapter.MyAdapter;
import com.example.cafeorderingapp.adapter.ViewPagerAdapter;
import com.example.cafeorderingapp.model.Cart;
import com.example.cafeorderingapp.model.FirebaseCart;
import com.example.cafeorderingapp.model.ItemModel;
import com.example.cafeorderingapp.model.MenuItem;
import com.example.cafeorderingapp.model.Order;
import com.example.cafeorderingapp.model.User;
import com.example.cafeorderingapp.viewmodel.MyViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<ItemModel> itemModelArrayList;
    ArrayList<Cart> temp;

    MyViewModel viewModel;
    ViewPager2 viewPager2;
    ArrayList<Fragment> fragmentArrayList;
    ArrayList<MenuItem> menuItems;
    ViewPagerAdapter pagerAdapter;
    ArrayList<String> uniqueKeyList;
    ArrayList<FirebaseCart> firebaseCartArrayList;
    FirebaseDatabase firebaseDatabase;

    ArrayList<FirebaseCart> cartArrayList;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    ImageView signOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn = findViewById(R.id.cartBtn);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        ImageView loader = findViewById(R.id.loader);
        TextView loadingText = findViewById(R.id.loadingText);
        CardView banner = findViewById(R.id.viewPagerCard);
//
//        viewPager2 = findViewById(R.id.viewPager);
//
//        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
//        fragmentArrayList = new ArrayList<>();

        menuItems = new ArrayList<>();
        signOut = findViewById(R.id.signOut);

//        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
//        //pagerAdapter.addFragment(new FirstFragment());
//        viewPager2.setAdapter(pagerAdapter);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        if(user != null) {
            Log.i("MYTAG", "" + user.getUid());

            boolean empty = getIntent().getBooleanExtra("empty", false);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("Menu");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    menuItems.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        MenuItem item = dataSnapshot.getValue(MenuItem.class);
//                    String id = dataSnapshot.child("item_id").getValue(String.class);
//                    item.setItemId(id);
                        Log.i("MYTAG", "" + item.getItemId().toString());
                        menuItems.add(item);

                    }

                    if(!menuItems.isEmpty()){
                        recyclerView.setVisibility(View.VISIBLE);
                        banner.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.GONE);
                        loadingText.setVisibility(View.GONE);
                    }

                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            firebaseCartArrayList = new ArrayList<>();

            DatabaseReference reference = firebaseDatabase.getReference("Cart").child(firebaseAuth.getCurrentUser().getUid());

            if(getIntent().getBooleanExtra("flag", false)){
                DatabaseReference userReference = firebaseDatabase.getReference().child("User");
                User user1 = new User(getIntent().getStringExtra("username"), getIntent().getStringExtra("email"), firebaseAuth.getCurrentUser().getUid());
                userReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(user1);
            }


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    firebaseCartArrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FirebaseCart firebaseCart = dataSnapshot.getValue(FirebaseCart.class);
                        firebaseCartArrayList.add(firebaseCart);
                    }
                    if(firebaseCartArrayList.isEmpty()){
                        btn.setVisibility(View.GONE);
                    }else {
                        btn.setVisibility(View.VISIBLE);
                    }

                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            for (MenuItem item : menuItems) {
                Log.i("MYTAG", "" + item.getImageUrl());
            }

            Button trackBtn = findViewById(R.id.trackBtn);

            trackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, OrderTrackingActivity.class));
                }
            });

            //User Order Tracking Logic

//            DatabaseReference orderTracking = firebaseDatabase.getReference().child("Orders").child(firebaseAuth.getCurrentUser().getUid());
//
//            ArrayList<Order> userOrderList = new ArrayList<>();
//
//            orderTracking.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        Order order = dataSnapshot.getValue(Order.class);
//                        userOrderList.add(order);
//                    }
//
//                    for(Order order : userOrderList){
//                        if(order.getStatus() != 5){
//                            trackBtn.setVisibility(View.VISIBLE);
//                            break;
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });



            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                int i = 0;
//                boolean flag = false;
//                temp = new ArrayList<>();

//                temp = new ArrayList<>();
////
//                for (Cart cart : MyAdapter.cartArrayList) {
//                    Log.i("MYTAG", cart.getCartItemName() + " " + cart.getCartItemCount());
//
//                    temp.add(cart);
//
//                }

//
//                }

//                if(flag) {
//
//                    startActivity(new Intent(getApplicationContext(), CartActivity.class));
//                }else {
//                    Toast.makeText(MainActivity.this, "OOPS", Toast.LENGTH_SHORT).show();
//
//                }

                    Intent intent = new Intent(MainActivity.this, CartActivity.class);

                    startActivity(intent);

                }
            });


            recyclerView = findViewById(R.id.recyclerView);

//        itemModelArrayList = new ArrayList<>();
//
//        ItemModel item1 = new ItemModel("Cheesy 7","An Exotic Combination Of White Mozarella, Cream, White Cheese, Monterey Jack, Cream Orange, Colby And Orange Cheddar Cheese", 700);
//        itemModelArrayList.add(item1);
//
//
//        ItemModel item2 = new ItemModel("Spicy Veggie 65","Red Peprika,jalapenos,capsicum,onion,paneer 65 with extra cheese", 600);
//        itemModelArrayList.add(item2);

            uniqueKeyList = new ArrayList<>();
            DatabaseReference databaseReference1 = firebaseDatabase.getReference("Cart").child(firebaseAuth.getCurrentUser().getUid());

            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    uniqueKeyList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String uniqueKey = dataSnapshot.getKey();
                        uniqueKeyList.add(uniqueKey);
                    }
                    myAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

//        for(String key : uniqueKeyList) {
//            Log.i("MYTAG", "" + key);
//        }
//
//        MyViewModel viewModel = new ViewModelProvider(this).get(MyViewModel.class);
//        if(uniqueKeyList.isEmpty()){
//            myAdapter = new MyAdapter(menuItems, firebaseCartArrayList, viewModel, this);
//            Log.i("MYTAG", "true");
//        }
//        else {
//            myAdapter = new MyAdapter(menuItems, firebaseCartArrayList, viewModel, this, uniqueKeyList);
//        }

            myAdapter = new MyAdapter(menuItems, firebaseCartArrayList, viewModel, getApplicationContext(), uniqueKeyList, firebaseAuth.getCurrentUser().getUid());
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(myAdapter);
        }
        else{
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();



    }
}