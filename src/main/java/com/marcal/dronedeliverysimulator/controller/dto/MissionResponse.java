package com.marcal.dronedeliverysimulator.controller.dto;

import java.util.UUID;

public record MissionResponse(

        UUID missionId,
        String droneId,
        int totalOrders,
        double totalWeight,
        double totalDistance

) {
}