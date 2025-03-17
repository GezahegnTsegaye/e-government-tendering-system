# E-Government Tendering System

A comprehensive microservices-based platform for managing government procurement and tendering processes.
# Note: This project is under development there is a lot of work to be done.
## Overview

The E-Government Tendering System is designed to streamline government procurement processes through a modern, secure, and transparent web-based solution. It allows government agencies to publish tenders, vendors to submit bids, and officials to evaluate and award contracts in a consistent and auditable manner.

The platform is built using a microservices architecture to ensure scalability, maintainability, and the ability to evolve individual components independently.

![System Architecture](docs/images/architecture.png)

## Microservices

The system consists of the following microservices:

### Core Services

| Service | Description | Port |
|---------|-------------|------|
| **Discovery Service** | Service registry and discovery using Netflix Eureka | 8761 |
| **Config Service** | Centralized configuration server using Spring Cloud Config | 8888 |
| **Gateway Service** | API gateway using Spring Cloud Gateway | 8080 |
| **Auth Service** | Authentication and authorization service using OAuth2/JWT | 9000 |

### Business Services

| Service | Description | Port |
|---------|-------------|------|
| **User Service** | Manages users, roles, and permissions | 8081 |
| **Tender Service** | Handles tender creation, publication, and management | 8082 |
| **Bidding Service** | Manages the bid submission and tracking process | 8083 |
| **Contract Service** | Handles contract creation and management after bid selection | 8084 |
| **Document Service** | Manages document uploads, storage, and retrieval | 8085 |
| **Notification Service** | Handles system notifications through various channels | 8086 |
| **Evaluation Service** | Supports bid evaluation processes and decision-making | 8087 |
| **Audit Service** | Tracks and logs all critical system actions | 8088 |

## Technical Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.2.x, Spring Cloud 2023.0.x
- **Build Tool**: Maven
- **Database**: MySQL 8.0
- **Messaging**: Apache Kafka
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Authentication**: OAuth 2.0 with JWT
- **Documentation**: OpenAPI 3 (Swagger)
- **Containerization**: Docker, Docker Compose
- **Testing**: JUnit 5, Mockito, Testcontainers
- **CI/CD**: GitHub Actions (or your preferred CI/CD tool)

## System Requirements

- Java 17 or higher
- Maven 3.8.x or higher
- Docker and Docker Compose (for containerized deployment)
- MySQL 8.0
- Kafka 3.x

## Project Structure

```
e-government-tendering-system/
├── common-util/                  # Shared libraries and utilities
├── config-service/              # Centralized configuration service
├── discovery-service/           # Service registry and discovery
├── gateway-service/             # API gateway
├── user-service/                # User management service
├── tender-service/              # Tender management service
├── bidding-service/             # Bid management service
├── contract-service/            # Contract management service
├── document-service/            # Document management service
├── notification-service/        # Notification service
├── audit-service/               # Audit logging service
├── evaluation-service/          # Bid evaluation service
├── docker-compose.yml           # Docker Compose for local deployment
└── pom.xml                      # Parent POM file
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8.x or higher
- Docker and Docker Compose (for containerized deployment)

### Building the Project

```bash
# Clone the repository
git clone https://github.com/your-org/e-government-tendering-system.git
cd e-government-tendering-system

# Build the entire project
mvn clean install
```

### Running Locally with Docker Compose // we will include this part in the feature

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f [service-name]

# Stop all services
docker-compose down
```

### Running Individual Services

```bash
cd service-name
mvn spring-boot:run
```

Note: For local development, you need to start the Core Services (Discovery, Config, Gateway) before starting the Business Services.

## Development Workflow

1. **Setup**: Clone the repository and build the project
2. **Core Services**: Start the Discovery, Config, and Gateway services
3. **Database**: Ensure MySQL is running and the required databases are created
4. **Kafka**: Start Kafka for event processing
5. **Business Services**: Start the required business services for your development task
6. **Testing**: Use Swagger UI for API testing or write automated tests

## API Documentation

Each service exposes its API documentation via Swagger UI at:
```
http://localhost:<service-port>/swagger-ui.html
```

To view documentation for all services through the Gateway:
```
http://localhost:8080/swagger-ui.html
```

## Configuration

The configuration for all services is centralized in the Config Service. The configuration files are stored in:
```
config-service/src/main/resources/config
```

Each service has its own configuration file named `<service-name>.yml`. Common configuration is defined in `application.yml`.

## Security

The system uses OAuth 2.0 with JWT for authentication and authorization. The Auth Service is responsible for issuing and validating JWT tokens.

User roles:
- **ADMIN**: System administrators with full access
- **PROCUREMENT_OFFICER**: Government officials who create and manage tenders
- **EVALUATION_COMMITTEE**: Officials who evaluate bids
- **VENDOR**: Organizations that submit bids
- **AUDITOR**: Users who can view all activities but not modify anything

## Event-Driven Architecture

The system uses Kafka for event-driven communication between services. Key events include:
- Tender created/updated/published
- Bid submitted/updated/withdrawn
- Contract awarded/signed
- Document uploaded/verified
- Notification events

## Database Schema

Each service manages its own database schema. The database migrations are handled using Flyway and are located in:
```
<service-name>/src/main/resources/db/migration
```

## Monitoring and Observability

The system exposes metrics via Spring Boot Actuator and can be integrated with:
- Prometheus for metrics collection
- Grafana for dashboards
- ELK Stack for log aggregation

## Deployment

### Docker Deployment // we will include this part in the feature

The `docker-compose.yml` file in the root directory can be used for local deployment. For production deployment, consider using Kubernetes.

### Kubernetes Deployment // we will include this part in the feature

Kubernetes manifests are provided in the `k8s/` directory for deploying the system on a Kubernetes cluster.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Based on research by Simon Fong and Zhuang Yan on "Design of a Web-based Tendering System for e-Government Procurement"
- Inspired by best practices in e-Government systems worldwide

## Contact

For questions or support, please contact me through my GitHub: [https://github.com/GezahegnTsegaye/e-government-tendering-system](https://github.com/GezahegnTsegaye/e-government-tendering-system)