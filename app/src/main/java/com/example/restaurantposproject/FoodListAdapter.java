package com.example.restaurantposproject;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.annotations.Nullable;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    private final List<FoodItem> foodItems;
    private String tableNumber;

    public FoodListAdapter(List<FoodItem> foodItems, String tableNumber) {
        this.foodItems = foodItems;
        this.tableNumber = tableNumber;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);

        holder.itemName.setText(foodItem.getName());
        holder.itemDescription.setText(foodItem.getDescription());
        holder.itemPrice.setText(String.valueOf(foodItem.getPrice()));

        Glide.with(holder.itemView).load(foodItem.getImage_url()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e("TAG", "Load failed", e);
                // Logs the individual causes:
                for (Throwable t : e.getRootCauses()) {
                    Log.e("TAG", "Caused by", t);
                }
                // Logs the root causes
                e.logRootCauses("TAG");
                return false; // Allow calling onLoadFailed on the Target.
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false; // Allow calling onResourceReady on the Target.
            }
        }).into(holder.itemImage);
        holder.addtoorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the item is already added
                if (OrderManager.getInstance().isItemAdded(foodItem,tableNumber)) {
                    // Show a message to the user that the item is already added
                    Toast.makeText(holder.itemView.getContext(), "Item is already added", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the item to the order
                    OrderManager.getInstance().addItem(foodItem,tableNumber);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemDescription, itemPrice;

        AppCompatButton addtoorder;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemPrice = itemView.findViewById(R.id.item_price);
            addtoorder = itemView.findViewById(R.id.add_to_order_button);

        }
    }
}
