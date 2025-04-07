package com.anshu.trackmyguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    FirebaseFirestore db;
    private Button loginButton;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("TrackMyGuard", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        //admin Register
        TextView registerAdmin = findViewById(R.id.registerAdmin);
        registerAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AdminRegisterActivity.class));
                //finish();
            }
        });
        //admin Login
        TextView loginAdmin = findViewById(R.id.adminLogin);
        loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminLoginActivity.class));
                //finish();

            }
        });

    }
    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        editor.putString("userType","Guard");
                        editor.apply();
                        FirebaseUser user = auth.getCurrentUser();
                        db.collection("Users").document(user.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                            String name  = documentSnapshot.getString("username");
                            String phone  = documentSnapshot.getString("phone");
                            editor.putString("username",name);
                            editor.putString("phone",phone);
                            editor.apply();
                        });
                        startActivity(new Intent(MainActivity.this, GuardDashboard.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void openRegisterActivity(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}