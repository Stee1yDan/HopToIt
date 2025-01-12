package me.hoptoit.apigateway.filter;

import me.hoptoit.apigateway.config.JwtService;
import me.hoptoit.apigateway.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleFilter implements GatewayFilter //TODO: Replace with factory
{
    private final JwtService jwtService;
    private final Role role;

    private WebClient webClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();

        final String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);
        final String username = jwtService.extractUsername(token);

        String roleUri = "http://localhost:8222/api/v1/auth/check/%s/%s";

        webClient = WebClient.builder().build();

        return webClient
                .get()
                .uri(String.format(roleUri,username,role.toString()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(response -> {
                    if(Boolean.FALSE.equals(response))
                        return Mono.error(new RuntimeException("un authorized access to application"));
                    return chain.filter(exchange);
                });
    }

    protected Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus)
    {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
