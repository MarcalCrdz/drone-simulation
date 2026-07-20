package com.marcal.dronedeliverysimulator.service;

import com.marcal.dronedeliverysimulator.controller.dto.CreateOrderRequest;
import com.marcal.dronedeliverysimulator.domain.enums.OrderPriority;
import com.marcal.dronedeliverysimulator.domain.model.Coordinate;
import com.marcal.dronedeliverysimulator.domain.model.Drone;
import com.marcal.dronedeliverysimulator.domain.model.Mission;
import com.marcal.dronedeliverysimulator.domain.model.Order;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DroneSimulationService {

    private static final Coordinate BASE_LOCATION = new Coordinate(0, 0);

    private final List<Drone> drones = new ArrayList<>();
    private final List<Order> pendingOrders = new ArrayList<>();
    private final List<Mission> executedMissions = new ArrayList<>();

    public DroneSimulationService() {
        drones.add(new Drone("DRONE-01", 10.0, 40.0, BASE_LOCATION));
        drones.add(new Drone("DRONE-02", 8.0, 35.0, BASE_LOCATION));
    }

    /**
     * Adiciona um novo pedido à fila em memória
     */
    public Order addOrder(CreateOrderRequest request) {
        Order order = Order.builder()
                .destination(new Coordinate(request.x(), request.y()))
                .weight(request.weight())
                .priority(request.priority())
                .build();

        pendingOrders.add(order);
        return order;
    }

    /**
     * Cancela e remove um pedido específico da fila de pendentes pelo ID
     */
    public boolean deleteOrder(UUID id) {
        return pendingOrders.removeIf(order -> order.getId().equals(id));
    }

    /**
     * Limpa toda a fila de pedidos pendentes em memória
     */
    public void clearPendingOrders() {
        pendingOrders.clear();
    }

    /**
     * Executa a simulação e aloca os pedidos nas missões
     */
    public List<Mission> runSimulation() {
        if (pendingOrders.isEmpty()) {
            return Collections.emptyList();
        }

        // Ordena por Prioridade (HIGH -> MEDIUM -> LOW) e Proximidade da Base
        pendingOrders.sort(Comparator
                .comparing((Order o) -> o.getPriority() == OrderPriority.HIGH ? 3 :
                        o.getPriority() == OrderPriority.MEDIUM ? 2 : 1)
                .reversed()
                .thenComparingDouble(o -> o.getDestination().distanceTo(BASE_LOCATION)));

        List<Order> unassignedOrders = new ArrayList<>(pendingOrders);

        while (!unassignedOrders.isEmpty()) {
            Drone drone = drones.stream()
                    .filter(Drone::isAvailable)
                    .findFirst()
                    .orElse(drones.get(0));

            Mission mission = new Mission(drone);
            drone.startMission(); // LOADING

            Iterator<Order> iterator = unassignedOrders.iterator();

            while (iterator.hasNext()) {
                Order order = iterator.next();

                if (drone.canCarry(mission.getTotalWeight() + order.getWeight())) {
                    double simulatedDistance = calculateRouteDistance(mission.getOrders(), order);

                    if (drone.canTravel(simulatedDistance)) {
                        mission.addOrder(order);
                        order.allocate();
                        iterator.remove();
                    }
                }
            }

            if (mission.getOrders().isEmpty()) {
                throw new IllegalArgumentException("Existe um pedido que excede a capacidade ou autonomia máxima da frota.");
            }

            double totalDistance = calculateRouteDistance(mission.getOrders(), null);
            mission.registerDistance(totalDistance);

            // Simulação da máquina de estados do Drone
            drone.depart();         // FLYING
            drone.startDelivery();  // DELIVERING
            drone.returnToBase();   // RETURNING
            drone.finishMission(BASE_LOCATION); // IDLE

            mission.start();
            mission.complete();
            mission.getOrders().forEach(Order::complete);

            executedMissions.add(mission);
        }

        pendingOrders.clear();
        return executedMissions;
    }

    private double calculateRouteDistance(List<Order> currentOrders, Order candidateOrder) {
        List<Order> route = new ArrayList<>(currentOrders);
        if (candidateOrder != null) {
            route.add(candidateOrder);
        }

        double totalDistance = 0.0;
        Coordinate currentPos = BASE_LOCATION;

        for (Order order : route) {
            totalDistance += currentPos.distanceTo(order.getDestination());
            currentPos = order.getDestination();
        }

        totalDistance += currentPos.distanceTo(BASE_LOCATION);
        return totalDistance;
    }

    public List<Drone> getDrones() {
        return Collections.unmodifiableList(drones);
    }

    public List<Order> getPendingOrders() {
        return Collections.unmodifiableList(pendingOrders);
    }
}