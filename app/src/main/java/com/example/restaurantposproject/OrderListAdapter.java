package com.example.restaurantposproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {

    private ArrayList<FoodItem> orderData;

    public OrderListAdapter(ArrayList<FoodItem> orderData) {
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        FoodItem foodItem = orderData.get(position);
        holder.itemName.setText(foodItem.getName());
        holder.itemPrice.setText(String.valueOf(foodItem.getPrice()));
        // Set other views in the holder with data from foodItem
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice;
        // Add other views that are in your order item layout

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            // Initialize other views from the itemView
        }
    }
}