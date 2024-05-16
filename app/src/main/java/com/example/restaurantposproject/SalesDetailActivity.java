package com.example.restaurantposproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

public class SalesDetailActivity extends AppCompatActivity {

    private RecyclerView salesDetailRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_detail);

        salesDetailRecyclerView = findViewById(R.id.sales_detail_recycler_view);

        // Get the SalesData from the intent
        SalesData salesData = (SalesData) getIntent().getSerializableExtra("salesData");

        // Set the RecyclerView's layout manager
        salesDetailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the RecyclerView's adapter
        salesDetailRecyclerView.setAdapter(new SalesDetailAdapter(salesData));
    }

    private class SalesDetailAdapter extends RecyclerView.Adapter<SalesDetailAdapter.SalesDetailViewHolder> {

        private SalesData salesData;

        SalesDetailAdapter(SalesData salesData) {
            this.salesData = salesData;
        }

        @Override
        public SalesDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_detail_item, parent, false);
            return new SalesDetailViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SalesDetailViewHolder holder, int position) {
            String itemName = (String) salesData.getItemQuantities().keySet().toArray()[position];
            Integer quantity = (Integer) salesData.getItemQuantities().values().toArray()[position];

            holder.itemNameTextView.setText(itemName);
            holder.quantityTextView.setText(String.valueOf(quantity));
        }

        @Override
        public int getItemCount() {
            return salesData.getItemQuantities().size();
        }

        class SalesDetailViewHolder extends RecyclerView.ViewHolder {

            TextView itemNameTextView;
            TextView quantityTextView;

            SalesDetailViewHolder(View itemView) {
                super(itemView);
                itemNameTextView = itemView.findViewById(R.id.item_name_text_view);
                quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            }
        }
    }
}