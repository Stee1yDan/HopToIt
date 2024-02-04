package com.example.stockinfoservice.service;

import com.example.stockinfoservice.client.StockClient;
import com.example.stockinfoservice.model.Stock;
import com.example.stockinfoservice.model.StockSymbols;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService
{
    private String StockCollectionName = "stock";
    private final StockClient stockClient;

    public void initAllStocks()
    {
        List<String> symbols = Arrays.stream(StockSymbols.values()).map(stockSymbols -> stockSymbols.toString()).collect(Collectors.toList());

        symbols.forEach(s ->
        {
            try
            {
                createStock(stockClient.getFormattedStockInfo(s));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });
    }
    public String createStock(Stock stock) throws ExecutionException, InterruptedException
    {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collections = firestore
                .collection(StockCollectionName)
                .document(stock.getSymbol())
                .set(stock);
        return collections.get().getUpdateTime().toString();
    }

    public Stock getStock(String documentId) throws ExecutionException, InterruptedException
    {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = firestore.collection(StockCollectionName).document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot documentSnapshot = future.get();

        if (documentSnapshot != null)
            return documentSnapshot.toObject(Stock.class);
        return null;
    }

    public void deleteStock(String documentId)
    {
        Firestore firestore = FirestoreClient.getFirestore();
        firestore
                .collection(StockCollectionName)
                .document(documentId)
                .delete();
    }

    public String updateStock(Stock stock) throws ExecutionException, InterruptedException
    {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> apiFuture = firestore
                .collection(StockCollectionName)
                .document(stock.getSymbol())
                .set(stock);
        return apiFuture.get().getUpdateTime().toString();
    }
}
