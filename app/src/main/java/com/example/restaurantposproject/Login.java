package com.example.restaurantposproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
EditText userName,password;
AppCompatButton login;

FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Login.this,user.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        userName = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString();
                String pass = password.getText().toString();
                if(user.equals("admin") && pass.equals("admin")){
                    Intent intent = new Intent(Login.this,admin.class);
                    startActivity(intent);
                } else if (user.isEmpty() || pass.isEmpty()) {
                    userName.setError("Please enter username");
                    password.setError("Please enter password");
                } else if (user.equals("admin") && pass.equals("admin")){
                    Intent intent = new Intent(Login.this,admin.class);
                    startActivity(intent);
                } else {
                    mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(Login.this,user.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                userName.setText("");
                password.setText("");
            }
        });
    }
}