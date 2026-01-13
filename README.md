# Ticketing Microservices

A ticketing system built using Spring Boot microservices.
The system uses Kafka for asynchronous communication, MySQL for persistence, and Keycloak for authentication with an API Gateway in front.

---

## Services

| Service | Port | Responsibility |
|-------|------|----------------|
| Inventory Service | 8080 | Manages ticket inventory |
| Booking Service | 8081 | Handles booking requests |
| Order Service | 8082 | Processes booking events |
| API Gateway | 8090 | Routes APIs and applies security |

---

## Tech Stack

- Java 21
- Spring Boot
- Apache Kafka
- MySQL
- Keycloak
- Docker & Docker Compose
- Swagger (Springdoc)

---

## Running Locally

### Prerequisites
- Java 21
- Maven
- Docker & Docker Compose

### Clone the repository
```bash
git clone git@github.com:jayantghadge/ticketing-microservices.git
cd ticketing-microservices
```

### Create environment file
```bash
cp .env.example .env
```

Update values if required.

### Start infrastructure
```bash
docker compose up -d
```

This starts MySQL, Kafka, Kafka UI, Schema Registry, and Keycloak.

### Run services
From each service directory:
```bash
mvn spring-boot:run
```

---

## API Documentation

- Inventory Service: http://localhost:8080/swagger-ui.html
- Booking Service: http://localhost:8081/swagger-ui.html
- Order Service: http://localhost:8082/swagger-ui.html
- API Gateway (combined): http://localhost:8090/swagger-ui.html

---

## Authentication

- Keycloak URL: http://localhost:8091
- Realm: ticketing-security-realm
- Credentials are configured via environment variables

---

## Project Structure

```
ticketing-microservices/
├── booking-service/
├── inventory-service/
├── order-service/
├── api-gateway/
├── docker-compose.yml
├── .env.example
└── README.md
```

Each service is independent and contains its own pom.xml and configuration.

---

## Notes

- Kafka is used for event-driven communication
- API Gateway integrates with Keycloak for security
- No secrets are committed (.env is ignored)
