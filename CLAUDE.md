# CLAUDE.md

This file is automatically read by Claude Code at the start of every work session on the project. It contains context that would otherwise need to be re-explained each time.

---

## Project overview

E-Commerce Microservices Engine — an e-commerce system split into microservices (Product, Order, Inventory, Payment Service) with an API Gateway and service discovery, built to demonstrate distributed consistency management (Saga pattern) and asynchronous event-driven communication.

## Tech stack

- Java 21, Spring Boot 3.x
- Spring Cloud (Eureka, Spring Cloud Gateway)
- PostgreSQL (one database per service)
- RabbitMQ / Apache Kafka for asynchronous messaging
- Docker + Docker Compose
- JUnit 5 + Testcontainers for testing

## Repo structure

```
/product-service
/order-service
/inventory-service
/payment-service
/api-gateway
/eureka-server
docker-compose.yml
```

## Useful commands

```bash
# Full local startup
docker-compose up --build

# Build a single service
./mvnw clean install -pl product-service

# Test a single service
./mvnw test -pl order-service

# Logs for a specific service
docker-compose logs -f order-service
```

## Conventions

- Event naming: `<Entity><Action>` in past tense, e.g. `OrderCreated`, `StockReserved`, `PaymentFailed`
- Each service owns its own database — no direct access to another service's DB, only via API or events
- DTOs are separate from JPA Entities, never expose Entities directly in the API
- Base package: `com.<yourname>.<servicename>`

## Rules for the AI

- Do not modify the Saga orchestration logic (Order Service) without first explaining the reasoning — this is the part I need to fully understand for interviews
- Small, specific tasks: prefer "implement the consumer for the StockReserved event" over broad requests like "build the Inventory Service"
- When generating code for a new service, follow the same structure (Entity, Repository, Service, Controller, DTO) already used in the other services
- If a test already exists for a feature, treat it as the spec to satisfy, not something to rewrite
- When in doubt about an architectural decision that's already been made, check DECISIONS.md first instead of re-proposing alternatives that were already discarded

## Reference files to consult

- `ARCHITECTURE.md` — design decisions and flow diagrams
- `DECISIONS.md` — why certain technical choices were made
- `API.md` — REST contracts between services
- `LEARNINGS.md` — problems already solved in the past
