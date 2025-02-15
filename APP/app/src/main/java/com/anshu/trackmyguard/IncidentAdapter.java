package com.anshu.trackmyguard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder> {
    private List<Incident> incidentList;

    public IncidentAdapter(List<Incident> incidentList) {
        this.incidentList = incidentList;
    }

    @NonNull
    @Override
    public IncidentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incident, parent, false);
        return new IncidentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentViewHolder holder, int position) {
        Incident incident = incidentList.get(position);
        holder.title.setText(incident.getTitle());
        holder.description.setText(incident.getDescription());
    }

    @Override
    public int getItemCount() {
        return incidentList.size();
    }

    static class IncidentViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, timestamp;

        IncidentViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.incidentTitle);
            description = itemView.findViewById(R.id.incidentDescription);
            timestamp = itemView.findViewById(R.id.incidentTimestamp);
        }
    }
}
