package com.example.stockinfoservice.service;

import com.example.stockinfoservice.client.PredictionClient;
import com.example.stockinfoservice.model.StockPrediction;
import com.example.stockinfoservice.model.StockSymbols;
import com.example.stockinfoservice.repository.StockPredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class PredictionService {
    private final PredictionClient predictionClient;
    private final StockPredictionRepository predictionRepository;

    @Async
    @Scheduled(cron = "0 0 * * 1-5")
    public void getPredictions() {
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(Enum::toString).toList();
        for (String s: symbols)
            predictionRepository.save(StockPrediction.builder()
                    .ticker(s)
                    .time(LocalDate.now())
                    .predictedValue(Double.parseDouble(predictionClient.getStockPrediction(s)))
                    .build());
    }
}
