package com.example.stockinfoservice.client;

import com.example.stockinfoservice.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.Future;

@FeignClient("stock-data-api")
public interface StockClient
{
    @GetMapping("/api/v1/stocks/getFormattedInfo/{symbol}")
    StockFormattedInfo getFormattedStockInfo(@PathVariable("symbol") String symbol);

    @GetMapping("/api/v1/stocks/calculateMetrics")
    void updateScoreInfo();

    @GetMapping("/api/v1/stocks/getRvScore")
    List<StockRvScore> getStockRvScore();

    @GetMapping("/api/v1/stocks/getHqmScore")
    List<StockHqmScore> getStockHqmScore();

    @PostMapping("/api/v1/stocks/getHistoricalData/{symbol}")
    List<StockHistoricalInfoResponse> getHistoricalStockInfo(@PathVariable("symbol") String symbol, @RequestBody StockHistoricalInfoRequest request);
}
