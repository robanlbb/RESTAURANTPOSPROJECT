package com.example.restaurantposproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        FoodItem foodItem = orderData.get(holder.getAdapterPosition());
        holder.itemName.setText(foodItem.getName());
        holder.itemPrice.setText(String.valueOf(foodItem.getPrice()));
        holder.itemQuantity.setText(String.valueOf(foodItem.getQuantity()));

        holder.itemTotalPrice.setText(String.valueOf(foodItem.getPrice() * foodItem.getQuantity()));

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.itemQuantity.getText().toString());
                quantity++;
                holder.itemQuantity.setText(String.valueOf(quantity));
                foodItem.setQuantity(quantity); // Update the quantity in the local list
                holder.itemTotalPrice.setText(String.valueOf(foodItem.getPrice() * quantity)); // Update the total price
                updateItemQuantity(foodItem);
                quantityChangeListener.onQuantityChange();
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.itemQuantity.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    holder.itemQuantity.setText(String.valueOf(quantity));
                    foodItem.setQuantity(quantity); // Update the quantity in the local list
                    holder.itemTotalPrice.setText(String.valueOf(foodItem.getPrice() * quantity)); // Update the total price
                    updateItemQuantity(foodItem);
                    quantityChangeListener.onQuantityChange();
                } else {
                    // If quantity is 0, remove the item from the order
                    orderData.remove(foodItem);
                    notifyDataSetChanged();
                }
            }
        });
    }


    public void updateItemQuantity(FoodItem item) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("orders").child(item.getTableNumber()).child("items").child(item.getName());

        if (item.getQuantity() == 0) {
            // If the quantity is 0, remove the item from the Firebase database
            mDatabase.removeValue();
        } else {
            // If the quantity is not 0, update the quantity in the Firebase database
            mDatabase.setValue(item);
        }

        // Calculate the new total
        double newTotal = 0;
        for (FoodItem foodItem : orderData) {
            newTotal += foodItem.getPrice() * foodItem.getQuantity();
        }

        // Update the total value in the Firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("orders").child(item.getTableNumber()).child("total");
        mDatabase.setValue(newTotal);
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public interface OnQuantityChangeListener {
        void onQuantityChange();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemQuantity, itemTotalPrice, minus, plus;

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