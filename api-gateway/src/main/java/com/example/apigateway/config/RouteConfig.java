package com.example.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RouteConfig
{
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://httpbin.org:80"))
//                .route(p -> p
//                        .path("/register")
//                        .uri("lb://user-service/register"))
//                .route(p -> p
//                        .path("/getAllUsers")
//                        .uri("lb://user-service/getAllUsers"))
                .route(p -> p
                        .path("/api/v1/auth/register")
                        .uri("http://localhost:8989/api/v1/auth/register"))
                .route(p -> p
                        .path("/api/v1/auth/authenticate")
                        .uri("http://localhost:8989/api/v1/auth/authenticate"))
                .build();
    }


}
