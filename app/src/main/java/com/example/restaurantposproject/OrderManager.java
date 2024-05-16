package com.example.restaurantposproject;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderManager {
    private static final OrderManager ourInstance = new OrderManager();

    public static OrderManager getInstance() {
        return ourInstance;
    }

    private final HashMap<String, ArrayList<FoodItem>> orderData;

    private OrderManager() {
        orderData = new HashMap<>();
    }

    public ArrayList<FoodItem> getOrderData(String tableNumber) {
        return orderData.getOrDefault(tableNumber, new ArrayList<>());
    }

    public void addItem(FoodItem item, String tableNumber) {
        ArrayList<FoodItem> tableOrder = getOrderData(tableNumber);
        int index = tableOrder.indexOf(item);
        if (index != -1) {
            // Item already exists in the order, just increase the quantity
            FoodItem existingItem = tableOrder.get(index);
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            // Item does not exist in the order, add it
            item.setQuantity(1); // Set an initial quantity
            item.setTableNumber(tableNumber); // Set the table number
            tableOrder.add(item);
        }
        orderData.put(tableNumber, tableOrder);

        // Calculate the total
        double total = 0;
        for (FoodItem foodItem : tableOrder) {
            total += foodItem.getPrice() * foodItem.getQuantity();
        }

        // Update the order and total in Firebase
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("orders").child(tableNumber);
        for (FoodItem foodItem : tableOrder) {
            mDatabase.child("items").child(foodItem.getName()).setValue(foodItem);
        }
        mDatabase.child("total").setValue(total);
    }



    public void clearOrderData(String tableNumber) {
        orderData.remove(tableNumber);
    }
}