package com.marcal.dronedeliverysimulator.domain.model;

import com.marcal.dronedeliverysimulator.domain.enums.DroneState;
import lombok.Getter;

@Getter
public class Drone {

    private final String id;

    private final double capacity;

    private final double maxRange;

    private Coordinate currentPosition;

    private DroneState state;

    public Drone(String id,
                 double capacity,
                 double maxRange,
                 Coordinate currentPosition) {

        this.id = id;
        this.capacity = capacity;
        this.maxRange = maxRange;
        this.currentPosition = currentPosition;
        this.state = DroneState.IDLE;
    }

    public boolean canCarry(double weight) {
        return weight <= capacity;
    }

    public boolean canTravel(double distance) {
        return distance <= maxRange;
    }

    public boolean isAvailable() {
        return state == DroneState.IDLE;
    }

    public void startMission() {
        this.state = DroneState.LOADING;
    }

    public void depart() {
        this.state = DroneState.FLYING;
    }

    public void startDelivery() {
        this.state = DroneState.DELIVERING;
    }

    public void returnToBase() {
        this.state = DroneState.RETURNING;
    }

    public void finishMission(Coordinate base) {
        this.currentPosition = base;
        this.state = DroneState.IDLE;
    }
}