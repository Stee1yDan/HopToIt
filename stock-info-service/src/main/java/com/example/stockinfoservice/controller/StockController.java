package com.example.stockinfoservice.controller;

import com.example.stockinfoservice.model.Stock;
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

    @PostMapping("/create")
    public String create(@RequestBody Stock stock) throws ExecutionException, InterruptedException
    {
        return stockService.createStock(stock);
    }

    @GetMapping("/get/{documentId}")
    public Stock get(@PathVariable(name = "documentId") String documentId) throws ExecutionException, InterruptedException
    {
        return stockService.getStock(documentId);
    }

    @DeleteMapping("/delete/{documentId}")
    public void delete(@PathVariable(name = "documentId") String documentId)
    {
        stockService.deleteStock(documentId);
    }
}

