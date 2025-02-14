package com.anshu.trackmyguard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private List<DashboardFeature> featureList;
    private Context context;

    public DashboardAdapter(Context context, List<DashboardFeature> featureList) {
        this.context = context;
        this.featureList = featureList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard_feature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardFeature feature = featureList.get(position);
        holder.tvFeatureName.setText(feature.getFeatureName());
        holder.imgFeatureIcon.setImageResource(feature.getIconResource());

        // Open respective activity on click
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, feature.getActivityClass());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return featureList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFeatureIcon;
        TextView tvFeatureName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgFeatureIcon = itemView.findViewById(R.id.imgFeatureIcon);
            tvFeatureName = itemView.findViewById(R.id.tvFeatureName);
        }
    }
}

