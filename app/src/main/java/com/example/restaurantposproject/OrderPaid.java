package com.example.restaurantposproject;

import java.util.ArrayList;

public class OrderPaid {
    private ArrayList<orderPaid_FoodItem> items;
    private double totalOrderBill;

    OrderPaid() {
    }

    public OrderPaid(ArrayList<orderPaid_FoodItem> items, double totalOrderPrice) {
        this.items = items;
        this.totalOrderBill = totalOrderPrice;
    }

    public ArrayList<orderPaid_FoodItem> getItems() {
        return items;
    }

    public double getTotalOrderBill() {
        return totalOrderBill;
    }
}