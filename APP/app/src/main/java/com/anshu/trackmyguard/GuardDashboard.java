package com.anshu.trackmyguard;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.HashMap;

public class GuardDashboard extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    private static final double GEOFENCE_RADIUS = 200.0; // 200 meters
    private static final int LOCATION_UPDATE_INTERVAL = 2 * 60 * 1000;
    private double dutyLatitude = 29.7770167;  // Replace with actual duty location latitude
    private double dutyLongitude = 88.144115;
    private boolean isOutsideZone = false;
    private Handler handler = new Handler();
    private Button startDutyBtn, stopDutyBtn, reportIncidentBtn;
    private TextView statusText;
    private boolean isOnDuty = false;
    MapView mapView;
    IMapController mapController;
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
        createNotificationChannel();
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
        mapView=findViewById(R.id.mapViewGuard);
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(15.0);
    }
    private void setupLocationUpdates() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000)
                .setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null || !isOnDuty) return;

                Location location = locationResult.getLastLocation();
                updateGuardLocation(location.getLatitude(), location.getLongitude());
                updateMap(location.getLatitude(), location.getLongitude());
                checkGeofence(location.getLatitude(), location.getLongitude(),dutyLatitude,dutyLongitude);
            }
        };
    }
    private void updateMap(double latitude, double longitude) {
        runOnUiThread(() -> {
            mapView.getOverlays().clear();

            GeoPoint geoPoint = new GeoPoint(latitude, longitude);
            Marker marker = new Marker(mapView);
            marker.setPosition(geoPoint);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Your Location");

            mapView.getOverlays().add(marker);
            mapView.getController().setCenter(geoPoint);
            mapView.invalidate();
        });
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
    private void checkGeofence(double latitude, double longitude, double assignedLat, double assignedLon) {
        String guardId = user.getUid();
        float[] results = new float[1];
        Location.distanceBetween(latitude, longitude, assignedLat, assignedLon, results);
        float distanceInMeters = results[0];
        Toast.makeText(this,"Distance: "+distanceInMeters+" meters",Toast.LENGTH_SHORT).show();
        if (distanceInMeters > 200) {
            Toast.makeText(this,"Outside Zone",Toast.LENGTH_SHORT).show();
            sendGuardNotification("⚠️ You are outside your duty area!");
            GuardAttendance attendance = new GuardAttendance(guardId,latitude,longitude,true);
            db.collection("GuardAttendance").document(guardId)
                    .set(attendance);

            // Start countdown for supervisor alert
            new Handler().postDelayed(() -> checkIfStillOutOfZone(guardId), 300000); // 5 min
        }
    }

    private void checkIfStillOutOfZone(String guardId) {
        db.collection("GuardAttendance").document(guardId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists() && doc.getBoolean("outOfZone")) {
                        notifySupervisor("🚨 Guard " + guardId + " is still out of duty area!");
                    }
                });
    }
    private void notifySupervisor(String message) {
        db.collection("SupervisorAlerts").add(new HashMap<String, Object>() {{
            put("message", message);
            put("timestamp", System.currentTimeMillis());
        }});
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "geofence_alert",
                    "Geofence Alerts",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void sendGuardNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "GeofenceAlerts")
                .setSmallIcon(R.drawable.baseline_add_alert_24)
                .setContentTitle("Geofence Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

}