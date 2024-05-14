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
    private OnQuantityChangeListener quantityChangeListener;

    public OrderListAdapter(ArrayList<FoodItem> orderData, OnQuantityChangeListener listener) {
        this.orderData = orderData;
        this.quantityChangeListener = listener;
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
        holder.itemQuantity.setText(String.valueOf(foodItem.getQuantity()));

        holder.itemTotalPrice.setText(String.valueOf(foodItem.getPrice()));

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = foodItem.getQuantity();
                if (quantity > 0) {
                    quantity--;
                    foodItem.setQuantity(quantity);
                    holder.itemQuantity.setText(String.valueOf(quantity));
                    holder.itemTotalPrice.setText(String.valueOf(foodItem.getPrice() * quantity));
                    quantityChangeListener.onQuantityChange();
                }
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = foodItem.getQuantity();
                quantity++;
                foodItem.setQuantity(quantity);
                holder.itemQuantity.setText(String.valueOf(quantity));
                holder.itemTotalPrice.setText(String.valueOf(foodItem.getPrice() * quantity));
                quantityChangeListener.onQuantityChange();
            }
        });
        // Set other views in the holder with data from foodItem
    }



    @Override
    public int getItemCount() {
        return orderData.size();
    }
    public interface OnQuantityChangeListener {
        void onQuantityChange();
    }
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemQuantity, itemTotalPrice, minus, plus;
        // Add other views that are in your order item layout

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemQuantity = itemView.findViewById(R.id.tv_itemQuantity);
            itemTotalPrice = itemView.findViewById(R.id.tv_itemTotal);
            minus = itemView.findViewById(R.id.btn_subQuantity);
            plus = itemView.findViewById(R.id.btn_addQuantity);


        }
    }
}