package com.example.stockinfoservice.client;

import com.example.stockinfoservice.model.Stock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.Future;

@FeignClient("stock-data-api")
public interface StockClient
{
    @GetMapping("/api/v1/stocks/getFormattedInfo/{symbol}")
    Stock getFormattedStockInfo(@PathVariable("symbol") String symbol);
}
