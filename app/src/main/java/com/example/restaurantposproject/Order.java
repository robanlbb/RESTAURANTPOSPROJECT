package com.example.restaurantposproject;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Order extends AppCompatActivity {

    AppCompatButton orderDetails;
    RecyclerView orderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        orderDetails = findViewById(R.id.order_paid_button); // Corrected button id
        orderList = findViewById(R.id.order_recycler_view);

        ArrayList<FoodItem> orderData = OrderManager.getInstance().getOrderData();
        OrderListAdapter adapter = new OrderListAdapter(orderData);
        orderList.setAdapter(adapter);
        orderList.setLayoutManager(new LinearLayoutManager(this));


        orderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Removed the incorrect public class declaration
            }
        });
    }
}