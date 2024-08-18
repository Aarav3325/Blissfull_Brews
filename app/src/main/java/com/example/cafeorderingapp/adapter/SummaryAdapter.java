package com.example.cafeorderingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeorderingapp.R;
import com.example.cafeorderingapp.model.FirebaseOrder;

import java.util.ArrayList;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    ArrayList<FirebaseOrder> summaryArrayList;

    public SummaryAdapter(ArrayList<FirebaseOrder> summaryArrayList) {
        this.summaryArrayList = summaryArrayList;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        FirebaseOrder summary = summaryArrayList.get(position);

        holder.itemName.setText(summary.getItemName());
        holder.itemCount.setText(String.valueOf(summary.getItemCount()));
        holder.itemPrice.setText("" + summary.getItemCount()*summary.getItemPrice());
        holder.price.setText("X " + summary.getItemPrice());
    }

    @Override
    public int getItemCount() {
        return summaryArrayList.size();
    }

    public static class SummaryViewHolder extends RecyclerView.ViewHolder{

        TextView itemName, itemCount, itemPrice, price;
        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.orderItemName);
            itemCount = itemView.findViewById(R.id.orderItemCount);
            itemPrice = itemView.findViewById(R.id.orderItemPrice);
            price = itemView.findViewById(R.id.price);
        }
    }
}
