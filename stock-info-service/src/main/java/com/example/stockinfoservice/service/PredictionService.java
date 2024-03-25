package com.example.stockinfoservice.service;

import com.example.stockinfoservice.client.PredictionClient;
import com.example.stockinfoservice.model.StockSymbols;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class PredictionService {
    private final PredictionClient predictionClient;


    @Async
    @Scheduled(cron = "@daily")
    public void getPredictions() {
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(Enum::toString).toList();
        List<Double> predictions = new ArrayList<>();

        for (String s: symbols) {
            predictions.add(Double.parseDouble(predictionClient.getStockPrediction(s)));
        }

        predictions.forEach(System.out::println);

    }
}
