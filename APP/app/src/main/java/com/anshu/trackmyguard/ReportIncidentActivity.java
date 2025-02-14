package com.anshu.trackmyguard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

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
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class ReportIncidentActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private FirebaseFirestore firestore;
    private String guardId = "Guard1";

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
        // Initialize UI elements
        firestore = FirebaseFirestore.getInstance();

        // Initialize OpenStreetMap
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        mapView = findViewById(R.id.mapIncidentLocation);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18.0);

        // Initialize location tracking
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationUpdates();
    }
//    private void submitIncidentReport() {
//        EditText editTextTitle = findViewById(R.id.editIncidentDescription);
//        String title = editTextTitle.getText().toString().trim();
//        String description = editTextDescription.getText().toString().trim();
//        String location = editTextLocation.getText().toString().trim();
//
//        if (TextUtils.isEmpty(title)) {
//            editTextTitle.setError("Title is required");
//            return;
//        }
//        if (TextUtils.isEmpty(description)) {
//            editTextDescription.setError("Description is required");
//            return;
//        }
//        if (TextUtils.isEmpty(location)) {
//            editTextLocation.setError("Location is required");
//            return;
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Get user ID
//        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "Unknown";
//
//        // Create Incident Report Data
//        Map<String, Object> report = new HashMap<>();
//        report.put("title", title);
//        report.put("description", description);
//        report.put("location", location);
//        report.put("timestamp", Timestamp.now());
//        report.put("reportedBy", userId);
//
//        // Save to Firestore
//        firestore.collection("incident_reports")
//                .add(report)
//                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        progressBar.setVisibility(View.GONE);
//                        if (task.isSuccessful()) {
//                            Toast.makeText(IncidentReportActivity.this, "Report Submitted!", Toast.LENGTH_SHORT).show();
//                            editTextTitle.setText("");
//                            editTextDescription.setText("");
//                            editTextLocation.setText("");
//                        } else {
//                            Toast.makeText(IncidentReportActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
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
                    updateGuardLocation(location.getLatitude(), location.getLongitude());
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void updateGuardLocation(double latitude, double longitude) {
        Log.d("GuardTracking", "Updated Location: " + latitude + ", " + longitude);

        // Update Firestore
        firestore.collection("Users").document(guardId)
                .set(new GuardLocation(latitude, longitude));

        // Update UI
        updateMap(latitude, longitude);
    }

    private void updateMap(double latitude, double longitude) {
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