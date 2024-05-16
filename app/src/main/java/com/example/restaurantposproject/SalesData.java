package com.example.restaurantposproject;

import java.io.Serializable;
import java.util.Map;

public class SalesData implements Serializable {
    private String date;
    private Map<String, Integer> itemQuantities;
    private Double total;

    public SalesData(String date, Map<String, Integer> itemQuantities, Double total) {
        this.date = date;
        this.itemQuantities = itemQuantities;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Integer> getItemQuantities() {
        return itemQuantities;
    }

    public void setItemQuantities(Map<String, Integer> itemQuantities) {
        this.itemQuantities = itemQuantities;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}