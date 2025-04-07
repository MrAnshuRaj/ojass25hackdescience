package com.anshu.trackmyguard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DutyAssignmentActivity extends AppCompatActivity {
    private Spinner guardSpinner;
    private TextView selectedLocationText;
    private EditText radiusEditText;
    private EditText shiftEditText;
    private Button pickLocationBtn, assignBtn;

    private FirebaseFirestore db;
    private List<Guard> guardList = new ArrayList<>();
    private ArrayAdapter<String> guardNameAdapter;
    private String selectedGuardId;
    private double selectedLat = 0.0, selectedLng = 0.0;
    private String adminOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_duty_assignment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();

        guardSpinner = findViewById(R.id.spinnerGuards);
        selectedLocationText = findViewById(R.id.tvSelectLocation);
        radiusEditText = findViewById(R.id.editRadius);
        shiftEditText = findViewById(R.id.editShiftTime);
        pickLocationBtn = findViewById(R.id.btnPickLocation);
        assignBtn = findViewById(R.id.btnAssign);
        db.collection("admins")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        adminOrg = documentSnapshot.getString("organization");
                        if (adminOrg != null) {
                            loadGuards();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch admin organization", Toast.LENGTH_SHORT).show();
                });

        pickLocationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, PickLocationActivity.class);
            startActivityForResult(intent, 101);
        });

        assignBtn.setOnClickListener(v -> assignDuty());
    }

    private void loadGuards() {
        db.collection("Users").whereEqualTo("role", "guard")
                .whereEqualTo("isVerified", true)
                .whereEqualTo("organization",adminOrg)
                .get()
                .addOnSuccessListener(query -> {
                    List<String> guardNames = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        Guard guard = doc.toObject(Guard.class);
                        guard.setId(doc.getId());
                        guardList.add(guard);
                        guardNames.add(guard.getUserName());
                    }

                    guardNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, guardNames);
                    guardNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    guardSpinner.setAdapter(guardNameAdapter);

                    guardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedGuardId = guardList.get(position).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {}
                    });
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            selectedLat = data.getDoubleExtra("latitude", 0.0);
            selectedLng = data.getDoubleExtra("longitude", 0.0);
            selectedLocationText.setText("Lat: " + selectedLat + ", Lng: " + selectedLng);
        }
    }

    private void assignDuty() {
        if (selectedGuardId == null || selectedLat == 0.0 || selectedLng == 0.0 ||
                TextUtils.isEmpty(radiusEditText.getText()) ||
                TextUtils.isEmpty(shiftEditText.getText()))
        {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;

        }

        double radius = Double.parseDouble(radiusEditText.getText().toString());
        String shiftStart = shiftEditText.getText().toString();


        Map<String, Object> assignment = new HashMap<>();
        assignment.put("lat", selectedLat);
        assignment.put("lng", selectedLng);
        assignment.put("radius", radius);
        assignment.put("shiftTiming", shiftStart);

        db.collection("GuardAssignments").document(selectedGuardId)
                .set(assignment)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Guard assigned successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}