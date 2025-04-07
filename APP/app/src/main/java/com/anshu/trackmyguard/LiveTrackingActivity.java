package com.anshu.trackmyguard;

import android.os.Bundle;
import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.Objects;
public class LiveTrackingActivity extends AppCompatActivity {
    private MapView mapView;
    private IMapController mapController;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_live_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();
        // Initialize OSM Map
        mapView = findViewById(R.id.mapView);
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(15.0);

        // Load Guard Locations
        fetchLiveGuardLocations();
    }
    private void fetchLiveGuardLocations() {
        db.collection("Users")
                .whereEqualTo("onDuty", true)  // Fetch only active guards
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Firestore Error", e.getMessage());
                            return;
                        }

                        if (snapshots != null) {
                            mapView.getOverlays().clear();  // Clear previous markers

                            for (DocumentSnapshot doc : snapshots) {
                                double latitude = Objects.requireNonNull(doc.getDouble("latitude"));
                                double longitude = Objects.requireNonNull(doc.getDouble("longitude"));
                                String guardName = doc.getString("username");
                                //Toast.makeText(LiveTrackingActivity.this, "Lat "+latitude+"long "+longitude, Toast.LENGTH_SHORT).show();
                                // Set the guard's location on map
                                GeoPoint guardLocation = new GeoPoint(latitude, longitude);
                                Marker guardMarker = new Marker(mapView);
                                guardMarker.setPosition(guardLocation);
                                guardMarker.setTitle(guardName);
                                guardMarker.setIcon(getResources().getDrawable(R.drawable.baseline_accessibility_24));

                                mapView.getOverlays().add(guardMarker);

                                // Move Camera to Latest Guard Location
                                mapController.setCenter(guardLocation);
                            }

                            mapView.invalidate(); // Refresh map
                        } else {
                            Toast.makeText(LiveTrackingActivity.this, "No active guards found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}