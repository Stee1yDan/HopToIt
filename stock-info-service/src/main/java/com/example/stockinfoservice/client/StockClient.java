package com.example.stockinfoservice.client;

import com.example.stockinfoservice.model.StockFormattedInfo;
import com.example.stockinfoservice.model.StockHistoricalInfoRequest;
import com.example.stockinfoservice.model.StockHistoricalInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("stock-data-api")
public interface StockClient
{
    @GetMapping("/api/v1/stocks/getFormattedInfo/{symbol}")
    StockFormattedInfo getFormattedStockInfo(@PathVariable("symbol") String symbol);

    @PostMapping("/api/v1/stocks/getHistoricalData/{symbol}")
    List<StockHistoricalInfoResponse> getHistoricalStockInfo(@PathVariable("symbol") String symbol, @RequestBody StockHistoricalInfoRequest request);
}
