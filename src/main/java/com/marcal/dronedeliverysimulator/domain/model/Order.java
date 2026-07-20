package com.marcal.dronedeliverysimulator.domain.model;

import com.marcal.dronedeliverysimulator.domain.enums.OrderPriority;
import com.marcal.dronedeliverysimulator.domain.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Order {

    @Builder.Default
    private final UUID id = UUID.randomUUID();

    private final Coordinate destination;

    private final double weight;

    private final OrderPriority priority;

    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    public void allocate() {
        this.status = OrderStatus.ALLOCATED;
    }

    public void startDelivery() {
        this.status = OrderStatus.DELIVERING;
    }

    public void complete() {
        this.status = OrderStatus.DELIVERED;
    }
}