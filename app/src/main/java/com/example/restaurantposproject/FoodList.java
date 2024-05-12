package com.example.restaurantposproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {
    ImageButton back_button;
    TextView category_name;
    RecyclerView food_list;

AppCompatButton orderDetails;

    private DatabaseReference mDatabase;
    private List<FoodItem> foodItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back_button = findViewById(R.id.imBut_backToCategories);
        category_name = findViewById(R.id.tv_categoryFoodList);
        food_list = findViewById(R.id.rv_foodList);
        orderDetails = findViewById(R.id.orderDetails_button);

        // Get the category name from the intent
        String categoryName = getIntent().getStringExtra("categoryName");
        category_name.setText(categoryName);

// Initialize Firebase Database

        mDatabase = FirebaseDatabase.getInstance().getReference().child("food_list").child(categoryName);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        // Attach a ValueEventListener to the food items DatabaseReference
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the list
                foodItems.clear();
                Log.d("Firebase", "Data retrieved: " + foodItems);

                // Loop through the snapshot and add the food items to the list
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    FoodItem foodItem = postSnapshot.getValue(FoodItem.class);
                    foodItems.add(foodItem);
                }

                // Create a new FoodListAdapter and set it to the RecyclerView
                food_list.setLayoutManager(new LinearLayoutManager(FoodList.this));
                FoodListAdapter adapter = new FoodListAdapter(foodItems);
                food_list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Firebase", "Error retrieving data: ", databaseError.toException());            }
        });
        orderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodList.this, Order.class);
                startActivity(intent);


            }
        });
    }
}