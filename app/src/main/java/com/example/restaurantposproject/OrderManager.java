package com.example.restaurantposproject;

import java.util.ArrayList;

public class OrderManager {
    private static final OrderManager ourInstance = new OrderManager();

    public static OrderManager getInstance() {
        return ourInstance;
    }

    private final ArrayList<FoodItem> orderData;

    private OrderManager() {
        orderData = new ArrayList<>();
    }

    public void addItem(FoodItem item) {
        orderData.add(item);
    }

    public ArrayList<FoodItem> getOrderData() {
        return orderData;
    }
}
