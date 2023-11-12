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
    private final AdminFilter adminFilter;
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/api/v1/users/**")
                        .filters(f -> f.filter(authFilter)) //TODO: GatewayFilterFactory
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                .route(p -> p
                        .path("/eureka")
                        .filters(f -> f
                                .filter(authFilter)
                                .filter(adminFilter)
                                .rewritePath("/eureka","/"))
                        .uri("http://localhost:8761"))
                .build();
    }




}
