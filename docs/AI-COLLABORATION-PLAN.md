# Action Plan for Using AI in the Project

**Project**: E-Commerce Microservices Engine (Distributed Backend)

Goal: use AI as an accelerator without missing the chance to shake off the rust and prepare for interviews. Work is split into 4 phases, each with a different degree of delegation to the AI.

## Status

| Phase | Topic | AI delegation | Status |
|---|---|---|---|
| 1 | Setup & Infrastructure | 90% | In progress — Maven multi-module skeleton done (parent POM + 6 modules, build verified, Eureka boot smoke-tested); `docker-compose.yml` still pending |
| 2 | Base Services (CRUD) | 50% | Not started |
| 3 | Saga Pattern | 30% | Not started |
| 4 | Deploy & Dockerfiles | 80% | Not started |

---

## Phase 1 — Setup and Infrastructure
**AI delegation: 90%**

**What to do**: Ask the AI to generate the multi-module structure (Maven/Gradle), the `application.yml` files for Spring Cloud (Eureka and Gateway), and the entire `docker-compose.yml` with multiple PostgreSQL instances and RabbitMQ/Kafka.

**Why**: There's no point spending time on tedious configuration files that teach nothing new about business logic.

**Your job**: Read the generated files to understand ports, dependencies, and environment variables.

---

## Phase 2 — Building the Base Services
**AI delegation: 50%**

**What to do**: Have the AI write the basic CRUD for Product Service and Inventory Service (Entity, Repository, Controller).

**Your job**: Write by hand (or review line by line) the specific business logic — for example how Inventory reserves or releases quantities — to regain fluency with Java and Spring Boot.

---

## Phase 3 — The Heart of the Project: the Saga Pattern
**You lead at 70%, AI as supervisor**

**What to do**: This is the most important part for interviews. Design the order orchestration logic (PENDING, CONFIRMED, CANCELLED) and compensation handling (rolling back stock if payment fails) yourself. Use the AI only to get suggestions on how to structure the message listeners or to debug when an event doesn't propagate.

**Why**: If the AI writes this part from start to finish without you understanding the async flow, you risk drawing a blank in a technical interview when asked how eventual consistency is handled.

---

## Phase 4 — Deploy and Dockerfiles
**AI delegation: 80%**

**What to do**: Ask the AI to write multi-stage Dockerfiles for the Java services and to help configure healthchecks in Docker Compose, so the microservices only start once their respective PostgreSQL databases are ready.

**Your job**: Run everything locally, verify the system works end-to-end, and prepare the `README.md` (optionally getting AI help to structure the diagrams and the architecture explanation).

---

## Delegation summary

| Phase | Topic | AI delegation | Who leads |
|---|---|---|---|
| 1 | Setup & Infrastructure | 90% | AI |
| 2 | Base Services (CRUD) | 50% | Balanced |
| 3 | Saga Pattern | 30% | You |
| 4 | Deploy & Dockerfiles | 80% | AI |
