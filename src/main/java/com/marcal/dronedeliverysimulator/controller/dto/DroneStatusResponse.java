package com.marcal.dronedeliverysimulator.controller.dto;

import com.marcal.dronedeliverysimulator.domain.enums.DroneState;

public record DroneStatusResponse(

        String id,
        DroneState state,
        double capacity,
        double maxRange

) {
}