# Gateway Service for E-Government Tendering System

The Gateway Service is the entry point for all client requests to the E-Government Tendering System. It provides a unified interface for clients while routing requests to appropriate microservices, handling cross-cutting concerns, and providing security.

## Features

- **API Routing**: Routes requests to the appropriate microservices
- **Load Balancing**: Distributes traffic across service instances
- **Security**: Handles authentication and authorization
- **Rate Limiting**: Prevents abuse of the API
- **Request Logging**: Logs all requests for auditing and debugging
- **Circuit Breaking**: Prevents cascading failures
- **Request Tracking**: Adds correlation IDs for distributed tracing
- **CORS Support**: Enables cross-origin resource sharing

## Architecture

The Gateway Service is built on Spring Cloud Gateway and provides:

- **Dynamic Routing**: Routes are defined in configuration or discovered via Eureka
- **JWT Authentication**: OAuth2 resource server with JWT validation
- **Resilience Patterns**: Circuit breaker, retry, and timeout patterns
- **Redis-based Rate Limiting**: Distributed rate limiting with Redis
- **Global Filters**: Apply cross-cutting concerns to all routes

## Technical Stack

- **Framework**: Spring Cloud Gateway on Spring Boot 3.x
- **Service Discovery**: Netflix Eureka Client
- **Configuration**: Spring Cloud Config Client
- **Security**: Spring Security with OAuth2 Resource Server
- **Circuit Breaker**: Resilience4j
- **Rate Limiting**: Redis Rate Limiter
- **Monitoring**: Micrometer and Spring Boot Actuator
- **Containerization**: Docker

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.8.x or higher
- Redis (for rate limiting)

### Building the Project

```bash
mvn clean install
```

### Running Locally

```bash
mvn spring-boot:run
```

### Configuration

The Gateway Service can be configured through the following files:

- `bootstrap.yml`: Configuration server and service discovery settings
- `application.yml`: Main configuration including route definitions
- Environment variables for sensitive information

#### Key Configuration Properties

- `spring.cloud.gateway.routes`: Route definitions
- `spring.security.oauth2.resourceserver.jwt.issuer-uri`: JWT issuer URI
- `resilience4j.circuitbreaker`: Circuit breaker settings
- `resilience4j.timelimiter`: Timeout settings
- `app.security.allowed-public-paths`: Paths that don't require authentication

### API Routes

The Gateway Service routes requests to the following microservices:

- `/api/v1/users/**` → User Service
- `/api/v1/tenders/**` → Tender Service
- `/api/v1/bids/**` → Bidding Service
- `/api/v1/contracts/**` → Contract Service
- `/api/v1/documents/**` → Document Service
- `/api/v1/notifications/**` → Notification Service
- `/api/v1/evaluations/**` → Evaluation Service
- `/api/v1/audit/**` → Audit Service

### Security

All routes require authentication except for:

- `/api/v1/auth/**`: Authentication endpoints
- `/api/v1/public/**`: Public endpoints
- `/api/v1/users/register`, `/api/v1/users/activate`: User registration
- `/actuator/**`: Actuator endpoints
- `/v3/api-docs/**`, `/swagger-ui/**`: API documentation

### Monitoring

The Gateway Service exposes several endpoints for monitoring:

- `/actuator/health`: Health information
- `/actuator/info`: Application information
- `/actuator/prometheus`: Metrics for Prometheus

## Docker Deployment

Build the Docker image:

```bash
docker build -t gateway-service .
```

Run the container:

```bash
docker run -p 8080:8080 gateway-service
```

## Using Docker Compose

The service can be started as part of the entire application using:

```bash
docker-compose up -d
```

## Troubleshooting

### Common Issues

1. **Connection to Config Server fails**: Ensure the Config Server is running and the `spring.cloud.config.uri` is correct.

2. **Service discovery issues**: Check that Eureka Server is running and the Gateway can reach it.

3. **JWT validation errors**: Verify the JWT issuer URI and that the JWT tokens are valid.

4. **Rate limiting not working**: Ensure Redis is running and properly configured.

## Contributing

Please see the [CONTRIBUTING.md](../CONTRIBUTING.md) file for details on contributing to this project.

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## Contact

For questions or support, please contact me through my GitHub: [https://github.com/GezahegnTsegaye/e-government-tendering-system](https://github.com/GezahegnTsegaye/e-government-tendering-system)

-