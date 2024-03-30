package com.example.stockinfoservice.controller;

import com.example.stockinfoservice.model.StockFormattedInfo;
import com.example.stockinfoservice.service.StockService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/firebase")
@RequiredArgsConstructor
public class StockFirebaseController
{
    private final StockService stockService;

    @PostMapping("/create/{collectionName}")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackCreateMethod")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<String>> create(@PathVariable("collectionName") String collectionName,
                                                            @RequestBody StockFormattedInfo stockFullInfo)
    {
        return CompletableFuture.supplyAsync(() -> {
            return new ResponseEntity<>(stockService.createStock(stockFullInfo, collectionName), HttpStatus.CREATED);
        });
    }

    @GetMapping("/get/{collectionName}/{documentId}")
    public ResponseEntity<StockFormattedInfo>  get(@PathVariable("collectionName") String collectionName,
                                  @PathVariable(name = "documentId") String documentId)
    {
        System.out.println(stockService.getStock(collectionName, documentId));
            return new ResponseEntity<>(stockService.getStock(collectionName, documentId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{collectionName}/{documentId}")
    @CircuitBreaker(name="default", fallbackMethod = "fallbackMethod")
    @Retry(name="default")
    public CompletableFuture<ResponseEntity<Void>> delete(@PathVariable("collectionName") String collectionName,
                       @PathVariable(name = "documentId") String documentId)
    {
        return CompletableFuture.supplyAsync(() -> {
            stockService.deleteStock(collectionName, documentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        });
    }

    public CompletableFuture<ResponseEntity<String>> fallbackUpdateMethod(String collectionName,
                                                                        StockFormattedInfo stockFullInfo,
                                                                        Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }

    public CompletableFuture<ResponseEntity<StockFormattedInfo>> fallbackMethod(String collectionName,
                                                                                   String documentId,
                                                                                   Throwable throwable)
    {
        return CompletableFuture.supplyAsync(() -> new ResponseEntity<>(null, HttpStatus.CONFLICT));
    }

}

