package com.anshu.trackmyguard;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AttendanceActivity extends AppCompatActivity {
    private MapView mapView;
    private RecyclerView recyclerView;
    private AttendanceAdapter adapter;
    private List<GuardAttendance> guardList = new ArrayList<>();
    private FirebaseFirestore firestore;
    IMapController mapController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mapView = findViewById(R.id.mapViewAttendance);
        recyclerView = findViewById(R.id.recyclerViewAttendance);
        mapView.getController().
                setZoom(15.0);
        mapController=mapView.getController();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AttendanceAdapter(guardList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        fetchGuardsAttendance();
    }
    private void fetchGuardsAttendance() {
        firestore.collection("GuardAttendance").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("AdminAttendance", "Error fetching attendance: " + e.getMessage());
                    return;
                }

                guardList.clear();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot doc : snapshot) {
                        GuardAttendance guard = doc.toObject(GuardAttendance.class);
                        guardList.add(guard);
                        addGuardMarker(guard.getLatitude(), guard.getLongitude(), guard.getName(), guard.isOutOfZone());
                        mapController.setCenter(new GeoPoint(guard.getLatitude(), guard.getLongitude()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addGuardMarker(double latitude, double longitude, String name, boolean outOfZone) {
        GeoPoint location = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(mapView);
        marker.setPosition(location);
        marker.setTitle(name + (outOfZone ? " 🚨 OUT OF ZONE!" : ""));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }
}