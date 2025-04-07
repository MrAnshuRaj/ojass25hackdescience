package com.anshu.trackmyguard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
import java.util.Map;

public class ReportIncidentActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private EditText editTextTitle, editTextDescription;
    private Button buttonSubmitReport;
    private ProgressBar progressBar;

    private String guardId = "Guard1"; // Placeholder, replace with actual guard ID
    private double currentLatitude, currentLongitude; // Store last known location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_incident);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editIncidentDescription);
        buttonSubmitReport = findViewById(R.id.btnSubmitIncident);
        progressBar = findViewById(R.id.progressBarSubmitReport);

        // Initialize OpenStreetMap
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        mapView = findViewById(R.id.mapIncidentLocation);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18.0);

        // Initialize location tracking
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationUpdates();

        // Submit Report Button Click
        buttonSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitIncidentReport();
            }
        });
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000) // Update every 5 seconds
                .setFastestInterval(2000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    updateGuardLocation(currentLatitude, currentLongitude);
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void updateGuardLocation(double latitude, double longitude) {
        Log.d("GuardTracking", "Updated Location: " + latitude + ", " + longitude);

        // Update Firestore
        Map<String, Object> locationData = new HashMap<>();
        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);
        firestore.collection("Users").document(guardId)
                .set(locationData);

        // Update UI
        updateMap(latitude, longitude);
    }

    private void updateMap(double latitude, double longitude) {
        if (mapView != null && mapView.getRepository() != null) {
            GeoPoint guardLocation = new GeoPoint(latitude, longitude); // Corrected class name
            mapView.getController().setCenter(guardLocation);
            mapView.getController().setZoom(18.0);

            // Remove existing markers
            mapView.getOverlays().clear();

            Marker marker = new Marker(mapView);
            marker.setPosition(guardLocation);
            marker.setTitle("Guard Location");
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mapView.getOverlays().add(marker);
            mapView.invalidate(); // Refresh map
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView = null; // clear mapView reference
    }
    private void SubmitIncidentReport() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            editTextTitle.setError("Title is required");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            editTextDescription.setError("Description is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Get user ID
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : guardId;

        // Create Incident Report Data
        Map<String, Object> report = new HashMap<>();
        report.put("title", title);
        report.put("description", description);
        report.put("latitude", currentLatitude);
        report.put("longitude", currentLongitude);
        report.put("reportedBy", userId);
        report.put("timestamp", FieldValue.serverTimestamp());
        // Save to Firestore
        firestore.collection("incident_reports")
                .add(report)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(ReportIncidentActivity.this, "Report Submitted!", Toast.LENGTH_SHORT).show();
                        editTextTitle.setText("");
                        editTextDescription.setText("");
                    } else {
                        Toast.makeText(ReportIncidentActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            }
        }
    }
}
