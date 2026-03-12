# Monitoring Stack

## Overview
RevWorkForce uses a comprehensive monitoring and observability stack to ensure the health, performance, and reliability of the microservices architecture.

## Components & Features

### 1. Prometheus
*   **Role**: Metrics collection and storage.
*   **Feature**: Scrapes metrics from `/actuator/prometheus` endpoints of all Spring Boot microservices.
*   **Port**: `9090`

### 2. Grafana
*   **Role**: Metrics visualization dashboard.
*   **Feature**: Connects to Prometheus as a data source to provide real-time dashboards for CPU, Memory, Request Latency, and Error rates.
*   **Port**: `3000`

### 3. ELK Stack (Elasticsearch, Logstash, Kibana)
*   **Role**: Centralized Log Management.
*   **Feature**:
    *   **Logstash**: Collects and parses logs from all containers.
    *   **Elasticsearch**: Indexes and stores log data.
    *   **Kibana**: Interface to search and analyze logs.
*   **Port**: `5601` (Kibana)

## Implementation Details
*   **Micrometer**: Included in each microservice to bridge Spring Boot Actuator metrics to Prometheus format.
*   **AOP Logging**: Centralized logging via `LoggingAspect` in each service ensures uniform log structures for ELK analysis.
