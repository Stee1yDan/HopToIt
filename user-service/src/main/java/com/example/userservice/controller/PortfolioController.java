package com.example.userservice.controller;

import com.example.userservice.model.Portfolio;
import com.example.userservice.interfaces.IPortfolioService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/portfolios")
@RequiredArgsConstructor
@Slf4j
public class PortfolioController
{
    private final IPortfolioService portfolioService;

    @PutMapping("/update/{name}/{username}")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackUpdateMethod")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<Void>> updatePortfolio(@RequestBody Portfolio portfolio,
                                                                   @PathVariable("username") String username,
                                                                   @PathVariable("name") String name)
    {
        return CompletableFuture.supplyAsync(() -> {portfolioService.updatePortfolio(portfolio,username,name);;
            return null;
        });
    }


    @PostMapping("/add/{username}")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackAddMethod")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<Void>> addPortfolio(@RequestBody Portfolio portfolio, @PathVariable("username") String username)
    {
        return CompletableFuture.supplyAsync(() -> {portfolioService.addPortfolio(portfolio, username);
            return null;
        });

    }

    @DeleteMapping("/delete/{name}/{username}")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackDeleteMethod")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<Void>> deletePortfolio(@PathVariable("username") String username, @PathVariable("name") String name)
    {
        return CompletableFuture.supplyAsync(() -> {portfolioService.deletePortfolio(username,name);;
            return null;
        });
    }

    public CompletableFuture<ResponseEntity<Void>> fallbackUpdateMethod(Portfolio portfolio,
                                                                        String username,
                                                                        String name,
                                                                        Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<Void>> fallbackAddMethod(Portfolio portfolio,
                                                                        String username,
                                                                        Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<Void>> fallbackDeleteMethod(String username,
                                                                        String name,
                                                                        Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }


}
