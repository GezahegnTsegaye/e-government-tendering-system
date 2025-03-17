package com.egov.tendering.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtConfigData {
    private String secretKey;
    private Long expiration;
    private RefreshToken refreshToken;

    @Data
    public static class RefreshToken {
        private Long expiration;
    }
}
