package com.example.apigateway.filter;

import com.example.apigateway.config.JwtService;
import com.example.apigateway.repository.UserRepository;
import com.example.apigateway.user.Role;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class RoleFilter implements GatewayFilter
{
    private JwtService jwtService;
    private UserRepository userRepository;
    private List<Role> accessRoles;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();

        System.out.println(request.getHeaders());

        final String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);
        final String username = jwtService.extractUsername(token);

        Role userRole = userRepository.findUserByUsername(username).get().getRole();

        if (!accessRoles.contains(userRole))
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
