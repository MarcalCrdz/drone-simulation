# Domain Model

## Objetivo do domínio

O sistema representa uma empresa de entregas urbanas utilizando uma frota de drones.

Seu objetivo é organizar pedidos em missões de entrega, utilizando o menor número possível de viagens e respeitando todas as restrições operacionais.

O domínio foi modelado para priorizar regras de negócio em vez de detalhes tecnológicos.

---

# Conceitos do domínio

## Drone

Um Drone representa um veículo autônomo responsável por executar missões de entrega.

Todo drone possui limitações físicas que determinam quais missões podem ser realizadas.

### Responsabilidades

- transportar pedidos
- executar missões
- informar seu estado atual
- informar sua posição atual

### Restrições

- possui capacidade máxima de carga
- possui alcance máximo por carga de bateria
- executa apenas uma missão por vez

---

## Order

Uma Order representa uma solicitação de entrega realizada por um cliente.

Cada pedido é independente e aguarda ser agrupado em uma missão.

### Informações

- destino
- peso
- prioridade

### Restrições

- pertence a apenas uma missão
- não conhece o drone responsável
- não conhece sua rota

---

## Mission

Uma Mission representa uma viagem completa realizada por um drone.

Ela agrupa um ou mais pedidos.

É a principal unidade de execução do sistema.

### Informações

- drone responsável
- lista de pedidos
- distância total
- peso total
- tempo estimado
- status

### Objetivo

Agrupar pedidos de maneira eficiente, reduzindo o número de viagens.

---

## Coordinate

Representa uma posição na cidade.

A cidade é modelada como uma malha bidimensional (2D).

```
(X,Y)
```

Toda distância utilizada pelo sistema é calculada a partir dessas coordenadas.

---

# Base de Operações

Todos os drones iniciam suas missões na base operacional.

A base possui coordenada fixa.

```
(0,0)
```

Toda missão possui:

Base

↓

Pedidos

↓

Retorno à Base

---

# Prioridades

Os pedidos possuem prioridade de atendimento.

```
HIGH

↓

MEDIUM

↓

LOW
```

Pedidos de maior prioridade devem ser considerados primeiro pelo algoritmo de alocação.

---

# Estados do Drone

Durante uma missão um drone percorre os seguintes estados.

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

Cada estado representa exatamente uma etapa da missão.

---

# Regras Fundamentais

## Regra 1 — Capacidade

O peso total transportado por uma missão nunca poderá ultrapassar a capacidade máxima do drone.

```
Mission Weight ≤ Drone Capacity
```

---

## Regra 2 — Alcance

A distância total percorrida nunca poderá ultrapassar o alcance máximo do drone.

```
Mission Distance ≤ Drone Range
```

---

## Regra 3 — Missão

Uma missão sempre possui exatamente um drone.

Uma missão pode possuir um ou vários pedidos.

---

## Regra 4 — Exclusividade

Um drone nunca executa duas missões simultaneamente.

---

## Regra 5 — Pedido

Um pedido pode pertencer apenas a uma missão.

---

# Processo de Negócio

O funcionamento do sistema pode ser resumido pelo fluxo abaixo.

```
Novo Pedido

↓

Fila de Pedidos

↓

Algoritmo de Alocação

↓

Criação da Missão

↓

Execução da Missão

↓

Entrega

↓

Retorno do Drone

↓

Drone Disponível
```

---

# Objetivo do Algoritmo

O algoritmo deve buscar um equilíbrio entre:

- reduzir o número de viagens
- respeitar capacidade
- respeitar alcance
- atender prioridades
- manter alta utilização dos drones

Não existe a obrigação de produzir a solução matemática ótima.

O foco é produzir uma solução eficiente, consistente e facilmente evoluível.

---

# Linguagem Ubíqua

Para manter consistência entre documentação e código, os seguintes termos serão utilizados em todo o projeto.

| Termo | Significado |
|--------|-------------|
| Drone | Veículo responsável pela entrega |
| Order | Pedido realizado por um cliente |
| Mission | Viagem completa realizada por um drone |
| Coordinate | Localização na cidade |
| Allocation | Processo de agrupamento de pedidos |
| Route | Ordem de visita dos pedidos |
| Simulation | Execução da missão |