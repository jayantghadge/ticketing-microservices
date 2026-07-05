# Ticketing Microservices

An event ticketing platform built with a microservices architecture using Spring Boot 3.5, secured with OAuth2/Keycloak, and orchestrated with asynchronous event-driven communication via Apache Kafka.

## Architecture

```
                    ┌─────────────┐
                    │   Keycloak  │
                    │  (Auth Svr) │
                    └──────┬──────┘
                           │
                    ┌──────▼──────┐
                    │  API Gateway │
                    │   (Port 8090)│
                    └──┬───┬───┬──┘
                       │   │   │
              ┌────────┘   │   └────────┐
              ▼            ▼            ▼
      ┌──────────┐ ┌──────────┐ ┌──────────┐
      │Inventory │ │  Booking │ │  Order   │
      │ Service  │ │  Service │ │  Service │
      │ (Port8080)│ │ (Port8081)│ │ (Port8082)│
      └────┬─────┘ └────┬─────┘ └────┬─────┘
           │            │            │
           └────────────┼────────────┘
                        │
                  ┌─────▼──────┐
                  │  Kafka     │
                  │  (Events)  │
                  └────────────┘
```

## Services

| Service | Port | Description | Tech |
|---|---|---|---|
| **apigateway** | 8090 | API Gateway — routes requests, enforces JWT auth (Keycloak) | Spring Cloud Gateway MVC, OAuth2 Resource Server |
| **inventoryservice** | 8080 | Event & venue inventory CRUD, capacity management | Spring Data JPA, Flyway, MySQL |
| **bookingservice** | 8081 | Handles ticket booking requests, price calculation | Spring Data JPA, Kafka Producer |
| **orderservice** | 8082 | Consumes booking events from Kafka, persists orders | Spring Data JPA, Kafka Consumer |

## Tech Stack

- **Java 21** + **Spring Boot 3.5.8**
- **Spring Cloud Gateway** (MVC variant) — API Gateway with route definitions
- **Spring Security** + **OAuth2 Resource Server** — JWT authentication via Keycloak
- **Spring Data JPA** + **MySQL 8** — persistence
- **Apache Kafka** — asynchronous event-driven communication between services
- **Flyway** — database migration management
- **SpringDoc OpenAPI** — Swagger UI documentation
- **Project Lombok** — boilerplate reduction
- **Maven** — build tool
- **Docker Compose** — infrastructure orchestration (MySQL, Kafka, Keycloak, etc.)
- **JaCoCo** — test coverage (90%+ across all services)

## Prerequisites

- Java 21+
- Maven 3.9+
- Docker & Docker Compose (for infrastructure)
- IntelliJ IDEA (recommended)

## Getting Started

### 1. Start Infrastructure

```bash
cd inventoryservice
docker compose up -d
```

This starts:
- **MySQL** (port 3307) — ticketing database
- **Zookeeper** (port 2181)
- **Kafka Broker** (port 9092)
- **Kafka UI** (port 8084)
- **Schema Registry** (port 8083)
- **Keycloak** (port 8091) — with pre-imported realm config
- **Keycloak MySQL** — dedicated database for Keycloak

### 2. Start Services

Run each service in a separate terminal:

```bash
# Terminal 1 — Inventory Service
cd inventoryservice && mvn spring-boot:run

# Terminal 2 — Booking Service
cd bookingservice && mvn spring-boot:run

# Terminal 3 — Order Service
cd orderservice && mvn spring-boot:run

# Terminal 4 — API Gateway
cd apigateway && mvn spring-boot:run
```

### 3. Access

| Component | URL |
|---|---|
| API Gateway (Swagger UI) | http://localhost:8090/swagger-ui.html |
| Inventory Service APIs | http://localhost:8080/swagger-ui.html |
| Booking Service APIs | http://localhost:8081/swagger-ui.html |
| Kafka UI | http://localhost:8084 |
| Keycloak Admin Console | http://localhost:8091 |

## API Endpoints

### Inventory Service (`/api/v1`)

| Method | Path | Description |
|---|---|---|
| GET | `/inventory/events` | List all events |
| GET | `/inventory/event/{eventId}` | Get event inventory |
| GET | `/inventory/venue/{venueId}` | Get venue details |
| PUT | `/inventory/event/{eventId}/capacity/{ticketsBooked}` | Update remaining capacity |

### Booking Service (`/api/v1`)

| Method | Path | Description |
|---|---|---|
| POST | `/booking` | Create a booking (validates user & inventory, publishes to Kafka) |

### API Gateway Routes

| Path | Target |
|---|---|
| `/api/v1/inventory/**` | Inventory Service (port 8080) |
| `/api/v1/booking` | Booking Service (port 8081) |
| `/docs/inventoryservice/v3/api-docs` | Swagger docs proxy |
| `/docs/bookingservice/v3/api-docs` | Swagger docs proxy |

## Event Flow (Booking -> Order)

```
Client -> POST /api/v1/booking
          │
          ▼
  BookingService
    ├─ Validates user via CustomerRepository
    ├─ Checks inventory via InventoryServiceClient
    ├─ Computes total price
    └─ Publishes BookingEvent to Kafka topic "booking"
          │
          ▼
  OrderService (Kafka Consumer)
    ├─ Creates Order record
    └─ Calls InventoryService to decrement capacity
```

## Security

Authentication is handled via **Keycloak** (OpenID Connect). The API Gateway validates JWT tokens for all requests except public endpoints (Swagger UI, API docs).

To obtain a token:

```bash
curl -X POST http://localhost:8091/realms/ticketing-security-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=<client-id>" \
  -d "username=<user>" \
  -d "password=<pass>" \
  -d "grant_type=password"
```

## Database Migrations (Flyway)

Located in `inventoryservice/src/main/resources/db/migration/`:

| File | Description |
|---|---|
| `V1__init.sql` | Creates `venue` and `event` tables |
| `V2__add_ticket_column_in_event_table.sql` | Adds `ticket_price` column |
| `V3__create_customer_table.sql` | Creates `customer` table |
| `V4__create_order_table.sql` | Creates `order` table |

## Testing

Each service has unit tests with **90%+ line coverage** verified by JaCoCo.

```bash
# Run tests for a specific service
cd <service-dir> && mvn test

# Run tests with coverage report
cd <service-dir> && mvn clean test jacoco:report
# Report available at target/site/jacoco/index.html
```

### Test Coverage

| Service | Coverage |
|---|---|
| inventoryservice | 96.2% |
| apigateway | 92.1% |
| orderservice | 92.0% |
| bookingservice | 90.9% |
