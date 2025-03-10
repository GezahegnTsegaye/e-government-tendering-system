package com.egov.tendering.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller that provides fallback responses when services are unavailable
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * Generic fallback endpoint for all services
     */
    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> fallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Service Unavailable");
        response.put("message", "The requested service is currently unavailable. Please try again later.");

        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response));
    }

    /**
     * Fallback specifically for the user service
     */
    @GetMapping("/user-service")
    public Mono<ResponseEntity<Map<String, Object>>> userServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "User Service Unavailable");
        response.put("message", "The user service is currently unavailable. Please try again later.");

        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response));
    }

    /**
     * Fallback specifically for the tender service
     */
    @GetMapping("/tender-service")
    public Mono<ResponseEntity<Map<String, Object>>> tenderServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Tender Service Unavailable");
        response.put("message", "The tender service is currently unavailable. Please try again later.");

        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response));
    }

    /**
     * Fallback specifically for the bidding service
     */
    @GetMapping("/bidding-service")
    public Mono<ResponseEntity<Map<String, Object>>> biddingServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("error", "Bidding Service Unavailable");
        response.put("message", "The bidding service is currently unavailable. Please try again later.");

        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response));
    }
}