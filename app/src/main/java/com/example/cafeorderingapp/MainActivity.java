package com.example.cafeorderingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeorderingapp.databinding.ItemViewBinding;
import com.example.cafeorderingapp.room.Cart;
import com.example.cafeorderingapp.room.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
   ArrayList<ItemModel> itemModelArrayList;
    ArrayList<Cart> temp;

    MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn = findViewById(R.id.cartBtn);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);



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


                    startActivity(new Intent(getApplicationContext(), CartActivity.class));

            }
        });





        recyclerView = findViewById(R.id.recyclerView);

        itemModelArrayList = new ArrayList<>();

        ItemModel item1 = new ItemModel("Cheesy 7","An Exotic Combination Of White Mozarella, Cream, White Cheese, Monterey Jack, Cream Orange, Colby And Orange Cheddar Cheese", 700);
        itemModelArrayList.add(item1);


        ItemModel item2 = new ItemModel("Spicy Veggie 65","Red Peprika,jalapenos,capsicum,onion,paneer 65 with extra cheese", 600);
        itemModelArrayList.add(item2);



        MyViewModel viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        myAdapter = new MyAdapter(itemModelArrayList, viewModel, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);



    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}