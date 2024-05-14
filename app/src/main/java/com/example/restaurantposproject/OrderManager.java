package com.example.restaurantposproject;

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
        item.setQuantity(1); // Set an initial quantity
        ArrayList<FoodItem> tableOrder = getOrderData(tableNumber);
        tableOrder.add(item);
        orderData.put(tableNumber, tableOrder);
    }

    public boolean isItemAdded(FoodItem foodItem, String tableNumber) {
        ArrayList<FoodItem> tableOrder = getOrderData(tableNumber);
        return tableOrder.contains(foodItem);
    }
}
