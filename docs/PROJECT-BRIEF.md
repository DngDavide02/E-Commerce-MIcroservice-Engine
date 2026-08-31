# E-Commerce Microservices Engine (Distributed Backend)

## Progress status

Updated as the project moves forward — reflects the actual state of the code in the repo, not just the plan.

- [x] **Phase 0 — Setup**: Maven multi-module skeleton (parent POM + 6 modules: `eureka-server`, `api-gateway`, `product-service`, `order-service`, `inventory-service`, `payment-service`), each with an `Application` class and `application.yml` (dedicated port, Eureka client, PostgreSQL datasource, RabbitMQ). Build verified with `./mvnw clean install`; Eureka Server smoke-tested with a real boot (responds on `:8761`).
- [x] **Phase 0 — Setup**: `docker-compose.yml` with 4 separate PostgreSQL instances (one per service, mapped to host ports 5433-5436) + RabbitMQ (with management UI on `:15672`), all with healthchecks. YAML validated with `docker compose config`; **not yet runtime-tested** — the sandbox this was written in can't start the Docker daemon, so run `docker-compose up -d` locally to confirm before relying on it.
- [x] **Phase 1 — Product Service**: CRUD (Entity, DTOs via records, Repository, Service, Controller with `/api/products`), Bean Validation, centralized exception handling (404/400). 3 unit tests passing (`ProductServiceTest`). A Testcontainers integration test (`ProductRepositoryIT`) is written but excluded from the default `test` run (surefire only picks up `*Test` classes) — run it explicitly with `-Dtest=ProductRepositoryIT` once Docker is available locally.
- [ ] **Phase 2 — Inventory Service**: stock model, reserve/release, event consumer
- [ ] **Phase 3 — Payment Service**: payment authorization simulation
- [ ] **Phase 4 — Order Service + Saga orchestration**: order states, compensation logic
- [ ] **Phase 5 — API Gateway**: routing to Eureka-discovered services already configured (discovery locator); rate limiting and centralized authentication still missing
- [ ] **Phase 6 — Polish**: distributed tracing, broker dashboard, README with diagrams, end-to-end demo

Message broker chosen: **RabbitMQ** (simpler to configure than Kafka, sufficient for the event volume this project needs).

---

## 1. Project overview

An e-commerce backend built with a microservices architecture, designed to demonstrate the skills required by structured tech companies in the Netherlands: distributed data consistency management, asynchronous event-driven communication, service discovery, and API routing.

Unlike a monolith, each service is independent, owns its own database, and communicates with the others via synchronous APIs (REST/gRPC) or asynchronous events (message broker).

**Project goal**: not a "complete" e-commerce system for end users, but a technical showcase of distributed patterns — this is a portfolio/CV project, so feature depth matters less than architectural quality.

---

## 2. System architecture

```
                        ┌─────────────────┐
                        │   API Gateway    │  (Spring Cloud Gateway)
                        └────────┬─────────┘
                                 │
                ┌────────────────┼────────────────┐
                │                │                 │
        ┌───────▼──────┐ ┌───────▼──────┐  ┌───────▼──────┐
        │Product Service│ │ Order Service │  │Payment Service│
        │ (PostgreSQL)  │ │ (PostgreSQL)  │  │ (PostgreSQL)  │
        └───────┬───────┘ └───────┬───────┘  └───────┬───────┘
                │                 │                   │
                └────────┬────────┴─────────┬─────────┘
                          │  RabbitMQ/Kafka  │
                          └───────────────────┘
                                 │
                        ┌────────▼─────────┐
                        │ Inventory Service │
                        │   (PostgreSQL)    │
                        └───────────────────┘

        ┌─────────────────┐        ┌─────────────────┐
        │  Eureka Server   │        │  Config Server   │
        │(Service Discovery)│      │  (optional)      │
        └─────────────────┘        └─────────────────┘
```

### Core microservices

| Service | Responsibility | Database |
|---|---|---|
| **Product Service** | Product catalog, prices, categories | PostgreSQL (`product_db`) |
| **Order Service** | Order creation and management, Saga orchestration | PostgreSQL (`order_db`) |
| **Inventory Service** | Stock availability, reserve/release quantities | PostgreSQL (`inventory_db`) |
| **Payment Service** | Payment simulation, authorization/refund | PostgreSQL (`payment_db`) |
| **API Gateway** | Routing, rate limiting, centralized authentication | — |
| **Eureka Server** | Service discovery/registry | — |

Each service has its **own database** (Database-per-Service pattern): no service accesses another service's DB directly, only via API or events.

---

## 3. Tech stack

- **Language**: Java 21 (uses modern features: records, pattern matching, virtual threads if you want to push further)
- **Framework**: Spring Boot 3.x
- **Spring Cloud**: Eureka (discovery), Spring Cloud Gateway (API Gateway), optional Config Server
- **Database**: PostgreSQL, one instance/schema per service
- **Messaging**: RabbitMQ (chosen — simpler to configure than Kafka, sufficient for this use case)
- **Synchronous communication**: REST (OpenFeign for service-to-service calls)
- **Containerization**: Docker + Docker Compose (to orchestrate all services and DBs locally)
- **Testing**: JUnit 5, Testcontainers (for integration tests against real DB/broker instances)
- **Observability (optional, strong plus)**: Spring Boot Actuator + Zipkin/Sleuth for distributed tracing

---

## 4. The Saga pattern (the heart of the project)

The core microservices problem: how do you guarantee consistency across different databases without distributed ACID transactions?

**Solution: Saga Pattern (choreography or orchestration)**

Recommended: **Orchestrated Saga**, easier to explain in an interview and easier to debug.

### Example flow: order creation

1. `Order Service` receives an order request → creates the order in `PENDING` state → publishes `OrderCreated`
2. `Inventory Service` consumes `OrderCreated` → checks/reserves stock → publishes `StockReserved` or `StockRejected`
3. `Payment Service` consumes `StockReserved` → authorizes payment → publishes `PaymentCompleted` or `PaymentFailed`
4. `Order Service` consumes the final outcome → updates the order to `CONFIRMED` or `CANCELLED`

**Compensation (distributed rollback)**: if payment fails after stock has already been reserved, `Order Service` must publish a `ReleaseStock` event that Inventory Service consumes to cancel the reservation. This is the part that demonstrates real understanding of the pattern.

---

## 5. Implementation roadmap (step-by-step)

### Phase 0 — Setup (days 1-2)
- Multi-module structure (or separate repos) with Maven/Gradle
- Docker Compose with PostgreSQL (separate instances), RabbitMQ/Kafka
- Minimal working Eureka Server

### Phase 1 — Product Service (days 3-5)
- Product CRUD, REST API, validation
- Eureka connection
- Unit tests + Testcontainers-based integration tests

### Phase 2 — Inventory Service (days 6-8)
- Per-product stock model
- Reserve/release quantity endpoints
- Consumer for events from Order Service

### Phase 3 — Payment Service (days 9-10)
- Payment authorization simulation (mock, no real gateway)
- Event consumer/producer

### Phase 4 — Order Service + Saga orchestration (days 11-15)
- Order model with states (PENDING, CONFIRMED, CANCELLED)
- Saga orchestration logic
- Compensation handling

### Phase 5 — API Gateway (days 16-17)
- Spring Cloud Gateway with routing to the services
- Basic rate limiting, optional centralized JWT authentication

### Phase 6 — Polish and interview "wow factor" (days 18-21)
- Distributed tracing (Zipkin) to visualize the Saga flow
- RabbitMQ/Kafka dashboard to show messages in transit
- README with sequence diagrams
- Postman collection or demo script to show the end-to-end flow

---

## 6. Time estimate

Assumptions: working solo, already solid experience in Spring Boot (bootcamp + work experience), part-time after work hours.

| Scenario | Estimated time | Notes |
|---|---|---|
| **Minimal working version** (4 base services + Eureka + synchronous communication, no full Saga) | **2-3 weeks** part-time (~10-12h/week) | Already good to show "I can work with microservices" |
| **Complete version** (Saga with compensation, Kafka/RabbitMQ, API Gateway, Docker Compose) | **4-6 weeks** part-time | The level described in the original brief |
| **"Advanced interview" version** (+ distributed tracing, end-to-end tests, CI/CD, cloud deployment) | **7-9 weeks** part-time | Needed if you want this as your absolute flagship project |

If you can work on it full-time (e.g. intensive weekends, vacation days), the timeline can shrink to roughly **1/3**: 1 week for the minimal version, 2 weeks for the complete one.

**Practical advice**: aim first for a minimal version that works end-to-end (even with just one real asynchronous event, like `OrderCreated → StockReserved`), then add complexity. A simple system that *actually works* with Docker Compose and a clear README is worth more than an ambitious but incomplete project.

---

## 7. What to highlight in the README/interview

- Why Database-per-Service instead of a shared DB
- How you handle eventual consistency instead of ACID transactions
- What happens if a service is down while an event is being published (retry, dead-letter queue)
- Difference between orchestrated and choreographed Saga, and why you chose the one you did
- How the Gateway centralizes cross-cutting concerns (auth, rate limiting, logging)
