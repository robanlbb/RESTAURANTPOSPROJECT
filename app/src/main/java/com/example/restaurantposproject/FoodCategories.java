package com.example.restaurantposproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.ArrayList;
import java.util.List;

public class FoodCategories extends AppCompatActivity {
EditText search_text;
ImageButton search_button,back_button;

ListView categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_foodcategories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        search_text = findViewById(R.id.et_search);
        search_button = findViewById(R.id.btn_search);
        back_button = findViewById(R.id.imBut_backToTables);
        categories = findViewById(R.id.lv_categories);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Create some sample food categories with specific image resource IDs
        List<FoodCategory> foodCategories = new ArrayList<>();
        foodCategories.add(new FoodCategory("meat", R.drawable.btn_4));
        foodCategories.add(new FoodCategory("pizza", R.drawable.btn_1));
        foodCategories.add(new FoodCategory("chicken", R.drawable.btn_3));
        foodCategories.add(new FoodCategory("drink", R.drawable.btn_7));
        foodCategories.add(new FoodCategory("salad", R.drawable.btn_8));

        // Create ArrayAdapter to display food categories in ListView
        ArrayAdapter<FoodCategory> adapter = new ArrayAdapter<FoodCategory>(this, 0, foodCategories) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_food_category, parent, false);
                }

                // Get the data item for this position
                FoodCategory foodCategory = getItem(position);

                // Lookup views for data population
                ImageView imageView = convertView.findViewById(R.id.image_food_category);
                TextView textView = convertView.findViewById(R.id.text_food_category_name);

                // Populate the data into the template view using the data object
                imageView.setImageResource(foodCategory.getImageResourceId()); // Use setImageResource for loading images
                textView.setText(foodCategory.getName());

                return convertView;
            }
        };

        // Attach the adapter to a ListView

        categories.setAdapter(adapter);

        // Set item click listener
        categories.setOnItemClickListener((parent, view, position, id) -> {
            // Get the clicked food category
            FoodCategory clickedCategory = adapter.getItem(position);

            // Perform action based on the clicked category
            Intent intent = new Intent(FoodCategories.this, FoodList.class);
            intent.putExtra("categoryName", clickedCategory.getName());
            intent.getStringExtra("tableNumber");
            startActivity(intent);
        });
    }

    // Define FoodCategory class here
    public class FoodCategory {
        private String name;
        private int imageResourceId; // Resource ID for the image

        public FoodCategory(String name, int imageResourceId) {
            this.name = name;
            this.imageResourceId = imageResourceId;
        }

        public String getName() {
            return name;
        }

        public int getImageResourceId() {
            return imageResourceId;
        }
    }
}

