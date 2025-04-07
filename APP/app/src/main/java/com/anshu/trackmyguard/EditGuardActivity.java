package com.anshu.trackmyguard;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditGuardActivity extends AppCompatActivity {
    private EditText etName, etAge, etPhone, etOrganization;
    private Button btnUpdate;
    private FirebaseFirestore db;
    private String guardId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_guard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etPhone = findViewById(R.id.etPhone);
        etOrganization = findViewById(R.id.etOrganization);
        btnUpdate = findViewById(R.id.btnUpdate);

        db = FirebaseFirestore.getInstance();
        guardId = getIntent().getStringExtra("guardId");

        loadGuardData();

        btnUpdate.setOnClickListener(v -> updateGuardDetails());
    }

    private void loadGuardData() {
        DocumentReference docRef = db.collection("Users").document(guardId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                etName.setText(documentSnapshot.getString("username"));
                etAge.setText(String.valueOf(documentSnapshot.getLong("age")));
                etPhone.setText(documentSnapshot.getString("phone"));
                etOrganization.setText(documentSnapshot.getString("organization"));
            }
        });
    }

    private void updateGuardDetails() {
        db.collection("Users").document(guardId)
                .update(
                        "username", etName.getText().toString(),
                        "age", Integer.parseInt(etAge.getText().toString()),
                        "phone", etPhone.getText().toString(),
                        "organization", etOrganization.getText().toString()
                )
                .addOnSuccessListener(aVoid -> finish());
    }
}