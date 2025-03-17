# E-Government Tendering System - Modular Docker Setup

This document provides instructions for running the E-Government Tendering System using a modular Docker Compose setup. Each service has its own Docker Compose file, allowing for greater flexibility and maintainability.

## Prerequisites

- Docker and Docker Compose installed
- Java 17 for building the services
- Maven for building the services

## Project Structure

The Docker setup consists of multiple files:

```
e-government-tendering-system/
├── docker-compose.base.yml        # Base network and volume definitions
├── docker-compose.infra.yml       # Infrastructure services (MySQL)
├── docker-compose.kafka.yml       # Kafka and Zookeeper services
├── docker-compose.core.yml        # Core services (discovery, config, gateway)
├── docker-compose.user.yml        # User service
├── docker-compose.tender.yml      # Tender service
├── docker-compose.bidding.yml     # Bidding service
├── docker-compose.evaluation.yml  # Evaluation service
├── docker-compose.contract.yml    # Contract service
├── docker-compose.document.yml    # Document service
├── docker-compose.notification.yml # Notification service
├── docker-compose.audit.yml       # Audit service
├── run.sh                         # Helper script for running services
├── init/
│   └── init-databases.sql         # Database initialization script
└── common-util/
    └── src/main/java/com/egov/tendering/common/config/
        └── KafkaDisableConfig.java # Common configuration for disabling Kafka
```

## Kafka Integration

To make Kafka integration optional, add the `KafkaDisableConfig.java` to your common-util module and include this dependency in all services. Then, in each service's `application.yml`, add:

```yaml
app:
  kafka:
    enabled: ${APP_KAFKA_ENABLED:false}
```

This will automatically disable Kafka when the environment variable `APP_KAFKA_ENABLED` is false or not set.

## Running the System

The included `run.sh` script provides a convenient way to start services with flexible combinations.

### Basic Usage

```bash
# Make the script executable
chmod +x run.sh

# Show help
./run.sh --help

# Run infrastructure and core services
./run.sh -i -c

# Run with Kafka enabled
./run.sh -i -c -k

# Run specific services with infrastructure and core
./run.sh -i -c user bidding

# Run everything
./run.sh -a

# Run everything with Kafka
./run.sh -a -k

# Stop all services
./run.sh -d
```

### Available Options

- `-h, --help`: Show help message
- `-k, --kafka`: Enable Kafka
- `-i, --infra`: Include infrastructure (MySQL)
- `-c, --core`: Include core services (discovery, config, gateway)
- `-a, --all`: Run all services
- `-d, --down`: Stop services instead of starting them

You can also specify individual services by name: `user`, `tender`, `bidding`, `contract`, `document`, `notification`, `audit`, `evaluation`.

## Service Dependencies

The Docker Compose files define the correct service dependencies to ensure they start in the right order:

1. Infrastructure (MySQL)
2. Core services (discovery, config, gateway)
3. User service
4. Other business services, with appropriate dependencies between them

This ensures proper startup sequence without unnecessary waiting.

## Customizing for Development

For development, you might want to run only specific services. For example:

```bash
# Run infrastructure, core, and bidding service with Kafka for event testing
./run.sh -i -c -k bidding

# Run just infrastructure and user service for authentication testing
./run.sh -i -c user
```

## Volumes and Persistence

The setup includes persistent volumes for:

- `mysql_data`: MySQL database files
- `document_uploads`: Files uploaded to the document service
- `bid_uploads`: Files uploaded to the bidding service

These volumes persist data between container restarts.

## Networking

All services are connected to the `egov-network` bridge network, allowing them to communicate with each other using service names as hostnames.

## Troubleshooting

If you encounter issues:

1. Check the logs: `docker-compose -f docker-compose.base.yml -f [service-files] logs -f`
2. Ensure all required images are built
3. Verify network connectivity between services
4. Check that volumes have appropriate permissions
5. Ensure the MySQL initialization script ran correctly

For Kafka-related issues, make sure the `app.kafka.enabled` property is correctly set in your services.