# E-Government Tendering System: Evaluation Service

## Overview

The Evaluation Service is a critical component of the E-Government Tendering System, responsible for evaluating bids, selecting winners, and allocating contractMilestones based on various strategies. This microservice implements the contractMilestone selection algorithms described in academic research papers on web-based tendering systems.

## Key Features

- **Bid Evaluation**: Score and rank tender offers based on various criteria
- **Contract Selection**: Implement three different winner selection strategies:
    - **Single Winner**: Select one tenderer with the highest overall score
    - **Cooperative Allocation**: Select different tenderers for different items based on expertise
    - **Competitive Allocation**: Select multiple tenderers based on a cut-off score
- **Committee Review**: Manage approvals from committee members for tender evaluations
- **Ranking System**: Calculate and manage bid rankings with support for different scoring methods
- **Allocation Management**: Allocate items to winners using various strategies with optimization for edge cases

## Technical Architecture

### Technologies

- Java 17
- Spring Boot 3.2.2
- Spring Cloud (Netflix Eureka, OpenFeign, Config)
- Spring Data JPA
- Kafka for event-driven communication
- MySQL for persistence

### Modules and Components

#### Domain Models
- `Evaluation`: Represents an evaluation of a bid by an evaluator
- `CriteriaScore`: Represents a score for a specific criteria in an evaluation
- `TenderRanking`: Represents the final ranking of bids for a tender
- `AllocationResult`: Represents the allocation of items to winners
- `CommitteeReview`: Represents a review by a committee member

#### Service Layer
- `EvaluationService`: Handles bid evaluation and scoring
- `TenderRankingService`: Calculates bid rankings
- `AllocationService`: Implements the three contractMilestone selection strategies
- `CommitteeReviewService`: Manages committee reviews

#### REST Controllers
- `EvaluationController`: API endpoints for evaluation operations
- `CommitteeReviewController`: API endpoints for committee review operations

#### Feign Clients
- `TenderClient`: For accessing tender data
- `BidClient`: For accessing bid data
- `UserClient`: For accessing user data

#### Event Handling
- `EvaluationEventPublisher`: For publishing Kafka events
- Event payload classes for various evaluation-related events

## Contract Selection Algorithms

### Single Winner Selection
The tenderer with the highest overall score is selected to supply all items. In case of a tie, a winner is randomly selected.

### Cooperative Allocation
For each item, the tenderer with the highest score for that specific item is selected. This allows for specialization, where different suppliers can be selected for different items based on their expertise.

### Competitive Allocation
Multiple tenderers with scores above a cut-off point are selected, and items are allocated among them using either:
- **Average Allocation**: Items are distributed evenly among winners
- **Ratio Allocation**: Items are distributed proportionally based on scores

## API Endpoints

### Evaluation APIs
- `POST /api/evaluations/tenders/{tenderId}`: Create a new evaluation
- `GET /api/evaluations/{evaluationId}`: Get an evaluation by ID
- `PUT /api/evaluations/{evaluationId}`: Update an evaluation
- `PATCH /api/evaluations/{evaluationId}/status`: Update evaluation status
- `DELETE /api/evaluations/{evaluationId}`: Delete an evaluation
- `GET /api/evaluations/tenders/{tenderId}`: Get all evaluations for a tender
- `GET /api/evaluations/bids/{bidId}`: Get all evaluations for a bid
- `POST /api/evaluations/tenders/{tenderId}/results`: Calculate tender results

### Committee Review APIs
- `POST /api/reviews/tenders/{tenderId}`: Create a new review
- `GET /api/reviews/{reviewId}`: Get a review by ID
- `PUT /api/reviews/{reviewId}`: Update a review
- `DELETE /api/reviews/{reviewId}`: Delete a review
- `GET /api/reviews/tenders/{tenderId}`: Get all reviews for a tender
- `GET /api/reviews/tenders/{tenderId}/status/{status}`: Get reviews for a tender with specific status
- `GET /api/reviews/members/{committeeMemberId}`: Get all reviews by a committee member
- `GET /api/reviews/tenders/{tenderId}/members/{committeeMemberId}`: Get a review by tender and committee member
- `GET /api/reviews/tenders/{tenderId}/approved`: Check if tender is approved

### Ranking APIs
- `POST /api/rankings/tenders/{tenderId}/calculate`: Calculate bid rankings
- `GET /api/rankings/tenders/{tenderId}`: Get all rankings for a tender
- `GET /api/rankings/tenders/{tenderId}/winners`: Get winning bids for a tender
- `DELETE /api/rankings/tenders/{tenderId}`: Clear all rankings for a tender

### Allocation APIs
- `POST /api/allocations/tenders/{tenderId}/single`: Allocate using single winner strategy
- `POST /api/allocations/tenders/{tenderId}/cooperative`: Allocate using cooperative strategy
- `POST /api/allocations/tenders/{tenderId}/competitive`: Allocate using competitive strategy
- `GET /api/allocations/tenders/{tenderId}`: Get all allocations for a tender
- `GET /api/allocations/bids/{bidId}`: Get all allocations for a bid
- `DELETE /api/allocations/tenders/{tenderId}`: Clear all allocations for a tender

## Event-Driven Integration

The service publishes events to the following Kafka topics:
- `evaluation-created`: When a new evaluation is created
- `evaluation-updated`: When an evaluation is updated
- `evaluation-status-changed`: When an evaluation's status changes
- `evaluation-deleted`: When an evaluation is deleted
- `tender-evaluation-completed`: When a tender evaluation is completed
- `tender-evaluation-approved`: When a tender evaluation is approved by committee
- `review-created`: When a new review is created
- `review-updated`: When a review is updated
- `review-deleted`: When a review is deleted

## Deployment

This service is designed to work as part of a microservice architecture and should be deployed alongside:
- Config Service
- Discovery Service
- Gateway Service
- Tender Service
- Bidding Service
- User Service
- common-util

## Configuration

The service uses Spring Cloud Config for externalized configuration. Sample configuration properties include:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/egov_evaluation
    username: root
    password: password
  kafka:
    bootstrap-servers: localhost:9092

app:
  feign:
    tender-service: tender-service
    bid-service: bidding-service
    user-service: user-service
```

## Building and Running

To build the service:
```bash
mvn clean package
```

To run the service:
```bash
java -jar target/evaluation-service-1.0.0-SNAPSHOT.jar
```

Or with Docker:
```bash
docker build -t evaluation-service .
docker run -p 8084:8084 evaluation-service
```