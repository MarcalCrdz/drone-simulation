package com.marcal.dronedeliverysimulator.service;

import com.marcal.dronedeliverysimulator.controller.dto.CreateOrderRequest;
import com.marcal.dronedeliverysimulator.domain.enums.OrderPriority;
import com.marcal.dronedeliverysimulator.domain.model.Mission;
import com.marcal.dronedeliverysimulator.domain.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DroneSimulationServiceTest {

    private DroneSimulationService simulationService;

    @BeforeEach
    void setUp() {
        simulationService = new DroneSimulationService();
    }

    @Test
    @DisplayName("Deve cadastrar um pedido com sucesso")
    void shouldAddOrderSuccessfully() {
        CreateOrderRequest request = new CreateOrderRequest(2.0, 3.0, 4.0, OrderPriority.HIGH);

        Order order = simulationService.addOrder(request);

        assertNotNull(order);
        assertEquals(4.0, order.getWeight());
        assertEquals(OrderPriority.HIGH, order.getPriority());
        assertEquals(1, simulationService.getPendingOrders().size());
    }

    @Test
    @DisplayName("Deve priorizar pedidos HIGH sobre LOW e agrupar em missões respeitando capacidade")
    void shouldPrioritizeHighPriorityAndGroupMissions() {
        // Pedido LOW
        simulationService.addOrder(new CreateOrderRequest(1.0, 1.0, 3.0, OrderPriority.LOW));
        // Pedido HIGH
        simulationService.addOrder(new CreateOrderRequest(2.0, 2.0, 5.0, OrderPriority.HIGH));

        List<Mission> missions = simulationService.runSimulation();

        assertNotNull(missions);
        assertFalse(missions.isEmpty());

        Order firstOrderProcessed = missions.get(0).getOrders().get(0);
        assertEquals(OrderPriority.HIGH, firstOrderProcessed.getPriority());
    }
}