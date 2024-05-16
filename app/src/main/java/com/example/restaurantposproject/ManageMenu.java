package com.example.restaurantposproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ManageMenu extends AppCompatActivity {
    ImageButton back;
    AppCompatButton add, delete, modify, selectImage;

    EditText itemName, price, description;

    Spinner category;
    private Uri mImageUri;

    private ActivityResultLauncher<String> mGetContent;
    private StorageReference mStorageRef;
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
            uploadFile();
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

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        mImageUri = uri;
                    }
                });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Wait a while until getDownloadUrl() is completed
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {}, 500);

                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Get the download URL and store it in Firebase Database
                            String downloadUrl = uri.toString();
                            String item = itemName.getText().toString();
                            String itemPrice = price.getText().toString();
                            String itemDescription = description.getText().toString();
                            String itemCategory = category.getSelectedItem().toString();
                            DatabaseReference myRef = database.getReference("food_list").child(itemCategory);
                            double price = Double.parseDouble(itemPrice);
                            FoodItem foodItem = new FoodItem(item, itemDescription, price, downloadUrl);
                            myRef.child(item).setValue(foodItem);
                            Toast.makeText(ManageMenu.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle unsuccessful uploads
                        Toast.makeText(ManageMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}





