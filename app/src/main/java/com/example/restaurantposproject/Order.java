package com.example.restaurantposproject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order extends AppCompatActivity implements OrderListAdapter.OnQuantityChangeListener{

    AppCompatButton orderPaid;
    RecyclerView orderList;
    TextView orderTotal;

    ImageView back;
    String tableNumber;

    @Override
    public void onQuantityChange() {
        double total = 0;
        for (FoodItem item : OrderManager.getInstance().getOrderData(tableNumber)) {
            total += item.getPrice() * item.getQuantity();
        }
        orderTotal.setText(String.format("%.2f", total));
    }

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

        orderPaid = findViewById(R.id.order_paid_button);
        orderList = findViewById(R.id.order_recycler_view);
        orderTotal = findViewById(R.id.tv_total);
        back = findViewById(R.id.imBut_backToCategories);
        tableNumber = getIntent().getStringExtra("tableNumber");


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("orders").child(tableNumber);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<FoodItem> orderData = new ArrayList<>();
                double total = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals("total")) {
                        total = postSnapshot.getValue(Double.class);
                    } else if (postSnapshot.getKey().equals("items")) {
                        for (DataSnapshot itemSnapshot : postSnapshot.getChildren()) {
                            FoodItem item = itemSnapshot.getValue(FoodItem.class);
                            orderData.add(item);
                        }
                    }
                }

                // Now you can use orderData to display the order
                OrderListAdapter adapter = new OrderListAdapter(orderData, Order.this);
                orderList.setAdapter(adapter);
                orderList.setLayoutManager(new LinearLayoutManager(Order.this));

                // Set the total
                orderTotal.setText(String.format("%.2f", total));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

    orderPaid.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("orders").child(tableNumber);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Integer> items = new HashMap<>();
                double totalOrderBill = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals("items")) {
                        for (DataSnapshot itemSnapshot : postSnapshot.getChildren()) {
                            FoodItem item = itemSnapshot.getValue(FoodItem.class);
                            items.put(item.getName(), item.getQuantity());
                            totalOrderBill += item.getPrice() * item.getQuantity();
                        }
                    }
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String orderDate = sdf.format(new Date()); // Use current date as order date

                // Format totalOrderBill to a string with two decimal places
                String formattedTotalOrderBill = String.format("%.2f", totalOrderBill);

                // Remove the order from the "orders" table
                mDatabase.removeValue();

                // Clear the current order data for the specific table number from the OrderManager instance
                OrderManager.getInstance().clearOrderData(tableNumber);

                // Get a reference to the "orderNumber" node in Firebase
                DatabaseReference orderNumberRef = FirebaseDatabase.getInstance().getReference().child("orderNumber");

                // Use a transaction to safely increment the order number
                orderNumberRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Integer currentValue = mutableData.getValue(Integer.class);
                        if (currentValue == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue(currentValue + 1);
                        }

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                        if (committed) {
                            // Use the incremented order number
                            String orderNumber = "Order_" + dataSnapshot.getValue(Integer.class);

                            // Add the order to the "paid_orders" table
                            DatabaseReference paidOrdersDatabase = FirebaseDatabase.getInstance().getReference().child("paid_orders").child(orderDate).child(orderNumber);
                            paidOrdersDatabase.child("items").setValue(items);
                            paidOrdersDatabase.child("total").setValue(formattedTotalOrderBill);

                            orderList.setBackgroundColor(Color.WHITE);

                            Toast.makeText(Order.this, "Transaction completed", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // Handle error

                        }

                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

    }
});


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}