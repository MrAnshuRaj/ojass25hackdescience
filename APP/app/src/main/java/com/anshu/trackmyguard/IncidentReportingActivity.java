package com.anshu.trackmyguard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class IncidentReportingActivity extends AppCompatActivity {
    private MapView mapView;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private IncidentAdapter incidentAdapter;
    private List<Incident> incidentList;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private IMapController mapController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_reporting);

        // Initialize UI elements
        mapView = findViewById(R.id.mapView);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar2);
        emptyTextView = findViewById(R.id.emptyTV);

        // Firestore
        firestore = FirebaseFirestore.getInstance();

        // Configure RecyclerView
        incidentList = new ArrayList<>();
        incidentAdapter = new IncidentAdapter(incidentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(incidentAdapter);

        // Configure OpenStreetMap
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapController = mapView.getController();
        mapController.setZoom(15.0);

        // Fetch incident data
        fetchIncidentReports();
    }

    private void fetchIncidentReports() {
        progressBar.setVisibility(View.VISIBLE);

        firestore.collection("incident_reports").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("IncidentReports", "Error fetching incidents: " + e.getMessage());
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (snapshot == null || snapshot.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                incidentList.clear();
                mapView.getOverlays().clear(); // Remove old markers

                for (QueryDocumentSnapshot doc : snapshot) {
                    Incident incident = doc.toObject(Incident.class);
                    incidentList.add(incident);
                    addIncidentMarker(incident.getLatitude(), incident.getLongitude(), incident.getTitle());
                }

                // Notify adapter & update UI
                incidentAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addIncidentMarker(double latitude, double longitude, String title) {
        GeoPoint location = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(mapView);
        marker.setPosition(location);
        marker.setTitle(title);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
        mapView.invalidate();
        mapController.setCenter(location);
    }
}
