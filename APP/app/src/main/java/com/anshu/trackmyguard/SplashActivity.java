package com.anshu.trackmyguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("TrackMyGuard", MODE_PRIVATE);

        checkLoginAndRedirect();
    }

    private void checkLoginAndRedirect() {
        String userType = sharedPreferences.getString("userType", "Guard");

        if (auth.getCurrentUser() != null && userType.equalsIgnoreCase("guard")) {
            db.collection("Users")
                    .document(auth.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Boolean isVerified = documentSnapshot.getBoolean("isVerified");
                            if (isVerified != null && isVerified) {
                                startActivity(new Intent(this, GuardDashboard.class));
                            } else {
                                startActivity(new Intent(this, ActivityVerificationPending.class));
                            }
                            finish();
                        } else {
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    });

        } else if (auth.getCurrentUser() != null && userType.equalsIgnoreCase("Admin")) {
            startActivity(new Intent(SplashActivity.this, AdminDashboardActivity.class));
            finish();
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}