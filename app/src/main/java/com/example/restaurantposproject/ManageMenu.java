package com.example.restaurantposproject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManageMenu extends AppCompatActivity {
ImageButton back;
AppCompatButton add,delete,modify,selectImage;

EditText itemName,price,description;

Spinner category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.imBut_backToAdmin);
        itemName = findViewById(R.id.et_newItemName);
        price = findViewById(R.id.et_newItemPrice);
        description = findViewById(R.id.et_itemDescription);
        modify = findViewById(R.id.btn_modifyItem);
        add = findViewById(R.id.btn_addItem);
        delete = findViewById(R.id.btn_deleteItem);
        selectImage = findViewById(R.id.btn_selectImage);
        category = findViewById(R.id.spinner_category);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = itemName.getText().toString();
                String itemPrice = price.getText().toString();
                String itemDescription = description.getText().toString();
                String itemCategory = category.getSelectedItem().toString();

                if(item.isEmpty() || itemPrice.isEmpty() || itemDescription.isEmpty() || selectImage == null || itemCategory.isEmpty()){
                    itemName.setError("Please enter item name");
                    price.setError("Please enter item price");
                    description.setError("Please enter item description");
                    Toast.makeText(ManageMenu.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManageMenu.this, "Please select a category", Toast.LENGTH_SHORT).show();
                } else {
                    // Add item to menu
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete item from menu
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Modify item in menu
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Select image for item
            }
        });



    }
}