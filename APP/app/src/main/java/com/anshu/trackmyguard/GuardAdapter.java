package com.anshu.trackmyguard;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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
        holder.tvGuardName.setText(guard.getName());
        holder.tvGuardAge.setText("Age: " + guard.getAge());
        holder.tvPastWork.setText("Past Work: " + guard.getPastWork());
        holder.tvResidence.setText("Residence: " + guard.getResidence());
        holder.tvCurrentDeployment.setText("Deployment: " + guard.getCurrentDeployment());

        // Load guard image
        Glide.with(context).load(guard.getPhotoUrl()).into(holder.guardImage);
    }

    @Override
    public int getItemCount() {
        return guardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView guardImage;
        TextView tvGuardName, tvGuardAge, tvPastWork, tvResidence, tvCurrentDeployment;

        public ViewHolder(View itemView) {
            super(itemView);
            guardImage = itemView.findViewById(R.id.guardImage);
            tvGuardName = itemView.findViewById(R.id.tvGuardName);
            tvGuardAge = itemView.findViewById(R.id.tvGuardAge);
            tvPastWork = itemView.findViewById(R.id.tvPastWork);
            tvResidence = itemView.findViewById(R.id.tvResidence);
            tvCurrentDeployment = itemView.findViewById(R.id.tvCurrentDeployment);
        }
    }
}
