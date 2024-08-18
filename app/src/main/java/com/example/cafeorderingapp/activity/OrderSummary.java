package com.example.cafeorderingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.cafeorderingapp.adapter.SummaryAdapter;
import com.example.cafeorderingapp.model.Cart;
import com.example.cafeorderingapp.model.FirebaseOrder;
import com.example.cafeorderingapp.viewmodel.MyViewModel;
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

public class OrderSummary extends AppCompatActivity {

    private RecyclerView rvSummary;
    private SummaryAdapter summaryAdapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String firebaseUser = firebaseAuth.getCurrentUser().getUid();

    ArrayList<Cart> cartArrayList = new ArrayList<>();
    TextView orderID, orderTotal;
    ArrayList<FirebaseOrder> summaryArrayList;
    ArrayList<FirebaseOrder> temp;
    DatabaseReference reference;

    Double itemTotal, taxes;
    int platformFee;
    TextView tvItemTotal, tvTaxes, tvPlatformFee, tvPaid, tvOrderDate, tvPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_summary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        orderID = findViewById(R.id.orderId);
        orderTotal = findViewById(R.id.orderTotal);
        Button homeRedirect = findViewById(R.id.homeRedirect);

        itemTotal = getIntent().getDoubleExtra("itemTotal", 0);
        taxes = getIntent().getDoubleExtra("taxes", 0);
        platformFee = getIntent().getIntExtra("platformFee", 0);

        summaryArrayList = new ArrayList<>();
        temp = new ArrayList<>();

        tvItemTotal = findViewById(R.id.itemTotalPrice);
        tvTaxes = findViewById(R.id.taxes);
        tvPlatformFee = findViewById(R.id.platformFeeOrder);
        tvPaid = findViewById(R.id.paid);
        tvOrderDate = findViewById(R.id.orderDate);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);





        String uniqueKey = getIntent().getStringExtra("uniqueKey");

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Orders").child(firebaseUser).child(uniqueKey);
        databaseReference = firebaseDatabase.getReference("Orders").child(firebaseUser).child(uniqueKey).child("firebaseOrders");

        rvSummary = findViewById(R.id.rvSummary);
        rvSummary.setLayoutManager(new LinearLayoutManager(this));


        homeRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSummary.this, MainActivity.class);
                intent.putExtra("uniqueKey", uniqueKey);
                startActivity(intent);
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FirebaseOrder orderSummary = dataSnapshot.getValue(FirebaseOrder.class);
                    summaryArrayList.add(orderSummary);
                }

                for (FirebaseOrder order : summaryArrayList){
                    Log.i("MYTAG", "" + order.getItemName() + order.getItemPrice());
                }


                summaryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer id = snapshot.child("orderId").getValue(Integer.class);
                Long total = snapshot.child("total").getValue(Long.class);

                orderID.setText("#" + id);
                orderTotal.setText("₹" + total);
                tvItemTotal.setText("₹" + itemTotal);
                tvTaxes.setText("₹" + taxes);
                tvPaid.setText("Paid Using : Cash (₹" + total + ")");
                tvPlatformFee.setText("₹" + platformFee);
                tvOrderDate.setText("" + new Date());
                tvPhoneNumber.setText("" + phone);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        summaryAdapter = new SummaryAdapter(summaryArrayList);
        rvSummary.setAdapter(summaryAdapter);


    }


}