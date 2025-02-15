package com.anshu.trackmyguard;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.views.MapView;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DashboardAdapter adapter;
    private List<DashboardFeature> featureList;
    TextView admin_name;
    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.rvDash);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        admin_name=findViewById(R.id.admin_name);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getUid();
        db.collection("admins").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        admin_name.setText("Welcome " + name);
                        Toast.makeText(AdminDashboardActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                    }
                });
        // Initialize feature list
        featureList = new ArrayList<>();
        featureList.add(new DashboardFeature("Security Guard Database 📋", R.drawable.logo_tmg, GuardDatabaseActivity.class));
        featureList.add(new DashboardFeature("Live Guard Location Tracking 📍",  R.drawable.logo_tmg, LiveTrackingActivity.class));
        featureList.add(new DashboardFeature("Assign & Rotate Guards 🔄",  R.drawable.logo_tmg, AssignGuardsActivity.class));
        featureList.add(new DashboardFeature("Shift & Attendance Monitoring ⏳", R.drawable.logo_tmg, AttendanceActivity.class));
        featureList.add(new DashboardFeature("Incident Reporting 🚨",  R.drawable.logo_tmg, IncidentReportingActivity.class));
        featureList.add(new DashboardFeature("Guard Performance & Feedback ⭐",  R.drawable.logo_tmg, FeedbackActivity.class));

        adapter = new DashboardAdapter(this, featureList);
        recyclerView.setAdapter(adapter);
    }
}