package com.example.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthUserFilter implements GatewayFilter
{
    private WebClient webClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();


        final String uri = request.getURI().toString();
        final Pattern pattern = Pattern.compile("\\b(\\w+)$");
        Matcher m = pattern.matcher(uri);

//        if (!m.find())
//        {
//            System.out.println("hahahah");
//            return onError(exchange, HttpStatus.UNAUTHORIZED);
//        }

        m.find();
        String username = m.group(0);

        if (authMissing(request)) {
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        final String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);

        String tokenUri = "http://localhost:8222/api/v1/auth/validate/%s/%s";

        webClient = WebClient.builder().build();

        return webClient
                .get()
                .uri(String.format(tokenUri,token, username))
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(response -> {
                    if(Boolean.FALSE.equals(response))
                        return Mono.error(new RuntimeException("Unauthorized access to application"));
                    return chain.filter(exchange);
                });
    }

    protected Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    protected boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
