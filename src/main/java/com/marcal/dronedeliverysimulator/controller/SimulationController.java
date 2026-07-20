package com.marcal.dronedeliverysimulator.controller;

import com.marcal.dronedeliverysimulator.controller.dto.CreateOrderRequest;
import com.marcal.dronedeliverysimulator.controller.dto.MissionResponse;
import com.marcal.dronedeliverysimulator.domain.model.Mission;
import com.marcal.dronedeliverysimulator.domain.model.Order;
import com.marcal.dronedeliverysimulator.service.DroneSimulationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/simulacao")
public class SimulationController {

    private final DroneSimulationService simulationService;

    public SimulationController(DroneSimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/pedidos")
    public ResponseEntity<Order> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        Order order = simulationService.addOrder(request);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/executar")
    public ResponseEntity<List<MissionResponse>> runSimulation() {
        List<Mission> missions = simulationService.runSimulation();

        List<MissionResponse> response = missions.stream()
                .map(m -> new MissionResponse(
                        m.getId(),
                        m.getDrone().getId(),
                        m.getOrders().size(),
                        m.getTotalWeight(),
                        m.getTotalDistance()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        boolean removed = simulationService.deleteOrder(id);
        if (removed) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/pedidos")
    public ResponseEntity<Void> clearPendingOrders() {
        simulationService.clearPendingOrders();
        return ResponseEntity.noContent().build();
    }
}