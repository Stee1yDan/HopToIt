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
    private final StockClient stockClient;
    private final FirebaseService firebaseService;

    @Async
    @Scheduled(fixedRate = 3600000)
    public void initAllStocks()
    {
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(stockSymbols -> stockSymbols.toString()).toList();

        symbols.forEach(s ->
        {
            try
            {
                StockFormattedInfo stockFormattedInfo = stockClient.getFormattedStockInfo(s);
                firebaseService.createDocument(stockFormattedInfo.getSymbol(), stockFormattedInfo, stockCollection);
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });
    }

    @Async
    @Scheduled(fixedRate = 3600000)
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
                        .start(date.minusDays(7).format(formatter))
                        .end(date.format(formatter))
                        .interval("1h")
                        .build();


                List<StockHistoricalInfoResponse> stockHistoricalInfoResponse =
                        stockClient.getHistoricalStockInfo(s, request);

                firebaseService.createDocument(s,
                        StockTimestamps.builder()
                                .symbol(s)
                                .stockTimestamps(stockHistoricalInfoResponse).build(), stockHistoricalData);

               Thread.sleep(10000);
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
