package com.example.apigateway.config;

import com.example.apigateway.repository.UserRepository;
import com.example.apigateway.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AdminFilter implements GatewayFilter
{

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();

        final String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);
        final String username = jwtService.extractUsername(token);

        Role userRole = userRepository.findUserByUsername(username).get().getRole();

        if (!userRole.equals(Role.ADMIN))
        {
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }
        return chain.filter(exchange);
    }

    protected Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus)
    {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
