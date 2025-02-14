package com.anshu.trackmyguard;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GuardDatabaseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GuardAdapter adapter;
    private List<Guard> guardList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guard_database);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerViewGuards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        guardList = new ArrayList<>();
        guardList.add(new Guard("Dheeraj",25,"Google_guard","Hostel H","Amazon_guard",
                "https://firebasestorage.googleapis.com/v0/b/antakshari-75fe9.appspot.com/o/uploads%2F1723835750958.jpeg?alt=media&token=ed6876d9-06c9-4dcd-adf1-d49d1e3431c7"));
        adapter = new GuardAdapter(this, guardList);
        recyclerView.setAdapter(adapter);

        db.collection("guards").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Guard guard = doc.toObject(Guard.class);
                guardList.add(guard);
            }
            adapter.notifyDataSetChanged();
        });
    }
}