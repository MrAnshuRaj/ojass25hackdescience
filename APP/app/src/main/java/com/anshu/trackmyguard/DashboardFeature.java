package com.anshu.trackmyguard;
public class DashboardFeature {
    private String featureName;
    private int iconResource;
    private Class<?> activityClass;

    public DashboardFeature(String featureName, int iconResource, Class<?> activityClass) {
        this.featureName = featureName;
        this.iconResource = iconResource;
        this.activityClass = activityClass;
    }

    public String getFeatureName() { return featureName; }
    public int getIconResource() { return iconResource; }
    public Class<?> getActivityClass() { return activityClass; }
}
