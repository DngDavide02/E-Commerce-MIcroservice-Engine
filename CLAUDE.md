# CLAUDE.md

Questo file viene letto automaticamente da Claude Code a ogni sessione di lavoro sul progetto. Contiene il contesto che altrimenti andrebbe rispiegato ogni volta.

---

## Panoramica progetto

E-Commerce Microservices Engine — sistema e-commerce diviso in microservizi (Product, Order, Inventory, Payment Service) con API Gateway e service discovery, per dimostrare gestione della consistenza distribuita (pattern Saga) e comunicazione asincrona a eventi.

## Stack tecnologico

- Java 21, Spring Boot 3.x
- Spring Cloud (Eureka, Spring Cloud Gateway)
- PostgreSQL (un database per servizio)
- RabbitMQ / Apache Kafka per messaggistica asincrona
- Docker + Docker Compose
- JUnit 5 + Testcontainers per i test

## Struttura del repo

```
/product-service
/order-service
/inventory-service
/payment-service
/api-gateway
/eureka-server
docker-compose.yml
```

## Comandi utili

```bash
# Avvio completo in locale
docker-compose up --build

# Build di un singolo servizio
./mvnw clean install -pl product-service

# Test di un singolo servizio
./mvnw test -pl order-service

# Logs di un servizio specifico
docker-compose logs -f order-service
```

## Convenzioni

- Naming eventi: `<Entità><Azione>` al passato, es. `OrderCreated`, `StockReserved`, `PaymentFailed`
- Ogni servizio ha il proprio database — mai accesso diretto al DB di un altro servizio, solo via API o eventi
- DTO separati dalle Entity JPA, mai esporre le Entity direttamente nelle API
- Package base: `com.<tuonome>.<nomeservizio>`

## Regole per l'IA

- Non modificare la logica di orchestrazione della Saga (Order Service) senza prima spiegare il ragionamento — è la parte che devo capire a fondo per i colloqui
- Task piccoli e specifici: preferire "implementa il consumer per l'evento StockReserved" a richieste ampie tipo "fai l'Inventory Service"
- Quando generi codice per un nuovo servizio, segui la stessa struttura (Entity, Repository, Service, Controller, DTO) già usata negli altri servizi
- Se un test esiste già per una funzionalità, trattalo come specifica da rispettare, non da riscrivere
- In caso di dubbio su una decisione architetturale già presa, controllare prima DECISIONS.md invece di riproporre alternative già scartate

## File di riferimento da consultare

- `ARCHITECTURE.md` — decisioni di design e diagrammi di flusso
- `DECISIONS.md` — perché sono state fatte certe scelte tecniche
- `API.md` — contratti REST tra servizi
- `LEARNINGS.md` — problemi già risolti in passato
