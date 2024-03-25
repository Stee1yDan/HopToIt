package com.example.stockinfoservice.client;

import com.example.stockinfoservice.model.StockFormattedInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("stock-prediction-service")
public interface PredictionClient {
    @GetMapping("/api/v1/models/predict//{symbol}")
    String getStockPrediction(@PathVariable("symbol") String symbol);
}
