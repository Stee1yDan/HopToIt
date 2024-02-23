package com.example.stockinfoservice.service;

import com.example.stockinfoservice.client.StockClient;
import com.example.stockinfoservice.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class StockService
{
    private String stockCollection = "stock";
    private String stockHistoricalData = "stock_historical_data";
    private String stockDailyHistoricalData = "stock_daily_historical_data";
    private String stockRvScore = "stock_rv_score";
    private String stockHqmScore = "stock_hqm_score";

    private final StockClient stockClient;
    private final FirebaseService firebaseService;
    @Async
    @Scheduled(cron="@monthly")
    private void setMonthlyRequests()
    {
        getRvScore();
        getHqmScore();
    }


    public void getRvScore()
    {
        List<StockRvScore> rvScores = stockClient.getStockRvScore();

        rvScores.forEach(stock ->
        {
            try {
                firebaseService.createDocument(stock.getTicker(), stock, stockRvScore);
            }
            catch (Exception e)
            {
                System.out.printf(e.toString());
            }
        });
    }

    public void getHqmScore()
    {
        List<StockHqmScore> hqmScores = stockClient.getStockHqmScore();

        hqmScores.forEach(stock ->
        {
            try {
                firebaseService.createDocument(stock.getTicker(), stock, stockHqmScore);
            }
            catch (Exception e)
            {
                System.out.printf(e.toString());
            }
        });
    }

    public void initAllStocks()
    {
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(stockSymbols -> stockSymbols.toString()).toList();

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

                Double lastDayChange = (stockHistoricalInfoResponse.get(size-1).getClose() -
                        stockHistoricalInfoResponse.get(size-6).getOpen()) / stockHistoricalInfoResponse.get(size-1).getClose();

                Double lastWeekChange = (stockHistoricalInfoResponse.get(size-1).getClose() -
                        stockHistoricalInfoResponse.get(size-42).getOpen()) / stockHistoricalInfoResponse.get(size-1).getClose();

                Double lastMonthChange = (stockHistoricalInfoResponse.get(size-1).getClose() -
                        stockHistoricalInfoResponse.get(size-138).getOpen()) / stockHistoricalInfoResponse.get(size-1).getClose();


                stockFormattedInfo.setHourlyChange(lastHourChange);
                stockFormattedInfo.setDailyChange(lastDayChange);
                stockFormattedInfo.setWeeklyChange(lastWeekChange);
                stockFormattedInfo.setMonthlyChange(lastMonthChange);

                firebaseService.createDocument(stockFormattedInfo.getSymbol(), stockFormattedInfo, stockCollection);

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
    public void initAllStocksWithDailyHistoricalData()
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
        return firebaseService.getStock(stockCollectionName, documentId);
    }

    public void deleteStock(String stockCollectionName, String documentId)
    {
        firebaseService.deleteStock(stockCollectionName, documentId);
    }
}
