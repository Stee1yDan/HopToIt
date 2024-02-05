package com.example.stockinfoservice.service;

import com.example.stockinfoservice.client.StockClient;
import com.example.stockinfoservice.model.StockHistoricalInfoRequest;
import com.example.stockinfoservice.model.StockHistoricalInfoResponse;
import com.example.stockinfoservice.model.StockFormattedInfo;
import com.example.stockinfoservice.model.StockSymbols;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(stockSymbols -> stockSymbols.toString()).collect(Collectors.toList());

        symbols.forEach(s ->
        {
            try
            {
                createStock(stockClient.getFormattedStockInfo(s), stockCollection);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });
    }

    @Async
    @Scheduled(fixedRate = 3600000)
    public void initAllStocksHistoricalData()
    {
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(stockSymbols -> stockSymbols.toString()).collect(Collectors.toList());

        symbols.forEach(s ->
        {
            try
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.now();

                var request = StockHistoricalInfoRequest.builder()
                        .start(date.minusDays(7).format(formatter).toString())
                        .end(date.format(formatter).toString())
                        .interval("1h")
                        .build();

               stockClient.getHistoricalStockInfo(s, request).forEach(d -> firebaseService.createDocument(d.getSymbol(), d, stockHistoricalData));
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
            throws ExecutionException, InterruptedException
    {
        return firebaseService.getStock(stockCollectionName, documentId);
    }

    public void deleteStock(String stockCollectionName, String documentId)
    {
        firebaseService.deleteStock(stockCollectionName, documentId);
    }
}
