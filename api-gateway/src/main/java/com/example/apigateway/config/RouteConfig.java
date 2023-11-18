package com.example.apigateway.config;

import com.example.apigateway.filter.FilterFactory;
import com.example.apigateway.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RouteConfig
{

    private final FilterFactory filterFactory;
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder)
    {
        return builder.routes()
                .route(p -> p
                        .path("/api/v1/users/**")
                        .filters(f -> f.filter(filterFactory.getAuthFilter())) //TODO: GatewayFilterFactory
                        .uri("lb://user-service"))
                .route(p -> p
                        .path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                .route(p -> p
                        .path("/eureka")
                        .filters(f -> f
                                .filter(filterFactory.getAuthFilter())
                                .filter(filterFactory.getRoleFilter(List.of(Role.ADMIN)))
                                .rewritePath("/eureka","/"))
                        .uri("http://localhost:8761"))
                .build();
    }




}
