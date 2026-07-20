package com.marcal.dronedeliverysimulator.domain.model;

public record Coordinate(
        double x,
        double y
) {

    public double distanceTo(Coordinate other) {
        return Math.hypot(
                this.x - other.x,
                this.y - other.y
        );
    }
}