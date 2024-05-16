
package com.example.restaurantposproject;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantposproject.R;
import com.example.restaurantposproject.SalesData;
import com.example.restaurantposproject.SalesDataAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class showSales extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private List<SalesData> salesDataList = new ArrayList<>();
    private RecyclerView salesRecyclerView;
    private SalesDataAdapter salesDataAdapter;
    private TextView totalSalesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_sales);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        salesRecyclerView = findViewById(R.id.sales_recycler_view);
        totalSalesTextView = findViewById(R.id.total_sales_text_view);
        salesDataAdapter = new SalesDataAdapter(salesDataList, this);
        salesRecyclerView.setAdapter(salesDataAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("paid_orders");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the list at the start of the method
                salesDataList.clear();

                double totalSales = 0;

                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    Map<String, Integer> mergedItemQuantities = new HashMap<>();
                    double mergedTotal = 0;

                    for (DataSnapshot orderSnapshot : dateSnapshot.getChildren()) {
                        String totalString = orderSnapshot.child("total").getValue(String.class);
                        Double total = totalString != null ? Double.parseDouble(totalString) : 0.0;
                        totalSales += total;
                        mergedTotal += total;

                        for (DataSnapshot itemSnapshot : orderSnapshot.child("items").getChildren()) {
                            String itemName = itemSnapshot.getKey();
                            Integer quantity = itemSnapshot.getValue(Integer.class);
                            if (quantity == null) {
                                quantity = 0; // Default value if quantity is null
                            }

                            // Merge the quantities of the same item
                            mergedItemQuantities.put(itemName, mergedItemQuantities.getOrDefault(itemName, 0) + quantity);
                        }
                    }

                    SalesData salesData = new SalesData(date, mergedItemQuantities, mergedTotal);
                    salesDataList.add(salesData);
                }
                // Notify the adapter to refresh the RecyclerView after the data set has been changed
                salesDataAdapter.notifyDataSetChanged();

                // Update the total sales TextView
                totalSalesTextView.setText(String.format("Total Sales: %.2f", totalSales));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}