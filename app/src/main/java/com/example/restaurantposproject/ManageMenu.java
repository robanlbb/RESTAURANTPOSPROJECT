package com.example.restaurantposproject;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ManageMenu extends AppCompatActivity {
    ImageButton back;
    AppCompatButton add, delete, modify, selectImage;

    EditText itemName, price, description;

    Spinner category;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

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

                if (item.isEmpty() || itemPrice.isEmpty() || itemDescription.isEmpty() || selectImage == null || itemCategory.isEmpty()) {
                    itemName.setError("Please enter item name");
                    price.setError("Please enter item price");
                    description.setError("Please enter item description");
                    Toast.makeText(ManageMenu.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManageMenu.this, "Please select a category", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference myRef = database.getReference("food_list").child(itemCategory);
                    double price = Double.parseDouble(itemPrice);
                    FoodItem foodItem = new FoodItem(item, itemDescription, price, "itemImage");
                    myRef.child(item).setValue(foodItem);
                    Toast.makeText(ManageMenu.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemCategory = category.getSelectedItem().toString();
                String item = itemName.getText().toString();


                DatabaseReference myRef = database.getReference("food_list").child(itemCategory);

                Query itemQuery = myRef.orderByChild("name").equalTo(item);

                itemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            itemSnapshot.getRef().removeValue();

                        }
                        if (dataSnapshot.getChildrenCount() == 0) {
                            Toast.makeText(ManageMenu.this, "Item not found", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ManageMenu.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemCategory = category.getSelectedItem().toString();
                String item = itemName.getText().toString();
                String itemPrice = price.getText().toString();
                String itemDescription = description.getText().toString();

                DatabaseReference myRef = database.getReference("food_list").child(itemCategory);

                Query itemQuery = myRef.orderByChild("name").equalTo(item);

                itemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                try {
                                    double price = Double.parseDouble(itemPrice);
                                    FoodItem foodItem = new FoodItem(item, itemDescription, price, "itemImage");
                                    itemSnapshot.getRef().setValue(foodItem);
                                    Toast.makeText(ManageMenu.this, "Item modified successfully", Toast.LENGTH_SHORT).show();
                                } catch (NumberFormatException e) {
                                    Toast.makeText(ManageMenu.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(ManageMenu.this, "Error modifying item", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(ManageMenu.this, "Item not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}