
# 🛸 Drone Delivery Simulator - dti digital

Simulador de logística e entregas por drones urbanos, desenvolvido para o desafio técnico do processo seletivo da dti digital (Enterprise Hakuna).

O projeto consiste em uma API RESTful completa que gerencia filas de pedidos, realiza a alocação e otimização de rotas por prioridades/distâncias e executa a simulação controlando a máquina de estados do voo.

---

## 🛠️ Tecnologias Utilizadas
* Language: Java 21
* Framework: Spring Boot 3.x
* Build Tool: Maven
* Documentation: OpenAPI 3 / Swagger UI
* Unit Tests: JUnit 5
* Utilities: Lombok, Bean Validation

---

## 🏗️ Arquitetura e Decisões de Design

O projeto adota princípios de Domain-Driven Design (DDD) e arquitetura em camadas, garantindo a separação entre regras puras do domínio e o framework Spring:

Controller (REST) -> Service (Aplicação) -> Domain (Regras Puras)

### Destaques da Solução:
* Algoritmo Guloso (Greedy): Minimiza o número de viagens agrupando pedidos com base na prioridade (HIGH > MEDIUM > LOW) e menor distância até a base (0,0), respeitando capacidade de carga (X kg) e autonomia de voo (Y km).
* Máquina de Estados: Controla o ciclo de vida dos drones (IDLE -> LOADING -> FLYING -> DELIVERING -> RETURNING -> IDLE).
* Gerenciamento em Memória: Dados de estado, fila de pedidos e missões são mantidos em memória durante a simulação conforme documentado no DECISIONS.md.

---

## 📑 Documentação Interativa via Swagger UI

A aplicação conta com interface visual interativa para teste e validação de todos os endpoints.

### Como acessar:
1. Inicie a aplicação (instruções na seção abaixo).
2. Abra seu navegador e acesse: http://localhost:8080/swagger-ui.html

---

## 🌐 Endpoints da API

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| POST | /api/simulacao/pedidos | Cadastra um novo pedido na fila pendente. |
| POST | /api/simulacao/executar | Processa a fila, gera as missões otimizadas e executa a simulação. |
| DELETE | /api/simulacao/pedidos/{id} | Cancela e remove um pedido específico da fila de memória por ID. |
| DELETE | /api/simulacao/pedidos | Limpa toda a fila de pedidos pendentes em memória. |

### Exemplo de Payload para Cadastro de Pedido (POST /api/simulacao/pedidos):
{
  "x": 3.0,
  "y": 4.0,
  "weight": 2.5,
  "priority": "HIGH"
}

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* Java 21 instalado
* Maven (ou o wrapper ./mvnw incluso no projeto)

### 1. Iniciar a Aplicação
No terminal, execute o comando na raiz do projeto:

* Linux / macOS:
  ./mvnw spring-boot:run

* Windows (PowerShell / CMD):
  mvnw spring-boot:run

A API estará pronta para consumo em http://localhost:8080.

### 2. Executar os Testes Unitários
Para rodar a suíte de testes unitários com JUnit 5:

./mvnw test

---

## 📑 Documentações Complementares no Repositório
* ARCHITECTURE.md: Árvore de pacotes, fluxo de dados e responsabilidades.
* DECISIONS.md: Registros de Decisões Arquiteturais (ADRs).
* DOMAIN.md: Mapeamento das regras do negócio, restrições operacionais e Linguagem Ubíqua.

```