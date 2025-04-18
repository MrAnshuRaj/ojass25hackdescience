package com.anshu.trackmyguard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameField, emailField, passwordField,ageField,phoneField;
    private Spinner organizationField;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    private Button registerButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String selectedOrganization;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("TrackMyGuard", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        usernameField = findViewById(R.id.user_name);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        registerButton = findViewById(R.id.btnRegister);
        organizationField=findViewById(R.id.spinnerOrganization);
        List<String> organizationList = Arrays.asList("Select Organization", "NIT Jamshedpur");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                organizationList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organizationField.setAdapter(adapter);

        organizationField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrganization = parent.getItemAtPosition(position).toString();

                if (!selectedOrganization.equals("Select Organization")) {
                    Toast.makeText(getApplicationContext(), "Selected: " + selectedOrganization, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedOrganization = null;
            }
        });
        ageField=findViewById(R.id.tvGuardAge);
        phoneField=findViewById(R.id.tvPhoneNumber);

        registerButton.setOnClickListener(v -> registerUser());
    }
    private void registerUser() {
        String username = usernameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String organization = selectedOrganization;
        String password = passwordField.getText().toString().trim();
        int age = Integer.parseInt(ageField.getText().toString().trim());
        String phone = phoneField.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user.getUid(), username, email,organization,age,phone);
                            SharedPreferences sharedPreferences = getSharedPreferences("TrackMyGuard", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username",username);
                            editor.putString("phone",phone);
                            editor.apply();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String userId, String username, String email, String organization,int age,String phone) {
        DocumentReference userRef = db.collection("Users").document(userId);
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("organization", organization);
        user.put("role", "guard");
        user.put("age",age);
        user.put("phone",phone);
        user.put("isVerified", false);
        // Default role (can be changed later)

        userRef.set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                    editor.putString("userType","Guard");
                    editor.apply();
                    startActivity(new Intent(RegisterActivity.this, ActivityVerificationPending.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void openLoginActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

}