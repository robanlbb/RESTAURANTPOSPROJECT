package com.example.restaurantposproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user extends AppCompatActivity {
    TextView loggedUser;
    GridView gridView;
    BaseAdapter tableAdapter;
    AppCompatButton logout;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loggedUser = findViewById(R.id.tv_loggedUser);
        logout = findViewById(R.id.btn_logout);
        gridView = findViewById(R.id.gridView);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");

        if (user == null) {
            Intent intent = new Intent(user.this, Login.class);
            startActivity(intent);
            finish();
        }

        final String[] tableNumbers = new String[]{"Table 1", "Table 2", "Table 3", "Table 4", "Table 5", "Table 6", "Table 7", "Table 8", "Table 9", "Table 10", "Table 11", "Table 12"};

        tableAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return tableNumbers.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }


            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    final LayoutInflater layoutInflater = LayoutInflater.from(user.this);
                    convertView = layoutInflater.inflate(R.layout.grid_item, null);
                }

                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageview);
                TextView nameTextView = (TextView) convertView.findViewById(R.id.textview);

                imageView.setImageResource(R.drawable.table);
                nameTextView.setText(tableNumbers[position]);

                // Add a listener to the specific table in the orders node
                View finalConvertView = convertView;
                ordersRef.child(tableNumbers[position]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // If there is an order for this table, set the background color to red
                            finalConvertView.setBackgroundColor(Color.RED);
                        } else {
                            // If there is no order for this table, set the background color to white
                            finalConvertView.setBackgroundColor(Color.WHITE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });

                return convertView;
            }
        };

        gridView.setAdapter(tableAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(user.this, FoodCategories.class);
                intent.putExtra("tableNumber", tableNumbers[position]); // Pass the table number to the new activity
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(user.this, Login.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
