package com.example.restaurantposproject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderData {
    private static HashMap<String, ArrayList<FoodItem>> orderData = new HashMap<>();

    public static void addItemToOrder(FoodItem item) {
        // Add the item to the order data

    }

   public static ArrayList<FoodItem> getOrderDataForTable(String tableNumber) {
    // Return the order data for the specified table...
    if (orderData.containsKey(tableNumber)) {
        return orderData.get(tableNumber);
    } else {
        return new ArrayList<FoodItem>();
    }
}

    public static void clearOrderDataForTable(String tableNumber) {
        // Clear the order data for the specified table...
    }
}
