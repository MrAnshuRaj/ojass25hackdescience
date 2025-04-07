package com.anshu.trackmyguard;

import static java.security.AccessController.getContext;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<GuardAttendance> guardList;
    private Context context;
    public AttendanceAdapter(List<GuardAttendance> guardList, Context context) {
        this.guardList = guardList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GuardAttendance guard = guardList.get(position);
        holder.guardName.setText(guard.getName());
        holder.guardLocation.setText("📍 " + guard.getLatitude() + ", " + guard.getLongitude());

        if (guard.isOutOfZone()) {
            holder.statusText.setText("🚨 OUT OF ZONE");
            holder.statusIcon.setImageResource(R.drawable.baseline_add_alert_24); // Red alert icon
        } else {
            holder.statusText.setText("✅ In Duty Zone");
            holder.statusIcon.setImageResource(R.drawable.baseline_check_circle_24); // Green check icon
        }
        holder.btnCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = guard.getPhone(); // Replace with the actual phone number
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+91" + phoneNumber));
                try {
                    context.startActivity(callIntent);
                } catch (SecurityException e) {
                    // Handle the case where the user has not granted the CALL_PHONE permission
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                } catch (ActivityNotFoundException e) {
                    // Handle the case where no activity can handle the intent
                    Toast.makeText(context, "No phone app found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return guardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView guardName, guardLocation, statusText;
        ImageView statusIcon;
        ImageButton btnCallBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            guardName = itemView.findViewById(R.id.textGuardName);
            guardLocation = itemView.findViewById(R.id.textGuardLocation);
            statusText = itemView.findViewById(R.id.textStatus);
            statusIcon = itemView.findViewById(R.id.imageStatus);
            btnCallBtn = itemView.findViewById(R.id.btnCallBtn);
        }
    }
}
