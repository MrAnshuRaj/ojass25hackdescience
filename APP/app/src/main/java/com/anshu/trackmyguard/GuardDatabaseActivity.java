package com.anshu.trackmyguard;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GuardDatabaseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GuardAdapter adapter;
    private List<Guard> guardList;
    private FirebaseFirestore db;
    private String adminOrg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guard_database);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewGuards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        guardList = new ArrayList<>();
        adapter = new GuardAdapter(this, guardList);
        recyclerView.setAdapter(adapter);
        db.collection("admins")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        adminOrg = documentSnapshot.getString("organization");
                        if (adminOrg != null) {
                            fetchGuardsFromFirestore();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch admin organization", Toast.LENGTH_SHORT).show();
                });

    }

    private void fetchGuardsFromFirestore() {
        db.collection("Users")
                .whereEqualTo("role", "guard")
                .whereEqualTo("organization", adminOrg)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        Log.e("Firestore Error", Objects.requireNonNull(error.getMessage()));
                        return;
                    }

                    guardList.clear();
                    for (DocumentSnapshot doc : snapshot) {
                        try {
                            String name = doc.getString("username");
                            String phone = doc.getString("phone");
                            String organization = doc.getString("organization");
                            boolean isVerified = Boolean.TRUE.equals(doc.getBoolean("isVerified"));
                            int age = doc.contains("age") ? doc.getLong("age").intValue() : 0; // Safe conversion

                            guardList.add(new Guard(doc.getId(), name, age, phone, organization,isVerified));
                        } catch (Exception e) {
                            Log.e("Data Error", "Error parsing document: " + e.getMessage());
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
