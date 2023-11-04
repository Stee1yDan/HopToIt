package com.example.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class RouteConfig
{

    private final AuthFilter authFilter;
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.filter(authFilter))
                        .uri("http://localhost:8111/get")
                )
//                .route(p -> p
//                        .path("/register")
//                        .uri("lb://user-service/register"))
//                .route(p -> p
//                        .path("/getAllUsers")
//                        .uri("lb://user-service/getAllUsers"))
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
