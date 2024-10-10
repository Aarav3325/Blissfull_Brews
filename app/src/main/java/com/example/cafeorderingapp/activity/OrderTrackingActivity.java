package com.example.cafeorderingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cafeorderingapp.R;
import com.example.cafeorderingapp.model.Cart;
import com.example.cafeorderingapp.viewmodel.MyViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class OrderTrackingActivity extends AppCompatActivity {

    TextView statusOrderId, statusName, statusPhoneNumber, statusGrandTotal, statusText, update;
    Button orderSummary;
    GifImageView statusImage;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String firebaseUser = firebaseAuth.getCurrentUser().getUid();

    ArrayList<Cart> cartArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        statusOrderId = findViewById(R.id.statusOrderId);
        statusName = findViewById(R.id.statusName);
        statusPhoneNumber = findViewById(R.id.statusPhoneNumber);
        statusGrandTotal = findViewById(R.id.statusGrandTotal);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        String uniqueKey = getIntent().getStringExtra("uniqueKey");

        orderSummary = findViewById(R.id.orderSummary);
        statusText = findViewById(R.id.statusText);
        statusImage = findViewById(R.id.statusImage);
        update = findViewById(R.id.update);


        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        Double itemTotal = getIntent().getDoubleExtra("itemTotal", 0);
        Double taxes = getIntent().getDoubleExtra("taxes", 0);
        int platformFee = getIntent().getIntExtra("platformFee", 0);


        orderSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderTrackingActivity.this, OrderSummary.class);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("uniqueKey", uniqueKey);
                intent.putExtra("itemTotal", itemTotal);
                intent.putExtra("taxes", taxes);
                intent.putExtra("platformFee", platformFee);
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Orders").child(firebaseUser).child(uniqueKey);

        //Update order status and display order details
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Integer id = snapshot.child("orderId").getValue(Integer.class);
                Long total = snapshot.child("total").getValue(Long.class);
                String name = getIntent().getStringExtra("name");
                String phone = getIntent().getStringExtra("phone");

                statusOrderId.setText("#" + id);
                statusGrandTotal.setText("₹" + total);
                statusName.setText(name);
                statusPhoneNumber.setText(phone);

                int status = snapshot.child("status").getValue(Integer.class);
                if(status == 0){
                    statusText.setText("Waiting for Confirmation");
                }
                else if(status == 1){
                    statusText.setText("Order Confirmed");
                    statusImage.setImageResource(R.drawable.confirm);
                    update.setText("Order will be ready in 20-25 minutes");
                }
                else if (status == 2){
                    statusText.setText("Preparing Order");
                    statusImage.setImageResource(R.drawable.preparing);
                    update.setText("Order will be ready in 20-25 minutes");
                }
                else if(status == 3){
                    statusText.setText("Ready for pickup");
                    statusImage.setImageResource(R.drawable.ready);
                    update.setText("Please pick up the order");
                }
                else if(status == 4){
                    statusText.setText("Order Completed");
                    statusImage.setImageResource(R.drawable.completed);
                    update.setText("Enjoy your food");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onStart() {
        super.onStart();


        MyViewModel viewModel = new ViewModelProvider(this).get(MyViewModel.class);

        viewModel.getAllCartItems().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                cartArrayList = (ArrayList<Cart>) carts;

                for(Cart c : cartArrayList){
                    viewModel.deleteItemFromCart(c);
                }
            }

        });
    }
}