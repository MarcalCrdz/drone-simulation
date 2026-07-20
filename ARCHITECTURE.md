# Drone Delivery Simulator

## Objetivo

Simular um sistema de entregas por drones urbanos capaz de alocar pedidos utilizando o menor número possível de viagens, respeitando:

- Capacidade máxima de carga
- Alcance máximo do drone
- Prioridade das entregas
- Distância percorrida

---

# Arquitetura

O projeto foi dividido em quatro camadas principais.

```
               REST API
                   │
        ┌──────────┴──────────┐
        │                     │
   Controllers            DTOs
        │
        ▼
     Services
        │
 ┌──────┴─────────┐
 │                │
AllocationService RoutePlanner
 │                │
 └──────┬─────────┘
        ▼
 SimulationEngine
        │
        ▼
      Domain
```

---

# Fluxo da aplicação

```
Cliente

↓

POST /orders

↓

OrderController

↓

OrderService

↓

OrderQueue

↓

AllocationService

↓

Mission

↓

RoutePlanner

↓

SimulationEngine

↓

Drone

↓

Mission Completed
```

---

# Estrutura de pacotes

```
src/main/java/com/marcal/dronesimulator

├── controller
│
├── service
│
├── domain
│
├── dto
│
├── exception
│
├── config
│
├── util
│
└── simulation
```

---

# Domínio

## Coordinate

Representa um ponto da cidade.

Responsabilidades

- armazenar X
- armazenar Y
- calcular distância entre dois pontos

---

## Order

Representa um pedido.

Possui

- id
- destino
- peso
- prioridade

Não conhece drones.

Não conhece missões.

---

## Drone

Representa um drone da frota.

Possui

- id
- capacidade máxima
- alcance máximo
- bateria
- posição atual
- estado

Não decide quais pedidos transportar.

---

## Mission

Representa uma viagem completa.

Possui

- drone responsável
- lista de pedidos
- peso total
- distância total
- tempo estimado
- status

---

# Serviços

## OrderService

Responsável por:

- cadastrar pedidos
- listar pedidos
- validar pedidos

---

## DroneService

Responsável por:

- cadastrar drones
- listar drones
- atualizar estados

---

## AllocationService

Responsável por montar missões.

Recebe:

- drones
- pedidos

Retorna:

- missão pronta

Estratégia inicial

1. Ordenar pedidos por prioridade
2. Ordenar por distância
3. Agrupar pedidos respeitando:

- capacidade
- alcance

---

## RoutePlanner

Responsável apenas por calcular a rota.

Inicialmente utilizará distância euclidiana.

No futuro poderá utilizar algoritmos mais sofisticados.

---

## SimulationEngine

Responsável por executar a missão.

Fluxo

```
IDLE

↓

LOADING

↓

FLYING

↓

DELIVERING

↓

RETURNING

↓

IDLE
```

---

# Enums

## DroneState

```
IDLE
LOADING
FLYING
DELIVERING
RETURNING
```

---

## OrderPriority

```
HIGH

MEDIUM

LOW
```

---

## MissionStatus

```
CREATED

IN_PROGRESS

COMPLETED

CANCELLED
```

---

# Regras do domínio

## Capacidade

```
mission.totalWeight <= drone.maxPayload
```

---

## Alcance

```
mission.totalDistance <= drone.maxRange
```

---

## Prioridade

```
HIGH

↓

MEDIUM

↓

LOW
```

---

# Cidade

A cidade será representada como uma malha bidimensional.

A base dos drones estará localizada em:

```
(0,0)
```

---

# Algoritmo inicial

O algoritmo utilizado será uma heurística gulosa (Greedy).

Passos

1. Ordenar pedidos por prioridade.
2. Dentro da prioridade ordenar pela menor distância até a base.
3. Adicionar pedidos à missão enquanto:
    - respeitar capacidade
    - respeitar alcance

Essa estratégia foi escolhida por ser simples, eficiente para o contexto do desafio e facilmente substituível por algoritmos mais sofisticados (como Vehicle Routing Problem) sem alterar a arquitetura do sistema.

---

# Princípios adotados

- Controllers sem regra de negócio.
- Cada classe possui uma única responsabilidade.
- O domínio é independente do Spring.
- Serviços contêm as regras de negócio.
- Entidades representam apenas o estado do sistema.
- A arquitetura deve permitir evolução sem grandes alterações estruturais.