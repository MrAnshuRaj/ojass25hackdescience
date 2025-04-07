package com.anshu.trackmyguard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class GuardAdapter extends RecyclerView.Adapter<GuardAdapter.ViewHolder> {
    private Context context;
    private List<Guard> guardList;

    public GuardAdapter(Context context, List<Guard> guardList) {
        this.context = context;
        this.guardList = guardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Guard guard = guardList.get(position);
        holder.tvGuardName.setText(guard.getUserName());
        holder.tvGuardAge.setText("Age: " + guard.getAge());
        holder.tvGuardNumber.setText("Phone Number: " + guard.getPhone());
        holder.tvOrganization.setText("Organization: " + guard.getOrganization());

        // Load image
        // Glide.with(context).load(guard.getPhotoUrl()).into(holder.guardImage);

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditGuardActivity.class);
            intent.putExtra("guardId", guard.getId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Users").document(guard.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        guardList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, guardList.size()); // Adjust indices properly
                    })
                    .addOnFailureListener(e -> Log.e("GuardAdapter", "Error deleting guard", e));
        });
    }

    @Override
    public int getItemCount() {
        return guardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView guardImage;
        TextView tvGuardName, tvGuardAge, tvGuardNumber, tvOrganization;
        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            guardImage = itemView.findViewById(R.id.guardImage);
            tvGuardName = itemView.findViewById(R.id.tvGuardName);
            tvGuardAge = itemView.findViewById(R.id.tvGuardAge);
            tvGuardNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvOrganization = itemView.findViewById(R.id.tvOrganization);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
