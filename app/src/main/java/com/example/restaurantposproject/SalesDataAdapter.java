package com.example.restaurantposproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SalesDataAdapter extends RecyclerView.Adapter<SalesDataAdapter.SalesDataViewHolder> {

    private List<SalesData> salesDataList;
    private Context context;

    public SalesDataAdapter(List<SalesData> salesDataList, Context context) {
        this.salesDataList = salesDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public SalesDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_data_item, parent, false);
        return new SalesDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesDataViewHolder holder, int position) {
        SalesData salesData = salesDataList.get(position);
        holder.dateTextView.setText(salesData.getDate());
        holder.totalTextView.setText(String.valueOf(salesData.getTotal()));

        // Set an OnClickListener for the item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SalesDetailActivity.class);
            intent.putExtra("salesData", salesData);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return salesDataList.size();
    }

    public static class SalesDataViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView totalTextView;

        public SalesDataViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.date_text_view);
            totalTextView = itemView.findViewById(R.id.total_text_view);
        }
    }
}