# Architecture Decisions

Este documento registra as principais decisões arquiteturais tomadas durante o desenvolvimento do projeto.

O objetivo é explicar **por que** determinadas soluções foram escolhidas e quais trade-offs foram considerados.

---

# ADR-001
## Utilização de armazenamento em memória

### Contexto

O desafio não exige persistência dos dados.

O objetivo principal é simular algoritmos de alocação e execução de entregas.

### Decisão

Toda a aplicação armazenará os dados em memória utilizando coleções Java.

Exemplos:

- List<Order>
- List<Drone>
- List<Mission>

### Motivo

Adicionar banco de dados aumentaria significativamente a complexidade do projeto sem agregar valor ao problema proposto.

A prioridade deste projeto é demonstrar:

- modelagem
- algoritmos
- arquitetura
- regras de negócio

### Consequências

Vantagens

- código mais simples
- menor tempo de desenvolvimento
- maior foco na lógica do problema

Desvantagens

- dados são perdidos ao reiniciar a aplicação

---

# ADR-002
## Separação entre domínio e framework

### Contexto

As regras de negócio não devem depender do Spring Boot.

### Decisão

As entidades do domínio não possuirão dependências do framework.

Classes como:

- Drone
- Order
- Mission
- Coordinate

serão simples objetos Java.

### Motivo

Essa abordagem facilita:

- testes unitários
- reutilização
- manutenção

### Consequências

Toda regra de negócio permanecerá nos serviços.

---

# ADR-003
## Utilização da entidade Mission

### Contexto

Uma entrega representa uma viagem completa do drone e pode transportar múltiplos pedidos.

### Decisão

Foi criada a entidade Mission.

Ela representa uma missão executada por um drone.

Uma missão contém:

- drone responsável
- pedidos
- distância
- peso
- tempo estimado
- status

### Motivo

Essa modelagem aproxima o sistema do problema real.

O algoritmo passa a otimizar viagens e não apenas pedidos individuais.

### Consequências

A lógica do domínio fica mais simples e mais próxima do objetivo do desafio.

---

# ADR-004
## Estratégia de alocação

### Contexto

Encontrar a melhor combinação possível de pedidos é um problema semelhante ao Vehicle Routing Problem.

Soluções ótimas possuem alta complexidade computacional.

### Decisão

Foi adotada inicialmente uma estratégia gulosa (Greedy).

Passos

1. Priorizar pedidos HIGH.
2. Dentro da prioridade ordenar pela menor distância.
3. Inserir pedidos enquanto respeitar:
    - capacidade
    - alcance

### Motivo

A heurística apresenta:

- implementação simples
- boa eficiência
- facilidade de entendimento
- facilidade de testes

### Consequências

A solução não garante o resultado matematicamente ótimo.

Entretanto, fornece excelente relação entre qualidade da solução e simplicidade para o contexto do desafio.

---

# ADR-005
## Máquina de Estados

### Contexto

Durante uma entrega o drone muda de comportamento.

### Decisão

Foi utilizada uma máquina de estados.

Estados

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

### Motivo

Representar explicitamente o ciclo de vida do drone torna a simulação mais clara e facilita futuras expansões.

### Consequências

A simulação torna-se mais organizada e previsível.

---

# ADR-006
## Classe Coordinate

### Contexto

Diversos componentes necessitam realizar cálculos de distância.

### Decisão

Foi criada a entidade Coordinate.

Ela encapsula:

- coordenada X
- coordenada Y
- cálculo de distância

### Motivo

Evitar duplicação da fórmula de distância em múltiplas classes.

### Consequências

Maior reutilização e melhor encapsulamento.

---

# ADR-007
## Controllers sem regra de negócio

### Contexto

A API REST representa apenas uma interface de entrada para o sistema.

### Decisão

Controllers apenas recebem requisições HTTP e delegam a execução para os serviços.

### Motivo

Separação de responsabilidades.

### Consequências

Código mais organizado.

Maior facilidade para testes.

Maior facilidade para manutenção.

---

# ADR-008
## Arquitetura orientada ao domínio

### Contexto

O desafio é centrado em regras de negócio.

### Decisão

O desenvolvimento seguirá uma abordagem Domain-Oriented.

Fluxo

Controller

↓

Service

↓

Domain

↓

Simulation

### Motivo

As entidades representam o problema.

Os serviços implementam o comportamento.

### Consequências

Arquitetura mais escalável e de fácil evolução.

---

# Possíveis evoluções

O projeto foi desenvolvido para permitir futuras melhorias sem alterações significativas na arquitetura.

Exemplos

- múltiplos centros de distribuição
- drones com velocidades diferentes
- consumo de bateria baseado no peso
- zonas de exclusão aérea
- algoritmo A*
- Vehicle Routing Problem
- banco de dados
- mensageria
- processamento paralelo