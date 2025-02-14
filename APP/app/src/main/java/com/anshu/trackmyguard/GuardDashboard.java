package com.anshu.trackmyguard;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class GuardDashboard extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    private Button startDutyBtn, stopDutyBtn, reportIncidentBtn;
    private TextView statusText;
    private boolean isOnDuty = false;

    private FirebaseFirestore db;
    private FirebaseUser user;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guard_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI Elements
        startDutyBtn = findViewById(R.id.startDutyBtn);
        stopDutyBtn = findViewById(R.id.stopDutyBtn);
        statusText = findViewById(R.id.statusText);
        reportIncidentBtn = findViewById(R.id.reportIncident);
        reportIncidentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuardDashboard.this,ReportIncidentActivity.class));
            }
        });

        // Initialize Location Services
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setupLocationUpdates();

        // Start Duty Button Click
        startDutyBtn.setOnClickListener(v -> startDuty());

        // Stop Duty Button Click
        stopDutyBtn.setOnClickListener(v -> stopDuty());
    }
    private void setupLocationUpdates() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || !isOnDuty) return;

                Location location = locationResult.getLastLocation();
                updateGuardLocation(location.getLatitude(), location.getLongitude());
            }
        };
    }

    private void startDuty() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        isOnDuty = true;
        statusText.setText("Status: On-Duty");
        startDutyBtn.setVisibility(View.GONE);
        stopDutyBtn.setVisibility(View.VISIBLE);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        Toast.makeText(this, "Duty Started. Location is being shared.", Toast.LENGTH_SHORT).show();
    }

    private void stopDuty() {
        isOnDuty = false;
        statusText.setText("Status: Off-Duty");
        stopDutyBtn.setVisibility(View.GONE);
        startDutyBtn.setVisibility(View.VISIBLE);

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        removeGuardLocation();
        Toast.makeText(this, "Duty Ended. Location sharing stopped.", Toast.LENGTH_SHORT).show();
    }

    private void updateGuardLocation(double lat, double lng) {
        DocumentReference guardRef = db.collection("Users").document(user.getUid());
        guardRef.update("latitude", lat, "longitude", lng, "onDuty", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(GuardDashboard.this,"Lat "+lat+"long "+lng,Toast.LENGTH_SHORT).show();
                        Log.d("Guard Location", "Location updated");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore Error", e.getMessage()));
    }

    private void removeGuardLocation() {
        DocumentReference guardRef = db.collection("guards").document(user.getUid());
        guardRef.update("onDuty", false)
                .addOnSuccessListener(aVoid -> Log.d("Guard Location", "Guard off-duty"))
                .addOnFailureListener(e -> Log.e("Firestore Error", e.getMessage()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDuty();
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}