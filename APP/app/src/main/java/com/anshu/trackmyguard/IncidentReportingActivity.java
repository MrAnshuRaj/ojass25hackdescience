package com.anshu.trackmyguard;

import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IncidentReportingActivity extends AppCompatActivity {
    private MapView mapView;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_incident_reporting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mapView = findViewById(R.id.mapView);
        mapView.getController().setZoom(15.0);
        mapView.setMultiTouchControls(true);

        firestore = FirebaseFirestore.getInstance();
        fetchGuardsLocation();
    }
    private void fetchGuardsLocation() {
        firestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("AdminMap", "Error fetching guard locations: " + e.getMessage());
                    return;
                }

                mapView.getOverlays().clear();
                for (QueryDocumentSnapshot doc : snapshot) {
                    GuardLocation guard = doc.toObject(GuardLocation.class);
                    addGuardMarker(guard.latitude, guard.longitude);
                }
            }
        });
    }

    private void addGuardMarker(double latitude, double longitude) {
        GeoPoint location = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(mapView);
        marker.setPosition(location);
        marker.setTitle("Guard Location");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }
}