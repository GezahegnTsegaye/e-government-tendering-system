package com.egov.tendering.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Global filter that logs request details and adds correlation ID
 * for request tracing across microservices
 */
@Component
@Slf4j
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final String CORRELATION_ID = "X-Correlation-ID";
    private static final String REQUEST_START_TIME = "requestStartTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LocalDateTime startTime = LocalDateTime.now();
        exchange.getAttributes().put(REQUEST_START_TIME, startTime);

        ServerHttpRequest request = exchange.getRequest();
        String correlationId = request.getHeaders().getFirst(CORRELATION_ID);

        // If correlation ID is not present, generate one
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }

        log.info("Request: {} {}, CorrelationID: {}, Client: {}, StartTime: {}",
                request.getMethod(), request.getPath(), correlationId,
                request.getRemoteAddress(), startTime);

        // Add correlation ID to the request headers
        ServerHttpRequest requestWithCorrelationId = exchange.getRequest().mutate()
                .header(CORRELATION_ID, correlationId)
                .build();

        // Replace the request with our modified one
        ServerWebExchange exchangeWithCorrelationId = exchange.mutate()
                .request(requestWithCorrelationId)
                .build();

        // Continue the filter chain with our modified exchange
        return chain.filter(exchangeWithCorrelationId).then(Mono.fromRunnable(() -> {
            LocalDateTime endTime = LocalDateTime.now();
            long durationMs = java.time.Duration.between(startTime, endTime).toMillis();

            log.info("Response: {} {}, CorrelationID: {}, Duration: {}ms, Status: {}",
                    request.getMethod(), request.getPath(),
                    durationMs, exchange.getResponse().getStatusCode());
        }));
    }

    @Override
    public int getOrder() {
        // Set a high precedence (lower number = higher precedence)
        return -1;
    }
}