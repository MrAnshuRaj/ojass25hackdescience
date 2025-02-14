package com.anshu.trackmyguard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminRegisterActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    private EditText etAdminName, etAdminEmail, etAdminPassword, etAdminConfirmPassword,organization;
    private Button btnAdminRegister;
    private TextView tvLoginRedirect;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("TrackMyGuard", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        // Initialize Views
        etAdminName = findViewById(R.id.etAdminName);
        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        etAdminConfirmPassword = findViewById(R.id.etAdminConfirmPassword);
        btnAdminRegister = findViewById(R.id.btnAdminRegister);
        tvLoginRedirect = findViewById(R.id.tvLoginRedirect);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        organization = findViewById(R.id.organizationName);

        // Register Button Click
        btnAdminRegister.setOnClickListener(view -> registerAdmin());

        // Redirect to Login
        tvLoginRedirect.setOnClickListener(view -> {
            startActivity(new Intent(AdminRegisterActivity.this, AdminLoginActivity.class));
            finish();
        });
    }
    private void registerAdmin() {
        String name = etAdminName.getText().toString().trim();
        String email = etAdminEmail.getText().toString().trim();
        String password = etAdminPassword.getText().toString().trim();
        String confirmPassword = etAdminConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        // Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Store Admin Data in Firestore
                            Map<String, Object> admin = new HashMap<>();
                            admin.put("name", name);
                            admin.put("email", email);
                            admin.put("organization", organization.getText().toString());
                            admin.put("role", "admin");

                            db.collection("admins").document(user.getUid())
                                    .set(admin)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Admin Registered!", Toast.LENGTH_SHORT).show();
                                        editor.putString("userType","Admin");
                                        editor.apply();
                                        startActivity(new Intent(this, AdminDashboardActivity.class));
                                        finish();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}