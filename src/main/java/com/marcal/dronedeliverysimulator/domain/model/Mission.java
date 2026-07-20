package com.marcal.dronedeliverysimulator.domain.model;

import com.marcal.dronedeliverysimulator.domain.enums.MissionStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class Mission {

    private final UUID id;

    private final Drone drone;

    private final List<Order> orders;

    private double totalWeight;

    private double totalDistance;

    private MissionStatus status;

    public Mission(Drone drone) {
        this.id = UUID.randomUUID();
        this.drone = drone;
        this.orders = new ArrayList<>();
        this.status = MissionStatus.CREATED;
    }

    public void addOrder(Order order) {
        orders.add(order);
        totalWeight += order.getWeight();
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public void registerDistance(double distance) {
        this.totalDistance = distance;
    }

    public void start() {
        status = MissionStatus.IN_PROGRESS;
    }

    public void complete() {
        status = MissionStatus.COMPLETED;
    }
}