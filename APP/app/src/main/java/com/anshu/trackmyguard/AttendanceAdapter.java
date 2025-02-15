package com.anshu.trackmyguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<GuardAttendance> guardList;

    public AttendanceAdapter(List<GuardAttendance> guardList) {
        this.guardList = guardList;
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
    }

    @Override
    public int getItemCount() {
        return guardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView guardName, guardLocation, statusText;
        ImageView statusIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            guardName = itemView.findViewById(R.id.textGuardName);
            guardLocation = itemView.findViewById(R.id.textGuardLocation);
            statusText = itemView.findViewById(R.id.textStatus);
            statusIcon = itemView.findViewById(R.id.imageStatus);
        }
    }
}
