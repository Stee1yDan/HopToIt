package com.example.stockinfoservice;

import com.example.stockinfoservice.service.PredictionService;
import com.example.stockinfoservice.service.StockService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
@RequiredArgsConstructor
@EnableFeignClients
public class StockInfoServiceApplication
{
    private final StockService stockService;
    private final PredictionService predictionService;
    public static void main(String[] args) throws IOException
    {
        ClassLoader classLoader = StockInfoServiceApplication.class.getClassLoader();
        File file = new File(classLoader.getResource("serviceAccountKey.json").getFile());

        FileInputStream serviceAccount =
                new FileInputStream(file.getAbsolutePath());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);

        SpringApplication.run(StockInfoServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner runner()
    {
        return args ->
        {
            stockService.getHqmScore();
            stockService.getRvScore();
            predictionService.getPredictions();
            stockService.initAllStocksWithHistoricalData();
//            stockService.getStocksDailyData();
        };
    }
}
