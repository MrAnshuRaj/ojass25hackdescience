package com.anshu.trackmyguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailField = findViewById(R.id.adminEmail);
        passwordField = findViewById(R.id.adminPassword);
        loginButton = findViewById(R.id.btnAdminLogin);

        loginButton.setOnClickListener(v -> loginAdmin());
    }
    private void loginAdmin() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            checkAdminRole(user.getUid());
                        }
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkAdminRole(String userId) {
        DocumentReference userRef = db.collection("Users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String role = documentSnapshot.getString("role");
                if ("admin".equals(role)) {
                    Toast.makeText(AdminLoginActivity.this, "Admin Login Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Access Denied! Not an Admin.", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(AdminLoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void resetPassword(View view) {
        String email = emailField.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter your email to reset password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Password reset email sent!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}