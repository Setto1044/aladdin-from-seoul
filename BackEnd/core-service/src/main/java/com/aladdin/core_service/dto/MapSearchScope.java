package com.aladdin.core_service.dto;

public enum MapSearchScope {
    NEAR(1_000),
    MIDDLE(10_000),
    FAR(100_000);

    private final double distanceMeter;

    MapSearchScope(double distanceMeter) {
        this.distanceMeter = distanceMeter;
    }

    public double getDistanceMeter() {
        return distanceMeter;
    }

    public boolean isDetailed() {
        return this == NEAR;
    }
}
