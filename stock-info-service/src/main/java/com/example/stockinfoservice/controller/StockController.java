package com.example.stockinfoservice.controller;

import com.example.stockinfoservice.model.StockFormattedInfo;
import com.example.stockinfoservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/stocks")
@RequiredArgsConstructor
public class StockController
{
    private final StockService stockService;

    @PostMapping("/{collectionName}/create")
    public String create(@PathVariable("collectionName") String collectionName, @RequestBody StockFormattedInfo stockFullInfo) throws ExecutionException, InterruptedException
    {
        return stockService.createStock(stockFullInfo, collectionName);
    }

    @GetMapping("/get/{collectionName}/{documentId}")
    public StockFormattedInfo get(@PathVariable("collectionName") String collectionName,
                                  @PathVariable(name = "documentId") String documentId) throws ExecutionException, InterruptedException
    {
        return stockService.getStock(collectionName, documentId);
    }

    @DeleteMapping("/delete/{collectionName}/{documentId}")
    public void delete(@PathVariable("collectionName") String collectionName,
                       @PathVariable(name = "documentId") String documentId)
    {
        stockService.deleteStock(collectionName, documentId);
    }
}

