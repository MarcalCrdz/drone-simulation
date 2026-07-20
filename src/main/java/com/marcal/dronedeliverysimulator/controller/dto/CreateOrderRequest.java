package com.marcal.dronedeliverysimulator.controller.dto;

import com.marcal.dronedeliverysimulator.domain.enums.OrderPriority;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(

        @NotNull
        Double x,

        @NotNull
        Double y,

        @Min(1)
        double weight,

        @NotNull
        OrderPriority priority

) {
}