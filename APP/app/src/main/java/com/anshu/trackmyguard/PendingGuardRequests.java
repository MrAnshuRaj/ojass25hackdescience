package com.anshu.trackmyguard;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PendingGuardRequests extends AppCompatActivity {
    private RecyclerView recyclerPendingRequests;
    private FirebaseFirestore db;
    TextView textNoPending;
    private List<Guard> pendingGuards = new ArrayList<>();
    private PendingGuardsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pending_guard_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerPendingRequests = findViewById(R.id.recyclerPendingRequests);
        db = FirebaseFirestore.getInstance();
        textNoPending=findViewById(R.id.textNoPending);
        adapter = adapter = new PendingGuardsAdapter(
                this,
                pendingGuards,
                new PendingGuardsAdapter.OnDecisionListener() {
                    @Override
                    public void onApprove(Guard guard) {
                        verifyGuard(guard);
                    }

                    @Override
                    public void onReject(Guard guard) {
                        rejectGuard(guard);
                    }
                }
        );
        recyclerPendingRequests.setAdapter(adapter);

        loadPendingGuards();
    }

    private void loadPendingGuards() {
        db.collection("Users")
                .whereEqualTo("role", "guard")
                .whereEqualTo("isVerified", false)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    pendingGuards.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Guard guard = doc.toObject(Guard.class);
                        if (guard != null) {
                            guard.setId(doc.getId());
                            //Toast.makeText(PendingGuardRequests.this, "Guard :"+guard.getUserName()+ guard.isVerified()+guard.getPhone(), Toast.LENGTH_SHORT).show();
                            pendingGuards.add(guard);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    checkIfPendingListEmpty();
                });
    }
    private void checkIfPendingListEmpty() {
        if (pendingGuards.isEmpty()) {
            textNoPending.setVisibility(View.VISIBLE);
        } else {
            textNoPending.setVisibility(View.GONE);
        }
    }

    private void verifyGuard(Guard guard) {
        db.collection("Users").document(guard.getId())
                .update("isVerified", true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Guard verified", Toast.LENGTH_SHORT).show();
                    pendingGuards.remove(guard);
                    adapter.notifyDataSetChanged();
                    checkIfPendingListEmpty();
                });
    }

    private void rejectGuard(Guard guard) {
        db.collection("Users").document(guard.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Guard rejected", Toast.LENGTH_SHORT).show();
                    pendingGuards.remove(guard);
                    adapter.notifyDataSetChanged();
                    checkIfPendingListEmpty();
                });
    }
}