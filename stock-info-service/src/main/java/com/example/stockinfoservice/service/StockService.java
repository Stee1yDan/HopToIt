package com.example.stockinfoservice.service;

import com.example.stockinfoservice.client.StockClient;
import com.example.stockinfoservice.model.*;
import com.example.stockinfoservice.repository.StockHqmScoreRepository;
import com.example.stockinfoservice.repository.StockPredictionRepository;
import com.example.stockinfoservice.repository.StockRepository;
import com.example.stockinfoservice.repository.StockRvScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Future;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class StockService
{
    private String stockCollection = "stock";
    private String stockHistoricalData = "stock_historical_data";
    private String stockDailyHistoricalData = "stock_daily_historical_data";

    private final StockClient stockClient;
    private final FirebaseService firebaseService;

    private final StockRvScoreRepository rvScoreRepository;
    private final StockHqmScoreRepository hqmScoreRepository;
    private final StockPredictionRepository predictionRepository;
    private final StockRepository stockRepository;
    @Async
    @Scheduled(cron = "@monthly")
    private void sendMonthlyRequests()
    {
        updateScore();
        getRvScore();
        getHqmScore();
    }

    public void updateScore()
    {
        stockClient.updateScoreInfo();
    }


    public void getRvScore()
    {
        List<StockRvScore> rvScores = stockClient.getStockRvScore();
        rvScoreRepository.saveAll(rvScores);
    }

    public void getHqmScore()
    {
        List<StockHqmScore> hqmScores = stockClient.getStockHqmScore();
        hqmScoreRepository.saveAll(hqmScores);
    }

    public void initAllStocks()
    {
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(Enum::toString).toList();

        symbols.forEach(s ->
        {
            try
            {
                StockFormattedInfo stockFormattedInfo = stockClient.getFormattedStockInfo(s);
                firebaseService.createDocument(stockFormattedInfo.getSymbol(), stockFormattedInfo, stockCollection);
            }
            catch (Exception e)
            {
                System.out.printf(e.toString());
            }
        });
    }

    @Async
    @Scheduled(cron = "@hourly")
    public void initAllStocksWithHistoricalData()
    {
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(Enum::toString).toList();

        symbols.forEach(s ->
        {
            try
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.now();

                StockHistoricalInfoRequest request = StockHistoricalInfoRequest.builder()
                        .start(date.minusDays(60).format(formatter))
                        .end(date.format(formatter))
                        .interval("1h")
                        .build();


                List<StockHistoricalInfoResponse> stockHistoricalInfoResponse =
                        stockClient.getHistoricalStockInfo(s, request);

                StockFormattedInfo stockFormattedInfo = stockClient.getFormattedStockInfo(s);

                int size = stockHistoricalInfoResponse.size();
                Double lastHourChange = (stockHistoricalInfoResponse.get(size-1).getClose() -
                        stockHistoricalInfoResponse.get(size-1).getOpen()) / stockHistoricalInfoResponse.get(size-1).getClose();

                if (hqmScoreRepository.findById(s).isPresent())
                {
                    StockHqmScore hqmScore = hqmScoreRepository.findById(s).get();
                    stockFormattedInfo.setHqmScore(hqmScore.getHQMScore());
                    stockFormattedInfo.setMonthlyChange(hqmScore.getOneMonthPriceReturn());
                }

                if (rvScoreRepository.findById(s).isPresent())
                    stockFormattedInfo.setRvScore(rvScoreRepository.findById(s).get().getRvScore());

                stockFormattedInfo.setHourlyChange(lastHourChange);


                if (predictionRepository.findByTicker(s).isPresent()){
                    List<StockPrediction> predictions =  predictionRepository.findByTicker(s).get();
                    predictions = predictions.stream().sorted(Comparator.comparing(StockPrediction::getTime).reversed()).toList();
                    stockFormattedInfo.setPrediction(predictions.get(0).getPredictedValue());
                }



                firebaseService.createDocument(stockFormattedInfo.getSymbol(), stockFormattedInfo, stockCollection);
                stockFormattedInfo.setLongBusinessSummary(stockFormattedInfo.getLongBusinessSummary().substring(0,254));
                stockRepository.save(stockFormattedInfo);

                firebaseService.createDocument(s,
                        StockTimestamps.builder()
                                .symbol(s)
                                .stockTimestamps(stockHistoricalInfoResponse).build(), stockHistoricalData);

            }
            catch (Exception e)
            {
                System.out.printf(e.toString());
            }
        });
    }

    @Async
    @Scheduled(cron = "@daily")
    public void getStocksDailyData()
    {
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(Enum::toString).toList();

        symbols.forEach(s ->
        {
            try
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.now();

                StockHistoricalInfoRequest request = StockHistoricalInfoRequest.builder()
                        .start(date.minusDays(180).format(formatter))
                        .end(date.format(formatter))
                        .interval("1d")
                        .build();


                List<StockHistoricalInfoResponse> stockHistoricalInfoResponse =
                        stockClient.getHistoricalStockInfo(s, request);

                firebaseService.createDocument(s,
                        StockTimestamps.builder()
                                .symbol(s)
                                .stockTimestamps(stockHistoricalInfoResponse).build(), stockDailyHistoricalData);

            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });
    }

    public String createStock(StockFormattedInfo stockInfo, String stockCollectionName)
    {
        return firebaseService.createDocument(stockInfo.getSymbol(), stockInfo, stockCollectionName);
    }

    public StockFormattedInfo getStock(String stockCollectionName, String documentId)
    {
        return (StockFormattedInfo) firebaseService.getDocument(stockCollectionName, documentId, StockFormattedInfo.class);
    }

    public void deleteStock(String stockCollectionName, String documentId)
    {
        firebaseService.deleteDocument(stockCollectionName, documentId);
    }
}
