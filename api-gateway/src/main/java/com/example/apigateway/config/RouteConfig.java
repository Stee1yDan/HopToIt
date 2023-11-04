package com.example.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RouteConfig
{

    private final AuthFilter authFilter;
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/v1/users/**")
                        .filters(f -> f.filter(authFilter))
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                .build();
    }


}
