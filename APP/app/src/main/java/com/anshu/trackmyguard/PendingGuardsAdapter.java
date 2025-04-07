package com.anshu.trackmyguard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PendingGuardsAdapter extends RecyclerView.Adapter<PendingGuardsAdapter.GuardViewHolder> {

    private List<Guard> pendingGuards;
    private Context context;
    private OnDecisionListener listener;

    public interface OnDecisionListener {
        void onApprove(Guard guard);
        void onReject(Guard guard);
    }

    public PendingGuardsAdapter(Context context, List<Guard> pendingGuards, OnDecisionListener listener) {
        this.context = context;
        this.pendingGuards = pendingGuards;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GuardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pending_guard, parent, false);
        return new GuardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuardViewHolder holder, int position) {
        Guard guard = pendingGuards.get(position);
        holder.tvGuardName.setText(guard.getUserName());
        holder.tvGuardPhone.setText(guard.getPhone());

        holder.btnApprove.setOnClickListener(v -> listener.onApprove(guard));
        holder.btnReject.setOnClickListener(v -> listener.onReject(guard));
    }

    @Override
    public int getItemCount() {
        return pendingGuards.size();
    }

    static class GuardViewHolder extends RecyclerView.ViewHolder {
        TextView tvGuardName, tvGuardPhone;
        Button btnApprove, btnReject;

        public GuardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGuardName = itemView.findViewById(R.id.tvGuardName);
            tvGuardPhone = itemView.findViewById(R.id.tvGuardPhone);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
