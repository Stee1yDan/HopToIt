package com.example.userservice.controller;

import com.example.userservice.model.Correlation;
import com.example.userservice.model.EfficientFrontier;
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

    @PostMapping("/getEfficientFrontier/**")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackGetEfficientFrontier")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<EfficientFrontier>> getEfficientFrontier(@RequestBody Portfolio portfolio)
    {
        return CompletableFuture.supplyAsync(() -> {
            return new ResponseEntity<>(portfolioService.getEfficientFrontier(portfolio), HttpStatus.OK);
        });
    }

    @PostMapping("/getCorrelation/**")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackGetCorrelation")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<Correlation[]>> getCorrelation(@RequestBody Portfolio portfolio)
    {
        return CompletableFuture.supplyAsync(() -> {
            return new ResponseEntity<>(portfolioService.getCorrelation(portfolio), HttpStatus.OK);
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

    public CompletableFuture<ResponseEntity<String>> fallbackUpdateMethod(Portfolio portfolio,
                                                                        String username,
                                                                        String name,
                                                                        Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>("Something went wrong", HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<String>> fallbackAddMethod(Portfolio portfolio,
                                                                        String username,
                                                                        Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>("Something went wrong", HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<String>> fallbackDeleteMethod(String username,
                                                                        String name,
                                                                        Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>("Something went wrong", HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<String>> fallbackGetEfficientFrontier(Portfolio portfolio,
                                                                                Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>("Couldn't get efficient frontier", HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<String>> fallbackGetCorrelation(Portfolio portfolio,
                                                                                Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>("Couldn't get correlation", HttpStatus.CONFLICT));
    }


}
